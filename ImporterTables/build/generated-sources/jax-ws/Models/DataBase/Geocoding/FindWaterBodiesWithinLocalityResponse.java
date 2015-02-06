
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
 *         &lt;element name="FindWaterBodiesWithinLocalityResult" type="{http://www.museum.tulane.edu/webservices/}ArrayOfString" minOccurs="0"/>
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
    "findWaterBodiesWithinLocalityResult"
})
@XmlRootElement(name = "FindWaterBodiesWithinLocalityResponse")
public class FindWaterBodiesWithinLocalityResponse {

    @XmlElement(name = "FindWaterBodiesWithinLocalityResult")
    protected ArrayOfString findWaterBodiesWithinLocalityResult;

    /**
     * Gets the value of the findWaterBodiesWithinLocalityResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getFindWaterBodiesWithinLocalityResult() {
        return findWaterBodiesWithinLocalityResult;
    }

    /**
     * Sets the value of the findWaterBodiesWithinLocalityResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setFindWaterBodiesWithinLocalityResult(ArrayOfString value) {
        this.findWaterBodiesWithinLocalityResult = value;
    }

}
