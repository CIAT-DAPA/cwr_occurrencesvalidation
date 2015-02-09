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

package Controllers.Validation;

import Models.DataBase.TNRS.RepositoryTNRS;
import Controllers.Validation.Policies.*;
import Models.DataBase.Geocoding.Country;
import Models.DataBase.Geocoding.GeocodingGeolocate;
import Models.DataBase.Geocoding.GeocodingGoogle;
import Models.DataBase.Geocoding.WaterBody;
import Models.DataBase.Geocoding.RepositoryCountry;
import Models.DataBase.MySQL;
import Models.DataBase.TNRS.TNRS;
import Models.DataBase.Taxonstand.Taxonstand;
import Tools.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class ValidationTempOccurrence extends ValidationBase {
    
    private String[] FIELDS_MANDATORY;
    private String[] CONTENT_AVAILABILITY;
    private String[] CONTENT_SOURCE;
    private String[] CONTENT_CULT;
    private String[] CONTENT_ORIGIN;
    private String[] CONTENT_HYBRID;
    private String[] GEO_NS;
    private String[] GEO_EW;
    private String REGEXP_COORDS;
    
    /*Methods*/
    public ValidationTempOccurrence(ArrayList<Policy> policies, String log)
    {
        super(new MySQL(Configuration.getParameter("currie_server"),Configuration.getParameter("currie_schema_gapanalysis"),Configuration.getParameter("currie_user"), Configuration.getParameter("currie_password")), policies, log);
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
     * Method that return count register in the table
     * @return
     */
    public long countRegister() {
        long a=0;
        try
        {
            this.db.getResults("Select count(id) From temp_occurrences");
            while(this.db.getRecordSet().next())
                a=this.db.getRecordSet().getLong(1);
        }
        catch (SQLException ex)
        {
            System.out.println("Error in count register " + ex);
        }
        return a;
    }
    
    
    @Override
    public long review(int step)
    {
        int a=0;
        HashMap googleGeocoding,googleReverse,taxonstand,geolocateGeocoding;
        String header="Update temp_occurrences set ",footer, query;
        String value1, value2;
        String name,taxon_final,taxon_tnrs_final,taxon_taxstand_final;
        TNRS[] tnrs;
        RepositoryCountry rCountry;
        Country country;
        WaterBody rWater;
        String water;
        String temp_country,temp_adm1,temp_adm2,temp_adm3,temp_local_area,temp_locality;
        String review_data;
        try
        {
            if(step==2)
                Log.register(super.log, TypeLog.REVIEW_DATA, "id|x1_genus|x1_sp1|x1_rank1|x1_sp2|x1_rank2|x1_sp3|" + TNRS.HEADER + Taxonstand.HEADER, false,"TempOccuReview" + String.valueOf(step),Configuration.getParameter("log_ext_review"));
            else if(step==3)
                Log.register(super.log, TypeLog.REVIEW_DATA, "id|country|adm1|adm2|adm3|local_area|locality|" + GeocodingGoogle.HEADER + GeocodingGeolocate.HEADER, false,"TempOccuReview" + String.valueOf(step),Configuration.getParameter("log_ext_review"));
            rWater=new WaterBody(Configuration.getParameter("geocoding_database_world"));
            long row=0, countRows=countRegister();
            this.db.getResults("Select id,old_id,availability,cult_stat,source,origin_stat,is_hybrid,filename, " +
                    "country,adm1,adm2,adm3,local_area,locality, " +
                    "iso, " +
                    "x1_rank1,x1_rank2, " +
                    "filename,username,provider_institute_id,field_collected_data,data_public_access,citation,source, " +
                    "x1_genus,x1_sp1, x1_sp2, x1_sp3, " +
                    "tnrs_final_taxon,taxstand_final_taxon, " +
                    "lat_deg,lat_min,lat_sec, " +
                    "long_deg,long_min,long_sec, " +
                    "ns,ew, " +
                    "latitude,longitude, " +
                    "taxon_final, " +
                    "final_iso2, " +
                    "coord_source, " +
                    "latitude_georef,longitude_georef " +
                    "From temp_occurrences ");
            rCountry=new RepositoryCountry();
            while(this.db.getRecordSet().next())
            {
                review_data = this.db.getRecordSet().getString("id") + "|";
                query=header;
                row+=1;
                try
                {
                    for(Policy p:super.policies)
                    {
                        try
                        {
                            //Group Check Part 1
                            //3.1
                            if(p.getTypePolicy()==TypePolicy.CHECK_FIELDS_MANDATORY)
                            {
                                for(String f : FIELDS_MANDATORY)
                                {
                                    if(this.db.getRecordSet().getString(f)== null)
                                        throw new Exception(f + " is mandatory");
                                }
                            }
                            //3.2
                            else if(p.getTypePolicy()==TypePolicy.CHECK_AVAILABILITY && !FixData.containsValue(this.db.getRecordSet().getString("availability"), FixData.toArrayList(CONTENT_AVAILABILITY)))
                                throw new Exception("Availability not correct: " + this.db.getRecordSet().getString("availability"));
                            //3.3
                            else if(p.getTypePolicy()==TypePolicy.CHECK_SOURCE && !FixData.containsValue(this.db.getRecordSet().getString("source"), FixData.toArrayList(CONTENT_SOURCE)))
                                throw new Exception("Source not correct: " + this.db.getRecordSet().getString("source"));
                            //3.4
                            else if(p.getTypePolicy()==TypePolicy.CHECK_CULT && !FixData.containsValue(this.db.getRecordSet().getString("cult_stat"), FixData.toArrayList(CONTENT_CULT)))
                                throw new Exception("Cult not correct: " + this.db.getRecordSet().getString("cult_stat"));
                            //3.5
                            else if(p.getTypePolicy()==TypePolicy.CHECK_ORIGIN_STAT && !FixData.containsValue(this.db.getRecordSet().getString("origin_stat"), FixData.toArrayList(CONTENT_ORIGIN)))
                                throw new Exception("Origin Stat not correct: " + this.db.getRecordSet().getString("origin_stat"));
                            //3.6
                            else if(p.getTypePolicy()==TypePolicy.CHECK_IS_HYBRID && !FixData.containsValue(this.db.getRecordSet().getString("is_hybrid"), FixData.toArrayList(CONTENT_HYBRID)))
                                throw new Exception("Is hybrid not correct: " + this.db.getRecordSet().getString("is_hybrid"));
                            //Group Correct
                            //3.3
                            else if(p.getTypePolicy()==TypePolicy.CORRECT_DEPENDENCE_SOURCE_AVAILABILITY && this.db.getRecordSet().getString("availability") != null && FixData.containsValue(this.db.getRecordSet().getString("availability"), FixData.toArrayList(new String[]{"0"})) && FixData.containsValue(this.db.getRecordSet().getString("source"), FixData.toArrayList(new String[]{"H"})))
                                query+="SOURCE='H',";
                            //Group Check Part 2
                            //3.4
                            else if(p.getTypePolicy()==TypePolicy.FINAL_CULT)
                                query+= "final_cult_stat=" + ((this.db.getRecordSet().getString("cult_stat") != null && FixData.containsValue(this.db.getRecordSet().getString("cult_stat"), FixData.toArrayList(CONTENT_CULT))) ? "'" + this.db.getRecordSet().getString("cult_stat") + "'" : "null" ) + ",";
                            //3.5
                            else if(p.getTypePolicy()==TypePolicy.FINAL_ORIGIN)
                            {
                                query+= "final_origin_stat=" + ((this.db.getRecordSet().getString("origin_stat") != null && FixData.containsValue(this.db.getRecordSet().getString("origin_stat"), FixData.toArrayList(CONTENT_ORIGIN))) ? "'" + FixData.translate(1, this.db.getRecordSet().getString("origin_stat"))  + "'" : "null" ) + ",";
                            }
                            //Group Add
                            else if(p.getTypePolicy()==TypePolicy.ADD_AVAILABILITY && p.getValues().get(0) != null)
                                query+="AVAILABILITY='" + p.getValues().get(0) + "',";
                            else if(p.getTypePolicy()==TypePolicy.ADD_CULT  && p.getValues().get(0) != null)
                                query+="cult_stat='" + p.getValues().get(0) + "',";
                            else if(p.getTypePolicy()==TypePolicy.ADD_SOURCE  && p.getValues().get(0) != null)
                                query+="source='" + p.getValues().get(0) + "',";
                            //Group TNRS                            
                            //4.4
                            else if(p.getTypePolicy()==TypePolicy.TNRS_QUERY)
                            {
                                name=FixData.concatenate(new String[]{this.db.getRecordSet().getString("x1_genus"),
                                    this.db.getRecordSet().getString("x1_sp1"),
                                    this.db.getRecordSet().getString("x1_rank1"),
                                    this.db.getRecordSet().getString("x1_sp2"),
                                    this.db.getRecordSet().getString("x1_rank2"),
                                    this.db.getRecordSet().getString("x1_sp3")}," ");
                                review_data += FixData.getValue(this.db.getRecordSet().getString("x1_genus")) + "|" +
                                        FixData.getValue(this.db.getRecordSet().getString("x1_sp1")) + "|" +
                                        FixData.getValue(this.db.getRecordSet().getString("x1_rank1")) + "|" +
                                        FixData.getValue(this.db.getRecordSet().getString("x1_sp2")) + "|" +
                                        FixData.getValue(this.db.getRecordSet().getString("x1_rank2")) + "|" +
                                        FixData.getValue(this.db.getRecordSet().getString("x1_sp3")) + "|" ;
                                tnrs=RepositoryTNRS.get(name, true);
                                if(tnrs==null)
                                    tnrs=RepositoryTNRS.get(name, false);
                                if(tnrs==null)
                                    throw new Exception("Not found taxon in tnrs");
                                for(TNRS t:tnrs)
                                {                                    
                                    if(t.acceptedName!=null && !t.acceptedName.equals(""))
                                    { 
                                        review_data+=t.toString();
                                        query+="tnrs_author1='" + t.authorAttributed + "',"+
                                                "tnrs_final_taxon='" + t.acceptedName.replaceAll(" ", "_") + "'," +
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
                                name=FixData.concatenate(new String[]{this.db.getRecordSet().getString("x1_genus"),
                                    this.db.getRecordSet().getString("x1_sp1"),
                                    this.db.getRecordSet().getString("x1_rank1"),
                                    this.db.getRecordSet().getString("x1_sp2"),
                                    this.db.getRecordSet().getString("x1_rank2"),
                                    this.db.getRecordSet().getString("x1_sp3")}," ");
                                taxonstand=Taxonstand.get(name);
                                if(taxonstand==null)
                                    throw new Exception("Not found taxon in taxonstand");
                                else
                                {
                                    review_data+=taxonstand.get("toString").toString();
                                    query+="taxstand_author1='" + taxonstand.get("taxstand_author1").toString() + "',"+
                                            "taxstand_family='" + taxonstand.get("taxstand_family").toString().replaceAll(" ", "_") + "'," +
                                            "taxstand_final_taxon='" + FixData.concatenate(new String[]{ taxonstand.get("taxstand_genus").toString(),
                                                                        taxonstand.get("taxstand_sp1").toString(),
                                                                        taxonstand.get("taxstand_sp2").toString()}," ").replaceAll(" ", "_")  + "'," +
                                            "taxstand_genus='" + taxonstand.get("taxstand_genus").toString() + "'," +
                                            "taxstand_sp1='" + taxonstand.get("taxstand_sp1").toString()  + "'," +
                                            "taxstand_sp2='" + taxonstand.get("taxstand_sp2").toString()  + "',";
                                }
                            }
                            //Group Geocoding
                            //5.1
                            else if(p.getTypePolicy()==TypePolicy.GEOCODING_VALIDATE_COUNTRY)
                            {
                                country=rCountry.searchIso3(this.db.getRecordSet().getString("iso"));
                                if(country == null)
                                {
                                    country=rCountry.searchByName(this.db.getRecordSet().getString("country"));
                                    if(country == null)
                                        throw new Exception("Country not found for country: iso=" + (this.db.getRecordSet().getString("iso") == null ? "null" : this.db.getRecordSet().getString("iso")) + " country=" + (this.db.getRecordSet().getString("country") == null ? "null" : this.db.getRecordSet().getString("country")));
                                    else
                                        query+="final_country='" +country.getName() + "',";
                                }
                                else
                                    query+="final_country='" +country.getName() + "',";
                            }
                            //5.2
                            else if(p.getTypePolicy()==TypePolicy.GEOCODING_VALIDATE_ISO2)
                            {
                                country=rCountry.searchIso3(this.db.getRecordSet().getString("iso"));
                                if(country == null)
                                {
                                    country=rCountry.searchByName(this.db.getRecordSet().getString("country"));
                                    if(country == null)
                                        throw new Exception("Country not found for final_iso: iso=" + (this.db.getRecordSet().getString("iso") == null ? "null" : this.db.getRecordSet().getString("iso")) + " country=" + (this.db.getRecordSet().getString("country") == null ? "null" : this.db.getRecordSet().getString("country")));
                                    else
                                        query+="final_iso2='" +country.getIso2() + "',";
                                }
                                else
                                    query+="final_iso2='" + country.getIso2() + "',";
                            }
                            //5.3.1  5.3.4
                            else if(p.getTypePolicy()==TypePolicy.GEOCODING_VALIDATE_COORDS_GRADS)
                            {
                                if(this.db.getRecordSet().getString("lat_deg") != null && !this.db.getRecordSet().getString("lat_deg").matches(REGEXP_COORDS))
                                    throw new Exception("Error in lat_deg: " + this.db.getRecordSet().getString("lat_deg"));
                                else if(this.db.getRecordSet().getString("lat_min") != null && !this.db.getRecordSet().getString("lat_min").matches(REGEXP_COORDS))
                                    throw new Exception("Error in lat_min: " + this.db.getRecordSet().getString("lat_min"));
                                else if(this.db.getRecordSet().getString("lat_sec") != null && !this.db.getRecordSet().getString("lat_sec").matches(REGEXP_COORDS))
                                    throw new Exception("Error in lat_sec: " + this.db.getRecordSet().getString("lat_sec"));
                                else if(this.db.getRecordSet().getString("long_deg") != null && !this.db.getRecordSet().getString("long_deg").matches(REGEXP_COORDS))
                                    throw new Exception("Error in long_deg: " + this.db.getRecordSet().getString("long_deg"));
                                else if(this.db.getRecordSet().getString("long_min") != null && !this.db.getRecordSet().getString("long_min").matches(REGEXP_COORDS))
                                    throw new Exception("Error in long_min: " + this.db.getRecordSet().getString("long_min"));
                                else if(this.db.getRecordSet().getString("long_sec") != null && !this.db.getRecordSet().getString("long_sec").matches(REGEXP_COORDS))
                                    throw new Exception("Error in long_sec: " + this.db.getRecordSet().getString("long_sec"));
                                else if(this.db.getRecordSet().getString("latitude") != null && !this.db.getRecordSet().getString("latitude").matches(REGEXP_COORDS))
                                    throw new Exception("Error in latitude: " + this.db.getRecordSet().getString("latitude"));
                                else if(this.db.getRecordSet().getString("longitude") != null && !this.db.getRecordSet().getString("longitude").matches(REGEXP_COORDS))
                                    throw new Exception("Error in longitude: " + this.db.getRecordSet().getString("longitude"));
                            }
                            //5.3.2
                            else if(p.getTypePolicy()==TypePolicy.GEOCODING_VALIDATE_NS_EW)
                            {
                                //ns
                                if(!FixData.containsValue(this.db.getRecordSet().getString("ns"), FixData.toArrayList(GEO_NS)))
                                    throw new Exception("Error in NS with data: " + (this.db.getRecordSet().getString("ns")==null ? "null" : this.db.getRecordSet().getString("ns")));
                                else if(this.db.getRecordSet().getString("ns")!= null && FixData.containsValue(this.db.getRecordSet().getString("ns"), FixData.toArrayList(GEO_NS)))
                                    query+="final_ns='" + this.db.getRecordSet().getString("ns") + "',";
                                //ew
                                if(!FixData.containsValue(this.db.getRecordSet().getString("ew"), FixData.toArrayList(GEO_EW)))
                                    throw new Exception("Error in EW with data: " + (this.db.getRecordSet().getString("ew")==null ? "null" : this.db.getRecordSet().getString("ew")));
                                else if(this.db.getRecordSet().getString("ew")!=null && FixData.containsValue(this.db.getRecordSet().getString("ew"), FixData.toArrayList(GEO_EW)))
                                    query+="final_ew='" + this.db.getRecordSet().getString("ew") + "',";
                            }
                            //5.3.3
                            else if(p.getTypePolicy()==TypePolicy.GEOCODING_VALIDATE_COORDS_LAT_LON)
                            {
                                if(this.db.getRecordSet().getString("lat_deg") != null && this.db.getRecordSet().getString("latitude") == null)
                                    query+= "latitude='" + FixData.degreesToDecimalDegrees(this.db.getRecordSet().getString("ns").contains("n") , this.db.getRecordSet().getDouble("lat_deg"), this.db.getRecordSet().getDouble("lat_min"), this.db.getRecordSet().getDouble("lat_sec"))  + "',";
                                if(this.db.getRecordSet().getString("long_deg") != null && this.db.getRecordSet().getString("longitude") == null)
                                    query+= "longitude='" + FixData.degreesToDecimalDegrees(this.db.getRecordSet().getString("ew").contains("e") , this.db.getRecordSet().getDouble("long_deg"), this.db.getRecordSet().getDouble("long_min"), this.db.getRecordSet().getDouble("long_sec"))  + "',";
                            }
                            //5.4
                            else if(p.getTypePolicy()==TypePolicy.GEOCODING_INITIAL)
                            {
                                temp_country=FixData.getValueImaginary(this.db.getRecordSet().getString("country"));
                                temp_adm1=FixData.getValueImaginary(this.db.getRecordSet().getString("adm1"));
                                temp_adm2=FixData.getValueImaginary(this.db.getRecordSet().getString("adm2"));
                                temp_adm3=FixData.getValueImaginary(this.db.getRecordSet().getString("adm3"));
                                temp_local_area=FixData.getValueImaginary(this.db.getRecordSet().getString("local_area"));
                                temp_locality=FixData.getValueImaginary(this.db.getRecordSet().getString("locality"));
                                googleGeocoding=GeocodingGoogle.georenferencing(temp_country,temp_adm1,temp_adm2,temp_adm3,temp_local_area,temp_locality);
                                geolocateGeocoding = GeocodingGeolocate.georenferencing(temp_country,temp_adm1,temp_adm2,temp_adm3,temp_local_area,temp_locality);
                                //Review data
                                review_data+= temp_country + "|" + temp_adm1 + "|" + temp_adm2 + "|" + temp_adm3 + "|" + temp_local_area + "|" + temp_locality + "|";                                
                                review_data+= googleGeocoding != null ? googleGeocoding.get("latitude").toString()+ "|" + googleGeocoding.get("longitude").toString() + "|" + googleGeocoding.get("distance").toString() + "|":"|||";
                                review_data+= geolocateGeocoding != null ? geolocateGeocoding.get("latitude").toString() + "|" + geolocateGeocoding.get("longitude").toString() + "|" + geolocateGeocoding.get("precision").toString() + "|" + geolocateGeocoding.get("uncertainty").toString() + "|":"||||";
                                
                                if(googleGeocoding != null)
                                    query+= "latitude_georef='" + googleGeocoding.get("latitude") + "'," +
                                        "longitude_georef='" + googleGeocoding.get("longitude") + "'," +
                                        "distance_georef='" + googleGeocoding.get("distance") + "',";                                
                                else if(geolocateGeocoding != null)
                                    query+= "latitude_georef='" + geolocateGeocoding.get("latitude") + "'," +
                                        "longitude_georef='" + geolocateGeocoding.get("longitude") + "'," +
                                        "distance_georef='" + geolocateGeocoding.get("uncertainty") + "',";                                
                                else
                                    throw new Exception("Can't geocoding register " + temp_country + "," + temp_adm1 + "," + temp_adm2 + "," + temp_adm3 + "," + temp_local_area + "," + temp_locality);
                            }
                            //Group POST CHECK
                            //4.6
                            else if(p.getTypePolicy()==TypePolicy.POSTCHECK_VALIDATE_TAXON)
                            {
                                taxon_final=FixData.concatenate(new String[]{this.db.getRecordSet().getString("x1_genus"),
                                    this.db.getRecordSet().getString("x1_sp1"),
                                    this.db.getRecordSet().getString("x1_rank1"),
                                    this.db.getRecordSet().getString("x1_sp2"),
                                    this.db.getRecordSet().getString("x1_rank2"),
                                    this.db.getRecordSet().getString("x1_sp3")},"_").toLowerCase();
                                taxon_tnrs_final= this.db.getRecordSet().getString("tnrs_final_taxon") == null ? "" : FixData.removePatternEnd(this.db.getRecordSet().getString("tnrs_final_taxon").toLowerCase(),"_");
                                taxon_taxstand_final= this.db.getRecordSet().getString("taxstand_final_taxon") == null ? "" : FixData.removePatternEnd(this.db.getRecordSet().getString("taxstand_final_taxon").toLowerCase(),"_");
                                if(taxon_final.equals(taxon_tnrs_final) && FixData.hideRank(taxon_final).equals(taxon_taxstand_final))
                                {
                                    query+= "taxon_final='" + FixData.toCapitalLetter(taxon_final) + "',";
                                    //4.1
                                    value1=FixData.validateRank(this.db.getRecordSet().getString("x1_rank1"));
                                    query+=value1==null || value1.equals("") ? "" : "f_x1_rank1='" + value1 + "'";
                                    value2=FixData.validateRank(this.db.getRecordSet().getString("x1_rank2"));
                                    query+=value2==null || value2.equals("") ? "" : "f_x1_rank2='" + value2 + "'";
                                    //4.2 4.3
                                    query+=FixData.prepareUpdate("f_x1_genus", FixData.toCapitalLetter(this.db.getRecordSet().getString("x1_genus")) , true, false) +
                                            FixData.prepareUpdate("f_x1_sp1", this.db.getRecordSet().getString("x1_sp1"), true, true) +
                                            FixData.prepareUpdate("f_x1_sp2", this.db.getRecordSet().getString("x1_sp2"), true, true) +
                                            FixData.prepareUpdate("f_x1_sp3", this.db.getRecordSet().getString("x1_sp3"), true, true);
                                }
                                else if(taxon_final.equals(taxon_tnrs_final) && !FixData.hideRank(taxon_final).equals(taxon_taxstand_final))
                                    throw new Exception("Semaphore yellow. Taxstands different. Taxon: " + taxon_final + " Taxstand: " +  taxon_taxstand_final);
                                else if(!taxon_final.equals(taxon_tnrs_final) && FixData.hideRank(taxon_final).equals(taxon_taxstand_final))
                                    throw new Exception("Semaphore yellow. TNRS different. Taxon: " + taxon_final + " TNRS: " +  taxon_tnrs_final);
                                else
                                    throw new Exception("Semaphore Red. All differents. Taxon: " + taxon_final + " TNRS: " +  taxon_tnrs_final + " Taxstand: " + taxon_taxstand_final);
                            }
                            //4.6.3
                            else if(p.getTypePolicy()==TypePolicy.POSTCHECK_VALIDATE_TAXON_MANDATORY && (this.db.getRecordSet().getString("taxon_final") == null || this.db.getRecordSet().getString("taxon_final").equals("")))
                                throw new Exception("Taxon final miss");
                            //5.3.6 - 5.3.7
                            else if(p.getTypePolicy()==TypePolicy.POSTCHECK_GEOCODING_CROSCHECK_COORDS)
                            {
                                if(this.db.getRecordSet().getString("coord_source") != null)
                                    System.out.println("The register already have cross check. coord_source: " + this.db.getRecordSet().getString("coord_source"));
                                else
                                {
                                    water=rWater.getDataFromLatLon(this.db.getRecordSet().getDouble("latitude"),this.db.getRecordSet().getDouble("longitude"));
                                    if(water==null)
                                        throw new Exception("Coords. Not found point in the water database. " + water);
                                    else if(!water.equals(Configuration.getParameter("geocoding_database_world_earth")))
                                        throw new Exception("Coords. Point in the water or boundaries. " + water + " Lat: " + FixData.getValue(this.db.getRecordSet().getDouble("latitude")) + " Lon: " + this.db.getRecordSet().getDouble("longitude"));
                                    //Validation with google
                                    googleReverse=GeocodingGoogle.reverse(this.db.getRecordSet().getDouble("latitude"), this.db.getRecordSet().getDouble("longitude"));
                                    if(googleReverse == null || !googleReverse.get("status").toString().equals("OK") || !FixData.getValue(googleReverse.get("iso")).toLowerCase().equals(FixData.getValue(this.db.getRecordSet().getString("final_iso2")).toLowerCase()))
                                        throw new Exception("Cross check coords error: Country don't match  or not found. Latitude: " + FixData.getValue(this.db.getRecordSet().getString("latitude")) +
                                                " Longitude: " + FixData.getValue(this.db.getRecordSet().getString("longitude")) +
                                                (googleReverse == null ? "" : (" Status: " + googleReverse.get("status").toString() + " Iso: " + FixData.getValue(googleReverse.get("iso")))));
                                    else
                                        query+="coord_source='original',final_lat='" + this.db.getRecordSet().getString("latitude") + "',final_lon='" + this.db.getRecordSet().getString("longitude") + "',";
                                }
                            }
                            //5.4.1
                            else if(p.getTypePolicy()==TypePolicy.POSTCHECK_GEOCODING_CROSCHECK_GEOREF)
                            {
                                if(this.db.getRecordSet().getString("coord_source") != null)
                                    System.out.println("The register already have cross check. coord_source: " + this.db.getRecordSet().getString("coord_source"));
                                else
                                {
                                    water=rWater.getDataFromLatLon(this.db.getRecordSet().getDouble("latitude_georef"),this.db.getRecordSet().getDouble("longitude_georef"));
                                    if(water==null)
                                        throw new Exception("Coords. Not found point in the water database. " + water);
                                    else if(!water.equals(Configuration.getParameter("geocoding_database_world_earth")))
                                        throw new Exception("Georef. Point in the water or boundaries. " + water + " Lat: " + FixData.getValue(this.db.getRecordSet().getDouble("latitude")) + " Lon: " + this.db.getRecordSet().getDouble("longitude"));
                                    if(this.db.getRecordSet().getString("latitude_georef") == null || this.db.getRecordSet().getString("longitude_georef") == null)
                                        throw new Exception("Cross check georef error: Data not found. Latitude: " + FixData.getValue(this.db.getRecordSet().getString("latitude_georef")) +
                                                " Longitude: " + FixData.getValue(this.db.getRecordSet().getString("longitude_georef")));
                                    else
                                        query+="coord_source='georef',final_lat='" + this.db.getRecordSet().getString("latitude_georef") + "',final_lon='" + this.db.getRecordSet().getString("longitude_georef") + "',";
                                }
                            }
                        }
                        catch(Exception e)
                        {
                            a+=1;
                            googleGeocoding=null;
                            googleReverse=null;
                            geolocateGeocoding=null;
                            StackTraceElement[] frames = e.getStackTrace();
                            String msgE="";
                            for(StackTraceElement frame : frames)
                                msgE += frame.getClassName() + "-" + frame.getMethodName() + "-" + String.valueOf(frame.getLineNumber());
                            System.out.println(e + msgE);
                            Log.register(super.log, TypeLog.REGISTER_ERROR, this.db.getRecordSet().getString("id") + "|" + e.toString() + "|" + msgE, true, "TempOccuStep" + String.valueOf(step),Configuration.getParameter("log_ext_review") );
                        }
                    }
                    footer=" Where id=" +this.db.getRecordSet().getString("id") + ";" ;
                    //Validate that have
                    if(!query.endsWith("set "))
                        Log.register(super.log, TypeLog.REGISTER_OK, query.substring(0, query.length()-1) + footer, false,"TempOccuStep" + String.valueOf(step),Configuration.getParameter("log_ext_sql"));
                }
                catch(Exception exc)
                {
                    a+=1;
                    googleGeocoding=null;
                    googleReverse=null;
                    geolocateGeocoding=null;
                    Log.register(super.log, TypeLog.REGISTER_ERROR, this.db.getRecordSet().getString("id") + "|" + exc.toString() + "|" + exc.getMessage(), true,"TempOccuStep" + String.valueOf(step),Configuration.getParameter("log_ext_review"));
                }
                //Log for review data
                if(step==2 || step == 3)
                    Log.register(super.log, TypeLog.REVIEW_DATA, review_data, false,"TempOccuReview" + String.valueOf(step),Configuration.getParameter("log_ext_review"));
                //Percent of progress
                System.out.println(FixData.toPercent(countRows, row) + "% row " + row + " of " + countRows);
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error in the connection with database " + ex);
            Log.register(super.log, TypeLog.REGISTER_ERROR, "Error in the connection with database " + ex, true,"TempOccuStep" + String.valueOf(step),Configuration.getParameter("log_ext_review"));
        }
        return a;
    }
}