###################### Dependencies #######################

library(RMySQL)
library(parallel)

###################### Configuration #######################

# Varibles Global
work_folder <- ""
# Options to export 1=Genus, 2=Taxon, 3=Country
work_type_filter <- 1

# Variables Database Connection
db_host <- ""
db_user <- ""
db_password <- ""
db_name <- ""
db_table <- ""

# Parameters
work_cores <- 10
work_export_extension <- ".csv"
work_sep_field <- "|"
work_decimal <- "."
work_na <- ""

###################### Internal Variables #######################

# Constants queries
db_query_general_filter <- " data_public_access=1 and visibility=1 "
db_query_genus <- paste0("select distinct f_x1_genus from ",db_table," where !isnull(f_x1_genus) and ",db_query_general_filter," order by f_x1_genus;")
db_query_taxon <- paste0("select distinct taxon_final from ",db_table," where !isnull(taxon_final) and ",db_query_general_filter," order by taxon_final;")
db_query_country <- paste0("select distinct final_country from ",db_table," where !isnull(final_country) and ",db_query_general_filter," order by final_country;")

# Variables Database Query
db_fields <- "id,source,provider_institute_id,provider_name,institute_id,institute_name,collection,source_url,availability,unique_number,barcode,vno_1,vno_2,x1_family,x1_genus,x1_sp1,x1_author1,x1_rank1,x1_sp2,x1_author2,x1_rank2,x1_sp3,x1_author3,x1_detby,x1_detdate,x1_detdd,x1_detmm,x1_detyy,x1_detstat,x2_genus,x2_sp1,x2_author1,x2_rank1,x2_sp2,x2_rank2,x2_sp3,is_hybrid,hybrid_memo,tnrs_final_taxon,taxon_final,f_x1_genus,f_x1_sp1,f_x1_rank1,f_x1_sp2,f_x1_rank2,f_x1_sp3,annotated_specimen,type,type_memo,collector,addcoll,collnumber,prefix,number,suffix,colldate,colldd,collmm,collyy,final_country,final_iso2,adm1,adm2,adm3,adm4,local_area,locality,lat_deg,lat_min,lat_sec,ns,final_ns,latitude,long_deg,long_min,long_sec,ew,final_ew,longitude,latitude_georef,alt,final_alt,final_cult_stat,final_origin_stat,habitat_txt,fl_code,fr_code,dups,notes,comments,citation,final_lat,final_lon,coord_source,taxstand_final_taxon"
db_table <- "final_occurrences"

# Database
db_cnn <- NULL
db_base_query <- NULL
db_rs_init <- NULL
db_rs_query <- NULL
db_data_init <- NULL
db_data_end <- NULL

# Export
exp_dir <- NULL
exp_filename <- NULL

###################### Functions #######################

# Process every item
process_item <- function(item){
    print(paste0(item," start"))
    
    # Temp connection to database
    db_cnn_temp <- dbConnect(MySQL(),user = db_user,password = db_password,host = db_host,dbname=db_name)
    
    # Search every item in the database
    db_rs_query <- dbSendQuery(db_cnn_temp, paste0(db_base_query,"'",item,"';"))
    db_data_end <- fetch(db_rs_query, n=-1)
    
    print(paste0(item," attached"))
    
    # Export data
    exp_filename <- paste0(work_folder,exp_dir,"/",item,work_export_extension)
    write.table(db_data_end,exp_filename,row.names=FALSE,sep=work_sep_field,dec=work_decimal,na=work_na, quote = F)
    
    # Close temp connection
    dbDisconnect(db_cnn_temp)
    
    print(paste0(item," end"))
}

###################### Process #######################

print("Start process")

# Connection Database
db_cnn <- dbConnect(MySQL(),user = db_user,password = db_password,host = db_host,dbname=db_name)

print("Connected to database")

# Build the base query
db_base_query <- paste0("select ",db_fields," from ",db_table," where ",db_query_general_filter)

# Select query depends of type
if(work_type_filter == 1){
    print("Filter genus")
    
    # name of the folder
    exp_dir <- "genus"    
    # List all genus 
    db_rs_init <- dbSendQuery(db_cnn, db_query_genus)    
    # Attach all items
    db_data_init <- fetch(db_rs_init, n=-1)    
    # Build the independen query 
    db_base_query <- paste0(db_base_query, " and f_x1_genus=")
} else if(work_type_filter == 2) {
    print("Filter taxon")
    
    # name of the folder
    exp_dir <- "taxon"    
    # List all taxon final 
    db_rs_init <- dbSendQuery(db_cnn, db_query_taxon)    
    # Attach all items
    db_data_init <- fetch(db_rs_init, n=-1)    
    # Build the independen query 
    db_base_query <- paste0(db_base_query, " and taxon_final=")    
} else if(work_type_filter == 3) {
    print("Filter country")
    
    # name of the folder
    exp_dir <- "country"    
    # List all countries 
    db_rs_init <- dbSendQuery(db_cnn, db_query_country)    
    # Attach all items
    db_data_init <- fetch(db_rs_init, n=-1)    
    # Build the independen query 
    db_base_query <- paste0(db_base_query, " and final_country=")
} 

# create folder for the results
dir.create(paste0(work_folder,exp_dir))

print("Folder created")

print("Items attached")

###################### Export #######################

# Funtion to parallel process
# Select query depends of type
if(work_type_filter == 1){
    export_data <- mclapply(db_data_init$f_x1_genus, process_item, mc.cores=work_cores)
} else if(work_type_filter == 2) {
    export_data <- mclapply(db_data_init$taxon_final, process_item, mc.cores=work_cores)
} else if(work_type_filter == 3) {
    export_data <- mclapply(db_data_init$final_country, process_item, mc.cores=work_cores)
}


# Close connection database
dbDisconnect(db_cnn)

print("End process")


