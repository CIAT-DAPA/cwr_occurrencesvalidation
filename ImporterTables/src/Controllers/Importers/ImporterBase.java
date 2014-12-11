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

import Models.DataBase.DBBase;
import Models.DataBase.DBFile;
import java.util.ArrayList;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public abstract class ImporterBase {
    
    /*Members Class*/
    protected ArrayList<String> source;
    protected DBBase db;
    protected DBFile file;
    protected boolean clean;
    protected String log;
    
    /*Propeties*/
    public String getNextLine()
    {
        return this.file.readLine();
    }
    
    public ArrayList<String> getNextLineSplit()
    {
        return this.file.readLineSplit();
    }
    
    /**
     * Method Construct
     * @param db Connection to database
     * @param split Expression to split row
     * @param file Path file source
     * @param clean Specific if the values should cleaner
     * @param log Path to write log
     */
    public ImporterBase(DBBase db, String split, String file, boolean clean, String log){
        this.db=db;
        this.file=new DBFile(split, file);
        this.clean=clean;
        this.log=log;
    }
    
    /**
     * Method that validate if exist all fields destination with the tables
     * @param destination
     * @return True if exist all fields, false otherwise
     */
    private boolean validateFieldsReal(ArrayList<String> destination)
    {
        for(String s: source)
        {
            if(!destination.contains(s))
            {
                System.out.println("Field didn't find in the database " + s);
                return false;
            }
        }
        return true;
    }
    
    /**
     * Method that initialize the importer
     * @param destination
     * @throws Exception 
     */
    protected void init(ArrayList<String> destination) throws Exception {
        this.file.open();
        if(this.file.isOpen())
        {   
            this.source=this.file.readLineSplit();            
            if(!validateFieldsReal(destination))
                throw new Exception("Error in the load from file for row field names");
        }
        else
            throw new Exception("Error when it tryed to open file");
    }
    
    /**
     * Method that return single String from array list separate by pattern
     * @param data Data to join
     * @param split Pattern to split values
     * @param between If you want that every field be between some other pattern
     * @param caseNullOrEmpty What should put in case that the field has been null or empty
     * @param count Count
     * @param complete What should put in case that the field has been null or empty
     * @return 
     */
    protected String separateByCharacter(ArrayList<String> data,String split,String between, String caseNullOrEmpty, int count, String complete)
    {
        StringBuilder s=new StringBuilder();
        int i=0;
        for(String temp:data)
        {
            //validate if field is null or empty
            temp=temp==null || temp.equals("") ? (caseNullOrEmpty == null ? "null" : caseNullOrEmpty): temp;
            //add data
            s.append((between == null || between.equals("") ? temp : 
                                                            (temp.equals("null") ? temp : between + temp + between)) +split);
            i+=1;
        }
        for(int k=i;k<count;k++)
            s.append(complete+split);
        return s.substring(0, s.length()-1);
    }
    
    /**
     * Method that return single line all fields
     * @return 
     */
    protected String singleSource()
    {
        return separateByCharacter(source,",","","",source.size(),"");
    }
    
    /**
     * Method that start the process import
     */
    public abstract int start();
}
