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

package Controllers.Validation.Policies;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public enum TypePolicy {
    CHECK_FIELDS_MANDATORY,                         //0
    CHECK_AVAILABILITY,                             //1
    CHECK_SOURCE,                                   //2
    CHECK_CULT,                                     //3    
    CHECK_ORIGIN_STAT,                              //4
    CHECK_IS_HYBRID,                                //5
    CORRECT_DEPENDENCE_SOURCE_AVAILABILITY,         //6    
    FINAL_CULT,                                     //7
    FINAL_ORIGIN,                                   //8
    ADD_AVAILABILITY,                               //9        
    ADD_SOURCE,                                     //10
    ADD_CULT,                                       //11
    TAXON_DATA_FINAL,                               //12
    TNRS_QUERY,                                     //13    
    TAXONDSTAND_QUERY,                              //14
    GEOCODING_VALIDATE_COUNTRY,                     //15
    GEOCODING_VALIDATE_ISO2,                        //16
    GEOCODING_VALIDATE_COORDS_GRADS,                //17
    GEOCODING_VALIDATE_NS_EW,                       //18
    GEOCODING_VALIDATE_COORDS_LAT_LON,              //19    
    GEOCODING_INITIAL,                              //20
    POSTCHECK_VALIDATE_TAXON,                       //21    
    POSTCHECK_VALIDATE_TAXON_MANDATORY,             //22
    POSTCHECK_GEOCODING_CROSCHECK_COORDS,           //23
    POSTCHECK_GEOCODING_CROSCHECK_GEOREF,           //24
}