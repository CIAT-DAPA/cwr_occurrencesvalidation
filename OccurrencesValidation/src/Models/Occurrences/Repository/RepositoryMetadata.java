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

package Models.Occurrences.Repository;

import Models.DataBase.ResultQuery;
import Models.Occurrences.Source.Metadata;
import Tools.Configuration;
import java.sql.SQLException;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class RepositoryMetadata extends BaseRepository {    
   
    /**
     * Method Construct
     */
    public RepositoryMetadata(){
        super(Configuration.getParameter("currie_table_metadata"));
    }
    
    /*Methods CRUD*/
    public ResultQuery add(Metadata entity) throws SQLException{
        return super.add(entity);
    }
    
}
