
package Models.DataBase.Geocoding;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "geolocatesvc", targetNamespace = "http://www.museum.tulane.edu/webservices/", wsdlLocation = "http://www.museum.tulane.edu/webservices/geolocatesvcv2/geolocatesvc.asmx?WSDL")
public class Geolocatesvc
    extends Service
{

    private final static URL GEOLOCATESVC_WSDL_LOCATION;
    private final static WebServiceException GEOLOCATESVC_EXCEPTION;
    private final static QName GEOLOCATESVC_QNAME = new QName("http://www.museum.tulane.edu/webservices/", "geolocatesvc");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://www.museum.tulane.edu/webservices/geolocatesvcv2/geolocatesvc.asmx?WSDL");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        GEOLOCATESVC_WSDL_LOCATION = url;
        GEOLOCATESVC_EXCEPTION = e;
    }

    public Geolocatesvc() {
        super(__getWsdlLocation(), GEOLOCATESVC_QNAME);
    }

    public Geolocatesvc(WebServiceFeature... features) {
        super(__getWsdlLocation(), GEOLOCATESVC_QNAME, features);
    }

    public Geolocatesvc(URL wsdlLocation) {
        super(wsdlLocation, GEOLOCATESVC_QNAME);
    }

    public Geolocatesvc(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, GEOLOCATESVC_QNAME, features);
    }

    public Geolocatesvc(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public Geolocatesvc(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns GeolocatesvcSoap
     */
    @WebEndpoint(name = "geolocatesvcSoap")
    public GeolocatesvcSoap getGeolocatesvcSoap() {
        return super.getPort(new QName("http://www.museum.tulane.edu/webservices/", "geolocatesvcSoap"), GeolocatesvcSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns GeolocatesvcSoap
     */
    @WebEndpoint(name = "geolocatesvcSoap")
    public GeolocatesvcSoap getGeolocatesvcSoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.museum.tulane.edu/webservices/", "geolocatesvcSoap"), GeolocatesvcSoap.class, features);
    }

    /**
     * 
     * @return
     *     returns GeolocatesvcSoap
     */
    @WebEndpoint(name = "geolocatesvcSoap12")
    public GeolocatesvcSoap getGeolocatesvcSoap12() {
        return super.getPort(new QName("http://www.museum.tulane.edu/webservices/", "geolocatesvcSoap12"), GeolocatesvcSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns GeolocatesvcSoap
     */
    @WebEndpoint(name = "geolocatesvcSoap12")
    public GeolocatesvcSoap getGeolocatesvcSoap12(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.museum.tulane.edu/webservices/", "geolocatesvcSoap12"), GeolocatesvcSoap.class, features);
    }

    /**
     * 
     * @return
     *     returns GeolocatesvcHttpGet
     */
    @WebEndpoint(name = "geolocatesvcHttpGet")
    public GeolocatesvcHttpGet getGeolocatesvcHttpGet() {
        return super.getPort(new QName("http://www.museum.tulane.edu/webservices/", "geolocatesvcHttpGet"), GeolocatesvcHttpGet.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns GeolocatesvcHttpGet
     */
    @WebEndpoint(name = "geolocatesvcHttpGet")
    public GeolocatesvcHttpGet getGeolocatesvcHttpGet(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.museum.tulane.edu/webservices/", "geolocatesvcHttpGet"), GeolocatesvcHttpGet.class, features);
    }

    /**
     * 
     * @return
     *     returns GeolocatesvcHttpPost
     */
    @WebEndpoint(name = "geolocatesvcHttpPost")
    public GeolocatesvcHttpPost getGeolocatesvcHttpPost() {
        return super.getPort(new QName("http://www.museum.tulane.edu/webservices/", "geolocatesvcHttpPost"), GeolocatesvcHttpPost.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns GeolocatesvcHttpPost
     */
    @WebEndpoint(name = "geolocatesvcHttpPost")
    public GeolocatesvcHttpPost getGeolocatesvcHttpPost(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.museum.tulane.edu/webservices/", "geolocatesvcHttpPost"), GeolocatesvcHttpPost.class, features);
    }

    private static URL __getWsdlLocation() {
        if (GEOLOCATESVC_EXCEPTION!= null) {
            throw GEOLOCATESVC_EXCEPTION;
        }
        return GEOLOCATESVC_WSDL_LOCATION;
    }

}
