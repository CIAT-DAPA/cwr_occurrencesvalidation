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

package Controllers.Validation;

import Controllers.Validation.Policies.Policy;
import Models.DataBase.DBBase;
import java.util.ArrayList;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public abstract class ValidationBase {
    
    /*Members class*/
    protected ArrayList<Policy> policies;
    protected DBBase db;
    protected String log;
    
    /*Methods*/
    /**
     * Method Construct
     * @param db Connection to database
     * @param policies Policies to apply for every register
     * @param log path for register log
     */
    public ValidationBase(DBBase db,ArrayList<Policy> policies, String log) 
    {
        this.db=db;
        this.policies=policies;
        this.log=log;
    }
    
    
    /**
     * Method that review every row from table and apply policies 
     * @return 
     */
    public abstract long review();
}
