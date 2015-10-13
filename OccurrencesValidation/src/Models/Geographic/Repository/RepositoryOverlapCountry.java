/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Models.Geographic.Repository;

import Tools.Configuration;
import Tools.FixData;
import java.io.File;
import java.io.FileNotFoundException;
import org.expr.rcaller.RCaller;
import org.expr.rcaller.RCode;
import org.expr.rcaller.exception.ExecutionException;
import org.w3c.dom.Element;

/**
 *
 * @author HSOTELO
 */
public class RepositoryOverlapCountry {
    /*Member Class*/
    
    
    public static String getIso2(double latitude,double longitude)
    {
        String a= null;
        try
        {
            RCaller caller = new RCaller();
            RCode code = new RCode();
            caller.setRscriptExecutable(Configuration.getParameter("taxonstand_rscript_path"));
            caller.redirectROutputToFile("overlap.txt", true);
            code.addRCode("require(maptools)");
            code.addRCode("require(shapefiles)");
            code.addRCode("require(rgdal)");
            code.addRCode("library(raster)");
            File shp=new File(Configuration.PATH_FILE);
            code.addRCode("path_to <- \"" +shp.getAbsolutePath().replaceAll(Configuration.PATH_FILE, "").replace(FixData.splitSO(), "/") + "\""); //P
            code.addRCode("wrld<-shapefile(paste0(path_to,\"" + Configuration.getParameter("geocoding_overlap_name") + "/" + Configuration.getParameter("geocoding_overlap_name") +  ".shp\"))");//P            
            code.addRCode("data <- data.frame(lat=c(" + String.valueOf(latitude) + "),lon=c(" + String.valueOf(longitude) + "))");//P
            code.addRCode("points<-cbind(data$lon,data$lat)");
            code.addRCode("points[,1]<-as.numeric(as.character(points[,1]))");
            code.addRCode("points[,2]<-as.numeric(as.character(points[,2]))");
            code.addRCode("points<-SpatialPoints(points)");
            code.addRCode("proj4string(points)<-CRS(\"+proj=longlat +datum=WGS84 +no_defs +ellps=WGS84 +towgs84=0,0,0\")");
            code.addRCode("ov<-over(points,wrld)");
            //code.addRCode("country <- data.frame(iso2=as.character(ov$ISO2),iso3=as.character(ov$ISO),name=as.character(ov$NAME_0))");
            code.addRCode("country <- as.character(ov$ISO2)");
            caller.setRCode(code);
            caller.runAndReturnResult("country");
            a=((Element)caller.getParser().getDocument().getElementsByTagName("variable").item(0)).getTextContent();
            a=a.replace("\n", "").trim();
        }
        catch (FileNotFoundException | ExecutionException ex)
        {
            a=null;
            System.out.println("Error Overlap country: " +ex);
        }
        return a;
    }
}
