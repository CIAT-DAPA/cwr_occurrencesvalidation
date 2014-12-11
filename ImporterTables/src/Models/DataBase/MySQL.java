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
public class MySQL extends DBBase {

    @Override
    void setUrl() {
        this.setUrl("jdbc:mysql://" + this.getServer() + "/" + this.getBd());
    }

    @Override
    boolean setConnection()  {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection(this.getUrl(), this.getLogin(), this.getPassword()));
        } 
        catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
        }
        return getConnection()!=null;
    }
    
    /***
     * Method Construct 
     * @param server Server name or ip where database is locate
     * @param bd Name of database
     * @param login User login for database
     * @param password Password for database
     */
    public MySQL(String server,String bd,String login,String password)
    {
        super(server,bd,login,password);
    }
    
}
