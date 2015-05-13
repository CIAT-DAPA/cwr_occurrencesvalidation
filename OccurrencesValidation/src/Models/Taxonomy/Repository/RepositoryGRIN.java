/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Taxonomy.Repository;

import Tools.Configuration;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author HSOTELO
 */
public class RepositoryGRIN {
    
    public final static String HEADER="GR_Taxon|";
    
    /**
     * Method that search into database of GRIN the name of taxon
     * @param name Name of taxon
     * @param common True if it is common name, otherwise false
     * @return Name Accepted
     */
    public static String get(String name,boolean common)
    {
        String a=null;
        try
        {
            URL url = new URL(Configuration.getParameter("grin_url_base") + name.trim().replaceAll(" ", "+").replace("_", "+") + (common ? ":com" : ":sci"));
            BufferedReader reader=new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
            String line;
            while((line=reader.readLine())!=null)
            {
                if(line.startsWith("ACC="))
                {
                    a=line.replaceAll("ACC=", "").trim();
                    break;
                }
                else if(line.startsWith("HOM="))
                {
                    a=name;
                    break;
                }
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return a;
    }
}
