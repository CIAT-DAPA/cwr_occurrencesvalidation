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

import Controllers.Tools.TypeReport;
import Controllers.Tools.Policy;
import Controllers.Tools.TypePolicy;
import Models.Complements.Repository.RepositoryOriginStatDistribution;
import Models.Complements.Source.OriginStatDistribution;
import Models.DataBase.BaseUpdate;
import Models.DataBase.DBFile;
import Models.DataBase.Field;
import Models.DataBase.ResultQuery;
import Models.DataBase.UpdateQuery;
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
    private final String PREFIX_UPDATE_FILE = "TO_UPDATE_FILE_";
    private final String PREFIX_REPORT_SUMMARY = "TO_REPORT_SUMMARY_";
    private final String PREFIX_REPORT_COMPARE = "TO_REPORT_COMPARE_";
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
    public CTempOccurrences() 
    {
        super(new RepositoryTempOccurrences());
        FIELDS_MANDATORY=FixData.valueParameterSplit(Configuration.getParameter("validation_fields_mandatory"));
        FIELDS_CONTENT=FixData.valueParameterSplit(Configuration.getParameter("validation_fields_content"),"\\|");        
        GEO_NS=FixData.valueParameterSplit(Configuration.getParameter("validation_content_ns"));
        GEO_EW=FixData.valueParameterSplit(Configuration.getParameter("validation_content_ew"));
        REGEXP_COORDS=Configuration.getParameter("validation_content_regexp_coords");
        CONTENT_CULT=FixData.valueParameterSplit(Configuration.getParameter("validation_content_cult"));
        CONTENT_ORIGIN=FixData.valueParameterSplit(Configuration.getParameter("validation_content_origin"));
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
    public long importFile(String filePath, String fileSplit, boolean clean, String log) throws SQLException, Exception
    {
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
    public long updateFile(String filePath, String fileSplit, String log, boolean isTaxonFile) throws SQLException, Exception
    {
        if(!isTaxonFile)
            return super.updateFile(filePath, fileSplit, new TempOccurrences(), log, PREFIX_UPDATE_FILE, "id");
        else
        {
            DBFile dbFile=new DBFile(fileSplit, filePath);
            long row=0,errors=0;
            String line;
            ArrayList<String> fields;
            ResultQuery result=null;
            //Load Header
            dbFile.open();
            if(dbFile.isOpen())
                fields=dbFile.readLineSplit();
            else
                throw new Exception("Error when it tryed to open file. Path: " + filePath );
            //Validation fields
            ArrayList<Field> finalFields=repository.validateFields(fields); 
            // Add methods of taxa
            finalFields.add(new Field("f_x1_genus","varchar"));
            finalFields.add(new Field("f_x1_sp1","varchar"));
            finalFields.add(new Field("f_x1_rank1","varchar"));
            finalFields.add(new Field("f_x1_sp2","varchar"));
            finalFields.add(new Field("f_x1_rank2","varchar"));
            finalFields.add(new Field("f_x1_sp3","varchar"));
            TempOccurrences entity;
            String[] taxonFinalSplit;
            ArrayList<String>  lineCurrentData;            
            System.out.println("Start process to update");
            while((line=dbFile.readLine()) != null)
            {
                try
                {
                    row+=1;
                    System.out.println("Row: " + row);
                    entity=new TempOccurrences();
                    lineCurrentData=FixData.lineSplit(line,dbFile.getSplit());
                    //Split to taxon
                    taxonFinalSplit=lineCurrentData.get(1).split("_");
                    lineCurrentData.add(FixData.toCapitalLetter(FixData.fixGapsInTaxon(taxonFinalSplit, 0)));//f_x1_genus
                    lineCurrentData.add(FixData.getValue(FixData.fixGapsInTaxon(taxonFinalSplit, 1)).toLowerCase());//f_x1_sp1
                    lineCurrentData.add(FixData.getValue(FixData.fixGapsInTaxon(taxonFinalSplit, 2)).toLowerCase());//f_x1_rank1
                    lineCurrentData.add(FixData.getValue(FixData.fixGapsInTaxon(taxonFinalSplit, 3)).toLowerCase());//f_x1_sp2
                    lineCurrentData.add(FixData.getValue(FixData.fixGapsInTaxon(taxonFinalSplit, 4)).toLowerCase());//f_x1_rank2
                    lineCurrentData.add(FixData.getValue(FixData.fixGapsInTaxon(taxonFinalSplit, 5)).toLowerCase());//f_x1_sp3
                    //Save into database
                    entity.load(finalFields, lineCurrentData);
                    result=repository.update(entity, "id");
                    if(result.getAffected() > 0)
                        Log.register(log,TypeLog.REGISTER_OK,result.getQuery(), true,PREFIX_UPDATE_FILE,Configuration.getParameter("log_ext_review"));
                    else
                        throw new Exception("Rows not affected");
                }
                catch(Exception e)
                {
                    Log.register(log,TypeLog.REGISTER_ERROR,String.valueOf(row) + "|" + e + "|" + line, true,PREFIX_UPDATE_FILE,Configuration.getParameter("log_ext_review"));
                    if(result!=null)
                        Log.register(log,TypeLog.QUERY_ERROR,result.getQuery(), true,PREFIX_UPDATE_FILE,Configuration.getParameter("log_ext_sql"));
                    errors+=1;
                    System.out.println("Error register: " + row + " " + e);
                }
            }
            System.out.println("End process");
            return errors;
        }
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
    public long crossCheck(int step,ArrayList<Policy> policies, String log, boolean reviewdata) throws SQLException
    {
        int a=0;
        String header="Update " + getRepository().getTable() + " set";
        UpdateQuery query;
        String content_field,content_error;
        TNRS[] tnrs;
        Taxonstand taxonstand;
        String grin;
        Location lGoogle,lGeolocate;
        RepositoryTempCountries rTCountries=new RepositoryTempCountries();
        TempCountries country;
        
        HashMap googleReverse;
        String fullAddress;
        String name,taxon_temp_final,taxon_tnrs_final,taxon_taxstand_final, taxon_grin_final;
        String[] taxon_split;
        
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
            query=new UpdateQuery(header);
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
                        query.add("source","H");
                    //Group Check Part 2
                    //3.4
                    else if(p.getTypePolicy()==TypePolicy.FINAL_CULT)
                        query.add("final_cult_stat", ((entity.getString("cult_stat") != null && FixData.containsValue(entity.getString("cult_stat"), FixData.toArrayList(CONTENT_CULT))) ? entity.getString("cult_stat") : FixData.NULL_DATABASE));
                    //Group TNRS
                    //4.4
                    else if(p.getTypePolicy()==TypePolicy.TNRS_QUERY)
                    {
                        name=generateName(entity," ");
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
                            throw new Exception("Taxon not found in TNRS: " + name);
                        for(TNRS t:tnrs)
                        {
                            if(t.finalTaxon!=null && !t.finalTaxon.equals(""))
                            {
                                review_data+=t.toString();
                                query.add("tnrs_author1", t.authorAttributed);
                                query.add("tnrs_final_taxon", FixData.toCapitalLetter(t.finalTaxon.replaceAll(" ", "_")));
                                query.add("tnrs_overall_score", String.valueOf(t.overall));
                                query.add("tnrs_source", t.acceptedNameUrl);
                                query.add("tnrs_x1_family", t.family);
                                break;
                            }
                        }
                    }
                    //Group Taxonstand
                    //4.5
                    else if(p.getTypePolicy()==TypePolicy.TAXONDSTAND_QUERY)
                    {
                        name=generateName(entity," ");
                        taxonstand=RepositoryTaxonstand.get(name);
                        if(taxonstand==null)
                            throw new Exception("Taxon not found in taxonstand: " + name);
                        else
                        {
                            review_data+=taxonstand.toString();
                            query.add("taxstand_author1", taxonstand.author);
                            query.add("taxstand_family", taxonstand.family.replaceAll(" ", "_"));
                            query.add("taxstand_final_taxon", FixData.toCapitalLetter(FixData.concatenate(new String[]{ taxonstand.genus,
                                                                                        taxonstand.species,
                                                                                        taxonstand.species2},"_")));
                            query.add("taxstand_genus", FixData.toCapitalLetter(taxonstand.genus));
                            query.add("taxstand_sp1", taxonstand.species);
                            query.add("taxstand_sp2", taxonstand.species2);
                        }
                    }
                    //Group GRIN
                    else if(p.getTypePolicy()==TypePolicy.GRIN_QUERY)
                    {
                        name=generateName(entity," ").replaceAll(" x ", " ");
                        grin=RepositoryGRIN.get(name,false);
                        if(grin==null || grin.equals(""))
                            throw new Exception("Taxon not found in GRIN: " + name);
                        else
                        {
                            review_data+=grin+"|";
                            query.add("grin_final_taxon", FixData.toCapitalLetter(grin.trim().replaceAll(" ", "_")));
                        }
                    }
                    //Group Geocoding
                    //5.1
                    else if(p.getTypePolicy()==TypePolicy.GEOCODING_VALIDATE_COUNTRY)
                    {
                        country=searchCountry(entity,rTCountries);
                        if(country != null)
                            query.add("final_country", country.getString("name"));
                        else
                            throw new Exception("Country not found for country: iso=" + FixData.getValue(entity.get("iso")) + " country=" + FixData.getValue(entity.get("country")));
                    }
                    //5.2
                    else if(p.getTypePolicy()==TypePolicy.GEOCODING_VALIDATE_ISO2)
                    {
                        country=searchCountry(entity,rTCountries);
                        if(country != null)
                            query.add("final_iso2", country.getString("iso2"));
                        else
                            throw new Exception("ISO 2 not found for country: iso=" + FixData.getValue(entity.get("iso")) + " country=" + FixData.getValue(entity.get("country")));
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
                            query.add("final_ns",  entity.getString("ns"));
                        //ew
                        if(!FixData.containsValue(entity.getString("ew"), FixData.toArrayList(GEO_EW)))
                            throw new Exception("Error in EW: " + (entity.getString("ew")==null ? "null" : entity.getString("ew")));
                        else if(entity.getString("ew")!=null && FixData.containsValue(entity.getString("ew"), FixData.toArrayList(GEO_EW)))
                            query.add("final_ew",  entity.getString("ew"));
                    }
                    //5.3.3
                    else if(p.getTypePolicy()==TypePolicy.GEOCODING_VALIDATE_COORDS_LAT_LON)
                    {
                        if(entity.getString("lat_deg") != null && entity.getString("latitude") == null)
                            query.add("latitude", String.valueOf(FixData.degreesToDecimalDegrees(entity.getString("ns").contains("n") , entity.getDouble("lat_deg"), entity.getDouble("lat_min"), entity.getDouble("lat_sec"))));
                        if(entity.getString("long_deg") != null && entity.getString("longitude") == null)
                            query.add("longitude", String.valueOf(FixData.degreesToDecimalDegrees(entity.getString("ew").contains("e") , entity.getDouble("long_deg"), entity.getDouble("long_min"), entity.getDouble("long_sec"))));
                    }
                    //5.4
                    else if(p.getTypePolicy()==TypePolicy.GEOCODING_INITIAL)
                    {
                        country=searchCountry(entity,rTCountries);
                        temp_country=country!=null ? country.getString("name") : "";                        
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
                        {
                            query.add("latitude_georef",  String.valueOf(lGoogle.getLatitude()));
                            query.add("longitude_georef",  String.valueOf(lGoogle.getLongitude()));
                            query.add("distance_georef",  String.valueOf(lGoogle.getUncertainty()));
                        }
                        else if(lGeolocate != null)
                        {
                            query.add("latitude_georef",  String.valueOf(lGeolocate.getLatitude()));
                            query.add("longitude_georef",  String.valueOf(lGeolocate.getLongitude()));
                            query.add("distance_georef",  String.valueOf(lGeolocate.getUncertainty()));
                        }
                        else
                            throw new Exception("Can't geocode record " + temp_country + "," + temp_adm1 + "," + temp_adm2 + "," + temp_adm3 + "," + temp_local_area + "," + temp_locality);
                    }
                    //Group POST CHECK
                    //4.6
                    else if(p.getTypePolicy()==TypePolicy.POSTCHECK_VALIDATE_TAXON)
                    {
                        taxon_split=null;
                        taxon_temp_final=FixData.removePatternEnd(generateName(entity,"_"),"_");
                        taxon_tnrs_final= entity.getString("tnrs_final_taxon") == null ? "" : FixData.removePatternEnd(entity.getString("tnrs_final_taxon"),"_");
                        taxon_taxstand_final= entity.getString("taxstand_final_taxon") == null ? "" : FixData.removePatternEnd(entity.getString("taxstand_final_taxon"),"_");
                        taxon_grin_final = entity.getString("grin_final_taxon") == null ? "" : FixData.removePatternEnd(entity.getString("grin_final_taxon"),"_");
                        if(!taxon_grin_final.equals(""))
                            taxon_split=generateTaxonFinal(taxon_grin_final);
                        else if(FixData.hideRankSP(taxon_temp_final).equals(taxon_tnrs_final) && FixData.hideRank(taxon_temp_final).equals(taxon_taxstand_final) )
                            taxon_split=generateTaxonFinal(taxon_temp_final);                        
                        else if(FixData.hideRank(taxon_tnrs_final).equals(FixData.hideRankSP(taxon_taxstand_final)) && !FixData.hideRankSP(taxon_temp_final).equals(taxon_tnrs_final))//Before Orange Traffic light                            
                            taxon_split=generateTaxonFinal(taxon_tnrs_final);
                        else if(FixData.hideRankSP(taxon_temp_final).equals(taxon_tnrs_final) && !FixData.hideRank(taxon_temp_final).equals(taxon_taxstand_final))//Before Yellow
                            taxon_split=generateTaxonFinal(taxon_temp_final);
                            //throw new Exception("Traffic light yellow. Taxstand is different. Taxon Temp: " + FixData.hideRank(FixData.hideRankSP(taxon_temp_final)) + " Taxstand: " +  taxon_taxstand_final);
                        else if(!FixData.hideRankSP(taxon_temp_final).equals(taxon_tnrs_final) && FixData.hideRank(taxon_temp_final).equals(taxon_taxstand_final))//Before Yellow
                            taxon_split=generateTaxonFinal(taxon_temp_final);
                            //throw new Exception("Traffic light yellow. TNRS is different. Taxon: " + FixData.hideRankSP(taxon_temp_final) + " TNRS: " +  taxon_tnrs_final);
                        else
                            throw new Exception("Traffic light Red. All differents. Taxon: " + taxon_temp_final + " TNRS: " +  taxon_tnrs_final + " Taxstand: " + taxon_taxstand_final + " Grin: " + taxon_grin_final);
                        if(taxon_split != null)
                        {
                            query.add("taxon_final",  taxon_split[0]);
                            query.add("f_x1_genus", taxon_split[1]);
                            query.add("f_x1_sp1", taxon_split[2]);
                            query.add("f_x1_rank1", taxon_split[3]);
                            query.add("f_x1_sp2", taxon_split[4]);
                            query.add("f_x1_rank2", taxon_split[5]);
                            query.add("f_x1_sp3", taxon_split[6]);
                        }
                        else
                            throw new Exception("Traffic light Red. Not found taxon. taxon split null. Taxon: " + taxon_temp_final + " TNRS: " +  taxon_tnrs_final + " Taxstand: " + taxon_taxstand_final + " Grin: " + taxon_grin_final);
                    }
                    //4.6.3
                    else if(p.getTypePolicy()==TypePolicy.POSTCHECK_VALIDATE_TAXON_MANDATORY && (entity.getString("taxon_final") == null || entity.getString("taxon_final").equals("")))
                        throw new Exception("Taxon final miss");
                    //5.3.6 - 5.3.7 - 5.4.1
                    else if(p.getTypePolicy()==TypePolicy.POSTCHECK_GEOCODING_CROSCHECK_COORDS || p.getTypePolicy()==TypePolicy.POSTCHECK_GEOCODING_CROSCHECK_GEOREF)
                    {
                        if(entity.get("coord_source") != null)
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
                                query.add("comments", (comments.equals("") ? "" : comments + ". ") + "Point in the water");
                                throw new Exception("Point in the water or boundaries. " + water + " Lat: " + String.valueOf(lat) + " Lon: " + String.valueOf(lon));
                            }
                            //Country centrois
                            else if(country != null && country.getDouble("lat")==lat && country.getDouble("lon")==lon)
                            {
                                query.add("comments", (comments.equals("") ? "" : comments + ". ") + "Point in the country centroid");
                                throw new Exception("Point in the country centroid. " + " Lat: " + String.valueOf(lat) + " Lon: " + String.valueOf(lon) + " Country ISO 2: " + entity.getString("final_iso2"));
                            }
                            // It validate that always it has final_country or step 6
                            if(origin || (!origin && entity.get("final_country")==null))
                            {
                                googleReverse=RepositoryGoogle.reverse(lat, lon);
                                if(googleReverse == null || !googleReverse.get("status").toString().equals("OK") || !FixData.getValue(googleReverse.get("iso")).toLowerCase().equals(FixData.getValue(entity.get("final_iso2")).toLowerCase()))                                    
                                    query.add("comments", (comments.equals("") ? "" : comments + ". ") + "Coordinates do not match to country in source data");
                                if(entity.get("final_country")==null && googleReverse != null && googleReverse.get("status").toString().equals("OK"))
                                {
                                    query.add("final_country", googleReverse.get("country").toString());
                                    query.add("final_iso2", googleReverse.get("iso").toString()); 
                                }
                            }
                            query.add("coord_source", origin ? "original": "georef");
                            query.add("final_lat", String.valueOf(lat));
                            query.add("final_lon", String.valueOf(lon));
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
                        if(origin_stat != null)
                        {
                            for(OriginStatDistribution os:origin_stat)
                            {
                                origin_stat_value=os.getType();
                                if(os.getType().toLowerCase().trim().equals(Configuration.getParameter("origin_stat_native")))
                                {
                                    origin_stat_found=true;
                                    break;
                                }
                            }
                        }
                        //validation to value searched
                        if(!origin_stat_value.equals(""))
                            query.add("origin_stat_inv", origin_stat_value);
                        //validation to final field
                        if(entity.getString("origin_stat") != null && FixData.containsValue(entity.getString("origin_stat"), FixData.toArrayList(CONTENT_ORIGIN)))
                            query.add("final_origin_stat", FixData.translate(1, entity.getString("origin_stat")));
                        else if(origin_stat_found)
                            query.add("final_origin_stat", origin_stat_value);
                        else if(!origin_stat_found && !origin_stat_value.equals(""))
                            query.add("final_origin_stat", Configuration.getParameter("origin_stat_non-native"));
                        else
                            throw new Exception("Not found value for field final_origin_stat. Values: Origin=" + FixData.getValue(entity.get("origin_stat")) + " Search=" + FixData.getValue(origin_stat_value) + " Taxon: " + FixData.getValue(entity.get("taxon_final")));
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
            query.setWhere("Where id=" +entity.getString("id"));
            //Validate that have
            if(query.count() > 0)
                Log.register(log, TypeLog.REGISTER_OK, query.toString(), false,PREFIX_CROSSCHECK + String.valueOf(step),Configuration.getParameter("log_ext_sql"));
            
            //Log for review data
            if(reviewdata && !review_data.equals("") && !review_data.equals(entity.getString("id") + "|") && (step==2 || step == 3 || step == 6 || step == 7))
                Log.register(log, TypeLog.REVIEW_DATA, review_data, false,PREFIX_CROSSCHECK + String.valueOf(step),Configuration.getParameter("log_ext_review"));
            //Percent of progress
            System.out.println(FixData.toPercent(countRows, row) + "% row " + row + " of " + countRows);
        }
        return a;
    }
    
    /**
     * Method that generate taxon name from all parts that its built
     * @param entity TempOccurrences from take the information
     * @param union String for add 
     * @return 
     */
    private String generateName(TempOccurrences entity, String union)
    {
        String value1=FixData.validateRank(entity.getString("x1_rank1"));
        String value2=FixData.validateRank(entity.getString("x1_rank2"));
        return FixData.toCapitalLetter(FixData.concatenate(new String[]{entity.getString("x1_genus"),
                            entity.getString("x1_sp1"),
                            value1,
                            entity.getString("x1_sp2"),
                            value2,
                            entity.getString("x1_sp3")},union));
    }
    
    /**
     * Method that generate the taxon final
     * @param taxon
     * @return 
     */
    private String[] generateTaxonFinal(String taxon)
    {
        String[] a = new String[7];
        String rank, taxon_work;
        taxon_work=taxon.replaceAll("_x_", "_x");
        a[0]=taxon_work;//taxon_final
        //4.1
        String[] taxon_split=taxon_work.split("_");
        a[1]=FixData.toCapitalLetter(FixData.fixGapsInTaxon(taxon_split, 0));//f_x1_genus
        a[2]=FixData.getValue(FixData.fixGapsInTaxon(taxon_split, 1)).toLowerCase();//f_x1_sp1
        rank=FixData.validateRank(FixData.fixGapsInTaxon(taxon_split, 2));//f_x1_rank1
        a[3]=FixData.getValue(rank==null || rank.equals("") ? FixData.NULL_DATABASE : rank).toLowerCase();
        a[4]=FixData.getValue(FixData.fixGapsInTaxon(taxon_split, 3)).toLowerCase();//f_x1_sp2
        rank=FixData.validateRank(FixData.fixGapsInTaxon(taxon_split, 4));
        a[5]=FixData.getValue(rank==null || rank.equals("") ? FixData.NULL_DATABASE : rank).toLowerCase();//f_x1_rank2
        a[6]=FixData.getValue(FixData.fixGapsInTaxon(taxon_split, 5)).toLowerCase();//f_x1_sp3
        return a;
    }
    
    /**
     * Method that search a country into database with origin data from occurrence
     * @param entity
     * @return 
     */
    private TempCountries searchCountry(TempOccurrences entity,RepositoryTempCountries rTCountries){  
        TempCountries country=null;
        HashMap googleReverse;
        if(entity.get("iso")!=null)
            country=rTCountries.searchIso3(entity.getString("iso"));
        if(country==null && entity.get("country")!=null)
            country=rTCountries.searchByName(entity.getString("country"));
        if(country==null && entity.get("latitude") != null && entity.get("longitude") != null) 
        {
            googleReverse=RepositoryGoogle.reverse(entity.getDouble("latitude"), entity.getDouble("longitude"));
            if(googleReverse != null && googleReverse.get("status").toString().equals("OK")){
                country=rTCountries.searchByName(googleReverse.get("country").toString());
                if(country == null)
                    country=rTCountries.searchIso2(googleReverse.get("iso").toString());
                if(country == null)
                    country=rTCountries.searchIso3(googleReverse.get("iso").toString());
            }
        }
        return country;       
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
