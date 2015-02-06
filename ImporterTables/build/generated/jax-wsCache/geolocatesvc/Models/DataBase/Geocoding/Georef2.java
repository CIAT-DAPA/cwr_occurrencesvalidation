
package Models.DataBase.Geocoding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Country" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="County" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LocalityString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HwyX" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="FindWaterbody" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="RestrictToLowestAdm" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="doUncert" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="doPoly" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="displacePoly" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="polyAsLinkID" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="LanguageKey" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "country",
    "state",
    "county",
    "localityString",
    "hwyX",
    "findWaterbody",
    "restrictToLowestAdm",
    "doUncert",
    "doPoly",
    "displacePoly",
    "polyAsLinkID",
    "languageKey"
})
@XmlRootElement(name = "Georef2")
public class Georef2 {

    @XmlElement(name = "Country")
    protected String country;
    @XmlElement(name = "State")
    protected String state;
    @XmlElement(name = "County")
    protected String county;
    @XmlElement(name = "LocalityString")
    protected String localityString;
    @XmlElement(name = "HwyX")
    protected boolean hwyX;
    @XmlElement(name = "FindWaterbody")
    protected boolean findWaterbody;
    @XmlElement(name = "RestrictToLowestAdm")
    protected boolean restrictToLowestAdm;
    protected boolean doUncert;
    protected boolean doPoly;
    protected boolean displacePoly;
    protected boolean polyAsLinkID;
    @XmlElement(name = "LanguageKey")
    protected int languageKey;

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the county property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCounty() {
        return county;
    }

    /**
     * Sets the value of the county property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCounty(String value) {
        this.county = value;
    }

    /**
     * Gets the value of the localityString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalityString() {
        return localityString;
    }

    /**
     * Sets the value of the localityString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalityString(String value) {
        this.localityString = value;
    }

    /**
     * Gets the value of the hwyX property.
     * 
     */
    public boolean isHwyX() {
        return hwyX;
    }

    /**
     * Sets the value of the hwyX property.
     * 
     */
    public void setHwyX(boolean value) {
        this.hwyX = value;
    }

    /**
     * Gets the value of the findWaterbody property.
     * 
     */
    public boolean isFindWaterbody() {
        return findWaterbody;
    }

    /**
     * Sets the value of the findWaterbody property.
     * 
     */
    public void setFindWaterbody(boolean value) {
        this.findWaterbody = value;
    }

    /**
     * Gets the value of the restrictToLowestAdm property.
     * 
     */
    public boolean isRestrictToLowestAdm() {
        return restrictToLowestAdm;
    }

    /**
     * Sets the value of the restrictToLowestAdm property.
     * 
     */
    public void setRestrictToLowestAdm(boolean value) {
        this.restrictToLowestAdm = value;
    }

    /**
     * Gets the value of the doUncert property.
     * 
     */
    public boolean isDoUncert() {
        return doUncert;
    }

    /**
     * Sets the value of the doUncert property.
     * 
     */
    public void setDoUncert(boolean value) {
        this.doUncert = value;
    }

    /**
     * Gets the value of the doPoly property.
     * 
     */
    public boolean isDoPoly() {
        return doPoly;
    }

    /**
     * Sets the value of the doPoly property.
     * 
     */
    public void setDoPoly(boolean value) {
        this.doPoly = value;
    }

    /**
     * Gets the value of the displacePoly property.
     * 
     */
    public boolean isDisplacePoly() {
        return displacePoly;
    }

    /**
     * Sets the value of the displacePoly property.
     * 
     */
    public void setDisplacePoly(boolean value) {
        this.displacePoly = value;
    }

    /**
     * Gets the value of the polyAsLinkID property.
     * 
     */
    public boolean isPolyAsLinkID() {
        return polyAsLinkID;
    }

    /**
     * Sets the value of the polyAsLinkID property.
     * 
     */
    public void setPolyAsLinkID(boolean value) {
        this.polyAsLinkID = value;
    }

    /**
     * Gets the value of the languageKey property.
     * 
     */
    public int getLanguageKey() {
        return languageKey;
    }

    /**
     * Sets the value of the languageKey property.
     * 
     */
    public void setLanguageKey(int value) {
        this.languageKey = value;
    }

}
