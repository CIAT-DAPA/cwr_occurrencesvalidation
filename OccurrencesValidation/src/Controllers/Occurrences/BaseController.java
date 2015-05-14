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

package Controllers.Occurrences;

import Models.DataBase.BaseUpdate;
import Models.DataBase.DBFile;
import Models.DataBase.Field;
import Models.DataBase.ResultQuery;
import Models.Occurrences.Repository.BaseRepository;
import Models.Occurrences.Source.BaseTable;
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
public abstract class BaseController {
    /*Members Class*/
    protected BaseRepository repository;
    
    /*Methods*/
    /**
     * Method Construct
     * @param repository 
     */
    public BaseController(BaseRepository repository){
        this.repository=repository;
    }
    
    /**
     * Method that import file to database
     * @param filePath Path of source file
     * @param fileSplit Pattern to split line
     * @param clean True if you want clean data of accents
     * @param entity instance of table to save
     * @param log Path to log
     * @param prefixLog Prefix for the log
     * @return Count of errors
     * @throws SQLException
     * @throws Exception 
     */
    protected long importFile(String filePath, String fileSplit, boolean clean, BaseTable entity, String log, String prefixLog) throws SQLException, Exception{        
        DBFile dbFile=new DBFile(fileSplit, filePath);
        long row=0,errors=0;
        String line, query;
        ArrayList<String> fields;
        ResultQuery result=null;
        //Load Header
        dbFile.open();
        if(dbFile.isOpen())
            fields=dbFile.readLineSplitLower();
        else
            throw new Exception("Error when it tryed to open file. Path: " + filePath );
        //Validation fields
        ArrayList<Field> finalFields=repository.validateFields(fields);        
        System.out.println("Start process to import");
        while((line=dbFile.readLine()) != null)
        {
            try
            {
                row+=1;
                System.out.println("Row: " + row);
                //Fix data for query
                line=FixData.fixToQuery(line);
                if(clean)
                    line=FixData.deleteAccent(line);
                //Save into database
                entity.load(finalFields, FixData.lineSplit(line,dbFile.getSplit()));
                result=add(entity);
                if(result.getAffected() > 0)
                    Log.register(log,TypeLog.REGISTER_OK,String.valueOf(row) + "|" + line, true,prefixLog,Configuration.getParameter("log_ext_review"));
                else
                    throw new Exception("Rows not affected");
            }
            catch(Exception e)
            {
                StackTraceElement[] frames = e.getStackTrace();
                String msgE="";
                for(StackTraceElement frame : frames)
                    msgE += frame.getClassName() + "-" + frame.getMethodName() + "-" + String.valueOf(frame.getLineNumber());
                Log.register(log,TypeLog.REGISTER_ERROR,String.valueOf(row) + "|" + msgE + "|" + e + "|" + line, true,prefixLog,Configuration.getParameter("log_ext_review"));
                query=result!=null?result.getQuery():(repository != null && !repository.getQuery(false).equals("") ? repository.getQuery(false):"");
                if(query != null && !query.equals(""))
                    Log.register(log,TypeLog.QUERY_ERROR,query + ";", false,prefixLog,Configuration.getParameter("log_ext_sql"));
                errors+=1;
                System.out.println("Error register: " + row + " " + e + " " + query);
            }
        }
        System.out.println("End process");
        return errors;
    }
    
