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

package Models.Geographic.Repository;

import Models.DataBase.Geocoding.Geolocatesvc;
import Models.DataBase.Geocoding.GeorefResult;
import Models.DataBase.Geocoding.GeorefResultSet;
import Models.Geographic.Source.Location;
import Tools.Configuration;
import Tools.FixData;
import java.util.HashMap;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class RepositoryGeolocate {
    private static HashMap db;
    private static Geolocatesvc client;
    /**
     * Method that geocoding a address from geolocate api
     * @param country
     * @param adm1
     * @param adm2
     * @param adm3
     * @param local_area
     * @param locality
     * @return 
     */
    public static Location georenferencing(String country,String adm1, String adm2, String adm3,String local_area,String locality)
    {
        Location a=null;
        boolean f=false, t=true;        
        GeorefResultSet result;
        String key;
        try {            
            key=FixData.generateKey(new String[]{country,adm1,adm2,adm3,local_area,locality});
            if(RepositoryGeolocate.db==null)
                RepositoryGeolocate.db=new HashMap();
            if(RepositoryGeolocate.db.containsKey(key))
                return (Location)RepositoryGeolocate.db.get(key);
            if(RepositoryGeolocate.client == null)
                RepositoryGeolocate.client=new Geolocatesvc();
            result = RepositoryGeolocate.client.getGeolocatesvcSoap().georef2(country, adm1, adm2, adm3 + "," + local_area + "," + locality, f, f, f, t, t, f, f, 0);
            if(result.getResultSet().size()>0)
            {
                for(GeorefResult gr : result.getResultSet()){
                    if(gr != null && gr.getWGS84Coordinate() != null && Double.parseDouble(gr.getUncertaintyRadiusMeters()) <= Double.parseDouble(Configuration.getParameter("geocoding_threshold")) && gr.getWGS84Coordinate().getLatitude() != 0 && gr.getWGS84Coordinate().getLongitude() != 0){
                        a=new Location(gr.getWGS84Coordinate().getLatitude(),gr.getWGS84Coordinate().getLongitude(),Double.parseDouble(gr.getUncertaintyRadiusMeters()));
                        break;
                    }
                }
                
            }
            RepositoryGeolocate.db.put(key, a);
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }            
        return a;
    }
}
