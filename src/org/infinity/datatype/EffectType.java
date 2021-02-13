// Near Infinity - An Infinity Engine Browser and Editor
// Copyright (C) 2001 - 2019 Jon Olav Hauglid
// See LICENSE.txt for license information

package org.infinity.datatype;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.infinity.resource.AbstractStruct;
import org.infinity.resource.EffectFactory;
import org.infinity.resource.StructEntry;

public final class EffectType extends Bitmap implements UpdateListener
{
  // EffectType-specific field labels
  public static final String EFFECT_TYPE        = "Type";
  public static final String EFFECT_TYPE_TARGET = "Target";
  public static final String EFFECT_TYPE_POWER  = "Power";

  private static final String s_target[] = {"None", "Self", "Preset target",
                                            "Party", "Everyone", "Everyone except party",
                                            "Caster group", "Target group", "Everyone except self", "Original caster"};
											
  public static final String[] s_proj_iwd2_effect = {"None", "Arrow",
    "Arrow exploding", "Arrow flaming", "Arrow heavy", "Arrow non-magical",
    "Axe", "Axe exploding", "Axe flaming", "Axe heavy", "Axe non-magical",
    "Bolt", "Bolt exploding", "Bolt flaming", "Bolt heavy", "Bolt non-magical",
    "Bullet", "Bullet exploding", "Bullet flaming", "Bullet heavy",
    "Bullet non-magical", "Burning hands", "Call lightning", "Chromatic orb",
    "Cone of cold", "Cone of fire", "Dagger", "Dagger exploding",
    "Dagger flaming", "Dagger heavy", "Dagger non-magical", "Dart",
    "Dart exploding", "Dart flaming", "Dart heavy", "Dart non-magical",
    "BG magic missile", "Fireball", "Ice fragments", "Lightning bolt",
    "Skipping stone", "Sleep", "Skeleton animation", "Smoke ball",
    "Smoke large", "Smoke small", "Sparkle blue", "Sparkle gold",
    "Sparkle purple", "Sparkle ice", "Sparkle stone", "Sparkle black",
    "Sparkle chromatic", "Sparkle red", "Sparkle green", "Spear",
    "Spear exploding", "Spear flaming", "Spear heavy", "Spear non-magical",
    "Star sprite", "Stoned", "Web travel", "Web ground", "Gaze", "Holy might",
    "Flame strike", "Magic missiles 1", "Magic missiles 2", "Magic missiles 3",
    "Magic missiles 4", "Magic missiles 5", "Magic missiles 6",
    "Magic missiles 7", "Magic missiles 8", "Magic missiles 9",
    "Magic missiles 10", "Magic missiles 11", "Invisible traveling",
    "Fire bolt", "Call lightning chain 1", "Call lightning chain 2",
    "Call lightning chain 3", "Call lightning chain 4",
    "Call lightning chain 5", "Call lightning chain 6",
    "Call lightning chain 7", "Call lightning chain 8",
    "Call lightning chain 9", "Call lightning chain 10",
    "Call lightning chain 11", "Fire storm", "Call lightning storm",
    "Instant area effect", "Stinking cloud", "Skulltrap", "Color spray",
    "Ice storm", "Fire wall", "Glyph", "Grease", "Flame arrow green",
    "Flame arrow blue", "Fireball green", "Fireball blue", "Potion",
    "Potion exploding", "Acid blob", "Agannazar's scorcher", "Travel door",
    "Glow necromancy", "Glow alteration", "Glow enchantment", "Glow abjuration",
    "Glow illusion", "Glow conjure", "Glow invocation", "Glow divination",
    "Hit necromancy air", "Hit necromancy earth", "Hit necromancy water",
    "Hit alteration air", "Hit alteration earth", "Hit alteration water",
    "Hit enchantment air", "Hit enchantment earth", "Hit enchantment water",
    "Hit abjuration air", "Hit abjuration earth", "Hit abjuration water",
    "Hit illusion air", "Hit illusion earth", "Hit illusion water",
    "Hit conjure air", "Hit conjure earth", "Hit conjure water",
    "Hit invocation air", "Hit invocation earth", "Hit invocation water",
    "Hit divination air", "Hit divination earth", "Hit divination water",
    "Hit mushroom fire", "Hit mushroom grey", "Hit mushroom green",
    "Hit shaft fire", "Hit shaft light", "Hit swirl white", "Sparkle area blue",
    "Sparkle area gold", "Sparkle area purple", "Sparkle area ice",
    "Sparkle area stone", "Sparkle area black", "Sparkle area chromatic",
    "Sparkle area red", "Sparkle area green", "Instant area (party only)",
    "Instant area (not party)", "Sparkle area blue (party only)",
    "Sparkle area gold (party only)", "Sparkle area purple (party only)",
    "Sparkle area ice (party only)", "Sparkle area stone (party only)",
    "Sparkle area black (party only)", "Sparkle area chromatic (party only)",
    "Sparkle area red (party only)", "Sparkle area green (party only)",
    "Sparkle area blue (not party)", "Sparkle area gold (not party)",
    "Sparkle area purple (not party)", "Sparkle area ice (not party)",
    "Sparkle area stone (not party)", "Sparkle area black (not party)",
    "Sparkle area chromatic (not party)", "Sparkle area red (not party)",
    "Sparkle area green (not party)", "Sparkle area magenta (not party)",
    "Sparkle area orange (not party)", "Sparkle area magenta (party only)",
    "Sparkle area orange (party only)", "Sparkle area magenta",
    "Sparkle area orange", "Sparkle magenta", "Sparkle orange",
    "Non-sprite area", "Cloudkill", "Flame arrow ice", "Cow", "Hold person",
    "Scorcher ice", "Acid blob mustard", "Acid blob grey", "Acid blob ochre",
    "Red holy might", "Hit necromancy area", "Hit alteration area",
    "Hit enchantment area", "Hit abjuration area", "Hit illusion area",
    "Hit conjure area", "Hit invocation area", "Hit divination area",
    "Fireball white", "Instant area effect small", "Lightning bolt ground",
    "Lightning no bounce", "Hit finger of death", "Malavon's rage",
    "Chain lightning", "Acid storm", "Death fog", "Spike stones",
    "Incendiary cloud", "Produce fire", "Insect plague",
    "Snilloc's snowball swarm", "Magic missile", "Hit abjuration",
    "Hit alteration", "Hit invocation", "Hit necromancy", "Hit conjuration",
    "Hit enchantment", "Hit illusion", "Hit divination", "Travel abjuration",
    "Travel alteration", "Travel invocation", "Travel necromancy",
    "Travel conjuration", "Travel enchantment", "Travel illusion",
    "Travel divination", "Entangle", "Bless", "Curse", "Remove fear",
    "Detect evil", "Detect invisibility", "Horror", "Resist fear", "Chant",
    "Find traps", "Silence 15' radius", "Dispel magic", "Haste", "Slow",
    "Hold animal", "Remove paralysis", "Icelance", "Strength of one", "Prayer",
    "Confusion (wizard)", "Emotion", "Malison", "Defensive harmony",
    "Protection from evil 10' radius", "Cloak of fear", "Recitation",
    "Otiluke's resilient sphere", "Static charge", "Hold monster", "Chaos",
    "Shroud of flame", "Righteous wrath of the faithful", "Death spell",
    "Disintegrate", "Otiluke's freezing sphere", "Fire seed",
    "Sol's searing orb", "Prismatic spray", "Mass invisibility", "Sunray",
    "Confusion (priest)", "Symbol of pain", "Symbol of hopelessness",
    "Power word, kill", "Malavon's corrosive fog", "Salamander aura",
    "Umber hulk gaze", "Bombardier beetle cloud", "Zombie lord aura",
    "Iron golem cloud", "Myconid spores", "Incendiary cloud explosion",
    "Incendiary cloud idling", "Heavenly inferno", "Area hit monster summoning",
    "Area hit animal summoning", "Area hit conjure earth elemental",
    "Area hit conjure fire elemental", "Area hit conjure water elemental",
    "Portal animation open", "Horror trap", "Winter wolf breath",
    "Portal animation close", "Alicorn lance", "Soul eater", "Spike growth",
    "Cloudburst", "Smashing wave", "Thorn spray", "Wall of moonlight",
    "Whirlwind", "Earthquake", "Mist of eldath", "Circle of bones",
    "Cloud of pestilence", "Undead ward", "Blade barrier", "Spiritual wrath",
    "Lance of disruption", "Mordenkainen's force missiles", "Shout",
    "Vitriolic sphere", "Suffocate", "Abi-dalzim's horrid wilting",
    "Great shout", "Mournful wail", "Death knell", "War cry", "Undying lament",
    "Mordenkainen's force missiles 1", "Mordenkainen's force missiles 2",
    "Mordenkainen's force missiles 3", "Mordenkainen's force missiles 4",
    "Mordenkainen's force missiles 5", "Mordenkainen's force missiles 6",
    "Mordenkainen's force missiles 7", "Mordenkainen's force missiles 8",
    "Mordenkainen's force missiles 9", "Mordenkainen's force missiles 10",
    "Mordenkainen's force missiles 11", "Sunfire", "Power word, blind",
    "Holy smite", "Unholy blight", "Greater command", "Holy word",
    "Unholy word", "Great roar", "Will-o-wisp spray",
    "Retribution (single projectile)", "Retribution", "Sekolah's fire",
    "Blue glow", "Dragon gem cutscene", "Dragon breath",
    "Crypt thing teleport", "Mustard jelly vapor", "Summon cornugons",
    "Container glow", "Container glow bad", "Crypt thing teleport (fighter)",
    "Crypt thing teleport (thief)", "Hold undead", "Invisibility 10' radius",
    "Mass cure", "Delayed blast fireball", "Area hit gate",
    "Wail of the banshee", "Symbol, fear", "Symbol, stun", "Symbol, death",
    "Meteor swarm", "Improved haste", "Frost fingers", "Gedlee's electric loop",
    "Wall of fire", "Aura of vitality", "Banishment", "Mass dominate",
    "Mind flayer psionic blast", "Boulder", "Turn undead", "Mind fog",
    "Half-dragon acid breath", "Half-dragon fire breath",
    "Half-dragon lightning breath", "Guardian acid breath", "Ultrablast",
    "Boulder big (trap)", "Fire trap", "Acid trap", "Chimera fire breath"};

