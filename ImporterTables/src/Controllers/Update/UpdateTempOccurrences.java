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

package Controllers.Update;

import Models.DataBase.MySQL;
import Tools.Configuration;
import Tools.Log;
import Tools.TypeLog;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class UpdateTempOccurrences extends UpdateBase
{
    
    /**
     * Method Construct     
     * @param file Path file source
     * @param log Path of log
     */
    public UpdateTempOccurrences(String file, String log) {
        super(new MySQL(Configuration.getParameter("currie_server"),Configuration.getParameter("currie_schema_gapanalysis"),Configuration.getParameter("currie_user"), Configuration.getParameter("currie_password")), file,log);
    }

    @Override
    public long start() 
    {
        long a=0;
        try
        {
            file.open();
            String line;
            while((line=file.readLine()) != null)
            {
                try
                {
                    a+=db.update(line);
                    System.out.println(line);
                }
                catch(Exception ex)
                {
                    System.out.println(ex);
                    Log.register(log,TypeLog.QUERY_ERROR, line + "|" + ex.toString(), true,"TempOccuUpdate",Configuration.getParameter("log_ext_review"));
                }
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return a;
    }

}
