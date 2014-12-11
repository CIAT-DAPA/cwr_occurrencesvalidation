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

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class SQLite extends DBBase {
    
    /*Member Class*/
    private String database;
    
    @Override
    void setUrl() {
        this.setUrl("jdbc:sqlite:" + this.getDatabase());
    }

    @Override
    boolean setConnection()  {
        try {
            Class.forName("org.sqlite.JDBC");
            setConnection(DriverManager.getConnection(this.getUrl()));
        } 
        catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
        }
        return getConnection()!=null;
    }
    
    /***
     * Method Construct 
     * @param database Full path where is located the database in sqlite
     */
    public SQLite(String database)
    {        
        super();
        this.database=database;
        this.setUrl();
        this.setConnection();
    }

    /**
     * @return the database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * @param database the database to set
     */
    public void setDatabase(String database) {
        this.database = database;
    }
}
