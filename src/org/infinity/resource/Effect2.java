// Near Infinity - An Infinity Engine Browser and Editor
// Copyright (C) 2001 - 2019 Jon Olav Hauglid
// See LICENSE.txt for license information

package org.infinity.resource;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.infinity.datatype.Bitmap;
import org.infinity.datatype.DecNumber;
import org.infinity.datatype.EffectType;
import org.infinity.datatype.Flag;
import org.infinity.datatype.IdsBitmap;
import org.infinity.datatype.PriTypeBitmap;
import org.infinity.datatype.ResourceRef;
import org.infinity.datatype.SecTypeBitmap;
import org.infinity.datatype.TextString;
import org.infinity.datatype.Unknown;
import org.infinity.util.IdsMapEntry;
import org.infinity.util.io.StreamUtils;

public final class Effect2 extends AbstractStruct implements AddRemovable
{
  // Effect-specific field labels
  public static final String EFFECT                   = Effect.EFFECT;
  public static final String EFFECT_PRIMARY_TYPE      = "Primary type (school)";
  public static final String EFFECT_USED_INTERNALLY   = "Used internally";
  public static final String EFFECT_MIN_LEVEL         = "Minimum level";
  public static final String EFFECT_MAX_LEVEL         = "Maximum level";
  public static final String EFFECT_DISPEL_TYPE       = "Dispel/Resistance";
  public static final String EFFECT_PARAMETER_3       = "Parameter 3";
  public static final String EFFECT_PARAMETER_4       = "Parameter 4";
  public static final String EFFECT_PARAMETER_5       = "Parameter 5 (unused)";
  public static final String EFFECT_SOURCE_CONSTANT_ID = "Source constant identifier";
  public static final String EFFECT_TIME_APPLIED      = "Time applied (ticks)";
  public static final String EFFECT_RESOURCE_2        = "Resource 2";
  public static final String EFFECT_RESOURCE_3        = "Resource 3";
  public static final String EFFECT_CASTER_LOCATION_X = "Caster location: X";
  public static final String EFFECT_CASTER_LOCATION_Y = "Caster location: Y";
  public static final String EFFECT_TARGET_LOCATION_X = "Target location: X";
  public static final String EFFECT_TARGET_LOCATION_Y = "Target location: Y";
  public static final String EFFECT_RESOURCE_TYPE     = "Resource type";
  public static final String EFFECT_PARENT_RESOURCE   = "Parent resource";
  public static final String EFFECT_RESOURCE_FLAGS    = "Resource flags";
  public static final String EFFECT_IMPACT_PROJECTILE = "Impact projectile";
  public static final String EFFECT_SOURCE_ITEM_SLOT  = "Source item slot";
  public static final String EFFECT_VARIABLE_NAME     = "Variable name";
  public static final String EFFECT_CASTER_LEVEL      = "Caster level";
  public static final String EFFECT_CASTER_CLASS      = "Caster class";
  public static final String EFFECT_CASTER_DOMAIN     = "Caster domain";
  public static final String EFFECT_INTERNAL_FLAGS    = "Internal flags";
  public static final String EFFECT_SECONDARY_TYPE    = "Secondary type";
  public static final String EFFECT_IDENTIFIER        = "Effect identifier";
  public static final String EFFECT_SPELL_RESISTANCE_PENETRATION    = "Spell resistance penetration";

  public static final String[] s_itmflag = {"No flags set", "Add strength bonus", "Breakable",
                                            null, null, null, null, null, null, null, null, "Hostile",
                                            "Recharge after resting", null, null, null, null,
                                            "Bypass armor", "Keen edge"};
  public static final String[] s_splflag = {"No flags set", null, null, null, null, null, null, null, null, null,
                                            null, "Hostile", "No LOS required", "Allow spotting", "Outdoors only",
                                            "Non-magical ability", "Trigger/Contingency", "Non-combat ability"};
  public static final String[] s_restype = {"None", "Spell", "Item"};
  public static final String[] s_dispel = {"Natural/Nonmagical", "Dispel/Not bypass resistance",
                                           "Not dispel/Bypass resistance", "Dispel/Bypass resistance"};
  public static final String[] s_dispel_ee = {"Natural/Nonmagical", "Dispel", "Bypass resistance",
                                              "Bypass deflection/reflection/trap", null, null, null, null, null,
                                              null, null, null, null, null, null, null, null,
                                              null, null, null, null, null, null, null, null,
                                              null, null, null, null, null, null, null, "Effect applied by item"};
//  public static final String[] s_dispel_v1 = {"None", "Dispellable", "Bypass resistance"};
//  public static final String[] s_dispel_v2 = {"None", "Dispellable", "Bypass resistance",
//                                              "Bypass turn/reflect/absorb"};

