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
import Models.Occurrences.Source.TempOccurrences;
import java.sql.SQLException;


/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class RepositoryTempOccurrences extends BaseRepository {    
   
    /**
     * Method Construct
     */
    public RepositoryTempOccurrences(){
        super("temp_occurrences");
    }
    
    /*Methods CRUD*/
    public ResultQuery add(TempOccurrences entity) throws SQLException{
        return super.add(entity);
    }
    
    /**
     * Query for bring the fields mandatory for cross check process
     * @throws SQLException 
     */
    public void listCrossCheck() throws SQLException{
        db.getResults("Select id,old_id,availability,cult_stat,source,origin_stat,is_hybrid,filename, " +
                    "country,adm1,adm2,adm3,local_area,locality, " +
                    "iso, " +
                    "x1_rank1,x1_rank2, " +
                    "filename,username,provider_institute_id,field_collected_data,data_public_access,citation,source, " +
                    "x1_genus,x1_sp1, x1_sp2, x1_sp3, " +
                    "tnrs_final_taxon,taxstand_final_taxon, " +
                    "lat_deg,lat_min,lat_sec, " +
                    "long_deg,long_min,long_sec, " +
                    "ns,ew, " +
                    "latitude,longitude, " +
                    "taxon_final, " +
                    "final_iso2, " +
                    "coord_source, " +
                    "latitude_georef,longitude_georef " +
                    "From temp_occurrences ");
    }
}
