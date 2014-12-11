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
import java.util.Iterator;
import java.util.Map;
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
                JSONObject address_components=(JSONObject)results.get(2);
                for(int i=0;i<address_components.size();i++)
                {
                    JSONArray types=(JSONArray)address_components.get(2);
                    if(((JSONObject)types.get(0)).toString().equals("country"))
                    {
                        a.put("country",((JSONObject)address_components.get(0)).toString());
                        a.put("iso",((JSONObject)address_components.get(1)).toString());
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
