/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Complements.Source;

/**
 *
 * @author HSOTELO
 */
public class OriginStatDistribution {
    
    private String scientificName;
    private String country;
    private String type;
    
    public OriginStatDistribution(String scientificName, String country, String type){
        this.scientificName=scientificName;
        this.country=country;
        this.type=type;
    }

    /**
     * @return the scientificName
     */
    public String getScientificName() {
        return scientificName;
    }

    /**
     * @param scientificName the scientificName to set
     */
    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the countryIso3 to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
}
