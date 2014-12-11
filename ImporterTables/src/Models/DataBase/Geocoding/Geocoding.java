/**
 * Copyright 2014 International Center for Tropical Agriculture (CIAT).
 * This file is part of:
 * Crop Wild Relatives
 * It is free software: You can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 * It is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * See <http://www.gnu.org/licenses/>.
 */
package Models.DataBase.Geocoding;

import Tools.Base64;
import Tools.Configuration;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class Geocoding {

    /*Parameter*/
    private String URL_SEND;
    private double THRESHOLD;
    private String GOOGLE_KEY;
    private String GOOGLE_CLIENT;
    
    /*Members class*/
    private String data;
    private byte[] key;
    
    /*Methods*/
    /**
     *
     * @param final_country
     * @param adm1
     * @param adm2
     * @param adm3
     * @param local_area
     * @param locality
     */
    public Geocoding(String final_country,String adm1, String adm2, String adm3,String local_area,String locality)
    {
        this.data=(final_country != null ? final_country + "+,+" : "")+
                (adm1 != null ? adm1 + "+,+" : "")+
                (adm2 != null ? adm2 + "+,+" : "")+
                (adm3 != null ? adm3 + "+,+" : "")+
                (local_area != null ? local_area + "+,+" : "")+
                (locality != null ? locality : "");
        this.THRESHOLD=Double.parseDouble(Configuration.getParameter("geocoding_threshold"));
        this.URL_SEND=Configuration.getParameter("geocoding_google_url_send_xml");
        this.GOOGLE_KEY=Configuration.getParameter("geocoding_google_key");
        this.GOOGLE_CLIENT=Configuration.getParameter("geocoding_google_client");
        
    }
    /**
     * Method that transform the input in a query for google geocoding
     * @param dataLocation Data
     * @return
     */
    private String transformToValidQuery(String dataLocation) {
        return "address=" +dataLocation.replace(" ","%20").replace(".","").replace(";","");
    }
    
    /**
     * Create a string URL with the corresponding signature code and client id.
     * @param path
     * @param query
     * @return the specified String URL
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws URISyntaxException
     */
    public String signRequest(String path, String query)
            throws NoSuchAlgorithmException, InvalidKeyException,
            UnsupportedEncodingException, URISyntaxException, IOException {
        
        this.key = Base64.decode(this.GOOGLE_KEY.replace('-', '+').replace('_', '/'));
        // Retrieve the proper URL components to sign
        String resource = path + '?' + query + "&client="+this.GOOGLE_CLIENT;
        
        // Get an HMAC-SHA1 signing key from the raw key bytes
        SecretKeySpec sha1Key = new SecretKeySpec(this.key, "HmacSHA1");
        
        // Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1
        // key
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(sha1Key);
        
        // compute the binary signature for the request
        byte[] sigBytes = mac.doFinal(resource.getBytes());
        
        // base 64 encode the binary signature
        String signature = Base64.encodeBytes(sigBytes);
        
        // convert the signature to 'web safe' base 64
        signature = signature.replace('+', '-').replace('/', '_');
        
        return resource + "&signature=" + signature;
    }
    
    /**
     * Method that geocoding 
     * @return HashMap
     */
    public HashMap georenferencing()
    {
        HashMap a=null;
        try {
            
            URL url = new URL(URL_SEND + transformToValidQuery(getData()));
            URL file_url = new URL(url.getProtocol() + "://" + url.getHost() + signRequest(url.getPath(), url.getQuery()));
            
            // Get information from URL
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // Create a proxy to work in CIAT (erase this in another place)
            //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy2.ciat.cgiar.org", 8080));
            
            DocumentBuilder db = dbf.newDocumentBuilder();
            //Document doc = db.parse(file_url.openConnection(proxy).getInputStream());
            Document doc = db.parse(file_url.openConnection().getInputStream());
            
            // Document with data
            if(doc !=null)
            {
                NodeList locationList = doc.getElementsByTagName("location");
                NodeList locationTypeList = doc.getElementsByTagName("location_type");
                NodeList viewPortList = doc.getElementsByTagName("viewport");
                
                Node location = null, lat = null, lng = null;
                
                if (locationList.getLength() > 0)
                {
                    for (int i = 0; i < locationList.getLength(); i++)
                    {
                        location = locationList.item(i);
                        if (location.hasChildNodes()) {
                            lat = location.getChildNodes().item(1);
                            lng = location.getChildNodes().item(3);
                        }
                    }
                    
                    Node locationType = null;
                    if (locationTypeList.getLength() > 0)
                    {
                        for (int i = 0; i < locationTypeList.getLength(); i++)
                            locationType = locationTypeList.item(i);
                    }
                    
                    Node viewPort = null, northeast = null, southwest = null, lat_northeast = null, lng_northeast = null, lat_southwest = null, lng_southwest = null;
                    
                    if (viewPortList.getLength() > 0)
                    {
                        for (int i = 0; i < viewPortList.getLength(); i++)
                        {
                            viewPort = viewPortList.item(i);
                            if (viewPort.hasChildNodes())
                            {
                                northeast = viewPort.getChildNodes().item(1);
                                southwest = viewPort.getChildNodes().item(3);
                            }
                            /* Extract data from viewport field */
                            if (northeast.hasChildNodes())
                            {
                                lat_northeast = northeast.getChildNodes().item(1);
                                lng_northeast = northeast.getChildNodes().item(3);
                            }
                            if (southwest.hasChildNodes())
                            {
                                lat_southwest = southwest.getChildNodes().item(1);
                                lng_southwest = southwest.getChildNodes().item(3);
                            }
                        }
                    }
                    
                    double[] coordValues = new double[]{Double.parseDouble(lat.getTextContent()), Double.parseDouble(lng.getTextContent())};
                    double[] coordValuesNortheast = new double[]{Double.parseDouble(lat_northeast.getTextContent()),Double.parseDouble(lng_northeast.getTextContent())};
                    double[] coordValuesSouthwest = new double[]{Double.parseDouble(lat_southwest.getTextContent()), Double.parseDouble(lng_southwest.getTextContent())} ;
                    double distance = getDistance(coordValuesNortheast, coordValuesSouthwest); 

                    // Distance - km between Northeast and Southeast
                    if (distance <= THRESHOLD) 
                    { 
                        a=new HashMap();
                        a.put("latitude_georef", coordValues[0]);
                        a.put("longitude_georef", coordValues[1]);
                        a.put("distance_georef", distance);
                    } 
                } 
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | 
                URISyntaxException | IOException | 
                ParserConfigurationException | SAXException ex ) {
            System.out.println(ex);
        }
        return a;
    }
    
    /**
     * Method that get distance between two coordinates
     * @param coord1 Coordinates Northeast 
     * @param coord2 Coordinates Southwest
     * @return 
     */
    public double getDistance(double[] coord1, double[] coord2) {
        if (coord1.length == 2 && coord2.length == 2) {
            double LatA = (coord1[0] * Math.PI) / 180;
            double LatB = (coord2[0] * Math.PI) / 180;
            double LngA = (coord1[1] * Math.PI) / 180;
            double LngB = (coord2[1] * Math.PI) / 180;
            // Retorna la distancia en kilometros
            return 6371 * Math.acos(Math.cos(LatA) * Math.cos(LatB) * Math.cos(LngB - LngA) + Math.sin(LatA) * Math.sin(LatB));
        } else {
            System.out.println("Error: coord length is not correct");
            return -1;
        }
    }

    /**
     * @return the data
     */
    public String getData() {
        return data == null ? "" : data;
    }

}
