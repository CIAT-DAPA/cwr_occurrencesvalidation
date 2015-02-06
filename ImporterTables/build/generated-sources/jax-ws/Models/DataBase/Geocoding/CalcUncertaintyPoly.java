
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
 *         &lt;element name="PolyGenerationKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "polyGenerationKey"
})
@XmlRootElement(name = "CalcUncertaintyPoly")
public class CalcUncertaintyPoly {

    @XmlElement(name = "PolyGenerationKey")
    protected String polyGenerationKey;

    /**
     * Gets the value of the polyGenerationKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolyGenerationKey() {
        return polyGenerationKey;
    }

    /**
     * Sets the value of the polyGenerationKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolyGenerationKey(String value) {
        this.polyGenerationKey = value;
    }

}
