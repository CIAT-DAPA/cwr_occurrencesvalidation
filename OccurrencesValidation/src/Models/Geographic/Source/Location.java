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
public class Location {
    
    /*Members Class*/
    private double latitude;
    private double longitude;
    private double uncertainty;
    
    /*Methods*/
    /**
     * Method Construct
     * @param latitude
     * @param longitude
     * @param uncertainty distance in meters
     */
    public Location(double latitude,double longitude,double uncertainty){
        this.latitude=latitude;
        this.longitude=longitude;
        this.uncertainty=uncertainty;
    }
    
    @Override
    public String toString(){
        return String.valueOf(latitude) + "|" + String.valueOf(longitude) + "|" + String.valueOf(uncertainty) + "|";
    }

    /*Properties*/
    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the uncertainty
     */
    public double getUncertainty() {
        return uncertainty;
    }

    /**
     * @param uncertainty the uncertainty to set
     */
    public void setUncertainty(double uncertainty) {
        this.uncertainty = uncertainty;
    }
}
