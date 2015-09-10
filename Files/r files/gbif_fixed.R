#require
require(ffbase)
require(ff)
library(RMySQL)

#functions
clearFields <- function(data){
    fields <- colnames(data)
    cleared<-lapply(fields,function(field){
        data[,field]<-gsub("|","",data[,field])
    })
    data
}

#Save data into tables and wrote a log file
save_table <- function(genus,data,path,connection,table){
    #write file
    print(paste0(genus," Start to write ",table))
    write.table(data,path,row.names=FALSE,sep="|",dec=".",na="", quote = F)
    print(paste0(genus," Finished to write ",table))
    
    #write database
    print(paste0(genus," Start to insert ",table))
    dbWriteTable(connection, value = data, name = table, append = TRUE, row.names=F) 
    print(paste0(genus," Finished to insert ",table))
    TRUE
}

#variables

#connection database
cnn <- dbConnect(MySQL(),user = '',password = '',host = '',dbname='')


#paths
#root_path <- "//DAPAdfs/workspace_cluster_6/CWR/CWR_PROJECT_CC_BD/occurrences/crosscheck/gbif_test"
#root_path <- "//DAPAdfs/workspace_cluster_6/CWR/CWR_PROJECT_CC_BD/occurrences/crosscheck/gbif/data"
root_path <- "/mnt/workspace_cluster_6/occurrences/crosscheck/gbif/data"
root_scripts <- "script"
#path log
path_log <- file.path(root_path,"log.csv")

#load headers files
header_gbif_add <- strsplit(readLines(file.path(root_path,root_scripts,"header_gbif_add.txt")), split = ",")[[1]]
header_gbif_current <- strsplit(readLines(file.path(root_path,root_scripts,"header_gbif_current.txt")), split = ",")[[1]]
header_occurrences <- strsplit(readLines(file.path(root_path,root_scripts,"header_occurrences.txt")), split = ",")[[1]]

#load directories
genus_list <- dir(root_path)
genus_list_not_process <- read.csv2(file.path(root_path,"no_work.csv"))
genus_list <- genus_list[!genus_list %in% as.character(genus_list_not_process$file)]

#cycle by every genus
process_gbif <- function(genus_list){
    table_check_procces <- data.frame(file=character(0),count_register=integer(0),saved_add=integer(0),saved_data=integer(0))
    i <- 0L
    proccesed<-lapply(genus_list,function(genus){  
        path_genus <- file.path(root_path,genus,"occurrence.txt")
        path_genus_add <- file.path(root_path,genus,"occurrences_add.csv")
        path_genus_occurrences <- file.path(root_path,genus,"occurrences_data.csv")            
        
        #remove preview version
        if(file.exists(path_genus_add))
            file.remove(path_genus_add)
        if(file.exists(path_genus_occurrences))
            file.remove(path_genus_occurrences)
        
        #read the genus file
        print(paste0(genus," Start to read"))
        genus_gbif <- read.csv2.ffdf(x=NULL,file=path_genus,encoding="UTF-8",sep = "\t",VERBOSE = TRUE,na.strings="",first.rows = 50000,next.rows = 50000,quote="",header=T,colClasses=rep("factor",224))        
        print(paste0(genus," End to read"))
        
        #clear fields
        print(paste0(genus," Start to clear"))
        genus_gbif <- clearFields(genus_gbif)            
        print(paste0(genus," End to clear"))
        
        #changed header
        occurrences_data <- genus_gbif[,header_gbif_current]
        names(occurrences_data)<-header_occurrences
        
        #save occurrences
        saved_occurrences <- save_table(genus,occurrences_data,path_genus_occurrences,cnn,"temp_occurrences")
        
        #save additional data
        saved_add <- save_table(genus,genus_gbif[,header_gbif_add],path_genus_add,cnn,"cwr_occurrences_gbif")
        
        #log
        table_check_procces$file[i]<-genus
        table_check_procces$count_register[i]<-nrow(genus_gbif)
        
        i<-i+1L
        
        remove(path_genus,path_genus_add,path_genus_occurrences,genus_gbif,occurrences_data)
        
        results <- if(saved_occurrences && saved_add)
                        TRUE
                    else
                        FALSE
    })
    
    #save log
    if(file.exists(path_log))
        file.remove(path_log)
    write.table(table_check_procces,path_log,row.names=FALSE,sep="|",dec=".",na="", quote = F)
    proccesed
}


my_process <- process_gbif(genus_list)

