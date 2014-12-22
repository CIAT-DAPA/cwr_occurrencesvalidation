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

package Controllers.Update;

import Models.DataBase.DBBase;
import Models.DataBase.DBFile;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public abstract class UpdateBase 
{
    /*Members Class*/    
    protected DBBase db;
    protected DBFile file;
    protected String log;
    
    /**
     * Method Construct
     * @param db Connection to database
     * @param file Path file source
     * @param log Path of log
     */
    public UpdateBase(DBBase db, String file, String log){
        this.db=db;
        this.file=new DBFile("", file);
        this.log=log;
    }
    
    /**
     * Method that initialize the data update
     * @return 
     */
    public abstract long start();
}
