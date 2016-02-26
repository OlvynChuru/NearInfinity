// Near Infinity - An Infinity Engine Browser and Editor
// Copyright (C) 2001 - 2005 Jon Olav Hauglid
// See LICENSE.txt for license information

package org.infinity.resource.are;

import javax.swing.JComponent;

import org.infinity.datatype.Bitmap;
import org.infinity.datatype.DecNumber;
import org.infinity.datatype.Flag;
import org.infinity.datatype.HexNumber;
import org.infinity.datatype.ResourceRef;
import org.infinity.datatype.StringRef;
import org.infinity.datatype.TextString;
import org.infinity.datatype.Unknown;
import org.infinity.gui.StructViewer;
import org.infinity.resource.AbstractStruct;
import org.infinity.resource.AddRemovable;
import org.infinity.resource.HasAddRemovable;
import org.infinity.resource.HasViewerTabs;
import org.infinity.resource.Profile;
import org.infinity.resource.StructEntry;
import org.infinity.resource.vertex.Vertex;

public final class Container extends AbstractStruct implements AddRemovable, HasVertices, HasViewerTabs,
                                                               HasAddRemovable
{
  // ARE/Container-specific field labels
  public static final String ARE_CONTAINER                            = "Container";
  public static final String ARE_CONTAINER_NAME                       = "Name";
  public static final String ARE_CONTAINER_LOCATION_X                 = "Location: X";
  public static final String ARE_CONTAINER_LOCATION_Y                 = "Location: Y";
  public static final String ARE_CONTAINER_TYPE                       = "Type";
  public static final String ARE_CONTAINER_LOCK_DIFFICULTY            = "Lock difficulty";
  public static final String ARE_CONTAINER_FLAGS                      = "Flags";
  public static final String ARE_CONTAINER_TRAP_DETECTION_DIFFICULTY  = "Trap detection difficulty";
  public static final String ARE_CONTAINER_TRAP_REMOVAL_DIFFICULTY    = "Trap removal difficulty";
  public static final String ARE_CONTAINER_TRAPPED                    = "Is trapped?";
  public static final String ARE_CONTAINER_TRAP_DETECTED              = "Is trap detected?";
  public static final String ARE_CONTAINER_LAUNCH_POINT_X             = "Launch point: X";
  public static final String ARE_CONTAINER_LAUNCH_POINT_Y             = "Launch point: Y";
  public static final String ARE_CONTAINER_BOUNDING_BOX_LEFT          = "Bounding box: Left";
  public static final String ARE_CONTAINER_BOUNDING_BOX_TOP           = "Bounding box: Top";
  public static final String ARE_CONTAINER_BOUNDING_BOX_RIGHT         = "Bounding box: Right";
  public static final String ARE_CONTAINER_BOUNDING_BOX_BOTTOM        = "Bounding box: Bottom";
  public static final String ARE_CONTAINER_FIRST_ITEM_INDEX           = "First item index";
  public static final String ARE_CONTAINER_NUM_ITEMS                  = "# items";
  public static final String ARE_CONTAINER_SCRIPT_TRAP                = "Trap script";
  public static final String ARE_CONTAINER_FIRST_VERTEX_INDEX         = "First vertex index";
  public static final String ARE_CONTAINER_NUM_VERTICES               = "# vertices";
  public static final String ARE_CONTAINER_ACTIVATION_RANGE           = "Activation range";
  public static final String ARE_CONTAINER_OWNER_NAME                 = "Owner name";
  public static final String ARE_CONTAINER_KEY                        = "Key";
  public static final String ARE_CONTAINER_LOCKPICK_STRING            = "Lockpick string";

  public static final String[] s_type = { "", "Bag", "Chest", "Drawer", "Pile", "Table", "Shelf",
                                          "Altar", "Non-visible", "Spellbook", "Body", "Barrel", "Crate"};
  public static final String[] s_noyes = {"No", "Yes"};
  public static final String[] s_flag = { "No flags set", "Locked", "", "Magical lock", "Trap resets",
                                          "", "Disabled" };
  public static final String[] s_flag_ee = { "No flags set", "Locked", "", "Magical lock", "Trap resets",
                                             "", "Disabled", "Don't clear" };

  public Container() throws Exception
  {
    super(null, ARE_CONTAINER, new byte[192], 0);
  }

  public Container(AbstractStruct superStruct, byte buffer[], int offset, int nr) throws Exception
  {
    super(superStruct, ARE_CONTAINER + " " + nr, buffer, offset);
  }

// --------------------- Begin Interface HasAddRemovable ---------------------

  @Override
  public AddRemovable[] getAddRemovables() throws Exception
  {
    return new AddRemovable[]{new Vertex(), new Item()};
  }

  @Override
  public AddRemovable confirmAddEntry(AddRemovable entry) throws Exception
  {
    return entry;
  }

  @Override
  public boolean confirmRemoveEntry(AddRemovable entry) throws Exception
  {
    return true;
  }

// --------------------- End Interface HasAddRemovable ---------------------


//--------------------- Begin Interface AddRemovable ---------------------

  @Override
  public boolean canRemove()
  {
    return true;
  }

//--------------------- End Interface AddRemovable ---------------------


// --------------------- Begin Interface HasViewerTabs ---------------------

  @Override
  public int getViewerTabCount()
  {
    return 1;
  }

  @Override
  public String getViewerTabName(int index)
  {
    return StructViewer.TAB_VIEW;
  }

  @Override
  public JComponent getViewerTab(int index)
  {
    return new ViewerContainer(this);
  }

  @Override
  public boolean viewerTabAddedBefore(int index)
  {
    return true;
  }

// --------------------- End Interface HasViewerTabs ---------------------


// --------------------- Begin Interface HasVertices ---------------------

  @Override
  public void readVertices(byte buffer[], int offset) throws Exception
  {
    int firstVertex = ((DecNumber)getAttribute(ARE_CONTAINER_FIRST_VERTEX_INDEX)).getValue();
    int numVertices = ((DecNumber)getAttribute(ARE_CONTAINER_NUM_VERTICES)).getValue();
    offset += firstVertex << 2;
    for (int i = 0; i < numVertices; i++) {
      addField(new Vertex(this, buffer, offset + 4 * i, i));
    }
  }

  @Override
  public int updateVertices(int offset, int number)
  {
    ((DecNumber)getAttribute(ARE_CONTAINER_FIRST_VERTEX_INDEX)).setValue(number);
    int count = 0;
    for (int i = 0; i < getFieldCount(); i++) {
      StructEntry entry = getField(i);
      if (entry instanceof Vertex) {
        entry.setOffset(offset);
        ((Vertex)entry).realignStructOffsets();
        offset += 4;
        count++;
      }
    }
    ((DecNumber)getAttribute(ARE_CONTAINER_NUM_VERTICES)).setValue(count);
    return count;
  }

// --------------------- End Interface HasVertices ---------------------

  @Override
  protected void setAddRemovableOffset(AddRemovable datatype)
  {
    if (datatype instanceof Vertex) {
      int index = ((DecNumber)getAttribute(ARE_CONTAINER_FIRST_VERTEX_INDEX)).getValue();
      index += ((DecNumber)getAttribute(ARE_CONTAINER_NUM_VERTICES)).getValue();
      int offset = ((HexNumber)getSuperStruct().getAttribute(AreResource.ARE_OFFSET_VERTICES)).getValue();
      datatype.setOffset(offset + 4 * index);
      ((AbstractStruct)datatype).realignStructOffsets();
    }
    else if (datatype instanceof Item) {
      int index = ((DecNumber)getAttribute(ARE_CONTAINER_FIRST_ITEM_INDEX)).getValue();
      index += ((DecNumber)getAttribute(ARE_CONTAINER_NUM_ITEMS)).getValue();
      int offset = ((HexNumber)getSuperStruct().getAttribute(AreResource.ARE_OFFSET_ITEMS)).getValue();
      datatype.setOffset(offset + 20 * index);
      ((AbstractStruct)datatype).realignStructOffsets();
    }
  }

  public void readItems(byte buffer[], int offset) throws Exception
  {
    int firstIndex = ((DecNumber)getAttribute(ARE_CONTAINER_FIRST_ITEM_INDEX)).getValue();
    int numItems = ((DecNumber)getAttribute(ARE_CONTAINER_NUM_ITEMS)).getValue();
    offset += firstIndex * 20;
    for (int i = 0; i < numItems; i++) {
      addField(new Item(this, buffer, offset + 20 * i, i));
    }
//    return offset + numItems * 20;
  }

  public int updateItems(int offset, int number)
  {
    ((DecNumber)getAttribute(ARE_CONTAINER_FIRST_ITEM_INDEX)).setValue(number);
    int count = 0;
    for (int i = 0; i < getFieldCount(); i++) {
      StructEntry entry = getField(i);
      if (entry instanceof Item) {
        entry.setOffset(offset);
        ((Item)entry).realignStructOffsets();
        offset += 20;
        count++;
      }
    }
    ((DecNumber)getAttribute(ARE_CONTAINER_NUM_ITEMS)).setValue(count);
    return count;
  }

  @Override
  public int read(byte buffer[], int offset) throws Exception
  {
    addField(new TextString(buffer, offset, 32, ARE_CONTAINER_NAME));
    addField(new DecNumber(buffer, offset + 32, 2, ARE_CONTAINER_LOCATION_X));
    addField(new DecNumber(buffer, offset + 34, 2, ARE_CONTAINER_LOCATION_Y));
    addField(new Bitmap(buffer, offset + 36, 2, ARE_CONTAINER_TYPE, s_type));
    addField(new DecNumber(buffer, offset + 38, 2, ARE_CONTAINER_LOCK_DIFFICULTY));
    if (Profile.isEnhancedEdition()) {
      addField(new Flag(buffer, offset + 40, 4, ARE_CONTAINER_FLAGS, s_flag_ee));
    } else {
      addField(new Flag(buffer, offset + 40, 4, ARE_CONTAINER_FLAGS, s_flag));
    }
    addField(new DecNumber(buffer, offset + 44, 2, ARE_CONTAINER_TRAP_DETECTION_DIFFICULTY));
    addField(new DecNumber(buffer, offset + 46, 2, ARE_CONTAINER_TRAP_REMOVAL_DIFFICULTY));
    addField(new Bitmap(buffer, offset + 48, 2, ARE_CONTAINER_TRAPPED, s_noyes));
    addField(new Bitmap(buffer, offset + 50, 2, ARE_CONTAINER_TRAP_DETECTED, s_noyes));
    addField(new DecNumber(buffer, offset + 52, 2, ARE_CONTAINER_LAUNCH_POINT_X));
    addField(new DecNumber(buffer, offset + 54, 2, ARE_CONTAINER_LAUNCH_POINT_Y));
    addField(new DecNumber(buffer, offset + 56, 2, ARE_CONTAINER_BOUNDING_BOX_LEFT));
    addField(new DecNumber(buffer, offset + 58, 2, ARE_CONTAINER_BOUNDING_BOX_TOP));
    addField(new DecNumber(buffer, offset + 60, 2, ARE_CONTAINER_BOUNDING_BOX_RIGHT));
    addField(new DecNumber(buffer, offset + 62, 2, ARE_CONTAINER_BOUNDING_BOX_BOTTOM));
    addField(new DecNumber(buffer, offset + 64, 4, ARE_CONTAINER_FIRST_ITEM_INDEX));
    addField(new DecNumber(buffer, offset + 68, 4, ARE_CONTAINER_NUM_ITEMS));
    addField(new ResourceRef(buffer, offset + 72, ARE_CONTAINER_SCRIPT_TRAP, "BCS"));
    addField(new DecNumber(buffer, offset + 80, 4, ARE_CONTAINER_FIRST_VERTEX_INDEX));
    addField(new DecNumber(buffer, offset + 84, 2, ARE_CONTAINER_NUM_VERTICES));
    addField(new DecNumber(buffer, offset + 86, 2, ARE_CONTAINER_ACTIVATION_RANGE));
    addField(new TextString(buffer, offset + 88, 32, ARE_CONTAINER_OWNER_NAME));
    addField(new ResourceRef(buffer, offset + 120, ARE_CONTAINER_KEY, "ITM"));
    addField(new Unknown(buffer, offset + 128, 4));
    addField(new StringRef(buffer, offset + 132, ARE_CONTAINER_LOCKPICK_STRING));
    addField(new Unknown(buffer, offset + 136, 56));
    return offset + 192;
  }
}
