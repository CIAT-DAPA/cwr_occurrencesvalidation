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

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class Taxonstand extends BaseTaxonomy {
    /*Members Class*/
    public String species2;
    private String allValues;
    
    /**
     * Method Construct
     * @param nList Node with information
     */
    public Taxonstand(NodeList nList){
        super();
        Element  var;
        String value;
        allValues="";
        for (int i = 0; i < nList.getLength(); i++)
        {
            var = (Element)nList.item(i);
            value=var.getTextContent()==null ? "" : var.getTextContent().replaceAll("\n", "").trim();
            allValues += value + "|";
            if(var.getAttribute("name").toLowerCase().equals("authority"))
                author=value;
            else if(var.getAttribute("name").toLowerCase().equals("family"))
                family=value;
            else if(var.getAttribute("name").toLowerCase().equals("new_genus"))
                genus=value;
            else if(var.getAttribute("name").toLowerCase().equals("new_species"))
                species=value;
            else if(var.getAttribute("name").toLowerCase().equals("new_infraspecific"))
                species2=value;
        }
    }
    
    public Taxonstand(String author,String family,String genus,String species,String species2){
        super();
        this.author=author;
        this.family=family;
        this.genus=genus;
        this.species=species;
        this.species2=species2;
    }
    
    @Override
    public String toString(){
        return allValues;
    }
}
