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

package Models.Occurrences.Source;

import Models.DataBase.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public abstract class BaseTable {
    /*Members Class*/
    protected HashMap row;
    
    /*Properties*/
    /**
     * Method that return field in int
     * @param field name field
     * @return int
     */
    public int getInt(String field){
        return Integer.parseInt(row.get(field).toString());
    }
    
    /**
     * Method that return field in double
     * @param field name field
     * @return double
     */
    public double getDouble(String field){
        return Double.parseDouble(row.get(field).toString());
    }
    
    /**
     * Method that return field in String
     * @param field name field
     * @return String
     */
    public String getString(String field){
        return row.get(field) == null ? null : row.get(field).toString();
    }
    
    /**
     * Method that return field in Object
     * @param field name field
     * @return Object
     */
    public Object get(String field){
        return row.get(field);
    }
    
    /*Methods*/
    /**
     * Method Construct
     */
    public BaseTable(){
        row=new HashMap();
    }
    
    /**
     * Method that load the current instance
     * @param content ResultSet with data
     * @throws java.sql.SQLException
     */
    public void load(ResultSet content) throws SQLException{
        ResultSetMetaData metadata = content.getMetaData();
        for(int i=1;i<=metadata.getColumnCount();i++)
            row.put(metadata.getColumnName(i), content.getString(metadata.getColumnName(i)));
    }
    
    /**
     * Method that load the current instance
     * @param fields list with name of fields
     * @param values values in the same order that fields
     */
    public void load(ArrayList<Field> fields,ArrayList<String> values){
        for(int i=0;i<fields.size();i++)
        {
            if(fields.get(i).getType().trim().equals("tinyint") || fields.get(i).getType().trim().equals("int") || fields.get(i).getType().trim().equals("double") || fields.get(i).getType().trim().equals("float"))
                row.put(fields.get(i).getName(),i < values.size() && !values.get(i).equals("") ?  values.get(i) : null);
            else
                row.put(fields.get(i).getName(),i < values.size() ?  values.get(i) : "");
        }
    }
    
    
    /***
     * Method that return all attributes of table
     * @return 
     */
    public String[] getAttributes(){
        return (String[])row.keySet().toArray(new String[row.keySet().size()]);
    }
}
