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

package Controllers.Configuration;

import Models.DataBase.DBFile;
import Models.DataBase.SQLite;
import Tools.Configuration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class RasterFile {
    
    /*Members Class*/
    private SQLite db;
    private DBFile file;
    private HashMap header;
    
    /**
     * Method Construct
     * @param source Path of raster file
     */
    public RasterFile(String source)
    {
        this.db=new SQLite(Configuration.getParameter("geocoding_database_world"));
        this.file=new DBFile(" ", source);
        header=new HashMap();
    }
    
    
    /**
     * Method that create database from
     * @param closeFile
     * @return
     */
    public boolean create(boolean closeFile)
    {
        boolean a=false;
        try
        {
            int maxCols=Integer.parseInt(Configuration.getParameter("geocoding_database_world_max_column"));
            file.open();
            //Table config
            db.update("drop table if exists " + Configuration.getParameter("geocoding_database_world_conf"));
            db.update("create table " + Configuration.getParameter("geocoding_database_world_conf") + " (key text,valueInit text,valueEnd text)");
            for(int i=1;i<=6;i++)
            {
                ArrayList<String> data=file.readLineSplit();
                header.put(data.get(0).toString().toLowerCase(), data.get(1));
                db.update("insert into " + Configuration.getParameter("geocoding_database_world_conf") + " values ('raster_" + data.get(0).toString().toLowerCase() + "','" + data.get(1).toString() +"','')");
            }
            double tables=Double.parseDouble(header.get("ncols").toString())/Double.parseDouble(Configuration.getParameter("geocoding_database_world_max_column"));
            int iTables=(int)tables;
            double fTables=tables-iTables;
            int maxTables=fTables > 0 ? iTables+1 : iTables;
            db.update("insert into " + Configuration.getParameter("geocoding_database_world_conf") + " values ('tables_columns','" + Configuration.getParameter("geocoding_database_world_max_column")  + "','')");
            db.update("insert into " + Configuration.getParameter("geocoding_database_world_conf") + " values ('tables_count','" + String.valueOf(maxTables)   + "','')");
            //prepare for query
            String columns, table;
            int init=1,end=maxCols;
            //Create table for every zone
            for(int l=1;l<=maxTables;l++)
            {
                table=Configuration.getParameter("geocoding_database_world_table") + String.valueOf(l);
                columns="";
                for(int i=init;i<=end;i++)
                    columns+="col" + String.valueOf(i) + " text,";
                db.update("drop table if exists " + table);
                db.update("create table " + table + " (id integer," + columns.substring(0,columns.length()-1) + ")");
                db.update("insert into " + Configuration.getParameter("geocoding_database_world_conf") + " values ('" + table + "','" + String.valueOf(init) +"','"+ String.valueOf(end) +"')");
                init=end+1;
                end += (end+maxCols) <= Integer.parseInt(header.get("ncols").toString()) ? maxCols : (Integer.parseInt(header.get("ncols").toString())-end) ;
            }
            a=true;
        }
        catch(Exception ex)
        {
            System.out.println("Error on create database raster: " + ex);
        }
        finally
        {
            if(closeFile)
                file.close();
        }
        return a;
    }
    
    /**
     * Method that register every row from raster file in the database
     * @param isOpenFile
     * @param closeFile
     * @return
     */
    public long register(boolean isOpenFile,boolean closeFile)
    {
        long a=0;
        String header;
        String query;
        ArrayList<String> values;
        HashMap tables=new HashMap();
        int init,end;
        Map.Entry e;
        Iterator it;
        try
        {
            if(!isOpenFile)
                file.open();
            //Load all tables created and range
            db.getResults("Select key,valueInit,valueEnd From " + Configuration.getParameter("geocoding_database_world_conf") + " Where key like '" + Configuration.getParameter("geocoding_database_world_table") + "%' Order by key");
            while(db.getRecordSet().next())
                tables.put(db.getRecordSet().getString("key"), db.getRecordSet().getString("valueInit") + "-" + db.getRecordSet().getString("valueEnd"));
            //Read all row from file
            while((values=file.readLineSplit()) != null)
            {
                a+=1;
                it = tables.entrySet().iterator();
                //Cicle for insert register by every table in the same row with the same id
                while (it.hasNext()) {
                    e = (Map.Entry)it.next();
                    header="insert into " + e.getKey().toString() + " values (";
                    query="";
                    init=Integer.parseInt(e.getValue().toString().split("-")[0]);
                    end=Integer.parseInt(e.getValue().toString().split("-")[1]);
                    for(int n=init-1;n<end;n++)
                        query+="'" + values.get(n) + "',";
                    db.update(header + "'" + String.valueOf(a) + "'," + query.substring(0,query.length()-1) + ")");
                }
                System.out.println("Rows register: " + a);
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error on register database raster: " + ex );
        }
        finally
        {
            if(closeFile)
                file.close();
        }
        return a;
    }
}
