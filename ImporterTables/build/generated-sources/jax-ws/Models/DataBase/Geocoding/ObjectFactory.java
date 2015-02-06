
package Models.DataBase.Geocoding;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the Models.DataBase.Geocoding package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GeographicPoint_QNAME = new QName("http://www.museum.tulane.edu/webservices/", "GeographicPoint");
    private final static QName _GeorefResultSet_QNAME = new QName("http://www.museum.tulane.edu/webservices/", "Georef_Result_Set");
    private final static QName _String_QNAME = new QName("http://www.museum.tulane.edu/webservices/", "string");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: Models.DataBase.Geocoding
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GeorefResponse }
     * 
     */
    public GeorefResponse createGeorefResponse() {
        return new GeorefResponse();
    }

    /**
     * Create an instance of {@link GeorefResultSet }
     * 
     */
    public GeorefResultSet createGeorefResultSet() {
        return new GeorefResultSet();
    }

    /**
     * Create an instance of {@link Georef2PlusBGResponse }
     * 
     */
    public Georef2PlusBGResponse createGeoref2PlusBGResponse() {
        return new Georef2PlusBGResponse();
    }

    /**
     * Create an instance of {@link Georef2Response }
     * 
     */
    public Georef2Response createGeoref2Response() {
        return new Georef2Response();
    }

    /**
     * Create an instance of {@link Georef3Response }
     * 
     */
    public Georef3Response createGeoref3Response() {
        return new Georef3Response();
    }

    /**
     * Create an instance of {@link SnapPointToNearestFoundWaterBody }
     * 
     */
    public SnapPointToNearestFoundWaterBody createSnapPointToNearestFoundWaterBody() {
        return new SnapPointToNearestFoundWaterBody();
    }

    /**
     * Create an instance of {@link LocalityDescription }
     * 
     */
    public LocalityDescription createLocalityDescription() {
        return new LocalityDescription();
    }

    /**
     * Create an instance of {@link GeographicPoint }
     * 
     */
    public GeographicPoint createGeographicPoint() {
        return new GeographicPoint();
    }

    /**
     * Create an instance of {@link Georef }
     * 
     */
    public Georef createGeoref() {
        return new Georef();
    }

    /**
     * Create an instance of {@link CalcUncertaintyPolyResponse }
     * 
     */
    public CalcUncertaintyPolyResponse createCalcUncertaintyPolyResponse() {
        return new CalcUncertaintyPolyResponse();
    }

    /**
     * Create an instance of {@link SnapPointToNearestFoundWaterBodyResponse }
     * 
     */
    public SnapPointToNearestFoundWaterBodyResponse createSnapPointToNearestFoundWaterBodyResponse() {
        return new SnapPointToNearestFoundWaterBodyResponse();
    }

    /**
     * Create an instance of {@link Georef2PlusBG }
     * 
     */
    public Georef2PlusBG createGeoref2PlusBG() {
        return new Georef2PlusBG();
    }

    /**
     * Create an instance of {@link FindWaterBodiesWithinLocalityResponse }
     * 
     */
    public FindWaterBodiesWithinLocalityResponse createFindWaterBodiesWithinLocalityResponse() {
        return new FindWaterBodiesWithinLocalityResponse();
    }

    /**
     * Create an instance of {@link ArrayOfString }
     * 
     */
    public ArrayOfString createArrayOfString() {
        return new ArrayOfString();
    }

    /**
     * Create an instance of {@link Georef3 }
     * 
     */
    public Georef3 createGeoref3() {
        return new Georef3();
    }

    /**
     * Create an instance of {@link Georef2 }
     * 
     */
    public Georef2 createGeoref2() {
        return new Georef2();
    }

    /**
     * Create an instance of {@link CalcUncertaintyPoly }
     * 
     */
    public CalcUncertaintyPoly createCalcUncertaintyPoly() {
        return new CalcUncertaintyPoly();
    }

    /**
     * Create an instance of {@link FindWaterBodiesWithinLocality }
     * 
     */
    public FindWaterBodiesWithinLocality createFindWaterBodiesWithinLocality() {
        return new FindWaterBodiesWithinLocality();
    }

    /**
     * Create an instance of {@link SnapPointToNearestFoundWaterBody2Response }
     * 
     */
    public SnapPointToNearestFoundWaterBody2Response createSnapPointToNearestFoundWaterBody2Response() {
        return new SnapPointToNearestFoundWaterBody2Response();
    }

    /**
     * Create an instance of {@link SnapPointToNearestFoundWaterBody2 }
     * 
     */
    public SnapPointToNearestFoundWaterBody2 createSnapPointToNearestFoundWaterBody2() {
        return new SnapPointToNearestFoundWaterBody2();
    }

    /**
     * Create an instance of {@link GeorefResult }
     * 
     */
    public GeorefResult createGeorefResult() {
        return new GeorefResult();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GeographicPoint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.museum.tulane.edu/webservices/", name = "GeographicPoint")
    public JAXBElement<GeographicPoint> createGeographicPoint(GeographicPoint value) {
        return new JAXBElement<GeographicPoint>(_GeographicPoint_QNAME, GeographicPoint.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GeorefResultSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.museum.tulane.edu/webservices/", name = "Georef_Result_Set")
    public JAXBElement<GeorefResultSet> createGeorefResultSet(GeorefResultSet value) {
        return new JAXBElement<GeorefResultSet>(_GeorefResultSet_QNAME, GeorefResultSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.museum.tulane.edu/webservices/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

}
