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

import java.util.ArrayList;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class Policy {
    
    /*Memebrs class*/
    private TypePolicy typePolicy;
    private ArrayList<String> values;
    
    /*Properties*/
    /**
     * @return the typePolicy
     */
    public TypePolicy getTypePolicy() {
        return typePolicy;
    }
    
    /**
     * @param typePolicy the typePolicy to set
     */
    public void setTypePolicie(TypePolicy typePolicy) {
        this.typePolicy = typePolicy;
    }
    
    /**
     * @return the values
     */
    public ArrayList<String> getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
    
        
    /*Methods*/
    /**
     * Method Construct
     * @param type Type Policy
     * @param values to apply the policy
     */
    public Policy(TypePolicy type, ArrayList<String> values)
    {
        this.typePolicy=type;
        this.values=values;
    }
}
