// Near Infinity - An Infinity Engine Browser and Editor
// Copyright (C) 2001 - 2005 Jon Olav Hauglid
// See LICENSE.txt for license information

package infinity.util;

import java.util.HashMap;
import java.util.Locale;

import infinity.resource.ResourceFactory;
import infinity.resource.key.ResourceEntry;

public class IniMapCache
{
  private static final HashMap<ResourceEntry, IniMap> map = new HashMap<ResourceEntry, IniMap>();

  public static void cacheInvalid(ResourceEntry entry)
  {
    if (entry != null) {
      map.remove(entry.toString().toUpperCase(Locale.ENGLISH));
    }
  }

  public static void clearCache()
  {
    map.clear();
  }

  public static IniMap get(String name)
  {
    IniMap retVal = null;
    if (name != null) {
      name = name.trim().toUpperCase(Locale.ENGLISH);
      ResourceEntry entry = ResourceFactory.getResourceEntry(name);
      if (entry != null) {
        retVal = get(entry);
      } else {
        System.err.println("Could not find " + name);
      }
    }
    return retVal;
  }

  public static synchronized IniMap get(ResourceEntry entry)
  {
    IniMap retVal = null;
    if (entry != null) {
      retVal = map.get(entry);
      if (retVal == null) {
        retVal = new IniMap(entry);
        map.put(entry, retVal);
      }
    }
    return retVal;
  }

  private IniMapCache() {}
}