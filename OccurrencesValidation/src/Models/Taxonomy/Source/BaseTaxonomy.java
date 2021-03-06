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

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public abstract class BaseTaxonomy {
    /*Members Class*/
    public String author;
    public String family;
    public String genus;
    public String species;
    public String finalTaxon;
    
    /*Methods*/
    /**
     * Method Construct
     */
    public BaseTaxonomy(){
        author="";
        family="";
        genus="";
        species="";
        finalTaxon="";
    }
}
