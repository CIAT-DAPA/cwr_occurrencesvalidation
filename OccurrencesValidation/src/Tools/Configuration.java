 /**
  * Copyright 2014 International Center for Tropical Agriculture (CIAT).
  * This file is part of:
  * Crop Wild Relatives
  * It is free software: You can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * at your option) any later version.
  * It is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * See <http://www.gnu.org/licenses/>.
  */

package Tools;

import Models.DataBase.DBFile;
import java.util.HashMap;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class Configuration {
    
    public static String PATH_FILE="conf.txt";
    public static HashMap VARS;
    
    /**
     * Method that return value for configuration all app
     * @param key name of parameter
     * @return 
     */
    public static String getParameter(String key)
    {
        String value=null;
        try {
            if(Configuration.VARS == null )
            {
                DBFile db=new DBFile("=", Configuration.PATH_FILE);                                
                Configuration.VARS =new HashMap<>();
                String line;
                db.open();
                while((line=db.readLine())!=null)
                {
                    if(!line.equals("") && !line.startsWith("#"))
                        Configuration.VARS.put(line.split("=",2)[0].trim(), line.split("=",2)[1].trim());
                }
                db.close();
            }
            value=Configuration.VARS.get(key) != null ? String.valueOf(Configuration.VARS.get(key)) : null;
        }
        catch (Exception ex) 
        {
            System.out.println("Error loading configuration: " + ex);
        }
        return value;
    }
}
