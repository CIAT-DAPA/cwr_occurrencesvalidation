/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Geographic.Repository;

import Models.Geographic.Source.Country;
import Tools.Configuration;
import java.io.FileNotFoundException;
import java.util.HashMap;
import org.expr.rcaller.RCaller;
import org.expr.rcaller.RCode;
import org.expr.rcaller.exception.ExecutionException;

/**
 *
 * @author HSOTELO
 */
public class RepositoryOverlapCountry {
    /*Member Class*/
    
    public static Country getIso(double latitude,double longitude)
    {
        Country a= null;
        try
        {
            RCaller caller = new RCaller();
            caller.setRscriptExecutable(Configuration.getParameter("taxonstand_rscript_path"));
            caller.redirectROutputToFile("overlap.txt", true);
            RCode code = new RCode();
            code.clear();
            /* R Code
            require(maptools)
            require(shapefiles)
            require(rgdal)
            library(raster)

            path_to <- "C:/Users/hsotelo/Desktop/"

            data <- data.frame(lat=c(22222),lon=c(22222))

            #Read shape
            wrld<-shapefile(paste0(path_to,"VALIDATION_SHP/VALIDATION_SHP.shp"))

            #search country in the shape file
            points<-cbind(data$lon,data$lat)
            points[,1]<-as.numeric(as.character(points[,1]))
            points[,2]<-as.numeric(as.character(points[,2]))
            points<-SpatialPoints(points)
            proj4string(points)<-CRS("+proj=longlat +datum=WGS84 +no_defs +ellps=WGS84 +towgs84=0,0,0")
            ov<-over(points,wrld)

            #Add new columns with countries found
            country <- data.frame(iso2=ov$ISO2,iso3=ov$ISO,name=ov$NAME_0)
            */
            code.addRCode("require(maptools)");
            code.addRCode("require(shapefiles)");
            code.addRCode("require(rgdal)");
            code.addRCode("library(raster)");
            code.addRCode("path_to <- " + Country.class.getProtectionDomain().getCodeSource().getLocation().getPath()); //P            
            code.addRCode("data <- data.frame(lat=c(" + String.valueOf(latitude) + "),lon=c(" + String.valueOf(longitude) + "))");//P
            code.addRCode("wrld<-shapefile(paste0(path_to,\"" + Configuration.getParameter("geocoding_overlap_name") + "/" + Configuration.getParameter("geocoding_overlap_name") +  ".shp\"))");//P
            code.addRCode("points<-cbind(data$lon,data$lat)");
            code.addRCode("points[,1]<-as.numeric(as.character(points[,1]))");
            code.addRCode("points[,2]<-as.numeric(as.character(points[,2]))");
            code.addRCode("points<-SpatialPoints(points)");
            code.addRCode("proj4string(points)<-CRS(\"+proj=longlat +datum=WGS84 +no_defs +ellps=WGS84 +towgs84=0,0,0\")");
            code.addRCode("ov<-over(points,wrld)");
            code.addRCode("country <- data.frame(iso2=ov$ISO2,iso3=ov$ISO,name=ov$NAME_0)");
            caller.setRCode(code);
            caller.runAndReturnResult("country");                         
            a=new Country(caller.getParser().getDocument().getElementsByTagName("variable"));
        }
        catch (FileNotFoundException | ExecutionException ex)
        {
            a=null;
            System.out.println("Error Overlap country: " +ex);
        }
        return a;
    }
}
