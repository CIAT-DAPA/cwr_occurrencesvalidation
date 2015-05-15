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

package Controllers.Tools;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public enum TypePolicy {
    CHECK_FIELDS_MANDATORY,                         //0
    CHECK_CONTENT,                                  //1
    CORRECT_DEPENDENCE_SOURCE_AVAILABILITY,         //2    
    FINAL_CULT,                                     //3    
    TNRS_QUERY,                                     //4    
    TAXONDSTAND_QUERY,                              //5
    GRIN_QUERY,                                     //6
    GEOCODING_VALIDATE_COUNTRY,                     //7
    GEOCODING_VALIDATE_ISO2,                        //8
    GEOCODING_VALIDATE_COORDS_GRADS,                //9
    GEOCODING_VALIDATE_NS_EW,                       //10
    GEOCODING_VALIDATE_COORDS_LAT_LON,              //11  
    GEOCODING_INITIAL,                              //12
    POSTCHECK_VALIDATE_TAXON,                       //13    
    POSTCHECK_VALIDATE_TAXON_MANDATORY,             //14
    POSTCHECK_GEOCODING_CROSCHECK_COORDS,           //15
    POSTCHECK_GEOCODING_CROSCHECK_GEOREF,           //16
    POSTCHECK_ORIGIN_STAT,                          //17
}