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

package Models.Taxonomy.Repository;

import Models.Taxonomy.Source.Taxonstand;
import Tools.Configuration;
import java.util.HashMap;
import org.expr.rcaller.RCaller;
import org.expr.rcaller.RCode;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class RepositoryTaxonstand {    
    /*Const*/
    public final static String HEADER="TXST_Genus|TXST_Species|TXST_Abbrev|TXST_Infraspecific|TXST_ID|TXST_Plant_Name_Index|TXST_TPL_version|TXST_Taxonomic_status|TXST_Family|TXST_New_Genus|TXST_New_Hybrid_marker|TXST_New_Species|TXST_New_Infraspecific|TXST_Authority|TXST_New_ID|TXST_Typo|TXST_WFormat|";
    /*Member Class*/
    public static HashMap db=null;
    
    public static Taxonstand get(String taxon)
    {
        Taxonstand a= null;
        try
        {
            if(db==null)
                db=new HashMap();
            if(db.containsKey(taxon.trim().replaceAll(" ", "_")))
                return (Taxonstand)db.get(taxon.trim().replaceAll(" ", "_"));
            RCaller caller = new RCaller();
            caller.setRscriptExecutable(Configuration.getParameter("taxonstand_rscript_path"));
            caller.redirectROutputToFile("taxon.txt", true);
            RCode code = new RCode();
            code.clear();
            code.addRCode("library(Taxonstand)");
            code.addRCode("r1 <- " + Configuration.getParameter("taxonstand_function_name") + "(\"" + taxon + "\"" +  Configuration.getParameter("taxonstand_function_parameters") + ")");            
            code.addRCode("r1$Authority<-gsub(\"&\", \"&amp;\",r1$Authority)");
            caller.setRCode(code);
            caller.runAndReturnResult("r1");                         
            a=new Taxonstand(caller.getParser().getDocument().getElementsByTagName("variable"));
            db.put(taxon.trim().replaceAll(" ", "_"), a);
        }
        catch (Exception ex)
        {
            a=null;
            System.out.println("Error Taxonstand: " +ex);
        }
        return a;
    }

}
