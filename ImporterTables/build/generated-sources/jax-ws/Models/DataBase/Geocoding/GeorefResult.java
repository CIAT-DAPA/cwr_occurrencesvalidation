
package Models.DataBase.Geocoding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Georef_Result complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Georef_Result">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WGS84Coordinate" type="{http://www.museum.tulane.edu/webservices/}GeographicPoint"/>
 *         &lt;element name="ParsePattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Precision" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Score" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="UncertaintyRadiusMeters" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UncertaintyPolygon" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReferenceLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DisplacedDistanceMiles" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="DisplacedHeadingDegrees" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="Debug" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Georef_Result", propOrder = {
    "wgs84Coordinate",
    "parsePattern",
    "precision",
    "score",
    "uncertaintyRadiusMeters",
    "uncertaintyPolygon",
    "referenceLocation",
    "displacedDistanceMiles",
    "displacedHeadingDegrees",
    "debug"
})
public class GeorefResult {

    @XmlElement(name = "WGS84Coordinate", required = true)
    protected GeographicPoint wgs84Coordinate;
    @XmlElement(name = "ParsePattern")
    protected String parsePattern;
    @XmlElement(name = "Precision")
    protected String precision;
    @XmlElement(name = "Score")
    protected int score;
    @XmlElement(name = "UncertaintyRadiusMeters")
    protected String uncertaintyRadiusMeters;
    @XmlElement(name = "UncertaintyPolygon")
    protected String uncertaintyPolygon;
    @XmlElement(name = "ReferenceLocation")
    protected String referenceLocation;
    @XmlElement(name = "DisplacedDistanceMiles")
    protected double displacedDistanceMiles;
    @XmlElement(name = "DisplacedHeadingDegrees")
    protected double displacedHeadingDegrees;
    @XmlElement(name = "Debug")
    protected String debug;

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

    /**
     * Gets the value of the parsePattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParsePattern() {
        return parsePattern;
    }

    /**
     * Sets the value of the parsePattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParsePattern(String value) {
        this.parsePattern = value;
    }

    /**
     * Gets the value of the precision property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrecision() {
        return precision;
    }

    /**
     * Sets the value of the precision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrecision(String value) {
        this.precision = value;
    }

    /**
     * Gets the value of the score property.
     * 
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the value of the score property.
     * 
     */
    public void setScore(int value) {
        this.score = value;
    }

    /**
     * Gets the value of the uncertaintyRadiusMeters property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUncertaintyRadiusMeters() {
        return uncertaintyRadiusMeters;
    }

    /**
     * Sets the value of the uncertaintyRadiusMeters property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUncertaintyRadiusMeters(String value) {
        this.uncertaintyRadiusMeters = value;
    }

    /**
     * Gets the value of the uncertaintyPolygon property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUncertaintyPolygon() {
        return uncertaintyPolygon;
    }

    /**
     * Sets the value of the uncertaintyPolygon property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUncertaintyPolygon(String value) {
        this.uncertaintyPolygon = value;
    }

    /**
     * Gets the value of the referenceLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceLocation() {
        return referenceLocation;
    }

    /**
     * Sets the value of the referenceLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceLocation(String value) {
        this.referenceLocation = value;
    }

    /**
     * Gets the value of the displacedDistanceMiles property.
     * 
     */
    public double getDisplacedDistanceMiles() {
        return displacedDistanceMiles;
    }

    /**
     * Sets the value of the displacedDistanceMiles property.
     * 
     */
    public void setDisplacedDistanceMiles(double value) {
        this.displacedDistanceMiles = value;
    }

    /**
     * Gets the value of the displacedHeadingDegrees property.
     * 
     */
    public double getDisplacedHeadingDegrees() {
        return displacedHeadingDegrees;
    }

    /**
     * Sets the value of the displacedHeadingDegrees property.
     * 
     */
    public void setDisplacedHeadingDegrees(double value) {
        this.displacedHeadingDegrees = value;
    }

    /**
     * Gets the value of the debug property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebug() {
        return debug;
    }

    /**
     * Sets the value of the debug property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebug(String value) {
        this.debug = value;
    }

}
