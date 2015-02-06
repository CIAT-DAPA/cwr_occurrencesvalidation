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

import java.util.HashMap;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class GeocodingGeolocate {

    public final static String HEADER ="geolocate_latitude|geolocate_longitude|geolocate_precision|geolocate_uncertainty|";
    
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
    public static HashMap georenferencing(String country,String adm1, String adm2, String adm3,String local_area,String locality)
    {
        HashMap a=null;
        boolean f=false, t=true;
        Geolocatesvc client=new Geolocatesvc() ;
        GeorefResultSet result;
        try {
            result = client.getGeolocatesvcSoap().georef2(country, adm1, adm2, adm3 + "," + local_area + "," + locality, f, f, f, t, t, f, f, 0);
            if(result.getResultSet().size()>0)
            {
                a=new HashMap();
                a.put("latitude", String.valueOf(result.getResultSet().get(0).getWGS84Coordinate().getLatitude()));
                a.put("longitude", String.valueOf(result.getResultSet().get(0).getWGS84Coordinate().getLongitude()));
                a.put("precision", String.valueOf(result.getResultSet().get(0).getPrecision()));
                a.put("uncertainty", String.valueOf(result.getResultSet().get(0).getUncertaintyRadiusMeters()));
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return a;
    }
}
