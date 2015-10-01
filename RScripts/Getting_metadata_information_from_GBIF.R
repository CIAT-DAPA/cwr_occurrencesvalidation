library("XML")

root_path <- "//DAPAdfs/workspace_cluster_6/CWR/CWR_PROJECT_CC_BD/occurrences/crosscheck/gbif/"
providers_list <- read.csv(file.path(root_path,"metadata/providers.csv"))

genus_list <- dir(file.path(root_path,"data"))
genus_list_not_process <- read.csv2(file.path(root_path,"metadata/no_work.csv"))
genus_list <- genus_list[!genus_list %in% as.character(genus_list_not_process$file)]

providers_process <-lapply(genus_list, function(genus){
    dataset_keys_path <- file.path(root_path,"data",genus,"dataset")
    
    if(file.exists(dataset_keys_path)){
       
        dataset_keys_list <- dir(dataset_keys_path)   
        dataset_keys_found <- lapply(dataset_keys_list,function(key){ 
            
            key_real <- gsub(".xml","",key)
            dataset <- xmlParse(file.path(root_path,"data",genus,"dataset",key))
           
            return(c(key_real,xmlValue(xmlRoot(dataset)[["dataset"]][["title"]]),xmlValue(xmlRoot(dataset)[["dataset"]][["creator"]][["organizationName"]])))
            
        })
        rb <- do.call(rbind,dataset_keys_found)
        colnames(rb) <-  c("keyReal","title","organizationName")
    }
    return(rb)
})

provLisf <- do.call(rbind,providers_process)

merProv <- merge(providers_list,provLisf,by.x="datasetkey",by.y="keyReal",all.x=T,all.y=F,sort=F)


write.table(merProv,paste0(root_path,"metadata/providers_final.csv"),row.names=FALSE,sep="|",dec=".",na="", quote = F)

