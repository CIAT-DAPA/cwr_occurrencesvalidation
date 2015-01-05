# OCCURRENCES VALIDATION #
=======

This project contains tools for validate occurrences of Crop Wild Relative. All rights are of CIAT

### What is this repository for? ###

* Quick summary
* Version

### How do I get set up? ###

* Summary of set up
* Configuration
* Dependencies
* How to run

### Who do I talk to? ###

* [Crop Wild Relatives](http://www.cwrdiversity.org)
* [Team](https://github.com/teamcwrciat)

=======

## Quick summary ##

This project is a set of tools aimed at the validation of occurrences of wild relatives of crops. With this tool you can validate 
the occurrences of different data source and make sure the information is correct. This process helps to validate occurrences 
from global database such as GBIF, among others.

Some of the process in this application such are data import to temporal database, data validation (called cross check), queries 
run for update register in the database

## Version

1.0.0.1

## Summary of set up ##

* Last release (ImporterTables.jar)
* Configuration File (conf.txt)
* Raster file of the world with the sea, land and coast or database already processed (waterbody.db)
* [R](http://www.r-project.org/)
* Taxonstand package for R installed

## Configuration ##

* You need have in the same folder the ImporterTables.jar, folder lib, database of land's point (if you don't have it you may generate another from 
application with the raster file) and the configuration file.

* Review data of variables in the file called conf.txt (configuration file), because this file have all parameters for that the application can be execute.

## How to run ##

```
$ java -jar ImporterTables.jar
```