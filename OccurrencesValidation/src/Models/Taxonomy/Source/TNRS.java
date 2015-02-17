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

package Models.Taxonomy.Source;

import org.json.simple.JSONObject;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class TNRS extends BaseTaxonomy {
    
    
    /*Methods*/
    public int group;
    public String acceptedAuthor;
    public String nameSubmitted;
    public String url;
    public String nameScientific;
    public String scientificScore;
    public String matchedFamily;
    public String matchedFamilyScore;
    public String authorAttributed;
    public double genusScore;
    public String speciesMatched;
    public double speciesMatchedScore;
    public String infraspecific1Rank;
    public String infraspecific1Epithet;
    public String infraspecific1EpithetScore;
    public String infraspecific2Rank;
    public String infraspecific2Epithet;
    public double infraspecific2EpithetScore;
    public double authorScore;
    public String annotation;
    public String unmatched;
    public double overall;
    public String epithet;
    public double epithetScore;
    public String acceptance;
    public String familySubmitted;
    public boolean selected;
    public String acceptedNameUrl;
    
    /**
     * Method Construct
     * @param data JSON with the information from API TNRS
     */
    public TNRS(JSONObject data){        
        super();
        group=Integer.parseInt(validateNumber(data.get("group")));
        finalTaxon=String.valueOf(data.get("acceptedName").toString()).trim();
        acceptedAuthor=String.valueOf(data.get("acceptedAuthor").toString()).trim();
        nameSubmitted=String.valueOf(data.get("nameSubmitted").toString()).trim();
        url=String.valueOf(data.get("url").toString()).trim();
        nameScientific=String.valueOf(data.get("nameScientific").toString()).trim();
        scientificScore=String.valueOf(data.get("scientificScore").toString()).trim();
        matchedFamily=String.valueOf(data.get("matchedFamily").toString()).trim();
        matchedFamilyScore=String.valueOf(data.get("matchedFamilyScore").toString()).trim();
        authorAttributed=String.valueOf(data.get("authorAttributed").toString()).trim();
        family=String.valueOf(data.get("family").toString()).trim();
        genus=String.valueOf(data.get("genus").toString()).trim();
        genusScore=Double.parseDouble(validateNumber(data.get("genusScore")));
        speciesMatched=String.valueOf(data.get("speciesMatched").toString()).trim();
        speciesMatchedScore=Double.parseDouble(validateNumber(data.get("speciesMatchedScore")));
        infraspecific1Rank=String.valueOf(data.get("infraspecific1Rank").toString()).trim();
        infraspecific1Epithet=String.valueOf(data.get("infraspecific1Epithet").toString()).trim();
        infraspecific1EpithetScore=String.valueOf(data.get("infraspecific1EpithetScore").toString()).trim();
        infraspecific2Rank=String.valueOf(data.get("infraspecific2Rank").toString()).trim();
        infraspecific2Epithet=String.valueOf(data.get("infraspecific2Epithet").toString()).trim();
        infraspecific2EpithetScore=Double.parseDouble(validateNumber(data.get("infraspecific2EpithetScore")));
        author=String.valueOf(data.get("author").toString()).trim();
        authorScore=Double.parseDouble(validateNumber(data.get("authorScore")));
        annotation=String.valueOf(data.get("annotation").toString()).trim();
        unmatched=String.valueOf(data.get("unmatched").toString()).trim();
        overall=Double.parseDouble(validateNumber(data.get("overall")));
        epithet=String.valueOf(data.get("epithet").toString()).trim();
        epithetScore=Double.parseDouble(validateNumber(data.get("epithetScore")));
        acceptance=String.valueOf(data.get("acceptance").toString()).trim();
        familySubmitted=String.valueOf(data.get("familySubmitted").toString()).trim();
        selected=(boolean)data.get("selected");
        acceptedNameUrl=String.valueOf(data.get("acceptedNameUrl").toString()).trim();
    }
    
    /**
     * Method that validate if the field content is for a number or not
     * @param field Field to validate
     * @return 
     */
    private String validateNumber(Object field)
    {
        return field==null || field.toString().equals("") ? "0" : field.toString();
    }
    
    @Override
    public String toString()
    {        
        String bring="|";
        return String.valueOf(group) + 
                bring + finalTaxon +
                bring + acceptedAuthor +
                bring + nameSubmitted +
                bring + url +
                bring + nameScientific +
                bring + scientificScore +
                bring + matchedFamily +
                bring + matchedFamilyScore +
                bring + authorAttributed +
                bring + family+
                bring + genus +
                bring + String.valueOf(genusScore) +
                bring + speciesMatched +
                bring + String.valueOf(speciesMatchedScore) +
                bring + infraspecific1Rank +
                bring + infraspecific1Epithet +
                bring + infraspecific1EpithetScore +
                bring + infraspecific2Rank +
                bring + infraspecific2Epithet +
                bring + String.valueOf(infraspecific2EpithetScore) +
                bring + author +
                bring + String.valueOf(authorScore) +
                bring + annotation +
                bring + unmatched +
                bring + String.valueOf(overall) +
                bring + epithet +
                bring + String.valueOf(epithetScore) +
                bring + acceptance +
                bring + familySubmitted +
                bring + String.valueOf(selected) +
                bring + acceptedNameUrl + bring;
    }
    
}