    /**
     * Method that import file to database
     * @param filePath Path of source file
     * @param fileSplit Pattern to split line
     * @param entity instance of table to save
     * @param log Path to log
     * @param field
     * @param prefixLog Prefix for the log
     * @return Count of errors
     * @throws SQLException
     * @throws Exception 
     */
    protected long updateFile(String filePath, String fileSplit, BaseTable entity, String log, String prefixLog,String field) throws SQLException, Exception{
        DBFile dbFile=new DBFile(fileSplit, filePath);
        long row=0,errors=0;
        String line;
        ArrayList<String> fields;
        ResultQuery result=null;
        //Load Header
        dbFile.open();
        if(dbFile.isOpen())
            fields=dbFile.readLineSplit();
        else
            throw new Exception("Error when it tryed to open file. Path: " + filePath );
        //Validation fields
        ArrayList<Field> finalFields=repository.validateFields(fields);        
        System.out.println("Start process to update");
        while((line=dbFile.readLine()) != null)
        {
            try
            {
                row+=1;
                System.out.println("Row: " + row);
                //Save into database
                entity.load(finalFields, FixData.lineSplit(line,dbFile.getSplit()));
                result=repository.update(entity, field);
                if(result.getAffected() > 0)
                    Log.register(log,TypeLog.REGISTER_OK,result.getQuery(), true,prefixLog,Configuration.getParameter("log_ext_review"));
                else
                    throw new Exception("Rows not affected");
            }
            catch(Exception e)
            {
                Log.register(log,TypeLog.REGISTER_ERROR,String.valueOf(row) + "|" + e + "|" + line, true,prefixLog,Configuration.getParameter("log_ext_review"));
                if(result!=null)
                    Log.register(log,TypeLog.QUERY_ERROR,result.getQuery(), true,prefixLog,Configuration.getParameter("log_ext_sql"));
                errors+=1;
                System.out.println("Error register: " + row + " " + e);
            }
        }
        System.out.println("End process");
        return errors;
    }

    /**
     * Method that import file to database
     * @param filePath Path of source file
     * @param log Path to log
     * @param prefixLog Prefix for the log
     * @return Count of errors
     * @throws SQLException
     * @throws Exception 
     */
    protected long updateFileQuery(String filePath,  String log, String prefixLog) throws SQLException, Exception{
        DBFile dbFile=new DBFile("", filePath);
        long row=0,errors=0;
        String line;
        ResultQuery result=null;
        dbFile.open();
        System.out.println("Start process to update");
        while((line=dbFile.readLine()) != null)
        {
            try
            {
                row+=1;
                System.out.println("Row: " + row);
                //Save into database
                if(!line.toLowerCase().startsWith("update " + repository.getTable().toLowerCase()))
                    throw new Exception("This query don't update the " + repository.getTable() + " table");
                               
                if(repository.executeQuery(line) > 0)
                    Log.register(log,TypeLog.REGISTER_OK,line, true,prefixLog,Configuration.getParameter("log_ext_review"));
                else
                    throw new Exception("Rows not affected");
            }
            catch(Exception e)
            {
                Log.register(log,TypeLog.REGISTER_ERROR,String.valueOf(row) + "|" + e + "|" + line, true,prefixLog,Configuration.getParameter("log_ext_review"));
                if(result!=null)
                    Log.register(log,TypeLog.QUERY_ERROR,result.getQuery(), true,prefixLog,Configuration.getParameter("log_ext_sql"));
                errors+=1;
                System.out.println("Error register: " + row + " " + e);
            }
        }
        System.out.println("End process");
        return errors;
    }

    /**
     * Method that update fields of table into database
     * @param updates updates to do
     * @param log directory of log
     * @param prefixLog prefix to log file
     */
    protected void updateFields(ArrayList<BaseUpdate> updates, String log, String prefixLog)
    {
        int i=1;
        ResultQuery temp;
        Log.register(log,TypeLog.REGISTER_OK,"update|query|rows", false,prefixLog,Configuration.getParameter("log_ext_review"));        
        for(BaseUpdate bu : updates)
        {
            try
            {
                temp=repository.update(bu);                                
                Log.register(log,TypeLog.REGISTER_OK,String.valueOf(i) + "|" + temp.getQuery() + "|" + String.valueOf(temp.getAffected()), false,prefixLog,Configuration.getParameter("log_ext_review"));
            }
            catch(Exception ex)
            {
                Log.register(log,TypeLog.REGISTER_ERROR,String.valueOf(i) + "|" + ex + "|" + bu.toString(), true,prefixLog,Configuration.getParameter("log_ext_review"));
            }
            i+=1;
            System.out.println("Update: " + i + " of " + updates.size());
        }
    }
    
    /**
     * Method that delete a entity from database
     * @param field name field for delete
     * @param value value for delete
     * @return Result execute query
     * @throws SQLException  
     */
    protected boolean delete(String field,String value) throws SQLException{
        ResultQuery result=repository.delete(field, value);
        return result.getAffected() > 0;
    }
    
    /**
     * Method that add new entity into database
     * @param entity entity to save
     * @return true if save, otherwise false
     * @throws SQLException 
     */
    protected ResultQuery add(BaseTable entity) throws SQLException{
        return repository.add(entity);
    }
}
