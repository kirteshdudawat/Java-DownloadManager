# Download-Manager 
@author kirteshdudawat
Download Manager to download multiple files based any underlying protocol SFTP, HTTP, HTTPS, FTP

Prerequesties: 
1. Define all properties mentioned in src/main/resources/application.properties

2. Define logging file path for property "log4j.appender.file.File" in src/main/resources/log4j.properties

This Application would parallelly download files for provided Url's in optimized space enviournment. This class would act as entry to the application.

The project is an maven project in Java, with compiler compliance set to Java 1.8 or above.


Applications feature overview:
 1. Would download data from multiple sources and protocol (FTP, SFTP, HTTP, HTTPS) to local disk.
 2. No partial downloaded data be kept at local disk in any case.


Application Package Design / Definition:

com.kirtesh.downloadmanager  
 Entry point to Application (We are currently here) 

com.kirtesh.downloadmanager.cache  
 All JVM level Cache would reside here. 

com.kirtesh.downloadmanager.enums  
 All Enums / Constants to be present here 

com.kirtesh.downloadmanager.factory 
 Factory would return Implementing class, the source can just call factory for providing implementation class. By this, source has been abstracted from Creating objects and would directly get required implementation without having to specify the exact class of the object that will be created by Factory. 

com.kirtesh.downloadmanager.metadata 
 Would contains Pojo's of all metadata required during call's / task scheduling across project.

com.kirtesh.downloadmanager.service 
 Package would keep abstraction of all Business logic required. 
 Note : For ease of development and readability we have separated Start-Up Logic / implementation along with Validation Service and its  implementation from all other Business logic.

com.kirtesh.downloadmanager.service.impl 
 Implements all Business Logic's as specified in com.agoda.downloadmanager.service package.

com.kirtesh.downloadmanager.service.startup 
 Package would keep abstraction of all Start-up logics required. 

com.kirtesh.downloadmanager.service.startup.impl 
 Implements all Start-Up Logic's as specified in com.agoda.downloadmanager.service.startup package. Eg. Initializing All properties to JVM Cache at Startup

com.kirtesh.downloadmanager.service.validation 
 Package would keep abstraction of all Validation logics required. 

com.kirtesh.downloadmanager.service.validation.impl 
 Implements all Validation Logic's as specified in com.agoda.downloadmanager.service.validation package. Eg. Validating if a location exist on local disk, before actually starting the download.

com.kirtesh.downloadmanager.utils 
 Would contain all Utility methods that would be used across multiple services. Eg. Checking if String object is empty or not.

Dependencies in project (Defined in POM):
 1. log4j : For Logging purpose
 2. nurkiewicz.asyncretry : For implementation of Retryable Executor for Downloading files.
 3. jcraft.jsch : For downloading SFTP protocol based file.
 4. JUnit4 : For testing purpose.

The Project has proper documentation and and taking a Java Doc would provide proper insights of project.
