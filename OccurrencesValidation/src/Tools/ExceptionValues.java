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

package Tools;

import java.util.ArrayList;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class ExceptionValues extends Exception {
    /*Members Class*/
    private ArrayList<String> values;
        
    
    /*Properties*/
    /**
     * @return the values
     */
    public ArrayList<String> getValues() {
        return values;
    }

    /**
     * @param value the value to set
     */
    public void addValue(String value) {
        this.values.add(value);
    }
    
    /*Methods*/
    /**
     * Method Construct
     * @param message Message of exception
     */
    public ExceptionValues(String message){
        super(message);
        values=new ArrayList<String>();
    }
    /**
     * Method Construct
     * @param message Message of exception
     * @param values list of values with error
     */
    public ExceptionValues(String message,ArrayList<String> values){
        super(message);
        this.values=values;
    }
}
