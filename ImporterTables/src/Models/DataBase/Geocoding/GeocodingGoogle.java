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

import Tools.Configuration;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class GeocodingGoogle {
    
    public static HashMap reverse(double latitude, double longitude)
    {
        HashMap a=new HashMap();
        try
        {
            
            URL url=new URL(Configuration.getParameter("geocoding_google_url_send_json") + "latlng=" + String.valueOf(latitude) + "," + String.valueOf(longitude));            
            BufferedReader lector=new BufferedReader(new InputStreamReader(url.openStream()));
            String textJson="",tempJs;
            while((tempJs=lector.readLine()) != null)
                textJson+=tempJs;
            if(textJson==null)
                throw new Exception("Don't found item " );
            JSONObject google=((JSONObject)JSONValue.parse(textJson));            
            a.put("status", google.get("status").toString());
            if(a.get("status").toString().equals("OK"))
            {
                JSONArray results=(JSONArray)google.get("results");
                JSONArray address_components=(JSONArray)((JSONObject)results.get(2)).get("address_components");
                for(int i=0;i<address_components.size();i++)
                {
                    JSONObject items=(JSONObject)address_components.get(i);
                    //if(((JSONObject)types.get(0)).get("").toString().equals("country"))
                    if(items.get("types").toString().contains("country"))
                    {
                        a.put("country",items.get("long_name").toString());
                        a.put("iso",items.get("short_name").toString());
                        break;
                    }
                }
            }
        }
        catch(Exception ex)
        {
            a=null;
            System.out.println("Error Google Geocoding: " + ex);
        }
        return a;
    }
}
