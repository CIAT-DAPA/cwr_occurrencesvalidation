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

package Models.DataBase.Taxonstand;

import Tools.Configuration;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import rcaller.RCaller;
import rcaller.RCode;
//import org.renjin.sexp.ListVector;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class Taxonstand {
    
    public static HashMap db=null;
    
    /**
     * Method that use R for ask to The Planet List about taxon
     * @param taxon name of taxon
     * @return 
     */
    public static HashMap get(String taxon)
    {
        HashMap a= null;
        try
        {
            if(db==null)
                db=new HashMap();
            if(db.containsKey(taxon.replaceAll(" ", "_")))
                return (HashMap)db.get(taxon.replaceAll(" ", "_"));
            RCaller caller = new RCaller();
            caller.setRscriptExecutable(Configuration.getParameter("taxonstand_rscript_path"));
            RCode code = new RCode();
            code.clear();
            code.addRCode("library(Taxonstand)");
            code.addRCode("r1 <- " + Configuration.getParameter("taxonstand_function_name") + "(\"" + taxon + "\"" +  Configuration.getParameter("taxonstand_function_parameters") + ")");
            code.addRCode("r1$Authority<-gsub(\"&\", \"y\", r1$Authority)");
            caller.setRCode(code);
            caller.runAndReturnResult("r1");
            //Xml
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            //load answer of r
            Document doc = dBuilder.parse( new InputSource(new ByteArrayInputStream(caller.getParser().getXMLFileAsString().getBytes("utf-8"))));
            //doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("variable");
            Element  var;
            String value;
            a=new HashMap();
            for (int i = 0; i < nList.getLength(); i++) 
            {
                var = (Element)nList.item(i);
                value=var.getTextContent()==null ? "" : var.getTextContent().replaceAll("\n", "").trim();
                if(var.getAttribute("name").toLowerCase().equals("authority"))
                    a.put("taxstand_author1",value);
                else if(var.getAttribute("name").toLowerCase().equals("family"))
                    a.put("taxstand_family",value);
                else if(var.getAttribute("name").toLowerCase().equals("new_genus"))
                    a.put("taxstand_genus",value);
                else if(var.getAttribute("name").toLowerCase().equals("new_species"))
                    a.put("taxstand_sp1",value);
                else if(var.getAttribute("name").toLowerCase().equals("new_infraspecific"))
                    a.put("taxstand_sp2",value);
            }
            db.put(taxon.replaceAll(" ", "_"), a);
        }
        catch (Exception ex)
        {
            a=null;
            System.out.println(ex);
        }
        return a;
    }
}
