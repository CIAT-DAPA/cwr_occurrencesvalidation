# Code prepared to automatize downloads from GBIF
# N. Castaneda - 2015

#----------------------------------------------------
# trying package rgbif - RGBIF only gets the first 200K records available through GBIF
# for obtaining the remianing data, asynchronous download API needs to be used
#----------------------------------------------------
# install.packages("devtools")
devtools::install_github("ropensci/rgbif")

require(rgbif)

# number of georeferenced records
occ_count(georeferenced=TRUE) #468821906 --> same as gbif.org for 29/05/15

outdir <- "D:/CWR/_inputs/occurrences/gbif_2015"
# outdir <- "/curie_data/ncastaneda/occurrences_gbif/"
setwd(outdir)

genlist <- read.csv("D:/CWR/_inputs/occurrences/gbif_2014/CWR-request-to-gbif-20140408-genus.csv")
# genlist <- read.csv("/curie_data/ncastaneda/occurrences_gbif/CWR-request-to-gbif-20140408-genus.csv")
genlist <- as.vector(genlist$Genus)

for(i in 1:length(genlist)){
  gen <- genlist[i]
  odir <- paste(outdir,"/", tolower(gen), sep="")
  if(!file.exists(odir)){dir.create(odir)}
  
  keys <- name_suggest(q=gen, rank='genus')
  key.gen <- keys[keys$canonicalName == gen,]
  key <- key.gen$key[1]
  no.r <- occ_count(taxonKey=key) # number of occurrences available on GBIF
  #   round(no.r/200000)
  limit = 5000
  it <- seq(from=200000, to=no.r, by=limit)
  
  for(y in it){
    cat("Downloading data for",gen,"| Starting at",y,"\n")
    
    occdata <- occ_search(taxonKey=key, limit=limit, start=y)
    occdata <- occdata$data
    write.csv(occdata, file=paste(odir,"/",gen,"_",y,".csv",sep=""), row.names=F)  
  }
}


occdata <- occ_search(taxonKey=key, limit=5000, start=200000)

occdata <- occ_search(taxonKey=key, limit=5000, start=0)
occdata <- occdata$data
write.csv(occdata, file=paste(outdir,"/",gen,"_all",".csv",sep=""), row.names=F)

nrow(occdata) # 5000 rows
length(unique(occdata$gbifID)) # 5000 rows

require(plyr)
filenames <- list.files(odir, pattern =".csv", full.names = T)

acer <- do.call("rbind.fill", lapply(filenames, read.csv, header=T))
write.csv(acer, file=paste(outdir,"/",gen,"_test",".csv",sep=""), row.names=F)


head(acer)
nrow(acer) # 13000 records
ncol(acer)
length(unique(acer$gbifID)) # only 8000 are unique records
key

# Getting the taxon_key for each genus
genlist <- read.csv("D:/CWR/_inputs/occurrences/gbif_2014/CWR-request-to-gbif-20140408-genus.csv")
keygen <- genlist
keygen$key <- NA
head(keygen)

genlist <- as.vector(genlist$Genus)

for(i in 1:length(genlist)){
  gen <- genlist[i]
  
  keys <- name_suggest(q=gen, rank='genus')
  if(is.null(keys)){
    cat("Key not available for", gen, "\n")
    
  }else{
    key.gen <- keys[keys$canonicalName == gen,]
    key <- key.gen$key[1]
    
    keygen$key[which(keygen$Genus==paste(gen))] <- key    
  }
}

write.csv(keygen, paste(outdir,"/keys_all_genera.csv", sep=""), row.names=F)

# ---------------------------------------------------------------
# Using occ_download option from S. Chamberlain - AWESOME!
# ---------------------------------------------------------------
# start a download with query parameters
q <- paste
req <- occ_download('taxonKey = 3247345', user="ncastaneda", pwd="ZR2H23bw", 
                    email="npcastaneda00@gmail.com")

# get metadata progress on previous download
down.key <- req[1]
progress <- occ_download_meta(req[1])

# retrieve a dowload
occ_download_get(key=down.key, path=outdir)

occ_download_get(key="0002998-150528172251762", path=outdir) # test with Acer data


list = 1:10

for(i in list){
  cat(i,"\n")
  Sys.sleep(2) # Time in seconds
}

# ---------------------------------------------------------------
# ---------------------------------------------------------------
# ---------------------------------------------------------------
outdir <- "D:/CWR/_inputs/occurrences/gbif_2015"
# outdir <- "/curie_data/ncastaneda/occurrences_gbif/"
setwd(outdir)

genlist <- read.csv("D:/CWR/_inputs/occurrences/gbif_2014/CWR-request-to-gbif-20140408-genus.csv")

keygen <- genlist
keygen$key <- NA
keygen$occ.num <- NA
keygen$request.no <- NA

genlist <- as.vector(genlist$Genus)
gen <- "Amblyopyrum"