  public static int readCommon(List<StructEntry> list, ByteBuffer buffer, int offset)
  {
    list.add(new PriTypeBitmap(buffer, offset, 4, EFFECT_PRIMARY_TYPE));
	if (Profile.getEngine() == Profile.Engine.IWD2) {
      list.add(new SecTypeBitmap(buffer, offset + 4, 4, EFFECT_SECONDARY_TYPE));
    } else if (Profile.isEnhancedEdition()) {
      list.add(new DecNumber(buffer, offset + 4, 4, EFFECT_USED_INTERNALLY));
    } else {
      list.add(new DecNumber(buffer, offset + 4, 4, COMMON_UNUSED));
    }
    list.add(new DecNumber(buffer, offset + 8, 4, EFFECT_MIN_LEVEL));
    list.add(new DecNumber(buffer, offset + 12, 4, EFFECT_MAX_LEVEL));
    if (Profile.isEnhancedEdition()) {
      list.add(new Flag(buffer, offset + 16, 4, EFFECT_DISPEL_TYPE, s_dispel_ee));
    } else {
      list.add(new Bitmap(buffer, offset + 16, 4, EFFECT_DISPEL_TYPE, s_dispel));
    }
    list.add(new DecNumber(buffer, offset + 20, 4, EFFECT_PARAMETER_3));
    list.add(new DecNumber(buffer, offset + 24, 4, EFFECT_PARAMETER_4));
    if (Profile.getEngine() == Profile.Engine.IWD2) {
      list.add(new DecNumber(buffer, offset + 28, 4, EFFECT_SOURCE_CONSTANT_ID));
    } else {
      list.add(new DecNumber(buffer, offset + 28, 4, EFFECT_PARAMETER_5));
    }
    
    if (Profile.isEnhancedEdition() || Profile.getEngine() == Profile.Engine.IWD2) {
      list.add(new DecNumber(buffer, offset + 32, 4, EFFECT_TIME_APPLIED));
    } else {
      list.add(new DecNumber(buffer, offset + 32, 4, COMMON_UNUSED));
    }
    list.add(new TextString(buffer, offset + 36, 8, EFFECT_RESOURCE_2));
    list.add(new TextString(buffer, offset + 44, 8, EFFECT_RESOURCE_3));
    list.add(new DecNumber(buffer, offset + 52, 4, EFFECT_CASTER_LOCATION_X));
    list.add(new DecNumber(buffer, offset + 56, 4, EFFECT_CASTER_LOCATION_Y));
    list.add(new DecNumber(buffer, offset + 60, 4, EFFECT_TARGET_LOCATION_X));
    list.add(new DecNumber(buffer, offset + 64, 4, EFFECT_TARGET_LOCATION_Y));
    Bitmap res_type = new Bitmap(buffer, offset + 68, 4, EFFECT_RESOURCE_TYPE, s_restype);
    list.add(res_type);
    if (res_type.getValue() == 2) {
      list.add(new ResourceRef(buffer, offset + 72, EFFECT_PARENT_RESOURCE, "ITM"));
      list.add(new Flag(buffer, offset + 80, 4, EFFECT_RESOURCE_FLAGS, s_itmflag));
    } else {
      list.add(new ResourceRef(buffer, offset + 72, EFFECT_PARENT_RESOURCE, "SPL"));
      list.add(new Flag(buffer, offset + 80, 4, EFFECT_RESOURCE_FLAGS, s_splflag));
    }
    if (ResourceFactory.resourceExists("PROJECTL.IDS")) {
      list.add(new IdsBitmap(buffer, offset + 84, 4, EFFECT_IMPACT_PROJECTILE, "PROJECTL.IDS"));
    } else if (Profile.getEngine() == Profile.Engine.IWD2) {
      list.add(new Bitmap(buffer, offset + 84, 4, EFFECT_IMPACT_PROJECTILE, EffectType.s_proj_iwd2_effect));
    } else {
      list.add(new DecNumber(buffer, offset + 84, 4, EFFECT_IMPACT_PROJECTILE));
    }
    IdsBitmap slot_type = new IdsBitmap(buffer, offset + 88, 4, EFFECT_SOURCE_ITEM_SLOT, "SLOTS.IDS");
    slot_type.addIdsMapEntry(new IdsMapEntry(4294967295L, "NONE"));
    list.add(slot_type);
    list.add(new TextString(buffer, offset + 92, 32, EFFECT_VARIABLE_NAME));
	if (Profile.getEngine() == Profile.Engine.IWD2) {
      list.add(new DecNumber(buffer, offset + 124, 1, EFFECT_CASTER_LEVEL));
      list.add(new IdsBitmap(buffer, offset + 125, 1, EFFECT_CASTER_CLASS, "CLASS.IDS"));
      list.add(new Bitmap(buffer, offset + 126, 1, EFFECT_CASTER_DOMAIN, new String[]{"None", "Cleric of Ilmater", "Cleric of Lathander", "Cleric of Selune", "Cleric of Helm", "Cleric of Oghma", "Cleric of Tempus", "Cleric of Bane", "Cleric of Mask", "Cleric of Talos"}));
      list.add(new DecNumber(buffer, offset + 127, 1, COMMON_UNUSED));
      list.add(new DecNumber(buffer, offset + 128, 4, EFFECT_SPELL_RESISTANCE_PENETRATION));
	} else {
      list.add(new DecNumber(buffer, offset + 124, 4, EFFECT_CASTER_LEVEL));
      list.add(new Flag(buffer, offset + 128, 4, EFFECT_INTERNAL_FLAGS, null));
	}
	if (Profile.getEngine() == Profile.Engine.IWD2) {
	  list.add(new DecNumber(buffer, offset + 132, 4, EFFECT_USED_INTERNALLY));
	  list.add(new DecNumber(buffer, offset + 136, 4, EFFECT_IDENTIFIER));
      list.add(new Flag(buffer, offset + 140, 4, EFFECT_INTERNAL_FLAGS, new String[]{"No flags set", "Effect initialized", null, null, null, "Invoke Lua initialized", "Delivered by projectile", "Whirlwind Attack effect", "On-tick function processed", 
	null, null, null, null, null, null, "Duration changed", null,
    "Spell persistent", "Spell extended", "Spell widened", "Spell safe", "Spell empowered", "Spell maximized", null, null,
	null, "Ignore EXEFFMOD", "Reflected"}));
	  list.add(new Unknown(buffer, offset + 144, 52));
    } else {
      list.add(new SecTypeBitmap(buffer, offset + 132, 4, EFFECT_SECONDARY_TYPE));
	  list.add(new Unknown(buffer, offset + 136, 60));
    }
 
    return offset + 196;
  }

