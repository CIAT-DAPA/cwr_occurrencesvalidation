###################### Dependencies #######################

library(RMySQL)

###################### Configuration #######################

# Varibles Global
work_folder <- ""
# Fields to export 1=public, 2=all
work_filter_fields <- 1
# Data to export 1 = public and visibility,  2 all
work_filter_condition <- 1
# Filter to export 1=Final Taxon, 2=Final Genus
work_filter_type <- 2

# Value to filter
work_filter <- ""

# Variables Database Connection
db_host <- ""
db_user <- ""
db_password <- ""
db_name <- ""

# Parameters
work_cores <- 10
work_export_extension <- ".csv"
work_sep_field <- "|"
work_decimal <- "."
work_na <- ""

###################### Internal Variables #######################

# Constants queries
work_filter_condition_public <- " data_public_access=1 and visibility=1 and "
db_query_fields_all <- "id,old_id,taxon_id,metadata_id,field_collected_data,data_public_access,filename,username,source,provider_institute_id,provider_name,institute_id,institute_name,is_expert,collection,source_url,botrecat,availability,image,final_image,unique_number,barcode,vno_1,vno_2,voucher_id,x1_family,x1_genus,x1_sp1,x1_author1,x1_rank1,x1_sp2,x1_author2,x1_rank2,x1_sp3,x1_author3,x1_detby,x1_detdate,x1_detdd,x1_detmm,x1_detyy,x1_detstat,x2_family,x2_genus,x2_sp1,x2_author1,x2_rank1,x2_sp2,x2_author2,x2_rank2,x2_sp3,x2_author3,x2_detby,x2_detdate,x2_detdd,x2_detmm,x2_detyy,x2_detstat,x3_family,x3_genus,x3_sp1,x3_author1,x3_rank1,x3_sp2,x3_author2,x3_rank2,x3_sp3,x3_author3,x3_detby,x3_detdate,x3_detdd,x3_detmm,x3_detyy,x3_detstat,is_hybrid,hybrid_memo,tnrs_overall_score,tnrs_source,tnrs_x1_family,tnrs_final_taxon,tnrs_author1,taxon_final,f_x1_genus,f_x1_sp1,f_x1_rank1,f_x1_sp2,f_x1_rank2,f_x1_sp3,annotated_specimen,type,type_memo,collector,addcoll,collnumber,prefix,number,suffix,colldate,colldd,collmm,collyy,final_country,country,iso,final_iso2,adm1,adm2,adm3,adm4,local_area,locality,coord,lat_deg,lat_min,lat_sec,ns,final_ns,latitude,long_deg,long_min,long_sec,ew,final_ew,longitude,llorig,lldatum,georef_history_id,latitude_georef,longitude_georef,distance_georef,georef_flag,alt,final_alt,alt_max,final_alt_max,cult_stat,final_cult_stat,origin_stat,final_origin_stat,soil,slope,aspect,habitat_txt,plant_desc,frequency,fl_code,fr_code,inflo_graminea,vernacular,language,uses,dups,notes,comments,timestamp,coord_check_old,citation,final_lat,final_lon,coord_source,taxstand_family,taxstand_genus,taxstand_sp1,taxstand_sp2,taxstand_author1,taxstand_final_taxon,temp_id,grin_final_taxon,x1_taxon,gbif_genus,gbif_species,collection_code,gbif_references,datasetKey,gbif_rank,origin_stat_inv,iso2,taxon_source,f_x1_family,quality_row,visibility"
db_query_fields_public <- "id,source,provider_institute_id,provider_name,institute_id,institute_name,collection,source_url,availability,unique_number,barcode,vno_1,vno_2,x1_family,x1_genus,x1_sp1,x1_author1,x1_rank1,x1_sp2,x1_author2,x1_rank2,x1_sp3,x1_author3,x1_detby,x1_detdate,x1_detdd,x1_detmm,x1_detyy,x1_detstat,x2_genus,x2_sp1,x2_author1,x2_rank1,x2_sp2,x2_rank2,x2_sp3,is_hybrid,hybrid_memo,tnrs_final_taxon,taxstand_final_taxon,taxon_final,f_x1_genus,f_x1_sp1,f_x1_rank1,f_x1_sp2,f_x1_rank2,f_x1_sp3,annotated_specimen,type,type_memo,collector,addcoll,collnumber,prefix,number,suffix,colldate,colldd,collmm,collyy,final_country,final_iso2,adm1,adm2,adm3,adm4,local_area,locality,lat_deg,lat_min,lat_sec,ns,final_ns,latitude,long_deg,long_min,long_sec,ew,final_ew,longitude,latitude_georef,alt,final_alt,final_cult_stat,final_origin_stat,habitat_txt,fl_code,fr_code,dups,notes,comments,citation,final_lat,final_lon,coord_source"
db_table <- "final_occurrences"

# Variables Database Query
db_query <- NULL

# Database
db_cnn <- NULL
db_rs_query <- NULL
db_rs_data <- NULL

# Export
exp_dir <- NULL
exp_filename <- NULL

###################### Process #######################

print("Start process")

if(work_filter_fields == 1){
    db_query <- paste0("Select ",db_query_fields_public, " from ",db_table," ")
    print("Filter public fields")
} else if(work_filter_fields == 2){
    db_query <- paste0("Select ",db_query_fields_all, " from ",db_table, " ")
    print("Filter all fields")
}


if(work_filter_condition == 1){    
    db_query <- paste0(db_query," where ",work_filter_condition_public)
    print("Filter public data")    
} else if(work_filter_condition == 2){
    db_query <- paste0(db_query," where ")
    print("Filter all data")    
} 

if(work_filter_type == 1){    
    db_query <- paste0(db_query," taxon_final = '",work_filter,"';")
    print("Filter taxon")    
} else if(work_filter_type == 2){
    db_query <- paste0(db_query," f_x1_genus = '",work_filter,"';")
    print("Filter genus")    
} 

# Connection Database
db_cnn <- dbConnect(MySQL(),user = db_user,password = db_password,host = db_host,dbname=db_name)
print("Connected to database")

db_rs_query <- dbSendQuery(db_cnn,db_query )
db_rs_data <- fetch(db_rs_query, n=-1)
print("Data attached")

exp_filename <- paste0(work_folder,"/",work_filter,work_export_extension)
write.table(db_rs_data,exp_filename,row.names=FALSE,sep=work_sep_field,dec=work_decimal,na=work_na, quote = F)

# Close connection database
dbDisconnect(db_cnn)

print("End process")