  private int attr_length;

  public EffectType(ByteBuffer buffer, int offset, int length)
  {
    super(buffer, offset, length, EFFECT_TYPE, EffectFactory.getFactory().getEffectNameArray());
  }

// --------------------- Begin Interface Editable ---------------------

  @Override
  public boolean updateValue(AbstractStruct struct)
  {
    super.updateValue(struct);
    try {
      final List<StructEntry> list = new ArrayList<>();
      readAttributes(struct.removeFromList(this, attr_length), 0, list);
      for (int i = 0; i < list.size(); i++) {
        StructEntry entry = list.get(i);
        entry.setOffset(entry.getOffset() + getOffset() + getSize());
      }
      struct.addFields(this, list);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

// --------------------- End Interface Editable ---------------------

// --------------------- Begin Interface UpdateListener ---------------------

  @Override
  public boolean valueUpdated(UpdateEvent event)
  {
    try {
      return EffectFactory.updateOpcode(event.getStructure());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

// --------------------- End Interface UpdateListener ---------------------

  public int readAttributes(ByteBuffer buffer, int off, List<StructEntry> list)
  {
    attr_length = off;
    boolean isV1 = (getSize() == 2);
    if (isV1) {
      // EFF V1.0
      list.add(new Bitmap(buffer, off, 1, EFFECT_TYPE_TARGET, s_target));
      list.add(new DecNumber(buffer, off + 1, 1, EFFECT_TYPE_POWER));
      off += 2;
    }
    else {
      // EFF V2.0
      list.add(new Bitmap(buffer, off, 4, EFFECT_TYPE_TARGET, s_target));
      list.add(new DecNumber(buffer, off + 4, 4, EFFECT_TYPE_POWER));
      off += 8;
    }
    try {
      off = EffectFactory.getFactory().makeEffectStruct(this, buffer, off, list, getValue(), isV1);
    } catch (Exception e) {
      e.printStackTrace();
    }
    attr_length = off - attr_length;
    return off;
  }
}