for(i in 43:length(genlist)){
  gen <- genlist[i]
  # Finding TAXON_KEY for each Genus
  keys <- name_suggest(q=gen, rank='genus')
  
  if(is.null(keys)){
    cat("Key not available for", gen, "\n")
    key <- NA
  }else if(sum(keys$canonicalName==gen)==1){
    cat("Only one key available for this genus, thus selecting it \n")
    key.gen <- keys[keys$canonicalName == gen,]
    key <- key.gen$key[1]
    
    keygen$key[which(keygen$Genus==paste(gen))] <- key
    
    # How many occurrence records are available today?
    occs <- occ_count(taxonKey = key)
    keygen$occ.num[which(keygen$Genus==paste(gen))] <- occs   
  }else{
    cat("Select TAXON_KEY manually \n")  
    keys <- name_suggest(q=gen, rank="genus", fields=c('key','canonicalName','higherClassificationMap'))
  }  
}

#~~~~~~~~~~~~~~~~~~~~~~
# Checking keys manually

keygen.miss <- keygen[is.na(keygen$key),]
gen.miss <- as.vector(keygen.miss$Genus)
nrow(keygen.miss)
i = 36
gen <- gen.miss[i]
gen
keys <- name_suggest(q=gen, rank="genus", fields=c('key','canonicalName','higherClassificationMap'))

keys[1]
keys[2]

# use this bit when only one plantae is included in the list of keys, otherwise select manually
for(i in 1:length(keys$hierarchy)){
  ids.ranks <- keys$hierarchy[[i]]$id
  if(isTRUE(is.element("6",ids.ranks))){
    key <- keys$data$key[i]
    keygen$key[which(keygen$Genus==paste(gen))] <- key
    occs <- occ_count(taxonKey = key)
    keygen$occ.num[which(keygen$Genus==paste(gen))] <- occs 
  }
}

# Adding TAXON_KEY manually
52  3256717               Zea
68  2705049               Zea

keys$hierarchy[68]

key <- 2705049

keygen$key[which(keygen$Genus==paste(gen))] <- key

# How many occurrence records are available today?
occs <- occ_count(taxonKey = key)
keygen$occ.num[which(keygen$Genus==paste(gen))] <- occs 

# Preparing download requests to GBIF

# keygen <- read.csv(paste(outdir,"/genera_keys.csv",sep=""))
keygen <- read.csv(paste(outdir,"/missing_genera.csv",sep=""))

keygen$request.no <- as.character(keygen$request.no)

down.list <- keygen[keygen$occ.num!=0,]

down.list <- down.list[complete.cases(down.list$occ.num),] # removing Raphanobrassica
down.list <- down.list[is.na(down.list$request.no),] # removing genera with requests codes

down.list <- down.list$key

for(key in down.list){
  
  gen <- keygen$Genus[which(keygen$key==key)]
  gen <- as.character(gen)
  cat("Downloading data for",gen,"\n")
  
  # Obtaining data from GBIF
  taxonKey <- paste("taxonKey =",key)
  req <- occ_download(taxonKey, user="ncastaneda", pwd="ZR2H23bw", 
                      email="npcastaneda00@gmail.com")
  
  # What's the key for downloading data?
  down.key <- req[1]
  
  keygen$request.no[which(keygen$Genus==paste(gen))] <- down.key  
  
  write.csv(keygen,paste(outdir,"/genera_keys_2.csv",sep=""), row.names=F)
  
  Sys.sleep(240) # Time in seconds
}


# re do the following (and comments while trying to re-process this data):
# Asparagus - ok
# Brassica - ok
# Comarum - ok
# Cynosurus  - ok
# Cytisus - ok
# Digitaria - ok
# Dioscorea - ok
# Malus - ok
# Oryza - ok
# Ononis - ok
# Phaseolus - ok
# Setaria - ok
# Vavilovia - ok
# Vernicia - ok
# Vigna - ok
# Vitellaria - ok
# Vitis - ok
# Voandzeia - ok


# Fondo acción: Belen, San Jose (hablar con Patrimonio) 1:25.000
# Director regional Caqueta Corpoamazonia: Juan de dios Vergel


# --------------=============-----------------

# Retrieving downloads
outdir <- "/curie_data/ncastaneda/occurrences_gbif/"
gen.keys <- read.csv(paste(outdir,"/genera_keys.csv", sep=""))
gen.keys <- read.csv(paste(outdir,"/genera_keys_2.csv", sep="")) #datasets with problems to be queried from GBIF

probs <- c("Asparagus",
           "Brassica",
           "Comarum",
           "Cynosurus",
           "Cytisus",
           "Digitaria",
           "Dioscorea",
           "Malus",
           "Oryza",
           "Ononis",
           "Phaseolus",
           "Setaria")

for(prob in probs){
  gen.keys$request.no[which(gen.keys$Genus ==prob)] <- NA
}

gen.keys <- gen.keys[!is.na(gen.keys$request.no),] # removing genera w/o requests codes

down.keys <- as.vector(gen.keys$request.no)

for(down.key in down.keys){
  gen <- gen.keys$Genus[which(gen.keys$request.no==down.key)]
  gen <- as.character(gen)
  cat("Downloading data for genus", gen, "\n")
  odir <- paste(outdir,"/", tolower(gen), sep="")
  if(!file.exists(odir)){
    dir.create(odir)
    
    occ_download_get(key=down.key, path=odir)
  }else{
    cat("Data for",gen, "already downloaded \n")
  }
}



occ_download_get(key="0002998-150528172251762", path=odir) # test with Acer data

# Use this code to obtain dois
my.down.list <- occ_download_list(user="ncastaneda",pwd ="ZR2H23bw")
my.down.list <- my.down.list$results



all <- merge(gen.keys, my.down.list, by="key")
?merge
