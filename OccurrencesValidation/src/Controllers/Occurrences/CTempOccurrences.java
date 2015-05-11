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

import Controllers.Tools.Reports.TypeReport;
import Controllers.Tools.Validation.Policy;
import Controllers.Tools.Validation.TypePolicy;
import Models.Complements.Repository.RepositoryOriginStatDistribution;
import Models.Complements.Source.OriginStatDistribution;
import Models.DataBase.BaseUpdate;
import Models.Geographic.Repository.RepositoryWaterBody;
import Models.Geographic.Repository.RepositoryGeolocate;
import Models.Geographic.Repository.RepositoryGoogle;
import Models.Geographic.Source.Location;
import Models.Occurrences.Repository.RepositoryTempCountries;
import Models.Occurrences.Repository.RepositoryTempOccurrences;
import Models.Occurrences.Source.TempCountries;
import Models.Occurrences.Source.TempOccurrences;
import Models.Taxonomy.Repository.RepositoryGRIN;
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
    private final String PREFIX_IMPORT = "TO_IMPORT_";
    private final String PREFIX_CROSSCHECK = "TO_CROSSCHECK_";
    private final String PREFIX_UPDATE = "TO_UPDATE_";
    private final String PREFIX_REPORT_SUMMARY = "TO_REPORT_SUMMARY_";
    private final String PREFIX_REPORT_COMPARE = "TO_REPORT_COMPARE";
    private final String PREFIX_UPDATE_QUERY = "TO_UPDATE_QUERY_";
    /*Members Class*/
    private String[] FIELDS_MANDATORY;
    private String[] FIELDS_CONTENT;
    private String[] CONTENT_CULT;
    private String[] CONTENT_ORIGIN;
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
        FIELDS_CONTENT=FixData.valueParameterSplit(Configuration.getParameter("validation_fields_content"),"\\|");        
        GEO_NS=FixData.valueParameterSplit(Configuration.getParameter("validation_content_ns"));
        GEO_EW=FixData.valueParameterSplit(Configuration.getParameter("validation_content_ew"));
        REGEXP_COORDS=Configuration.getParameter("validation_content_regexp_coords");
        CONTENT_CULT=FixData.valueParameterSplit(Configuration.getParameter("validation_content_cult"));
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
        return super.updateFileQuery(filePath, log, PREFIX_UPDATE_QUERY);
    }
    
    /**
     * Method that update fields of table into database
     * @param updates updates to do
     * @param log directory of log
     */
    public void updateFields(ArrayList<BaseUpdate> updates, String log)
    {
        super.updateFields(updates, log, PREFIX_UPDATE);
    }
    
    /**
     * Method that validate data of the occurrences
     * @param step
     * @param policies
     * @param log
     * @param reviewdata
     * @return
     * @throws SQLException
     */
    public long crossCheck(int step,ArrayList<Policy> policies, String log, boolean reviewdata) throws SQLException{
        int a=0;
        String header="Update " + getRepository().getTable() + " set ",footer, query;
        String content_field,content_error;
        TNRS[] tnrs;
        Taxonstand taxonstand;
        String grin;
        Location lGoogle,lGeolocate;
        RepositoryTempCountries rTCountries=new RepositoryTempCountries();
        TempCountries country;
        
        HashMap googleReverse;
        String fullAddress;
        String value1, value2;
        String name,taxon_temp_final,taxon_tnrs_final,taxon_taxstand_final, taxon_grin_final;
        String[] taxon_grin_split;
        
        RepositoryWaterBody rWater;
        String water;
        String temp_country,temp_adm1,temp_adm2,temp_adm3,temp_local_area,temp_locality;
        String review_data;
        long row=0, countRows;
        TempOccurrences entity=new TempOccurrences();
        
        String comments;
        
        boolean origin;
        double lat,lon;
        
        ArrayList<OriginStatDistribution> origin_stat;
        boolean origin_stat_found;
        String origin_stat_value;
        
        String countryTempGeoref;
        //Headers for log of review data
        if(reviewdata)
            Log.register(log,TypeLog.REVIEW_DATA, step==2? "id|x1_genus|x1_sp1|x1_rank1|x1_sp2|x1_rank2|x1_sp3|" + RepositoryTNRS.HEADER + RepositoryTaxonstand.HEADER + RepositoryGRIN.HEADER :
                    (step==3 || step==6 || step==7 ? "id|" + RepositoryGoogle.HEADER : ""), false,PREFIX_CROSSCHECK + String.valueOf(step),Configuration.getParameter("log_ext_review"));
        rWater=new RepositoryWaterBody(Configuration.getParameter("geocoding_database_world"));
        countRows=repository.count();
        getRepository().listCrossCheck();
        while((entity=(TempOccurrences)getRepository().hasNext(entity)) != null)
        {
            review_data = entity.getString("id") + "|";
            countryTempGeoref="";
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
                    //3.2, 3.3, 3.4, 3.5, 3.6
                    else if(p.getTypePolicy()==TypePolicy.CHECK_CONTENT)
                    {
                        content_error="";
                        for(String list_field:FIELDS_CONTENT)
                        {
                            content_field=list_field.split("@")[0];
                            if(!FixData.containsValue(entity.getString(content_field), FixData.toArrayList(list_field.split("@")[1].split(","))))
                                content_error += content_field + " not correct: " + FixData.getValue(entity.get(content_field)) + ",";
                        }
                        if(!content_error.equals(""))
                            throw new Exception("Errors: " + content_error);
                    }
                    //Group Correct
                    //3.3
                    else if(p.getTypePolicy()==TypePolicy.CORRECT_DEPENDENCE_SOURCE_AVAILABILITY && entity.getString("availability") != null && FixData.containsValue(entity.getString("availability"), FixData.toArrayList(new String[]{"0"})) && FixData.containsValue(entity.getString("source"), FixData.toArrayList(new String[]{"H"})))
                        query+="SOURCE='H',";
                    //Group Check Part 2
                    //3.4
                    else if(p.getTypePolicy()==TypePolicy.FINAL_CULT)
                        query+= "final_cult_stat=" + ((entity.getString("cult_stat") != null && FixData.containsValue(entity.getString("cult_stat"), FixData.toArrayList(CONTENT_CULT))) ? "'" + entity.getString("cult_stat") + "'" : "null" ) + ",";                    
                    //Group TNRS
                    //4.4
                    else if(p.getTypePolicy()==TypePolicy.TNRS_QUERY)
                    {
                        name=generateName(entity);
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
                            throw new Exception("Taxon not found in tnrs");
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
                        name=generateName(entity);
                        taxonstand=RepositoryTaxonstand.get(name);
                        if(taxonstand==null)
                            throw new Exception("Taxon not found in taxonstand");
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
                    //Group GRIN
                    else if(p.getTypePolicy()==TypePolicy.GRIN_QUERY)
                    {
                        name=generateName(entity);
                        grin=RepositoryGRIN.get(name,false);
                        if(grin==null || grin.equals(""))
                            throw new Exception("Taxon not found in GRIN: " + name);
                        else
                        {
                            review_data+=grin+"|";
                            query+="grin_final_taxon='" + FixData.toCapitalLetter(grin.trim().replaceAll(" ", "_"))  + "',";
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
                            {
                                googleReverse=RepositoryGoogle.reverse(entity.getDouble("latitude"), entity.getDouble("longitude"));
                                if(googleReverse != null || googleReverse.get("status").toString().equals("OK"))
                                {
                                    query+="country='" + googleReverse.get("country").toString() + "'," +
                                            "iso='" + googleReverse.get("iso").toString() + "',";
                                    countryTempGeoref=googleReverse.get("country").toString();
                                    country=rTCountries.searchByName(googleReverse.get("country").toString());
                                    if(country != null)
                                        query+="final_country='" +country.getString("name") + "',";
                                    else
                                        throw new Exception("Country not found for country: name=" + googleReverse.get("country").toString() + " original=" +  FixData.getValue(entity.get("country")));
                                }
                                else
                                    throw new Exception("Country not found for country: iso=" + FixData.getValue(entity.get("iso")) + " country=" + FixData.getValue(entity.get("country")));
                            }
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
                            {
                                googleReverse=RepositoryGoogle.reverse(entity.getDouble("latitude"), entity.getDouble("longitude"));
                                if(googleReverse != null || googleReverse.get("status").toString().equals("OK"))
                                {
                                    query+="country='" + googleReverse.get("country").toString() + "'," +
                                            "iso='" + googleReverse.get("iso").toString() + "',";
                                    countryTempGeoref=googleReverse.get("country").toString();
                                    country=rTCountries.searchByName(googleReverse.get("country").toString());
                                    if(country != null)
                                        query+="final_iso2='" +country.getString("iso2") + "',";
                                    else
                                        throw new Exception("ISO 2 not found for country: name=" + googleReverse.get("country").toString() + " original=" +  FixData.getValue(entity.get("country")));
                                }
                                else
                                    throw new Exception("ISO 2 not found for country: iso=" + FixData.getValue(entity.get("iso")) + " country=" + FixData.getValue(entity.get("country")));
                            }
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
                            throw new Exception("Error in NS: " + (entity.getString("ns")==null ? "null" : entity.getString("ns")));
                        else if(entity.getString("ns")!= null && FixData.containsValue(entity.getString("ns"), FixData.toArrayList(GEO_NS)))
                            query+="final_ns='" + entity.getString("ns") + "',";
                        //ew
                        if(!FixData.containsValue(entity.getString("ew"), FixData.toArrayList(GEO_EW)))
                            throw new Exception("Error in EW: " + (entity.getString("ew")==null ? "null" : entity.getString("ew")));
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
                        //Validate the country for georef process
                        if(temp_country.equals("") && !countryTempGeoref.equals(""))
                            temp_country=countryTempGeoref;
                        temp_adm1=FixData.getValueImaginary(entity.getString("adm1"));
                        temp_adm2=FixData.getValueImaginary(entity.getString("adm2"));
                        temp_adm3=FixData.getValueImaginary(entity.getString("adm3"));
                        temp_local_area=FixData.getValueImaginary(entity.getString("local_area"));
                        temp_locality=FixData.getValueImaginary(entity.getString("locality"));
                        lGoogle= RepositoryGoogle.georenferencing(temp_country,temp_adm1,temp_adm2,temp_adm3,temp_local_area,temp_locality);
                        lGeolocate = RepositoryGeolocate.georenferencing(temp_country,temp_adm1,temp_adm2,temp_adm3,temp_local_area,temp_locality);
                        //Review data
                        if(lGoogle == null && lGeolocate == null)
                            review_data="";
                        else{
                            fullAddress=temp_country + "-" + temp_adm1 + "-" + temp_adm2 + "-" + temp_adm3 + "-" + temp_local_area + "-" + temp_locality;
                            review_data+= lGoogle != null ? "0|" + lGoogle.toString() + " Google " + fullAddress +"||" + (lGeolocate==null?"":"\n") :"" ;
                            review_data+= lGeolocate != null ? (lGoogle==null? "" : entity.getString("id") + "|" ) + "2|" + lGeolocate.toString() + " Geolocate " + fullAddress + "||":"";
                        }
                        
                        if(lGoogle != null)
                            query+= "latitude_georef='" + String.valueOf(lGoogle.getLatitude()) + "'," +
                                    "longitude_georef='" + String.valueOf(lGoogle.getLongitude()) + "'," +
                                    "distance_georef='" + String.valueOf(lGoogle.getUncertainty()) + "',";
                        else if(lGeolocate != null)
                            query+= "latitude_georef='" + String.valueOf(lGeolocate.getLatitude()) + "'," +
                                    "longitude_georef='" + String.valueOf(lGeolocate.getLongitude()) + "'," +
                                    "distance_georef='" + String.valueOf(lGeolocate.getUncertainty()) + "',";
                        else
                            throw new Exception("Can't geocode record " + temp_country + "," + temp_adm1 + "," + temp_adm2 + "," + temp_adm3 + "," + temp_local_area + "," + temp_locality);
                    }
                    //Group POST CHECK
                    //4.6
                    else if(p.getTypePolicy()==TypePolicy.POSTCHECK_VALIDATE_TAXON)
                    {
                        taxon_temp_final=FixData.concatenate(new String[]{entity.getString("x1_genus"),
                            entity.getString("x1_sp1"),
                            entity.getString("x1_rank1"),
                            entity.getString("x1_sp2"),
                            entity.getString("x1_rank2"),
                            entity.getString("x1_sp3")},"_").toLowerCase();
                        taxon_tnrs_final= entity.getString("tnrs_final_taxon") == null ? "" : FixData.removePatternEnd(entity.getString("tnrs_final_taxon").toLowerCase(),"_");
                        taxon_taxstand_final= entity.getString("taxstand_final_taxon") == null ? "" : FixData.removePatternEnd(entity.getString("taxstand_final_taxon").toLowerCase(),"_");
                        taxon_grin_final = entity.getString("grin_final_taxon") == null ? "" : FixData.removePatternEnd(entity.getString("grin_final_taxon").toLowerCase(),"_");
                        if(!taxon_grin_final.equals(""))
                        {
                            query+= "taxon_final='" + FixData.toCapitalLetter(taxon_grin_final.replaceAll("_x_", "_x")) + "',";
                            taxon_grin_split=taxon_grin_final.split("_");
                            query += FixData.prepareUpdate("f_x1_genus", FixData.fixGapsInTaxon(taxon_grin_split, 0, true),true, false) +
                                    FixData.prepareUpdate("f_x1_sp1", FixData.fixGapsInTaxon(taxon_grin_split, 1, false), true, true)+
                                    FixData.prepareUpdate("f_x1_rank1", FixData.fixGapsInTaxon(taxon_grin_split, 2, false), true, true)+
                                    FixData.prepareUpdate("f_x1_sp2", FixData.fixGapsInTaxon(taxon_grin_split, 3, false), true, true)+
                                    FixData.prepareUpdate("f_x1_rank2", FixData.fixGapsInTaxon(taxon_grin_split, 4, false), true, true) +
                                    FixData.prepareUpdate("f_x1_sp3", FixData.fixGapsInTaxon(taxon_grin_split, 5, false), true, true);
                        }
                        else if(taxon_temp_final.equals(taxon_tnrs_final) && FixData.hideRank(taxon_temp_final).equals(taxon_taxstand_final) )
                        {
                            query+= "taxon_final='" + FixData.toCapitalLetter(taxon_temp_final) + "',";
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
                        else if(taxon_temp_final.equals(taxon_tnrs_final) && !FixData.hideRank(taxon_temp_final).equals(taxon_taxstand_final))
                            throw new Exception("Traffic light yellow. Taxstand is different. Taxon Temp: " + FixData.hideRank(taxon_temp_final) + " Taxstand: " +  taxon_taxstand_final);
                        else if(!taxon_temp_final.equals(taxon_tnrs_final) && FixData.hideRank(taxon_temp_final).equals(taxon_taxstand_final))
                            throw new Exception("Traffic light yellow. TNRS is different. Taxon: " + taxon_temp_final + " TNRS: " +  taxon_tnrs_final);
                        else if(FixData.hideRank(taxon_tnrs_final).equals(taxon_taxstand_final) && !taxon_temp_final.equals(taxon_tnrs_final))
                            throw new Exception("Traffic light Orange. Taxonstand and TNRS are equals, but taxon_final is different. Taxondstand: " + taxon_taxstand_final + " TNRS: " +  taxon_tnrs_final + " Taxon Temp:" + taxon_temp_final);
                        else
                            throw new Exception("Traffic light Red. All differents. Taxon: " + taxon_temp_final + " TNRS: " +  taxon_tnrs_final + " Taxstand: " + taxon_taxstand_final + " Grin: " + taxon_grin_final);
                    }
                    //4.6.3
                    else if(p.getTypePolicy()==TypePolicy.POSTCHECK_VALIDATE_TAXON_MANDATORY && (entity.getString("taxon_final") == null || entity.getString("taxon_final").equals("")))
                        throw new Exception("Taxon final miss");
                    //5.3.6 - 5.3.7 - 5.4.1
                    else if(p.getTypePolicy()==TypePolicy.POSTCHECK_GEOCODING_CROSCHECK_COORDS || p.getTypePolicy()==TypePolicy.POSTCHECK_GEOCODING_CROSCHECK_GEOREF)
                    {
                        if(entity.getString("coord_source") != null)
                            System.out.println("The record has already been crosschecked. coord_source: " + entity.getString("coord_source"));
                        else
                        {
                            origin=p.getTypePolicy()==TypePolicy.POSTCHECK_GEOCODING_CROSCHECK_COORDS;                            
                            comments = FixData.getValueImaginary(entity.get("comments"));
                            if(origin)
                            {
                                lat=entity.getDouble("latitude");
                                lon=entity.getDouble("longitude");
                            }
                            else
                            {
                                lat=entity.getDouble("latitude_georef");
                                lon=entity.getDouble("longitude_georef");
                            }
                            water=rWater.getDataFromLatLon(lat,lon);
                            review_data+= water != null ? water+"|" + String.valueOf(lat) + "|" + String.valueOf(lon) + "|Id: " + entity.getString("id") + "<br />Where: " + (origin? "origin" : "georef") + "<br />Value Water: " + water + "|" + water   :
                                                           "4|" + String.valueOf(lat) + "|" + String.valueOf(lon) + "|Id: " + entity.getString("id") + "<br />Not found in water database|-1";
                            country=rTCountries.searchIso2(entity.getString("final_iso2"));
                            if(water==null)
                                throw new Exception("Point not found in the water database. " + water);
                            //It is not earth
                            else if(!water.equals(Configuration.getParameter("geocoding_database_world_earth")))
                            {
                                query+="comments='" +  (comments.equals("") ? "" : comments + ". ") + "Point in the water',";
                                throw new Exception("Point in the water or boundaries. " + water + " Lat: " + String.valueOf(lat) + " Lon: " + String.valueOf(lon));
                            }
                            //Country centrois
                            else if(country != null && country.getDouble("lat")==lat && country.getDouble("lon")==lon)
                            {
                                query+="comments='" +  (comments.equals("") ? "" : comments + ". ") + "Point in the country centroid',";
                                throw new Exception("Point in the country centroid. " + " Lat: " + String.valueOf(lat) + " Lon: " + String.valueOf(lon) + " Country ISO 2: " + entity.getString("final_iso2"));
                            }
                            else if(origin)
                            {
                                googleReverse=RepositoryGoogle.reverse(lat, lon);
                                if(googleReverse == null || !googleReverse.get("status").toString().equals("OK") || !FixData.getValue(googleReverse.get("iso")).toLowerCase().equals(FixData.getValue(entity.get("final_iso2")).toLowerCase()))
                                    query+="coord_source='" + (origin ? "original" : "georef") + "',final_lat='" + String.valueOf(lat) + "',final_lon='" + String.valueOf(lon) + "'," +
                                        "comments='" +  (comments.equals("") ? "" : comments + ". ") + "Coordinates do not match to country in source data',";
                                else
                                    query+="coord_source='original',final_lat='" + String.valueOf(lat) + "',final_lon='" + String.valueOf(lon) + "',";
                            }
                            else if(!origin)
                                query+="coord_source='georef',final_lat='" + String.valueOf(lat) + "',final_lon='" + String.valueOf(lon) + "',";
                            else
                                throw new Exception("Erro. " + water);
                        }
                    }
                    else if(p.getTypePolicy()==TypePolicy.POSTCHECK_ORIGIN_STAT)
                    {
                        if(FixData.getValueImaginary(entity.get("final_iso2")).equals(""))
                            throw new Exception("The occurrence don't has final_iso2");
                        else if(FixData.getValueImaginary(entity.get("taxon_final")).equals(""))
                            throw new Exception("The occurrence don't has taxon_final");
                        origin_stat=RepositoryOriginStatDistribution.get(entity.getString("taxon_final"), entity.getString("final_iso2"));
                        origin_stat_value="";
                        origin_stat_found=false;
                        //Search for origin_stat_inv
                        for(OriginStatDistribution os:origin_stat)
                        {
                            origin_stat_value=os.getType();
                            if(os.getType().toLowerCase().trim().equals(Configuration.getParameter("origin_stat_native")))
                            {
                                //query+= "final_origin_stat='" + FixData.translate(1, entity.getString("origin_stat"))  + "',";
                                origin_stat_found=true;
                                break;
                            }
                        }
                        //validation to value searched
                        if(!origin_stat_value.equals(""))
                            query+= "origin_stat_inv='" + origin_stat_value  + "',";
                        //validation to final field
                        if(entity.getString("origin_stat") != null && FixData.containsValue(entity.getString("origin_stat"), FixData.toArrayList(CONTENT_ORIGIN)))
                            query+= "final_origin_stat='" + FixData.translate(1, entity.getString("origin_stat"))  + "',";
                        else if(origin_stat_found)
                            query+= "final_origin_stat='" + origin_stat_value  + "',";
                        else if(!origin_stat_found && !origin_stat_value.equals(""))
                            query+= "final_origin_stat='" + Configuration.getParameter("origin_stat_non-native")  + "',";
                        else
                            throw new Exception("Not found value for field final_origin_stat. Values: Origin=" + FixData.getValue(entity.get("origin_stat")) + " Search=" + origin_stat_value);
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
            if(reviewdata && !review_data.equals("") && !review_data.equals(entity.getString("id") + "|") && (step==2 || step == 3 || step == 6 || step == 7))
                Log.register(log, TypeLog.REVIEW_DATA, review_data, false,PREFIX_CROSSCHECK + String.valueOf(step),Configuration.getParameter("log_ext_review"));
            //Percent of progress
            System.out.println(FixData.toPercent(countRows, row) + "% row " + row + " of " + countRows);
        }
        return a;
    }
    
    private String generateName(TempOccurrences entity)
    {
        String value1=FixData.validateRank(entity.getString("x1_rank1"));
        String value2=FixData.validateRank(entity.getString("x1_rank2"));
        return FixData.concatenate(new String[]{entity.getString("x1_genus"),
                            entity.getString("x1_sp1"),
                            value1,
                            entity.getString("x1_sp2"),
                            value2,
                            entity.getString("x1_sp3")}," ");
    }
    
    /**
     * Method that generate a summary of values into table temp
     * @param log path log
     */
    public void reportSummary(String log)
    {
        try
        {
            HashMap report=getRepository().summary(), temp;
            
            Log.register(log, TypeLog.REGISTER_OK,"FIELD|VALUE|COUNT", false,PREFIX_REPORT_SUMMARY,Configuration.getParameter("log_ext_review"));
            for(Object k1:report.keySet().toArray())
            {
                temp=(HashMap)report.get(k1.toString());
                for(Object k2:temp.keySet().toArray())
                    Log.register(log, TypeLog.REGISTER_OK,k1.toString()+"|" + k2.toString() + "|" + temp.get(k2.toString()), false,PREFIX_REPORT_SUMMARY,Configuration.getParameter("log_ext_review"));
            }
        }
        catch(Exception ex)
        {
            Log.register(log, TypeLog.REGISTER_ERROR, ex.toString() , true, PREFIX_REPORT_SUMMARY ,Configuration.getParameter("log_ext_review") );
        }
    }
    
    /**
     * Method that generate a summary of values into table temp
     * @param log path log
     * @param table table to compare
     * @param fields list fields 
     * @param condition condition to filter
     */
    public void reportCompare(String log, String table, String fields, String condition)
    {
        try
        {
            HashMap report=getRepository().compare(table, fields, condition), temp;
            String line;
            Log.register(log, TypeLog.REGISTER_OK,"FIELD|VALUE|TEMP_OCCURRENCES|" + table.toUpperCase() + "|DIFF", false,PREFIX_REPORT_COMPARE,Configuration.getParameter("log_ext_review"));
            for(Object k1:report.keySet().toArray())
            {
                temp=(HashMap)report.get(k1.toString());
                if(temp != null && temp.containsKey("field") && temp.containsKey("value")&& temp.containsKey("temp_occurrences")&& temp.containsKey("compare")&& temp.containsKey("difference"))
                {
                    line=temp.get("field").toString() + "|" +temp.get("value").toString() + "|" + temp.get("temp_occurrences").toString() + "|"+ temp.get("compare").toString() + "|"+ temp.get("difference").toString();
                    Log.register(log, TypeLog.REGISTER_OK, line, false,PREFIX_REPORT_COMPARE,Configuration.getParameter("log_ext_review"));
                }
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            Log.register(log, TypeLog.REGISTER_ERROR, ex.toString() , true, PREFIX_REPORT_COMPARE ,Configuration.getParameter("log_ext_review") );
        }
    }
    
    /**
     * Method that generate a summary of values into table temp
     * @param log path log
     * @param table table to compare
     * @param condition condition to filter
     */
    public void reportCompareSimple(String log, String table,  String condition, TypeReport type)
    {
        try
        {
            HashMap report=getRepository().compareSimple(table, condition, type == TypeReport.COMPARE_GEOGRAPHIC ? Configuration.getParameter("compare_geographic_fields") : (type == TypeReport.COMPARE_TAXA ? Configuration.getParameter("compare_taxa_fields") : "")), temp;
            String line;
            ArrayList<String> fields=FixData.lineSplit(report.get("COLUMNS").toString(), "\\|");
            Log.register(log,TypeLog.REGISTER_OK, report.get("COLUMNS").toString(), false,PREFIX_REPORT_COMPARE + type.toString(),Configuration.getParameter("log_ext_review"));
            Object[] keys=report.keySet().toArray();
            double max=keys.length, row=1;
            for(Object k1:keys)
            {
                System.out.println(((row/max)*100) + " %");
                row+=1;
                if(!k1.toString().equals("COLUMNS"))
                {
                    temp=(HashMap)report.get(k1.toString());                
                    if(temp != null )
                    {
                        line = "";
                        for(int i=0;i<fields.size();i++)
                            line+=temp.get(fields.get(i)) + "|";
                        Log.register(log, TypeLog.REGISTER_OK, line, false,PREFIX_REPORT_COMPARE + type.toString(),Configuration.getParameter("log_ext_review"));
                    }                    
                }
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            Log.register(log, TypeLog.REGISTER_ERROR, ex.toString() , true, PREFIX_REPORT_COMPARE ,Configuration.getParameter("log_ext_review") );
        }
    }
}
