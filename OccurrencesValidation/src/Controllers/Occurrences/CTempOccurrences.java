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

package Controllers.Occurrences;

import Controllers.Tools.Validation.Policy;
import Controllers.Tools.Validation.TypePolicy;
import Models.Geographic.Repository.RepositoryWaterBody;
import Models.Geographic.Repository.RepositoryGeolocate;
import Models.Geographic.Repository.RepositoryGoogle;
import Models.Geographic.Source.Location;
import Models.Geographic.Source.LocationGeolocate;
import Models.Occurrences.Repository.RepositoryTempCountries;
import Models.Occurrences.Repository.RepositoryTempOccurrences;
import Models.Occurrences.Source.TempCountries;
import Models.Occurrences.Source.TempOccurrences;
import Models.Taxonomy.Repository.RepositoryTNRS;
import Models.Taxonomy.Repository.RepositoryTaxonstand;
import Models.Taxonomy.Source.TNRS;
import Models.Taxonomy.Source.Taxonstand;
import Tools.Configuration;
import Tools.FixData;
import Tools.Log;
import Tools.TypeLog;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class CTempOccurrences extends BaseController {
    
    /*Const*/
    private final String PREFIX_IMPORT = "TOI";
    private final String PREFIX_CROSSCHECK = "TOC";
    private final String PREFIX_UPDATE = "TOU";
    /*Members Class*/
    private String[] FIELDS_MANDATORY;
    private String[] CONTENT_AVAILABILITY;
    private String[] CONTENT_SOURCE;
    private String[] CONTENT_CULT;
    private String[] CONTENT_ORIGIN;
    private String[] CONTENT_HYBRID;
    private String[] GEO_NS;
    private String[] GEO_EW;
    private String REGEXP_COORDS;
    
    /*Properties*/
    /**
     * Get repository of TempOccurrences
     * @return
     */
    private RepositoryTempOccurrences getRepository(){
        return (RepositoryTempOccurrences)repository;
    }
    
    /*Methods*/
    /**
     * Method Construct
     */
    public CTempOccurrences() {
        super(new RepositoryTempOccurrences());
        FIELDS_MANDATORY=FixData.valueParameterSplit(Configuration.getParameter("validation_fields_mandatory"));
        CONTENT_AVAILABILITY=FixData.valueParameterSplit(Configuration.getParameter("validation_content_availability"));
        CONTENT_SOURCE=FixData.valueParameterSplit(Configuration.getParameter("validation_content_source"));
        CONTENT_CULT=FixData.valueParameterSplit(Configuration.getParameter("validation_content_cult"));
        CONTENT_ORIGIN=FixData.valueParameterSplit(Configuration.getParameter("validation_content_origin"));
        CONTENT_HYBRID=FixData.valueParameterSplit(Configuration.getParameter("validation_content_hybrid"));
        GEO_NS=FixData.valueParameterSplit(Configuration.getParameter("validation_content_ns"));
        GEO_EW=FixData.valueParameterSplit(Configuration.getParameter("validation_content_ew"));
        REGEXP_COORDS=Configuration.getParameter("validation_content_regexp_coords");
    }
    
    /**
     * Method that import file to database
     * @param filePath Path of source file
     * @param fileSplit Pattern to split line
     * @param clean True if you want clean data of accents
     * @param log Path to log
     * @return Count of errors
     * @throws SQLException
     * @throws Exception
     */
    public long importFile(String filePath, String fileSplit, boolean clean, String log) throws SQLException, Exception{
        return super.importFile(filePath, fileSplit, clean, new TempOccurrences(), log, PREFIX_IMPORT);
    }
    
    /**
     * Method that import file to database
     * @param filePath Path of source file
     * @param fileSplit Pattern to split line
     * @param log Path to log
     * @return Count of errors
     * @throws SQLException
     * @throws Exception
     */
    public long updateFile(String filePath, String fileSplit, String log) throws SQLException, Exception{
        return super.updateFile(filePath, fileSplit, new TempOccurrences(), log, PREFIX_UPDATE, "id");
    }
    
    /**
     * Method that import file to database
     * @param filePath Path of source file
     * @param log Path to log
     * @return Count of errors
     * @throws SQLException
     * @throws Exception
     */
    public long updateFileQuery(String filePath, String log) throws SQLException, Exception{
        return super.updateFileQuery(filePath, log, PREFIX_UPDATE);
    }
    
    /**
     * Method that validate data of the occurrences
     * @param step
     * @param policies
     * @param log
     * @return
     * @throws SQLException
     */
    public long crossCheck(int step,ArrayList<Policy> policies, String log) throws SQLException{
        int a=0;
        String header="Update " + getRepository().getTable() + " set ",footer, query;
        TNRS[] tnrs;
        Taxonstand taxonstand;
        Location lGoogle,lGeolocate;
        RepositoryTempCountries rTCountries=new RepositoryTempCountries();
        TempCountries country;
        
        HashMap googleReverse;
        String value1, value2;
        String name,taxon_final,taxon_tnrs_final,taxon_taxstand_final;
        
        RepositoryWaterBody rWater;
        String water;
        String temp_country,temp_adm1,temp_adm2,temp_adm3,temp_local_area,temp_locality;
        String review_data;
        long row=0, countRows;
        TempOccurrences entity=new TempOccurrences();
        if(step==2)
            Log.register(log, TypeLog.REVIEW_DATA, "id|x1_genus|x1_sp1|x1_rank1|x1_sp2|x1_rank2|x1_sp3|" + RepositoryTNRS.HEADER + RepositoryTaxonstand.HEADER, false,PREFIX_CROSSCHECK + String.valueOf(step),Configuration.getParameter("log_ext_review"));
        else if(step==3)
            Log.register(log, TypeLog.REVIEW_DATA, "id|country|adm1|adm2|adm3|local_area|locality|" + RepositoryGoogle.HEADER + RepositoryGeolocate.HEADER, false,PREFIX_CROSSCHECK + String.valueOf(step),Configuration.getParameter("log_ext_review"));
        rWater=new RepositoryWaterBody(Configuration.getParameter("geocoding_database_world"));
        countRows=repository.count();
        getRepository().listCrossCheck();
        while((entity=(TempOccurrences)getRepository().hasNext(entity)) != null)
        {
            review_data = entity.getString("id") + "|";
            query=header;
            row+=1;
            for(Policy p: policies)
            {
                try
                {
                    //Group Check Part 1
                    //3.1
                    if(p.getTypePolicy()==TypePolicy.CHECK_FIELDS_MANDATORY)
                    {
                        for(String f : FIELDS_MANDATORY)
                        {
                            if(entity.getString(f)== null)
                                throw new Exception(f + " is mandatory");
                        }
                    }
                    //3.2
                    else if(p.getTypePolicy()==TypePolicy.CHECK_AVAILABILITY && !FixData.containsValue(entity.getString("availability"), FixData.toArrayList(CONTENT_AVAILABILITY)))
                        throw new Exception("Availability not correct: " + entity.getString("availability"));
                    //3.3
                    else if(p.getTypePolicy()==TypePolicy.CHECK_SOURCE && !FixData.containsValue(entity.getString("source"), FixData.toArrayList(CONTENT_SOURCE)))
                        throw new Exception("Source not correct: " + entity.getString("source"));
                    //3.4
                    else if(p.getTypePolicy()==TypePolicy.CHECK_CULT && !FixData.containsValue(entity.getString("cult_stat"), FixData.toArrayList(CONTENT_CULT)))
                        throw new Exception("Cult not correct: " + entity.getString("cult_stat"));
                    //3.5
                    else if(p.getTypePolicy()==TypePolicy.CHECK_ORIGIN_STAT && !FixData.containsValue(entity.getString("origin_stat"), FixData.toArrayList(CONTENT_ORIGIN)))
                        throw new Exception("Origin Stat not correct: " + entity.getString("origin_stat"));
                    //3.6
                    else if(p.getTypePolicy()==TypePolicy.CHECK_IS_HYBRID && !FixData.containsValue(entity.getString("is_hybrid"), FixData.toArrayList(CONTENT_HYBRID)))
                        throw new Exception("Is hybrid not correct: " + entity.getString("is_hybrid"));
                    //Group Correct
                    //3.3
                    else if(p.getTypePolicy()==TypePolicy.CORRECT_DEPENDENCE_SOURCE_AVAILABILITY && entity.getString("availability") != null && FixData.containsValue(entity.getString("availability"), FixData.toArrayList(new String[]{"0"})) && FixData.containsValue(entity.getString("source"), FixData.toArrayList(new String[]{"H"})))
                        query+="SOURCE='H',";
                    //Group Check Part 2
                    //3.4
                    else if(p.getTypePolicy()==TypePolicy.FINAL_CULT)
                        query+= "final_cult_stat=" + ((entity.getString("cult_stat") != null && FixData.containsValue(entity.getString("cult_stat"), FixData.toArrayList(CONTENT_CULT))) ? "'" + entity.getString("cult_stat") + "'" : "null" ) + ",";
                    //3.5
                    else if(p.getTypePolicy()==TypePolicy.FINAL_ORIGIN)
                        query+= "final_origin_stat=" + ((entity.getString("origin_stat") != null && FixData.containsValue(entity.getString("origin_stat"), FixData.toArrayList(CONTENT_ORIGIN))) ? "'" + FixData.translate(1, entity.getString("origin_stat"))  + "'" : "null" ) + ",";
                    //Group TNRS
                    //4.4
                    else if(p.getTypePolicy()==TypePolicy.TNRS_QUERY)
                    {
                        name=FixData.concatenate(new String[]{entity.getString("x1_genus"),
                            entity.getString("x1_sp1"),
                            entity.getString("x1_rank1"),
                            entity.getString("x1_sp2"),
                            entity.getString("x1_rank2"),
                            entity.getString("x1_sp3")}," ");
                        review_data += FixData.getValue(entity.getString("x1_genus")) + "|" +
                                FixData.getValue(entity.getString("x1_sp1")) + "|" +
                                FixData.getValue(entity.getString("x1_rank1")) + "|" +
                                FixData.getValue(entity.getString("x1_sp2")) + "|" +
                                FixData.getValue(entity.getString("x1_rank2")) + "|" +
                                FixData.getValue(entity.getString("x1_sp3")) + "|" ;
                        tnrs=RepositoryTNRS.get(name, true);
                        if(tnrs==null)
                            tnrs=RepositoryTNRS.get(name, false);
                        if(tnrs==null)
                            throw new Exception("Not found taxon in tnrs");
                        for(TNRS t:tnrs)
                        {
                            if(t.finalTaxon!=null && !t.finalTaxon.equals(""))
                            {
                                review_data+=t.toString();
                                query+="tnrs_author1='" + t.authorAttributed + "',"+
                                        "tnrs_final_taxon='" + t.finalTaxon.replaceAll(" ", "_") + "'," +
                                        "tnrs_overall_score='" + String.valueOf(t.overall)  + "'," +
                                        "tnrs_source='" + t.acceptedNameUrl + "'," +
                                        "tnrs_x1_family='" + t.family  + "',";
                                break;
                            }
                        }
                    }
                    //Group Taxonstand
                    //4.5
                    else if(p.getTypePolicy()==TypePolicy.TAXONDSTAND_QUERY)
                    {
                        name=FixData.concatenate(new String[]{entity.getString("x1_genus"),
                            entity.getString("x1_sp1"),
                            entity.getString("x1_rank1"),
                            entity.getString("x1_sp2"),
                            entity.getString("x1_rank2"),
                            entity.getString("x1_sp3")}," ");
                        taxonstand=RepositoryTaxonstand.get(name);
                        if(taxonstand==null)
                            throw new Exception("Not found taxon in taxonstand");
                        else
                        {
                            review_data+=taxonstand.toString();
                            query+="taxstand_author1='" + taxonstand.author + "',"+
                                    "taxstand_family='" + taxonstand.family.replaceAll(" ", "_") + "'," +
                                    "taxstand_final_taxon='" + FixData.concatenate(new String[]{ taxonstand.genus,
                                        taxonstand.species,
                                        taxonstand.species2}," ").replaceAll(" ", "_")  + "'," +
                                    "taxstand_genus='" + taxonstand.genus + "'," +
                                    "taxstand_sp1='" + taxonstand.species  + "'," +
                                    "taxstand_sp2='" + taxonstand.species2  + "',";
                        }
                    }
                    //Group Geocoding
                    //5.1
                    else if(p.getTypePolicy()==TypePolicy.GEOCODING_VALIDATE_COUNTRY)
                    {
                        country=rTCountries.searchIso3(entity.getString("iso"));
                        if(country == null)
                        {
                            country=rTCountries.searchByName(entity.getString("country"));
                            if(country == null)
                                throw new Exception("Country not found for country: iso=" + (entity.getString("iso") == null ? "null" : entity.getString("iso")) + " country=" + (entity.getString("country") == null ? "null" : entity.getString("country")));
                            else
                                query+="final_country='" +country.getString("name") + "',";
                        }
                        else
                            query+="final_country='" + country.getString("name") + "',";
                    }
                    //5.2
                    else if(p.getTypePolicy()==TypePolicy.GEOCODING_VALIDATE_ISO2)
                    {
                        country=rTCountries.searchIso3(entity.getString("iso"));
                        if(country == null)
                        {
                            country=rTCountries.searchByName(entity.getString("country"));
                            if(country == null)
                                throw new Exception("Country not found for final_iso: iso=" + (entity.getString("iso") == null ? "null" : entity.getString("iso")) + " country=" + (entity.getString("country") == null ? "null" : entity.getString("country")));
                            else
                                query+="final_iso2='" +country.getString("iso2") + "',";
                        }
                        else
                            query+="final_iso2='" + country.getString("iso2") + "',";
                    }
                    //5.3.1  5.3.4
                    else if(p.getTypePolicy()==TypePolicy.GEOCODING_VALIDATE_COORDS_GRADS)
                    {
                        if(entity.getString("lat_deg") != null && !entity.getString("lat_deg").matches(REGEXP_COORDS))
                            throw new Exception("Error in lat_deg: " + entity.getString("lat_deg"));
                        else if(entity.getString("lat_min") != null && !entity.getString("lat_min").matches(REGEXP_COORDS))
                            throw new Exception("Error in lat_min: " + entity.getString("lat_min"));
                        else if(entity.getString("lat_sec") != null && !entity.getString("lat_sec").matches(REGEXP_COORDS))
                            throw new Exception("Error in lat_sec: " + entity.getString("lat_sec"));
                        else if(entity.getString("long_deg") != null && !entity.getString("long_deg").matches(REGEXP_COORDS))
                            throw new Exception("Error in long_deg: " + entity.getString("long_deg"));
                        else if(entity.getString("long_min") != null && !entity.getString("long_min").matches(REGEXP_COORDS))
                            throw new Exception("Error in long_min: " + entity.getString("long_min"));
                        else if(entity.getString("long_sec") != null && !entity.getString("long_sec").matches(REGEXP_COORDS))
                            throw new Exception("Error in long_sec: " + entity.getString("long_sec"));
                        else if(entity.getString("latitude") != null && !entity.getString("latitude").matches(REGEXP_COORDS))
                            throw new Exception("Error in latitude: " + entity.getString("latitude"));
                        else if(entity.getString("longitude") != null && !entity.getString("longitude").matches(REGEXP_COORDS))
                            throw new Exception("Error in longitude: " + entity.getString("longitude"));
                    }
                    //5.3.2
                    else if(p.getTypePolicy()==TypePolicy.GEOCODING_VALIDATE_NS_EW)
                    {
                        //ns
                        if(!FixData.containsValue(entity.getString("ns"), FixData.toArrayList(GEO_NS)))
                            throw new Exception("Error in NS with data: " + (entity.getString("ns")==null ? "null" : entity.getString("ns")));
                        else if(entity.getString("ns")!= null && FixData.containsValue(entity.getString("ns"), FixData.toArrayList(GEO_NS)))
                            query+="final_ns='" + entity.getString("ns") + "',";
                        //ew
                        if(!FixData.containsValue(entity.getString("ew"), FixData.toArrayList(GEO_EW)))
                            throw new Exception("Error in EW with data: " + (entity.getString("ew")==null ? "null" : entity.getString("ew")));
                        else if(entity.getString("ew")!=null && FixData.containsValue(entity.getString("ew"), FixData.toArrayList(GEO_EW)))
                            query+="final_ew='" + entity.getString("ew") + "',";
                    }
                    //5.3.3
                    else if(p.getTypePolicy()==TypePolicy.GEOCODING_VALIDATE_COORDS_LAT_LON)
                    {
                        if(entity.getString("lat_deg") != null && entity.getString("latitude") == null)
                            query+= "latitude='" + FixData.degreesToDecimalDegrees(entity.getString("ns").contains("n") , entity.getDouble("lat_deg"), entity.getDouble("lat_min"), entity.getDouble("lat_sec"))  + "',";
                        if(entity.getString("long_deg") != null && entity.getString("longitude") == null)
                            query+= "longitude='" + FixData.degreesToDecimalDegrees(entity.getString("ew").contains("e") , entity.getDouble("long_deg"), entity.getDouble("long_min"), entity.getDouble("long_sec"))  + "',";
                    }
                    //5.4
                    else if(p.getTypePolicy()==TypePolicy.GEOCODING_INITIAL)
                    {
                        temp_country=FixData.getValueImaginary(entity.getString("country"));
                        temp_adm1=FixData.getValueImaginary(entity.getString("adm1"));
                        temp_adm2=FixData.getValueImaginary(entity.getString("adm2"));
                        temp_adm3=FixData.getValueImaginary(entity.getString("adm3"));
                        temp_local_area=FixData.getValueImaginary(entity.getString("local_area"));
                        temp_locality=FixData.getValueImaginary(entity.getString("locality"));
                        lGoogle= RepositoryGoogle.georenferencing(temp_country,temp_adm1,temp_adm2,temp_adm3,temp_local_area,temp_locality);
                        lGeolocate = RepositoryGeolocate.georenferencing(temp_country,temp_adm1,temp_adm2,temp_adm3,temp_local_area,temp_locality);
                        //Review data
                        review_data+= temp_country + "|" + temp_adm1 + "|" + temp_adm2 + "|" + temp_adm3 + "|" + temp_local_area + "|" + temp_locality + "|";
                        review_data+= lGoogle != null ? lGoogle.toString():"|||";
                        review_data+= lGeolocate != null ? ((LocationGeolocate)lGeolocate).toString():"||||";
                        
                        if(lGoogle != null)
                            query+= "latitude_georef='" + String.valueOf(lGoogle.getLatitude()) + "'," +
                                    "longitude_georef='" + String.valueOf(lGoogle.getLongitude()) + "'," +
                                    "distance_georef='" + String.valueOf(lGoogle.getUncertainty()) + "',";
                        else if(lGeolocate != null)
                            query+= "latitude_georef='" + String.valueOf(lGeolocate.getLatitude()) + "'," +
                                    "longitude_georef='" + String.valueOf(lGeolocate.getLongitude()) + "'," +
                                    "distance_georef='" + String.valueOf(lGeolocate.getUncertainty()) + "',";
                        else
                            throw new Exception("Can't geocoding register " + temp_country + "," + temp_adm1 + "," + temp_adm2 + "," + temp_adm3 + "," + temp_local_area + "," + temp_locality);
                    }
                    //Group POST CHECK
                    //4.6
                    else if(p.getTypePolicy()==TypePolicy.POSTCHECK_VALIDATE_TAXON)
                    {
                        taxon_final=FixData.concatenate(new String[]{entity.getString("x1_genus"),
                            entity.getString("x1_sp1"),
                            entity.getString("x1_rank1"),
                            entity.getString("x1_sp2"),
                            entity.getString("x1_rank2"),
                            entity.getString("x1_sp3")},"_").toLowerCase();
                        taxon_tnrs_final= entity.getString("tnrs_final_taxon") == null ? "" : FixData.removePatternEnd(entity.getString("tnrs_final_taxon").toLowerCase(),"_");
                        taxon_taxstand_final= entity.getString("taxstand_final_taxon") == null ? "" : FixData.removePatternEnd(entity.getString("taxstand_final_taxon").toLowerCase(),"_");
                        if(taxon_final.equals(taxon_tnrs_final) && FixData.hideRank(taxon_final).equals(taxon_taxstand_final))
                        {
                            query+= "taxon_final='" + FixData.toCapitalLetter(taxon_final) + "',";
                            //4.1
                            value1=FixData.validateRank(entity.getString("x1_rank1"));
                            query+=value1==null || value1.equals("") ? "" : "f_x1_rank1='" + value1 + "'";
                            value2=FixData.validateRank(entity.getString("x1_rank2"));
                            query+=value2==null || value2.equals("") ? "" : "f_x1_rank2='" + value2 + "'";
                            //4.2 4.3
                            query+=FixData.prepareUpdate("f_x1_genus", FixData.toCapitalLetter(entity.getString("x1_genus")) , true, false) +
                                    FixData.prepareUpdate("f_x1_sp1", entity.getString("x1_sp1"), true, true) +
                                    FixData.prepareUpdate("f_x1_sp2", entity.getString("x1_sp2"), true, true) +
                                    FixData.prepareUpdate("f_x1_sp3", entity.getString("x1_sp3"), true, true);
                        }
                        else if(taxon_final.equals(taxon_tnrs_final) && !FixData.hideRank(taxon_final).equals(taxon_taxstand_final))
                            throw new Exception("Semaphore yellow. Taxstands different. Taxon: " + taxon_final + " Taxstand: " +  taxon_taxstand_final);
                        else if(!taxon_final.equals(taxon_tnrs_final) && FixData.hideRank(taxon_final).equals(taxon_taxstand_final))
                            throw new Exception("Semaphore yellow. TNRS different. Taxon: " + taxon_final + " TNRS: " +  taxon_tnrs_final);
                        else
                            throw new Exception("Semaphore Red. All differents. Taxon: " + taxon_final + " TNRS: " +  taxon_tnrs_final + " Taxstand: " + taxon_taxstand_final);
                    }
                    //4.6.3
                    else if(p.getTypePolicy()==TypePolicy.POSTCHECK_VALIDATE_TAXON_MANDATORY && (entity.getString("taxon_final") == null || entity.getString("taxon_final").equals("")))
                        throw new Exception("Taxon final miss");
                    //5.3.6 - 5.3.7
                    else if(p.getTypePolicy()==TypePolicy.POSTCHECK_GEOCODING_CROSCHECK_COORDS)
                    {
                        if(entity.getString("coord_source") != null)
                            System.out.println("The register already have cross check. coord_source: " + entity.getString("coord_source"));
                        else
                        {
                            water=rWater.getDataFromLatLon(entity.getDouble("latitude"),entity.getDouble("longitude"));
                            if(water==null)
                                throw new Exception("Coords. Not found point in the water database. " + water);
                            else if(!water.equals(Configuration.getParameter("geocoding_database_world_earth")))
                                throw new Exception("Coords. Point in the water or boundaries. " + water + " Lat: " + FixData.getValue(entity.getDouble("latitude")) + " Lon: " + entity.getDouble("longitude"));
                            //Validation with google
                            googleReverse=RepositoryGoogle.reverse(entity.getDouble("latitude"), entity.getDouble("longitude"));
                            if(googleReverse == null || !googleReverse.get("status").toString().equals("OK") || !FixData.getValue(googleReverse.get("iso")).toLowerCase().equals(FixData.getValue(entity.getString("final_iso2")).toLowerCase()))
                                throw new Exception("Cross check coords error: Country don't match  or not found. Latitude: " + FixData.getValue(entity.getString("latitude")) +
                                        " Longitude: " + FixData.getValue(entity.getString("longitude")) +
                                        (googleReverse == null ? "" : (" Status: " + googleReverse.get("status").toString() + " Iso: " + FixData.getValue(googleReverse.get("iso")))));
                            else
                                query+="coord_source='original',final_lat='" + entity.getString("latitude") + "',final_lon='" + entity.getString("longitude") + "',";
                        }
                    }
                    //5.4.1
                    else if(p.getTypePolicy()==TypePolicy.POSTCHECK_GEOCODING_CROSCHECK_GEOREF)
                    {
                        if(entity.getString("coord_source") != null)
                            System.out.println("The register already have cross check. coord_source: " + entity.getString("coord_source"));
                        else
                        {
                            water=rWater.getDataFromLatLon(entity.getDouble("latitude_georef"),entity.getDouble("longitude_georef"));
                            if(water==null)
                                throw new Exception("Coords. Not found point in the water database. " + water);
                            else if(!water.equals(Configuration.getParameter("geocoding_database_world_earth")))
                                throw new Exception("Georef. Point in the water or boundaries. " + water + " Lat: " + FixData.getValue(entity.getDouble("latitude")) + " Lon: " + entity.getDouble("longitude"));
                            if(entity.getString("latitude_georef") == null || entity.getString("longitude_georef") == null)
                                throw new Exception("Cross check georef error: Data not found. Latitude: " + FixData.getValue(entity.getString("latitude_georef")) +
                                        " Longitude: " + FixData.getValue(entity.getString("longitude_georef")));
                            else
                                query+="coord_source='georef',final_lat='" + entity.getString("latitude_georef") + "',final_lon='" + entity.getString("longitude_georef") + "',";
                        }
                    }
                }
                catch(Exception e)
                {
                    a+=1;
                    lGoogle=null;
                    lGeolocate=null;
                    googleReverse=null;
                    StackTraceElement[] frames = e.getStackTrace();
                    String msgE="";
                    for(StackTraceElement frame : frames)
                        msgE += frame.getClassName() + "-" + frame.getMethodName() + "-" + String.valueOf(frame.getLineNumber());
                    System.out.println(e + msgE);
                    Log.register(log, TypeLog.REGISTER_ERROR, entity.getString("id") + "|" + e.toString() + "|" + msgE, true, PREFIX_CROSSCHECK + String.valueOf(step),Configuration.getParameter("log_ext_review") );
                }
            }
            footer=" Where id=" +entity.getString("id") + ";" ;
            //Validate that have
            if(!query.endsWith("set "))
                Log.register(log, TypeLog.REGISTER_OK, query.substring(0, query.length()-1) + footer, false,PREFIX_CROSSCHECK + String.valueOf(step),Configuration.getParameter("log_ext_sql"));
            
            //Log for review data
            if(step==2 || step == 3)
                Log.register(log, TypeLog.REVIEW_DATA, review_data, false,PREFIX_CROSSCHECK + String.valueOf(step),Configuration.getParameter("log_ext_review"));
            //Percent of progress
            System.out.println(FixData.toPercent(countRows, row) + "% row " + row + " of " + countRows);
        }
        return a;
    }
    
}