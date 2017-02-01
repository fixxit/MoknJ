# MoknJ 0.0.1b #
This dragon scroll would serve for who ever takes on this project to understand the basics and get the system to run and capture dragons which could be hiding in the dark.

## Mission ##
MoknJ is a simple but effective solution to move data from excel sheets to an customizable platform which makes it easy build custom templates that behave like predefined business components. The system helps the user to consolidate his company spreadsheet data which results in more effective management of the data.

## High level break down ##
* MoknJ is rest API which which uses token authentication to identify the logged in user. 
* The platform is split up in 2 applications GUI (Node.js app) and API (Spring boot app). This creates the impression when using the app that is fast. 
* As the GUI only handles rest (ajax) request and display (data manipulation) with the free API which is  tomcat/tomee/wildfly to handle db related requests and access based data manipulation.
* GUI is pure HTML5 and javascript which makes it easy for most developers to contribute to it.
* Moknj app consists out of templates and menu item (pages), templates and menu's are dynamic or abstract.
* MoKnj also has a high level graph stats system to setup and generate statistics on template created by the administrator.

## Low level break down ##
### Template ###
### Menu ###
### Module ###
### User ###
### Access ###

## How do I get set up? ##
### Dependencies ###
* Node.js (latest)
* npm (latest)
* Maven 4
* Mongo 
* Mongo chef core (Mongo db editor)

### Configuration ###
This is to be added to the security.properties file the main Moknj package.
```
#!xml
security.realm = FIXX_OAUTH_REALM
security.client = fixx-trusted-client
security.secret = fixx_secret
security.token_validity_seconds = 72000
security.refresh_token_validity_seconds = 72000
security.resource_id = fixx_rest_api
admin.user = fixxit
admin.pass = test
```

### Database configuration ###
This is to be added to the system.properties file the main Moknj package.
```
#!xml
system.db = moknj
system.url = localhost
system.port = 27017
system.username = 
system.password = 
system.environment =
```

### Who do I talk to? ###
Riaan Schoeman (riaan.schoeman@fixx.it)
![tumblr_mtxyuyRJZZ1s4xenlo1_400.gif](https://bitbucket.org/repo/aEK7Ey/images/1657564402-tumblr_mtxyuyRJZZ1s4xenlo1_400.gif)