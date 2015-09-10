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

package Models.Occurrences.Repository;

import Models.DataBase.MySQL;
import Models.DataBase.ResultQuery;
import Models.Occurrences.Source.TempOccurrences;
import Tools.Configuration;
import Tools.FixData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class RepositoryTempOccurrences extends BaseRepository {    
   
    /**
     * Method Construct
     */
    public RepositoryTempOccurrences(){
        super(Configuration.getParameter("currie_table_temp_occurrences"));
    }
    
    /*Methods CRUD*/
    public ResultQuery add(TempOccurrences entity) throws SQLException{
        return super.add(entity);
    }
    
    /**
     * Query for bring the fields mandatory for cross check process
     * @throws SQLException 
     */
    public void listCrossCheck(String condition) throws SQLException{
        db.getResults("Select id,old_id,availability,cult_stat,source,origin_stat,is_hybrid,filename, " +
                    "country,adm1,adm2,adm3,local_area,locality, " +
                    "iso, " +
                    "x1_rank1,x1_rank2, " +
                    "filename,username,provider_institute_id,field_collected_data,data_public_access,citation,source, " +
                    "x1_genus,x1_sp1, x1_sp2, x1_sp3, " +
                    "tnrs_final_taxon,taxstand_final_taxon, " +
                    "lat_deg,lat_min,lat_sec, " +
                    "long_deg,long_min,long_sec, " +
                    "ns,ew, " +
                    "latitude,longitude, " +
                    "taxon_final, " +
                    "final_iso2, " +
                    "coord_source, " +
                    "latitude_georef,longitude_georef, " +
                    "comments, " +
                    "grin_final_taxon, " +
                    "iso2, " +
                    "From " + super.getTable() + " " +
                    (condition != null && !condition.equals("") ? "Where " + condition : ""));
    }
    
    /**
     * Method that genera a special table with information of occurrences
     * @return HashMap of HashMap's
     * @throws SQLException 
     */
    public HashMap summary() throws SQLException{
        HashMap a=new HashMap(), temp;
        String field, listFields="";        
        // nulls
        for(String f : FixData.valueParameterSplit(Configuration.getParameter("summary_fields_nulls")))
            listFields+="'" + f + "',";
        dbInformationSchema.getResults("Select COLUMN_NAME " +
                                       "From COLUMNS " +
                                       "Where TABLE_NAME = '" + getTable() +"' and Table_schema = '" + Configuration.getParameter("currie_schema_gapanalysis") + "' and " + 
                                           "COLUMN_NAME in (" + listFields.substring(0,listFields.length()-1) + ") ;" );
        while(dbInformationSchema.getRecordSet().next())
        {
            field=dbInformationSchema.getRecordSet().getString("COLUMN_NAME");
            temp=new HashMap();
            db.getResults("Select 'null', count(*) " +
                            "From " + getTable() + " " +
                            "where " + field +" is null " +
                            "Union " +
                            "Select 'not null', count(*) " +
                            "From " + getTable() + " " +
                            "where " + field +" is not null " +
                            "Union " +
                            "Select 'Blank', count(*) " +
                            "From " + getTable() + " " +
                            "where " + field +" = ''; ");
            while(db.getRecordSet().next())
                temp.put(FixData.getValue(db.getRecordSet().getString(1)), db.getRecordSet().getString(2));
            a.put(field, temp);
        }
        // values
        listFields="";
        for(String f : FixData.valueParameterSplit(Configuration.getParameter("summary_fields_values")))
            listFields+="'" + f + "',";
        dbInformationSchema.getResults("Select COLUMN_NAME " +
                                       "From COLUMNS " +
                                       "Where TABLE_NAME = '" + getTable() +"' and Table_schema = '" + Configuration.getParameter("currie_schema_gapanalysis") + "' and " + 
                                           "COLUMN_NAME in (" + listFields.substring(0,listFields.length()-1) + ") ;" );
        while(dbInformationSchema.getRecordSet().next())
        {
            field=dbInformationSchema.getRecordSet().getString("COLUMN_NAME");
            temp=new HashMap();
            db.getResults("Select distinct " + field + ", count(*) as count " +
                          "From " + getTable() + " " +
                          "Group by " + field + " " +
                          "Order by " + field + ";");
            while(db.getRecordSet().next())
                temp.put(FixData.getValue(db.getRecordSet().getString(field)), db.getRecordSet().getString("count"));
            a.put(field, temp);
        }
        return a;
    }
    
    /**
     * Method that genera a special table with information of occurrences
     * @return HashMap of HashMap's
     * @throws SQLException 
     */
    public HashMap compare(String tableCompare, String fields,String condition) throws SQLException{
        HashMap a=new HashMap(), temp;
        String field="";        
        long temp_occurrences, compare, i=1;        
        //List fields
        dbInformationSchema.getResults("Select COLUMN_NAME " +
                                       "From COLUMNS " +
                                       "Where TABLE_NAME = '" + getTable() +"' and Table_schema = '" + Configuration.getParameter("currie_schema_gapanalysis") + "' " +
                                            "and COLUMN_NAME in (" + fields + ");" );
        MySQL dbCompare=new MySQL(Configuration.getParameter("currie_server"),Configuration.getParameter("currie_schema_gapanalysis"), Configuration.getParameter("currie_user"), Configuration.getParameter("currie_password"));
        while(dbInformationSchema.getRecordSet().next())
        {            
            field=dbInformationSchema.getRecordSet().getString("COLUMN_NAME");
            db.getResults("Select distinct " + field + " " + ", count(*) as count " +
                          "From " + getTable() + " " +
                          "Group by " + field + " " +
                          "Order by " + field + ";");            
            while(db.getRecordSet().next())
            {
                temp = new HashMap();  
                System.out.println(field + " - " + db.getRecordSet().getString(1));
                temp.put("field", field);                
                temp.put("value", db.getRecordSet().getString(1));
                temp_occurrences = db.getRecordSet().getInt(2);
                temp.put("temp_occurrences", temp_occurrences);
                dbCompare.getResults("Select count(*) " +
                                     "From " + tableCompare + " " +
                                     "Where " + field + "='" + db.getRecordSet().getString(1) + "' " +
                                        (!condition.equals("") ? "and " + condition : "") +";");
                compare = dbCompare.getRecordSet().next() ? dbCompare.getRecordSet().getInt(1) : 0;
                temp.put("compare", compare);
                temp.put("difference", temp_occurrences-compare);
                a.put(String.valueOf(i), temp);
                i+=1;
            }
        }
        return a;
    }
    
    /**
     * Method that genera a special table with information of occurrences
     * @return HashMap of HashMap's
     * @throws SQLException 
     */
    public HashMap compareSimple(String table,String condition,String detailFields) throws SQLException{
        HashMap a=new HashMap(), temp;
        String prefix_temp=Configuration.getParameter("compare_prefix_temporal"),prefix_occurrences=Configuration.getParameter("compare_prefix_occurrence");
        String listFields=prefix_temp + ".old_id,"; 
        for(String f : FixData.valueParameterSplit(detailFields))
            listFields+=prefix_temp + "." + f + " as " + prefix_temp + "_" + f + "," + prefix_occurrences + "." + f + " as " + prefix_occurrences + "_" + f + "," ;
        listFields=listFields.substring(0,listFields.length()-1);
        db.getResults("Select " + listFields + " " +
                                       "From " + getTable() + " as " + prefix_temp + " " +
                                            "inner join " + table + " as " + prefix_occurrences + " " +
                                                " on " + prefix_temp + ".old_id=" + prefix_occurrences + ".old_id " +
                                       (condition.equals("") ? "" : "Where " + condition + " ") +
                                       "Order by " + prefix_temp + ".old_id");
        ArrayList<String> lastFields=db.getColumnNames();
        a.put("COLUMNS", FixData.concatenate(lastFields.toArray(new String[lastFields.size()]), "|"));
        int k=1;
        while(db.getRecordSet().next())
        {
            temp=new HashMap();
            for(int i=0;i<lastFields.size();i++)
                temp.put(lastFields.get(i),db.getRecordSet().getString(lastFields.get(i)));
            a.put(String.valueOf(k), temp);
            k+=1;
        }
        return a;
    }
}
