# MoknJ 0.0.1 API #


## Introduction ##
This dragon scroll would serve for who ever takes on this project as a guide to unlock the unknowns and travel to far lands and slay dragons... One might ask ones self why would I be reading about dragons and I the writer would reply cause words can make a logical argument, does it matter? (rhetorical question)

![funny_header.gif](https://github.com/fixxit/MoknJ/blob/master/images/funny%20header.gif?raw=true)

## Mission ##
MoknJ started as an idea to move data from excel sheets to an customizable platform which creates an environment where it is easy for the user to build custom data templates that behave like predefined business components. 

* The platform should make it easy and less of a effort for business to consolidate its spreadsheet business data. 
* Business must save time when doing nitty-gritty administration work.

## Technology Stack ##
HTML5, CSS, Bootstrap, AngularJS, AngularUI, Angular Routing, Node, Express, Spring Boot, Spring Security (OAuth 2), Mongo DB, Java 8, Rest (Jersey with Jackson), Maven, Junit

### Running API ###
When this app starts for the 1st time you will see your username and password printed in the logs (this is your admin user). To start using the app you need to create template 1st with your respected fields mapped out then just add that template to menu (create menu) item and you are set. Repeat that few times with few unique templates and menu mappings and you've got yourself a web app (WHUTTT!!!!?)

```
2017-03-14 21:33:29.744  INFO 448 --- [io-8084-exec-21] n.i.fixx.moknj.runner.ServerInitializer  : ########Remove me in production########
2017-03-14 21:33:29.744  INFO 448 --- [io-8084-exec-21] n.i.fixx.moknj.runner.ServerInitializer  : Admin : user
2017-03-14 21:33:29.744  INFO 448 --- [io-8084-exec-21] n.i.fixx.moknj.runner.ServerInitializer  : Password : user!2
2017-03-14 21:33:29.744  INFO 448 --- [io-8084-exec-21] n.i.fixx.moknj.runner.ServerInitializer  : #######################################

```

## High level break down ##
* MoknJ is rest API which which uses token authentication to identify the logged in user. 
* The platform is split up in 2 applications ([GUI](https://github.com/fixxit/MoknJ-WebUI)) (Node.js app) and API (Spring boot app). This creates the impression when using the app that is fast. 
* As the GUI only handles rest (ajax) request and display (data manipulation) with the API which is tomcat/tomee/wildfly free to handle db related requests and data manipulation.
* GUI is pure HTML5 and javascript which makes it easy for most developers to contribute to.
* Moknj app consists out of templates and menu item (pages), templates and menu's are dynamic or abstract.
* Moknj also contains simple graph stats implementation to setup and generate statistics on individual templates.
* MoknJ overs the capability to change the database structure of save records on the fly as needs of the user grow.

## Low level break down (Also high level but more detaaail hah!) ##
### Template ###
Templates represent components in the platform. Templates define the fields and field behavior. Templates are linked to menu (pages). See menu for more information.

* Template consists out of multiple fields, which can be saved. (Showed in red box of screen shot)
* Name of field to be displayed on the page. (Shown in green box of screen shot)
* Field Type consists out of few types namely text, number, date, monetary. (Shown in blue box of screen shot)
* Field Index, the index of the field on the view of the template. (Showed in red box of screen shot)
* Drop down fields are fields which have multiple values. These fields are displayed as you guessed a drop down... 
* Templates also have behavior (Asset, Employee) assigned to them.  

![template](https://github.com/fixxit/MoknJ/blob/master/images/template.jpg?raw=true)

### Menu ###
Menu item consists out a group of templates which represent a single page in the platform. The platform allows for multiple menu assigned to main modules.

* Name of Page and Name of Menu Item to link to the page.
* Index of menu item in menu list.
* Group of templates. Templates can be order to display above one another.

See screen shot for example of edit menu screen.
![menu](https://github.com/fixxit/MoknJ/blob/master/images/menu.jpg?raw=true)

### Module ###
Modules can be seen as view and behavior of a template and menu. The module dictates the view display.  

* Asset view contains a horizontal view list views.
![module](https://github.com/fixxit/MoknJ/blob/master/images/module.jpg?raw=true)
* Employee view contains a horizontal user filter shown in red and tab view shown purple and list view shown in green.
![module_2](https://github.com/fixxit/MoknJ/blob/master/images/module_2.jpg?raw=true)
### User ###
Users and employees are one of the same, a user is just an employee with user access. Employees are linked to the assets and employee templates records.

Example of user view/new screen
![user](https://github.com/fixxit/MoknJ/blob/master/images/user.jpg?raw=true)

### Access ###
User access can be defined once menu and template is loaded to the platform. **The user access is stored in the access table**, note that user access is assigned to template and menu id and user id with a right (new,update,delete,view). This access is linked to user and the user is linked to token. The token is used to check if any action to rest api is allowed like view or deleting. Also keep in mind that this system has no session for the front end but token expiry date. 
![access](https://github.com/fixxit/MoknJ/blob/master/images/access.jpg?raw=true)

Screen shot info
* MoknJ API Access rights in the red box, is used to define access to rest controller or path. Simple terms may this token access this url on the platform.
* Page access in blue box this used to give the user access to the template and menu item.

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
Riaan Schoeman (Devin Alrighty) (riaan.schoeman@fixx.it)

![funny_footer.gif](https://github.com/fixxit/MoknJ/blob/master/images/funny%20footer.gif?raw=true)
