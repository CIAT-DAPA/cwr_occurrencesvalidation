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

package Models.DataBase;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class ResultQuery {
    /*Members Class*/
    private String query;
    private long affected;

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return the affected
     */
    public long getAffected() {
        return affected;
    }

    /**
     * @param affected the affected to set
     */
    public void setAffected(long affected) {
        this.affected = affected;
    }
    
    /**
     * Method Construct
     * @param query query execute
     * @param affected rows affected
     */
    public ResultQuery(String query,long affected)
    {
        this.query=query;
        this.affected=affected;
    }
}
