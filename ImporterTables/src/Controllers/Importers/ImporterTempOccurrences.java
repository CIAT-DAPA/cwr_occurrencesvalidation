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

package Controllers.Importers;

import Models.DataBase.MySQL;
import Tools.Configuration;
import Tools.FixData;
import Tools.Log;
import Tools.TypeLog;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class ImporterTempOccurrences extends ImporterBase {
    
    /*Methods*/
    /**
     * Method Construct
     * @param filePath Path file source
     * @param fileSplit Expression to split values
     * @param clean Specific if the data should cleaner
     * @param log Path to log
     */
    public ImporterTempOccurrences(String filePath, String fileSplit, boolean clean, String log)
    {        
        super(new MySQL(Configuration.getParameter("currie_server"),Configuration.getParameter("currie_schema_base"),Configuration.getParameter("currie_user"), Configuration.getParameter("currie_password")),fileSplit,filePath,clean,log);
        try {
            //Load fields from database
            super.db.getResults( "Select lower(COLUMN_NAME) as COLUMN_NAME " +
                    "From COLUMNS " +
                    "Where TABLE_NAME = 'temp_occurrences' and Table_schema = 'cwr_gapanalysis';");
            ArrayList<String> destination=new ArrayList<String>();
            while(super.db.getRecordSet().next())
                destination.add(super.db.getRecordSet().getString(1));
            init(destination);
        } catch (SQLException ex) {
            System.out.println("Error in load temp_occurrences in the database " + ex);
        } catch (Exception ex) {
            System.out.println("Error in load temp_occurrences " + ex);
        }
    }
    
    @Override
    public int start()
    {
        int errors=0;
        System.out.println("Start process to import");
        //Changed database
        super.db=new MySQL(Configuration.getParameter("currie_server"),Configuration.getParameter("currie_schema_gapanalysis"), Configuration.getParameter("currie_user"), Configuration.getParameter("currie_password"));
        String header="INSERT INTO temp_occurrences (" + super.singleSource() + ") VALUES (";
        String values, q="";
        int affected, row=0;
        while((values=file.readLine()) != null)
        {
            try
            {
                affected=0;
                row+=1;
                //Fix data for query
                values=FixData.fixToQuery(values);
                if(super.clean)
                    values=FixData.deleteAccent(values);
                //Start to fix query
                q=header +
                        super.separateByCharacter(FixData.lineSplit(values,file.getSplit()), ",", "'",null,super.source.size(),"''") +
                        ")";
                Log.register(log,TypeLog.REGISTER_OK,String.valueOf(row) + "|" + values, true);
                System.out.println(q);
                affected=db.update(q);
                System.out.println("Row: " + row + " Affected: " + affected);
            }
            catch(Exception e)
            {
                Log.register(log,TypeLog.REGISTER_ERROR,values, false);
                Log.register(log,TypeLog.QUERY_ERROR,String.valueOf(row) + "|" + q, true);
                errors+=1;
                System.out.println("Error register: " + row + " " + e);
            }
        }
        System.out.println("End process");
        return errors;
    }
}