  public Effect2() throws Exception
  {
    super(null, EFFECT, StreamUtils.getByteBuffer(264), 0);
  }

  public Effect2(AbstractStruct superStruct, ByteBuffer buffer, int offset, int number) throws Exception
  {
    super(superStruct, EFFECT + " " + number, buffer, offset);
  }

  public Effect2(AbstractStruct superStruct, ByteBuffer buffer, int offset, String name) throws Exception
  {
    super(superStruct, name, buffer, offset);
  }

//--------------------- Begin Interface AddRemovable ---------------------

  @Override
  public boolean canRemove()
  {
    return true;
  }

//--------------------- End Interface AddRemovable ---------------------

  @Override
  public int read(ByteBuffer buffer, int offset) throws Exception
  {
    addField(new TextString(buffer, offset, 4, COMMON_SIGNATURE));
    addField(new TextString(buffer, offset + 4, 4, COMMON_VERSION));
    EffectType type = new EffectType(buffer, offset + 8, 4);
    addField(type);
    final List<StructEntry> list = new ArrayList<>();
    offset = type.readAttributes(buffer, offset + 12, list);
    addFields(getFields().size() - 1, list);

    list.clear();
    offset = readCommon(list, buffer, offset);
    addFields(getFields().size() - 1, list);

    return offset;
  }

  /**
   * Creates a copy of the current structure, optionally converted to the EFF V1.0 format.
   * @param asV1 {@code true} if result should be of {@link Effect} type.
   * @return A copy of the current instance.
   * @throws Exception
   */
  public Object clone(boolean asV1) throws Exception
  {
    StructEntry retVal = null;

    if (asV1) {
      ByteBuffer src = getDataBuffer().order(ByteOrder.LITTLE_ENDIAN);
      ByteBuffer dst = StreamUtils.getByteBuffer(48);
      byte[] resref = new byte[8];

      dst.putShort((short)src.getInt(0x08)); // Type
      dst.put((byte)src.getInt(0x0c)); // Target
      dst.put((byte)src.getInt(0x10)); // Power
      dst.putInt(src.getInt(0x14));  // Parameter 1
      dst.putInt(src.getInt(0x18));  // Parameter 2
      dst.put((byte)src.getInt(0x1c)); // Timing mode
      dst.put((byte)src.getInt(0x54)); // Dispel/Resistance
      dst.putInt(src.getInt(0x20));  // Duration
      dst.put((byte)src.getShort(0x24));  // Probability 1
      dst.put((byte)src.getShort(0x26));  // Probability 2
      src.position(0x28);
      src.get(resref);
      dst.put(resref);  // Resource
      src.position(0);
      dst.putInt(src.getInt(0x30));  // # dice thrown/maximum level
      dst.putInt(src.getInt(0x34));  // Dice size/minimum level
      dst.putInt(src.getInt(0x38));  // Save type
      dst.putInt(src.getInt(0x3c));  // Save bonus
      dst.putInt(src.getInt(0x40));  // Special
      dst.position(0);

      int offset = getOffset();
      retVal = new Effect(null, dst, 0, getName());
      retVal.setOffset(offset);
      ((AbstractStruct)retVal).realignStructOffsets();
    } else {
      retVal = (StructEntry)clone();
    }

    return retVal;
  }
}
