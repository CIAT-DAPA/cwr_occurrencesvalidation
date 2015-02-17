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

package Controllers.Occurrences;

import Models.Occurrences.Repository.RepositoryMetadata;
import Models.Occurrences.Source.Metadata;
import java.sql.SQLException;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class CMetadata extends BaseController {

    /*Methods*/
    /**
     * Method Construct
     */
    public CMetadata() {
        super(new RepositoryMetadata());
    }
    
    /**
     * Method that import file to database
     * @param filePath Path of source file
     * @param fileSplit Pattern to split line
     * @param clean True if you want clean data of accents
     * @param log Path to log
     * @return Count of errors
     * @throws SQLException
     * @throws Exception 
     */
    public long importFile(String filePath, String fileSplit, boolean clean, String log) throws SQLException, Exception{
        return super.importFile(filePath, fileSplit, clean, new Metadata(), log, "MDI");
    }

}
