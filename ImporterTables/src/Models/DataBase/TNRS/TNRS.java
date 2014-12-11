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

package Models.DataBase.TNRS;

import org.json.simple.JSONObject;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class TNRS {
    
    
    /*Methods*/
    public int group;
    public String acceptedName;
    public String acceptedAuthor;
    public String nameSubmitted;
    public String url;
    public String nameScientific;
    public String scientificScore;
    public String matchedFamily;
    public String matchedFamilyScore;
    public String authorAttributed;
    public String family;
    public String genus;
    public double genusScore;
    public String speciesMatched;
    public double speciesMatchedScore;
    public String infraspecific1Rank;
    public String infraspecific1Epithet;
    public String infraspecific1EpithetScore;
    public String infraspecific2Rank;
    public String infraspecific2Epithet;
    public double infraspecific2EpithetScore;
    public String author;
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
        this.group=Integer.parseInt(validateNumber(data.get("group")));
        this.acceptedName=String.valueOf(data.get("acceptedName").toString());
        this.acceptedAuthor=String.valueOf(data.get("acceptedAuthor").toString());
        this.nameSubmitted=String.valueOf(data.get("nameSubmitted").toString());
        this.url=String.valueOf(data.get("url").toString());
        this.nameScientific=String.valueOf(data.get("nameScientific").toString());
        this.scientificScore=String.valueOf(data.get("scientificScore").toString());
        this.matchedFamily=String.valueOf(data.get("matchedFamily").toString());
        this.matchedFamilyScore=String.valueOf(data.get("matchedFamilyScore").toString());
        this.authorAttributed=String.valueOf(data.get("authorAttributed").toString());
        this.family=String.valueOf(data.get("family").toString());
        this.genus=String.valueOf(data.get("genus").toString());
        this.genusScore=Double.parseDouble(validateNumber(data.get("genusScore")));
        this.speciesMatched=String.valueOf(data.get("speciesMatched").toString());
        this.speciesMatchedScore=Double.parseDouble(validateNumber(data.get("speciesMatchedScore")));
        this.infraspecific1Rank=String.valueOf(data.get("infraspecific1Rank").toString());
        this.infraspecific1Epithet=String.valueOf(data.get("infraspecific1Epithet").toString());
        this.infraspecific1EpithetScore=String.valueOf(data.get("infraspecific1EpithetScore").toString());
        this.infraspecific2Rank=String.valueOf(data.get("infraspecific2Rank").toString());
        this.infraspecific2Epithet=String.valueOf(data.get("infraspecific2Epithet").toString());
        this.infraspecific2EpithetScore=Double.parseDouble(validateNumber(data.get("infraspecific2EpithetScore")));
        this.author=String.valueOf(data.get("author").toString());
        this.authorScore=Double.parseDouble(validateNumber(data.get("authorScore")));
        this.annotation=String.valueOf(data.get("annotation").toString());
        this.unmatched=String.valueOf(data.get("unmatched").toString());
        this.overall=Double.parseDouble(validateNumber(data.get("overall")));
        this.epithet=String.valueOf(data.get("epithet").toString());
        this.epithetScore=Double.parseDouble(validateNumber(data.get("epithetScore")));
        this.acceptance=String.valueOf(data.get("acceptance").toString());
        this.familySubmitted=String.valueOf(data.get("familySubmitted").toString());
        this.selected=(boolean)data.get("selected");
        this.acceptedNameUrl=String.valueOf(data.get("acceptedNameUrl").toString());
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
    
}
