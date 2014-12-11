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


/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class Country {
    /*Members Class*/
    private int id;
    private String name;
    private String iso2;
    private String iso3;
    
    /*Methods*/
    
    /**
     * Method Construct
     */
    public Country(){
        name=iso2=iso3="";
        id=0;
    }
    
    /**
     * Method Construct
     * @param id Country id
     * @param name Country name
     * @param iso2 Representation of country in iso2 
     * @param iso3 Representation of country in iso3
     */
    public Country(int id,String name,String iso2,String iso3){
        this.id=id;
        this.name=name;
        this.iso2=iso2;
        this.iso3=iso3;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the iso2
     */
    public String getIso2() {
        return iso2;
    }

    /**
     * @param iso2 the iso2 to set
     */
    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    /**
     * @return the iso3
     */
    public String getIso3() {
        return iso3;
    }

    /**
     * @param iso3 the iso3 to set
     */
    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
}