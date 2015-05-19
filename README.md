# OCCURRENCES VALIDATION #

This project contains tools for validate occurrences of Crop Wild Relative. All rights are of CIAT

### What is this repository for? ###

* Quick summary
* Version

### How do I get set up? ###

* Summary of set up
* Configuration
* Dependencies
* How to run
* [Wiki](https://github.com/teamcwrciat/occurrencesvalidation/wiki)

### Who do I talk to? ###

* [Team](https://github.com/teamcwrciat)
* [CIAT](http://ciat.cgiar.org/)
* [Crop Wild Relatives](http://www.cwrdiversity.org)

## Quick summary ##

This project is a set of tools aimed at the validation of occurrences of wild relatives of crops. With this tool you can validate 
the occurrences of different data source and make sure the information is correct. This process helps to validate occurrences 
from global database such as GBIF, among others.

Some of the process in this application such are data import to temporal database, data validation (called cross check), update register
and generate reports about of the current status

## Version

1.0.2

## Summary of set up ##

* Last release (OccurrencesValidation.jar)
* Configuration File (conf.txt)
* Raster file of the world with the sea, land and coast or database already processed (waterbody.db)
* Inventory Database, (if you want validate the origin of the every species) (origin_stat_inventory.db)
* [R](http://www.r-project.org/)
* [MySQL](http://dev.mysql.com/downloads/mysql/)

## Configuration ##

* You need have in the same folder the OccurrencesValidation.jar, folder lib, database of land's point (if you don't have it you may generate another from 
application with the raster file), Inventory's database and the configuration file.

* Review data of variables in the file called conf.txt (configuration file), because this file have all parameters for that the application can be execute.

## Dependencies

* [MySQL](http://dev.mysql.com/downloads/mysql/)
* [R](http://www.r-project.org/) 
    * Taxonstand package for R installed. In the shield of R you can write the next commands for install the package
```
install.packages(Taxonstand);
```
* The next libs .jar are include in the folder called "Libs"
    * [json simple](https://code.google.com/p/json-simple/). 
    * [mysql jdbc](http://www.mysql.com/products/connector/).
    * [SQLite jdbc](https://bitbucket.org/xerial/sqlite-jdbc).
    * [RCaller](https://code.google.com/p/rcaller/).

## How to run ##

```
$ java -jar OccurrencesValidation.jar
```