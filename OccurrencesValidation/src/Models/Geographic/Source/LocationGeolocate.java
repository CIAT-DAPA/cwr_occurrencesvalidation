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

package Models.Geographic.Source;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class LocationGeolocate extends Location {

    /*Members Class*/
    private double precision;
    
    /*Mehtods*/
    /**
     * Method Construct
     * @param latitude
     * @param longitude
     * @param uncertainty
     * @param precision 
     */
    public LocationGeolocate(double latitude, double longitude, double uncertainty, double precision) {
        super(latitude, longitude, uncertainty);
        this.precision=precision;
    }
    
    @Override
    public String toString(){
        return super.toString() + String.valueOf(precision) + "|";
    }
    
    /*Propeties*/
    /**
     * @return the precision
     */
    public double getPrecision() {
        return precision;
    }

    /**
     * @param precision the precision to set
     */
    public void setPrecision(double precision) {
        this.precision = precision;
    }

}
