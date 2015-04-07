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

package Controllers.Tools.Validation;

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
    TNRS_QUERY,                                     //9    
    TAXONDSTAND_QUERY,                              //10
    GRIN_QUERY,                                     //11
    GEOCODING_VALIDATE_COUNTRY,                     //12
    GEOCODING_VALIDATE_ISO2,                        //13
    GEOCODING_VALIDATE_COORDS_GRADS,                //14
    GEOCODING_VALIDATE_NS_EW,                       //15
    GEOCODING_VALIDATE_COORDS_LAT_LON,              //16    
    GEOCODING_INITIAL,                              //17
    POSTCHECK_VALIDATE_TAXON,                       //18    
    POSTCHECK_VALIDATE_TAXON_MANDATORY,             //19
    POSTCHECK_GEOCODING_CROSCHECK_COORDS,           //20
    POSTCHECK_GEOCODING_CROSCHECK_GEOREF,           //21
}