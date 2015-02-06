
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
    "localityDescription"
})
@XmlRootElement(name = "FindWaterBodiesWithinLocality")
public class FindWaterBodiesWithinLocality {

    @XmlElement(name = "LocalityDescription", required = true)
    protected LocalityDescription localityDescription;

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

}
