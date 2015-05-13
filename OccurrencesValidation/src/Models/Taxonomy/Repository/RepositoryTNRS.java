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
    public final static String HEADER = "TNRS_Group|TNRS_AcceptedName|TNRS_AcceptedAuthor|TNRS_NameSubmitted|TNRS_Url|TNRS_NameScientific|TNRS_ScientificScore|TNRS_MatchedFamily|TNRS_MatchedFamilyScore|TNRS_AuthorAttributed|TNRS_Family|TNRS_Genus|TNRS_GenusScore|TNRS_SpeciesMatched|TNRS_SpeciesMatchedScore|TNRS_Infraspecific1Rank|TNRS_Infraspecific1Epithet|TNRS_Infraspecific1EpithetScore|TNRS_Infraspecific2Rank|TNRS_Infraspecific2Epithet|TNRS_Infraspecific2EpithetScore|TNRS_Author|TNRS_AuthorScore|TNRS_Annotation|TNRS_Unmatched|TNRS_Overall|TNRS_Epithet|TNRS_EpithetScore|TNRS_Acceptance|TNRS_FamilySubmitted|TNRS_Selected|TNRS_AcceptedNameUrl|";
    
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
