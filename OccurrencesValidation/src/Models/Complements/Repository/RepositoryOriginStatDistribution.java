/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Complements.Repository;

import Models.Complements.Source.OriginStatDistribution;
import Models.DataBase.SQLite;
import Tools.Configuration;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author HSOTELO
 */
public class RepositoryOriginStatDistribution {
    
    private static HashMap data;
    
    /**
     * Method that search a taxon into database a return their distribution
     * @param scientific_name
     * @return 
     */
    public static ArrayList<OriginStatDistribution> get(String scientific_name,String country)
    {
        try
        {
            if(data!=null && data.containsKey(scientific_name.replaceAll(" ", "_")))
                return (ArrayList<OriginStatDistribution>)data.get(scientific_name.replaceAll(" ", "_"));
            SQLite db=new SQLite(Configuration.getParameter("origin_stat_database"));
            db.getResults("Select scientific_name,country,type " +
                          "From " + Configuration.getParameter("origin_stat_database_table") + " " +
                          "Where scientific_name = '" +  scientific_name +"' and country='" + country +"'");
            ArrayList<OriginStatDistribution> temp=new ArrayList<>();
            while(db.getRecordSet().next())
                temp.add(new OriginStatDistribution(db.getRecordSet().getString("scientific_name"), db.getRecordSet().getString("country"), db.getRecordSet().getString("type")));
            if(data==null)
                data=new HashMap();            
            data.put(scientific_name.replaceAll(" ", "_"),temp.size()> 0 ? temp:null);
            return (ArrayList<OriginStatDistribution>)data.get(scientific_name);
        }
        catch(Exception ex)
        {
            return null;
        }
    }
}
