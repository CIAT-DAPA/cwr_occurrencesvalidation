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

package Models.DataBase.Geocoding;

import Models.DataBase.MySQL;
import Tools.Configuration;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class RepositoryCountry {
    /*Members Class*/
    private MySQL db;
    
    /*Methods*/
    /**
     * Method Construct
     */
    public RepositoryCountry(){
        this.db=new MySQL(Configuration.getParameter("currie_server"),Configuration.getParameter("currie_schema_gapanalysis"), Configuration.getParameter("currie_user"), Configuration.getParameter("currie_password"));
    }
    
    /**
     * Method that list all countries from database
     * @return 
     */
    public ArrayList<Country> list()
    {
        ArrayList<Country> a=null;
        try {
            a=new ArrayList<>();
            this.db.getResults("Select id,name,iso2,iso3 From temp_countries order by name");
            while(this.db.getRecordSet().next())
                a.add(new Country(this.db.getRecordSet().getInt("id"),this.db.getRecordSet().getString("name"),this.db.getRecordSet().getString("iso2"),this.db.getRecordSet().getString("iso3")));
            
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
    public Country searchByName(String name)
    {
        Country a=null;
        try 
        {            
            this.db.getResults("Select id,name,iso2,iso3 From temp_countries Where name like '%" + name + "%'");
            if(this.db.getRecordSet().next())
            {
                // It is a validation because a country can be more that one times, so we always selected the first country
                a=searchIso2(this.db.getRecordSet().getString("iso2"));
                if(a==null)
                    a=new Country(this.db.getRecordSet().getInt("id"),this.db.getRecordSet().getString("name"),this.db.getRecordSet().getString("iso2"),this.db.getRecordSet().getString("iso3"));
            }
                
        } 
        catch (SQLException ex) {
            a=null;
            System.out.println("Error in countries repository: " + ex);
        }
        return a;
    }
    
    /**
     * Method that search country by iso3
     * @param iso
     * @return 
     */
    public Country searchIso3(String iso)
    {
        Country a=null;
        try 
        {            
            this.db.getResults("Select id,name,iso2,iso3 From temp_countries Where iso3 like '%" + iso + "%'");
            if(this.db.getRecordSet().next())
            {
                // It is a validation because a country can be more that one times, so we always selected the first country
                a=searchIso2(this.db.getRecordSet().getString("iso2"));
                if(a==null)
                    a=new Country(this.db.getRecordSet().getInt("id"),this.db.getRecordSet().getString("name"),this.db.getRecordSet().getString("iso2"),this.db.getRecordSet().getString("iso3"));
            }
        } 
        catch (SQLException ex) {
            a=null;
            System.out.println("Error in countries repository: " + ex);
        }
        return a;
    }
    
    /**
     * Method that search country by iso2
     * @param iso
     * @return 
     */
    public Country searchIso2(String iso)
    {
        Country a=null;
        try 
        {      
            // Always the query should order by id in this method
            this.db.getResults("Select id,name,iso2,iso3 From temp_countries Where iso2 like '%" + iso + "%' order by id");
            if(this.db.getRecordSet().next())
                a=new Country(this.db.getRecordSet().getInt("id"),this.db.getRecordSet().getString("name"),this.db.getRecordSet().getString("iso2"),this.db.getRecordSet().getString("iso3"));
        } 
        catch (SQLException ex) {
            a=null;
            System.out.println("Error in countries repository: " + ex);
        }
        return a;
    }
    
    /**
     * Method that add a new country to database
     * @param country Country to add
     * @return number of rows affected
     */
    public int insert(Country country)
    {
        try 
        {            
            return this.db.update("Insert into temp_countries (name,iso2,iso3) values ('" + country.getName() +"','" + country.getIso2() + "','" + country.getIso3() + "')");
        } 
        catch (SQLException ex) {            
            System.out.println("Error in countries repository: " + ex);
            return 0;
        }
    }
    
    /**
     * Method that delete a country in the database
     * @param country Country to delete
     * @return number of rows affected
     */
    public int delete(Country country)
    {
        try 
        {            
            return this.db.update("Delete from temp_countries where id=" + String.valueOf(country.getId()));
        } 
        catch (SQLException ex) {            
            System.out.println("Error in countries repository: " + ex);
            return 0;
        }
    }
}
