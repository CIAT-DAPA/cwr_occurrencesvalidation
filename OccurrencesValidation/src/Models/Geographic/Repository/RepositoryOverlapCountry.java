/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Models.Geographic.Repository;

import Tools.Configuration;
import Tools.FixData;
import java.io.File;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

/**
 *
 * @author HSOTELO
 */
public class RepositoryOverlapCountry {
    /*Member Class*/
    /*private static File file;
    private static FileDataStore dataStore;
    private static String typeName;
    private static SimpleFeatureSource simpleFeatureSource;
    */
    /**
     * Method that search the coordinates in shapefile and later return the iso2 code of the country
     * @param latitude
     * @param longitude
     */
    
    public static String getIso2(double latitude,double longitude)
    {
        //Reference http://docs.geoserver.org/2.5.x/en/user/filter/ecql_reference.html#filter-ecql-reference
        String a=null;
        try{
            /*if(RepositoryOverlapCountry.file==null){
                File shp=new File(Configuration.PATH_FILE);
                RepositoryOverlapCountry.file = new File(shp.getAbsolutePath().replaceAll(Configuration.PATH_FILE, "") + Configuration.getParameter("geocoding_overlap_name") + FixData.splitSO() + Configuration.getParameter("geocoding_overlap_name") + ".shp" );
                RepositoryOverlapCountry.dataStore = FileDataStoreFinder.getDataStore(RepositoryOverlapCountry.file);
                RepositoryOverlapCountry.typeName = RepositoryOverlapCountry.dataStore.getTypeNames()[0];
                RepositoryOverlapCountry.simpleFeatureSource = dataStore.getFeatureSource(typeName);
            }*/
            
            File shp=new File(Configuration.PATH_FILE);
            File file = new File(shp.getAbsolutePath().replaceAll(Configuration.PATH_FILE, "") + Configuration.getParameter("geocoding_overlap_name") + FixData.splitSO() + Configuration.getParameter("geocoding_overlap_name") + ".shp" );
            FileDataStore dataStore = FileDataStoreFinder.getDataStore(file);
            String typeName = dataStore.getTypeNames()[0];
            SimpleFeatureSource simpleFeatureSource = dataStore.getFeatureSource(typeName);
            
            String point=String.valueOf(longitude) + " " + String.valueOf(latitude);
            Filter filter = ECQL.toFilter("INTERSECTS(the_geom, POINT(" + point + "))");
            SimpleFeatureCollection result = simpleFeatureSource.getFeatures(filter);
            FeatureIterator<SimpleFeature> iterator=result.features();
            if(iterator.hasNext()){
                SimpleFeature feature=iterator.next();
                a=feature.getAttribute("ISO2").toString();
            }
            iterator.close();
            
        }
        catch (Exception ex)
        {
            a=null;
            System.out.println("Error Overlap country: " +ex);
        }
        return a;
    }
}
