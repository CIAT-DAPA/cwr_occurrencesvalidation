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
import Models.DataBase.Field;
import Models.Geographic.Repository.RepositoryGoogle;
import Models.Geographic.Source.Location;
import Models.Occurrences.Repository.RepositoryTempCountries;
import Models.Occurrences.Source.TempCountries;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class CTempCountries extends BaseController {

    /*Const*/
    private final String PREFIX_IMPORT = "TC_IMPORT_";
    private final String PREFIX_UPDATE = "TC_UPDATE_";
    
    /*Properties*/
    /**
     * Get repository of TempOccurrences
     * @return
     */
    private RepositoryTempCountries getRepository(){
        return (RepositoryTempCountries)repository;
    }
    
    /*Methods*/
    /**
     * Method Construct
     */
    public CTempCountries() {
        super(new RepositoryTempCountries());
    }
    
    /**
     * Method that import file to database
     * @param filePath Path of source file
     * @param fileSplit Pattern to split line
     * @param clean True if you want clean data of accents
     * @param log Path to log
     * @return Count of errors
     * @throws SQLException
     * @throws Exception
     */
    public long importFile(String filePath, String fileSplit, boolean clean, String log) throws SQLException, Exception{
        return super.importFile(filePath, fileSplit, clean, new TempCountries(), log, PREFIX_IMPORT);
    }
    
    /**
     * Method that fill table model 
     * @param model 
     */    
    public void fillTableAll(DefaultTableModel model){
        ArrayList<TempCountries> countries= getRepository().list();
        for(int i=0;i<countries.size();i++)
            model.addRow(new Object[]{countries.get(i).getString("id"),countries.get(i).getString("name"),countries.get(i).getString("iso2"),countries.get(i).getString("iso3"),countries.get(i).getString("lat"),countries.get(i).getString("lon")});
    }
    
    /**
     * Method that generate a entity of temp countries from values
     * @param name name country
     * @param iso2 code iso2
     * @param iso3 code iso3
     * @param lat latitude country's center
     * @param lon longitude country's center
     * @return Entity temp countries
     */
    public TempCountries generateEntity(String name,String iso2, String iso3,double lat,double lon){
        TempCountries a=new TempCountries();
        ArrayList<Field> fields=new ArrayList<>();
        fields.add(new Field("name","varchar"));
        fields.add(new Field("iso2","varchar"));
        fields.add(new Field("iso3","varchar"));
        fields.add(new Field("lat","double"));
        fields.add(new Field("lon","double"));
        a.load(fields,new ArrayList<>(Arrays.asList(name,iso2,iso3,String.valueOf(lat),String.valueOf(lon))));
        return a;
    }
    
    /**
     * Method that delete a entity from database
     * @param id id entity
     * @return Result execute query
     * @throws SQLException  
     */
    public boolean delete(String id) throws SQLException{        
        return delete("id", id);
    }
    
    /**
     * Method that save a entity into database
     * @param name name country
     * @param iso2 code iso2
     * @param iso3 code iso3
     * @param lat latitude country's center
     * @param lon longitude country's center
     * @return True if save, otherwise false
     * @throws SQLException 
     */
    public boolean add(String name,String iso2, String iso3, double lat,double lon) throws SQLException{
        return add(generateEntity(name, iso2, iso3, lat, lon)).getAffected() > 0;
    }
    
    /**
     * This method search the latitude and longitude for every country in google maps and update de center maps coordinates
     * @param log path to save the log file
     */
    public void findCenterPointCountry(String log){
        Location l;
        ArrayList<BaseUpdate> updates= new ArrayList<BaseUpdate>();
        for(TempCountries tc: getRepository().list()){
            l = RepositoryGoogle.georenferencing(tc.getString("name"),"","","","","",99999);
            if(l != null){
                updates.add(new BaseUpdate("lat", String.valueOf(l.getLatitude()),"id='" + tc.getString("id") + "'"));
                updates.add(new BaseUpdate("lon", String.valueOf(l.getLongitude()),"id='" + tc.getString("id") + "'"));
            }
        }
        super.updateFields(updates, log, PREFIX_UPDATE);
    }
    
}