/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Geographic.Source;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author HSOTELO
 */
public class Country {
    //Members Class
    private String iso2;    
    private String iso3;
    private String name;

    /**
     * Method Construct
     */
    public Country() {
        this.iso2 = "";
        this.iso3 = "";
        this.name = "";
    }

    /**
     * Method Construct
     */
    public Country(String iso2, String iso3, String name) {
        this.iso2 = iso2;
        this.iso3 = iso3;
        this.name = name;
    }
    
    /**
     * Method Construct
     * @param nList Node with information
     */
    public Country(NodeList nList){
        super();
        Element  var;
        String value;
        for (int i = 0; i < nList.getLength(); i++)
        {
            var = (Element)nList.item(i);
            value=var.getTextContent()==null ? "" : var.getTextContent().replaceAll("\n", "").trim();
            if(var.getAttribute("name").toLowerCase().equals("iso2"))
                iso2=value;
            else if(var.getAttribute("name").toLowerCase().equals("iso3"))
                iso3=value;
            else if(var.getAttribute("name").toLowerCase().equals("name"))
                name=value;
        }
    }

    /**
     * @return the iso2
     */
    public String getIso2() {
        return iso2;
    }

    /**
     * @param iso2 the iso2 to set
     */
    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    /**
     * @return the iso3
     */
    public String getIso3() {
        return iso3;
    }

    /**
     * @param iso3 the iso3 to set
     */
    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
