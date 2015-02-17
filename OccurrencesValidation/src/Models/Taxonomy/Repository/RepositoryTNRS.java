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

package Models.Taxonomy.Repository;

import Models.Taxonomy.Source.TNRS;
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
public class RepositoryTNRS {
    /*Const*/
    public final static String HEADER = "group|acceptedName|acceptedAuthor|nameSubmitted|url|nameScientific|scientificScore|matchedFamily|matchedFamilyScore|authorAttributed|family|genus|genusScore|speciesMatched|speciesMatchedScore|infraspecific1Rank|infraspecific1Epithet|infraspecific1EpithetScore|infraspecific2Rank|infraspecific2Epithet|infraspecific2EpithetScore|author|authorScore|annotation|unmatched|overall|epithet|epithetScore|acceptance|familySubmitted|selected|acceptedNameUrl|";
    
    /*Members class*/
    public static HashMap db=null;    
    
    /**
     * Method that consuming web services from tnrs and get results
     * @param name Name from specie to evaluate
     * @param best True for best results, false if all results
     * @return 
     */
    public static TNRS[] get(String name, boolean best)
    {
        TNRS[] a=null;
        try
        {            
            if(db==null)
                db=new HashMap();
            if(db.containsKey(name.trim().replaceAll(" ", "_")))
                return (TNRS[])db.get(name.trim().replaceAll(" ", "_"));
            URL url=new URL(Configuration.getParameter("tnrs_url_base") + (best?"retrieve=best":"retrieve=all") + "&names=" + name.replaceAll(" ","%20"));
            BufferedReader lector=new BufferedReader(new InputStreamReader(url.openStream()));
            String textJson=lector.readLine();
            if(textJson==null)
                throw new Exception("Don't found item " + name);
            JSONArray jsNames=(JSONArray)((JSONObject)JSONValue.parse(textJson)).get("items");
            a=new TNRS[jsNames.size()];
            for(int i=0; i < a.length;i++)
                a[i]=new TNRS((JSONObject)jsNames.get(i));
            db.put(name.trim().replaceAll(" ", "_"), a);
        }
        catch(Exception ex)
        {
            System.out.println("Error TNRS: " + ex);
        }
        return a;
    }
}
