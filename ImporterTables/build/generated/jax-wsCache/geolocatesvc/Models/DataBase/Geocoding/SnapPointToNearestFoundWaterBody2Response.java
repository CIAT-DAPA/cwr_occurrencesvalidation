
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
 *         &lt;element name="WGS84Coordinate" type="{http://www.museum.tulane.edu/webservices/}GeographicPoint"/>
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
    "wgs84Coordinate"
})
@XmlRootElement(name = "SnapPointToNearestFoundWaterBody2Response")
public class SnapPointToNearestFoundWaterBody2Response {

    @XmlElement(name = "WGS84Coordinate", required = true)
    protected GeographicPoint wgs84Coordinate;

    /**
     * Gets the value of the wgs84Coordinate property.
     * 
     * @return
     *     possible object is
     *     {@link GeographicPoint }
     *     
     */
    public GeographicPoint getWGS84Coordinate() {
        return wgs84Coordinate;
    }

    /**
     * Sets the value of the wgs84Coordinate property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeographicPoint }
     *     
     */
    public void setWGS84Coordinate(GeographicPoint value) {
        this.wgs84Coordinate = value;
    }

}
