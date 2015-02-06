
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
 *         &lt;element name="LocalityDescription" type="{http://www.museum.tulane.edu/webservices/}LocalityDescription"/>
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
    "localityDescription",
    "hwyX",
    "findWaterbody",
    "restrictToLowestAdm",
    "doUncert",
    "doPoly",
    "displacePoly",
    "polyAsLinkID",
    "languageKey"
})
@XmlRootElement(name = "Georef")
public class Georef {

    @XmlElement(name = "LocalityDescription", required = true)
    protected LocalityDescription localityDescription;
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
     * Gets the value of the localityDescription property.
     * 
     * @return
     *     possible object is
     *     {@link LocalityDescription }
     *     
     */
    public LocalityDescription getLocalityDescription() {
        return localityDescription;
    }

    /**
     * Sets the value of the localityDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalityDescription }
     *     
     */
    public void setLocalityDescription(LocalityDescription value) {
        this.localityDescription = value;
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
