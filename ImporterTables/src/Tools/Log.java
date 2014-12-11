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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class Log {
    
    /**
     * Method that register all information
     * @param path Path when the log will save
     * @param type Type Log
     * @param line line to write
     * @param addDate True if you want add date and time for register, false otherwise
     * @return 
     */
    public static boolean register(String path,TypeLog type,String line, boolean addDate)
    {
        boolean a=false;
        PrintWriter writer = null;
        try 
        {
            writer = new PrintWriter(new BufferedWriter(new FileWriter((path.endsWith("\\") ? path+type.name() : path +"\\" + type.name()) + ".txt", true)));
            writer.println((addDate ? FixData.getDateTime() + "|" : "") + line);
            writer.close();
            a=true;
        }
        catch (IOException  ex) {
            System.out.println(ex);
        } 
        finally {
            try { writer.close(); }
            catch (Exception ex) { System.out.println(ex); }
        }
        return a;
    }
    
    /**
     * Method that valid if a log exist
     * @param path path to search log file
     * @param type type log
     * @return 
     */
    public static boolean existLog(String path,TypeLog type)
    {
       return new File((path.endsWith("\\") ? path+type.name() : path +"\\" + type.name()) + ".txt").exists();
    }
    
}