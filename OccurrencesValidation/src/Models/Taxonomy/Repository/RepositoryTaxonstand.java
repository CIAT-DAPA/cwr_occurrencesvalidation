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
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import rcaller.RCaller;
import rcaller.RCode;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class RepositoryTaxonstand {    
    /*Const*/
    public final static String HEADER="Genus|Species|Abbrev|Infraspecific|ID|Plant_Name_Index|TPL_version|Taxonomic_status|Family|New_Genus|New_Hybrid_marker|New_Species|New_Infraspecific|Authority|New_ID|Typo|WFormat|";
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
            RCode code = new RCode();
            code.clear();
            code.addRCode("library(Taxonstand)");
            code.addRCode("r1 <- " + Configuration.getParameter("taxonstand_function_name") + "(\"" + taxon + "\"" +  Configuration.getParameter("taxonstand_function_parameters") + ")");
            code.addRCode("r1$Authority<-gsub(\"&\", \"&amp;\", r1$Authority)");
            caller.setRCode(code);
            caller.runAndReturnResult("r1");
            //Xml
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            //load answer of r
            Document doc = dBuilder.parse( new InputSource(new ByteArrayInputStream(caller.getParser().getXMLFileAsString().getBytes("utf-8"))));
            //Document doc = dBuilder.parse(caller.getParser().getXMLFile());
            //doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("variable");           
            a=new Taxonstand(nList);
            db.put(taxon.trim().replaceAll(" ", "_"), a);
        }
        catch (Exception ex)
        {
            a=null;
            System.out.println(ex);
        }
        return a;
    }

}
