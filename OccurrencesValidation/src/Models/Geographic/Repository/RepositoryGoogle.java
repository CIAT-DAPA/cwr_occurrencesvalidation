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

package Models.Geographic.Repository;

import Models.Geographic.Source.Location;
import Tools.Base64;
import Tools.Configuration;
import Tools.FixData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class RepositoryGoogle {

    public final static String HEADER ="group|latitude|longitude|text|value";
    
    private static HashMap db;
    
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
    private static String signRequest(String path, String query)throws NoSuchAlgorithmException, InvalidKeyException,UnsupportedEncodingException, URISyntaxException, IOException {
        byte[] key= Base64.decode(Configuration.getParameter("geocoding_google_key").replace('-', '+').replace('_', '/'));
        // Retrieve the proper URL components to sign
        String resource = path + '?' + query + "&client="+Configuration.getParameter("geocoding_google_client");
        // Get an HMAC-SHA1 signing key from the raw key bytes
        SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");
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
     * Method that geocoding a address from google api
     * @param country
     * @param adm1
     * @param adm2
     * @param adm3
     * @param local_area
     * @param locality
     * @return 
     */
    public static Location georenferencing(String country,String adm1, String adm2, String adm3,String local_area,String locality)
    {
        return RepositoryGoogle.georenferencing(country, adm1, adm2, adm3, local_area, locality,Double.parseDouble(Configuration.getParameter("geocoding_threshold")));
    }
    
    /**
     * Method that geocoding a address from google api
     * @param country
     * @param adm1
     * @param adm2
     * @param adm3
     * @param local_area
     * @param locality
     * @param uncertainty
     * @return 
     */
    public static Location georenferencing(String country,String adm1, String adm2, String adm3,String local_area,String locality,double uncertainty)
    {
        Location a=null;
        String key;
        try {
            key=FixData.generateKey(new String[]{country,adm1,adm2,adm3,local_area,locality});
            if(RepositoryGoogle.db==null)
                RepositoryGoogle.db=new HashMap();
            if(RepositoryGoogle.db.containsKey(key))
                return (Location)RepositoryGoogle.db.get(key);
            String data=(!country.equals("") ? country + "+,+" : "")+(!adm1.equals("") ? adm1 + "+,+" : "")+(!adm2.equals("") ? adm2 + "+,+" : "")+(!adm3.equals("") ? adm3 + "+,+" : "")+(!local_area.equals("") ? local_area + "+,+" : "")+(!locality.equals("") ? locality : "");
            URL url = new URL(Configuration.getParameter("geocoding_google_url_send_xml") + "address=" + data.replace(" ","%20").replace(".","").replace(";",""));
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
                    double distance = FixData.getDistance(coordValuesNortheast, coordValuesSouthwest); 
                    // Distance - km between Northeast and Southeast                    
                    if (distance <= uncertainty) 
                        a=new Location(coordValues[0], coordValues[1], distance);
                    else
                    {
                        RepositoryGoogle.db.put(key, a);
                        throw new Exception("Exceede uncertainty. " + "Uncertainty: " + distance + " THRESHOLD: " + Configuration.getParameter("geocoding_threshold"));
                    }
                        
                } 
            }            
            RepositoryGoogle.db.put(key, a);
        } catch (NoSuchAlgorithmException | InvalidKeyException | URISyntaxException | IOException | ParserConfigurationException | SAXException ex ) {
            System.out.println(ex);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return a;
    }
    
    /**
     * 
     * @param latitude
     * @param longitude
     * @return 
     */
    public static HashMap reverse(double latitude, double longitude)
    {
        HashMap a=new HashMap();
        try
        {
            //URL url=new URL(Configuration.getParameter("geocoding_google_url_send_json") + "latlng=" + String.valueOf(latitude) + "," + String.valueOf(longitude));            
            URL url = new URL(Configuration.getParameter("geocoding_google_url_send_json") + "latlng=" + String.valueOf(latitude) + "," + String.valueOf(longitude));
            URL file_url = new URL(url.getProtocol() + "://" + url.getHost() + signRequest(url.getPath(), url.getQuery()));
            //BufferedReader lector=new BufferedReader(new InputStreamReader(url.openStream()));
            BufferedReader lector=new BufferedReader(new InputStreamReader(file_url.openStream()));
            String textJson="",tempJs;
            while((tempJs=lector.readLine()) != null)
                textJson+=tempJs;
            if(textJson==null)
                throw new Exception("Don't found item" );
            JSONObject google=((JSONObject)JSONValue.parse(textJson));            
            a.put("status", google.get("status").toString());
            if(a.get("status").toString().equals("OK"))
            {
                JSONArray results=(JSONArray)google.get("results");
                JSONArray address_components=(JSONArray)((JSONObject)results.get(2)).get("address_components");
                for(int i=0;i<address_components.size();i++)
                {
                    JSONObject items=(JSONObject)address_components.get(i);
                    //if(((JSONObject)types.get(0)).get("").toString().equals("country"))
                    if(items.get("types").toString().contains("country"))
                    {
                        a.put("country",items.get("long_name").toString());
                        a.put("iso",items.get("short_name").toString());
                        break;
                    }
                }
            }
        }
        catch(Exception ex)
        {
            a=null;
            System.out.println("Error Google Geocoding: " + ex);
        }
        return a;
    }
}
