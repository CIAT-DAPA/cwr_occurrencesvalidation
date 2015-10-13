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

import Models.DataBase.ResultQuery;
import Models.Occurrences.Source.TempCountries;
import Tools.Configuration;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class RepositoryTempCountries extends BaseRepository {    
   
    /**
     * Method Construct
     */
    public RepositoryTempCountries(){
        super(Configuration.getParameter("currie_table_countries"));
    }
    
    /*Queries CRUD*/
    public ResultQuery add(TempCountries entity) throws SQLException{
        return super.add(entity);
    }
    
    /*Queries*/
    
    /**
     * Method that list all countries from database
     * @return 
     */
    public ArrayList<TempCountries> list()
    {
        ArrayList<TempCountries> a=null;
        TempCountries temp;
        try {
            a=new ArrayList<>();
            
            this.db.getResults("Select id,name,iso2,iso3,lat,lon From temp_countries order by name");
            while(this.db.getRecordSet().next())
            {
                temp = new TempCountries();
                temp.load(db.getRecordSet());
                a.add(temp);
            }
        } catch (SQLException ex) {
            a=null;
            System.out.println("Error in countries repository: " + ex);
        }
        return a;
    }
    
    /**
     * Method that search country by the name
     * @param name
     * @return 
     */
    public TempCountries searchByName(String name)
    {
        TempCountries a=null;
        try 
        {            
            this.db.getResults("Select id,name,iso2,iso3,lat,lon From temp_countries Where name like '" + name + "%' order by id");
            if(db.getRecordSet().next())
            {
                a=new TempCountries();
                a.load(db.getRecordSet());
            }   
        } 
        catch (SQLException ex) {
            a=null;
            System.out.println("Error search by name in countries repository: " + ex);
        }
        return a;
    }
    
    /**
     * Method that search country by iso3
     * @param iso
     * @return 
     */
    public TempCountries searchIso3(String iso)
    {
        TempCountries a=null;
        try 
        {            
            db.getResults("Select id,name,iso2,iso3,lat,lon From temp_countries Where iso3 like '" + iso + "%' order by id");
            if(db.getRecordSet().next())
            {
                a=new TempCountries();
                a.load(db.getRecordSet());
            }
        } 
        catch (SQLException ex) {
            a=null;
            System.out.println("Error search by Iso 3 in countries repository: " + ex);
        }
        return a;
    }
    
    /**
     * Method that search country by iso2
     * @param iso iso2 code
     * @return 
     */
    public TempCountries searchIso2(String iso)
    {
        TempCountries a=null;
        try 
        {      
            // Always the query should order by id in this method
            db.getResults("Select id,name,iso2,iso3,lat,lon From temp_countries Where iso2 like '" + iso + "%' order by id");
            if(db.getRecordSet().next())
            {
                a=new TempCountries();
                a.load(db.getRecordSet());
            }
        } 
        catch (SQLException ex) {
            a=null;
            System.out.println("Error search by Iso 2 in countries repository: " + ex);
        }
        return a;
    }

}
