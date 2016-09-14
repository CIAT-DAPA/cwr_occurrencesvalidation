############################## Readme ##############################
# Author: Steven Sotelo
# Description: Script to import data from csv file to table in mysql database
############################## Readme ##############################

############################## Require Packages ##############################
install.packages("ff")
install.packages("ffbase")
install.packages("RMySQL")
# Package requiere
require(ffbase)
require(ff)
library(RMySQL)
############################## Require Packages ##############################

############################## Parameters ##############################
# CSV source file path
source_file <- ""
# Database Connection
db_host <- ""
db_user <- ""
db_password <- ""
db_name <- ""
db_table <- ""
############################## Parameters ##############################

############################## Process ##############################
# Read source file
data <- read.csv2.ffdf(x=NULL,file=source_file,encoding="UTF-8",sep = "|",VERBOSE = TRUE,na.strings="",first.rows = 50000,next.rows = 50000,quote="\"",header=T,colClasses=rep("factor",224))
# Fix data to write in the database
header <- names(data)
occurrences <- data[,header]
names(occurrences)<-header
# Connection database
db_cnn <- dbConnect(MySQL(),user = db_user,password = db_password,host = db_host,dbname=db_name)
# Write data into a table in the database
dbWriteTable(db_cnn, value = occurrences, name = db_table, append = TRUE, row.names=F)
# Close connection to database
dbDisconnect(db_cnn)
############################## Process ##############################