# MoknJ 0.0.1b #
## Introduction ##
This dragon scroll would serve for who ever takes on this project to unlock the unknowns and travel to far lands and slay dragons.
One might ask ones self why would I be reading about dragons and I the writer would reply cause words can make a logical argument, the dragon represents the unknown which you have found yourself (netherlands) in and mission of acquiring skills to strengthening your stats, so you too can one day rain......fire...on...your...colleagues...

![tumblr_inline_ngc8ik5Hft1t6v9pj.gif](https://bitbucket.org/repo/aEK7Ey/images/1843827414-tumblr_inline_ngc8ik5Hft1t6v9pj.gif)

## Mission ##
MoknJ is a simple but effective solution to move data from excel sheets to an customizable platform which makes it easy build custom templates that behave like predefined business components. The system helps the user to consolidate his company spreadsheet data which results in more effective management of the data.

## High level break down ##
* MoknJ is rest API which which uses token authentication to identify the logged in user. 
* The platform is split up in 2 applications ([GUI](https://bitbucket.org/fixxitprofessionalservices/fixxassettrackerui)) (Node.js app) and API (Spring boot app). This creates the impression when using the app that is fast. 
* As the GUI only handles rest (ajax) request and display (data manipulation) with the free API which is  tomcat/tomee/wildfly to handle db related requests and access based data manipulation.
* GUI is pure HTML5 and javascript which makes it easy for most developers to contribute to it.
* Moknj app consists out of templates and menu item (pages), templates and menu's are dynamic or abstract.
* Moknj also has a high level graph stats system to setup and generate statistics on template created by the administrator.
* MoknJ overs the capability to change the database structure of save records on the fly as needs of the user grow.

## Low level break down (Also high level but more detaaail hah!) ##
### Template ###
Templates represent components in the platform. Templates are hold the component design. Template config contain the field names and field types which are linked. Templates are linked to menu (pages). See menu for more information.

* Template consists out of multiple fields, which can be saved. (Showed in red box of screen shot)
* Name of field to be displayed on the page. (Shown in green box of screen shot)
* Field Type consists out of few types namely text, number, date, monetary.
* Field Index, the index of the field on the view of the template. (Shown in blue box of screen shot)
* Drop down fields are fields which have multiple values. These fields are displayed as you guessed a drop down... 
* Templates also have behavior (Asset, Employee) assigned to them.  

See screen shot for example:
![2017-02-01_13-13-05.jpg](https://bitbucket.org/repo/aEK7Ey/images/193534309-2017-02-01_13-13-05.jpg)

### Menu ###
Menu item consists out a group of templates which represent a single page in the platform. The platform allows for multiple menu assigned to main modules.

* Name of Page and Name of Menu Item to link to the page.
* Index of menu item in menu list.
* Group of templates. Templates can be order to display above one another.

See screen shot for example of edit menu screen.
![2017-02-01_13-11-14.jpg](https://bitbucket.org/repo/aEK7Ey/images/3443332288-2017-02-01_13-11-14.jpg)

### Module ###
Modules can be seen as view and behavior of a template and menu. The module dictates the view display.  

* Asset view contains a horizontal view list views.
![2017-02-01_13-08-28.jpg](https://bitbucket.org/repo/aEK7Ey/images/3967650579-2017-02-01_13-08-28.jpg)

* Employee view contains a horizontal user filter shown in red and tab view shown purple and list view shown in green.
![2017-02-01_13-04-33.jpg](https://bitbucket.org/repo/aEK7Ey/images/4291436877-2017-02-01_13-04-33.jpg)

### User ###
Users and employees are one of the same, a user is just an employee with user access. Employees are linked to the assets and employee templates records.

Example of user view/new screen

![2017-02-01_13-33-36.jpg](https://bitbucket.org/repo/aEK7Ey/images/382843295-2017-02-01_13-33-36.jpg)

### Access ###
User access can be defined once menu and template is loaded to the platform. **The user access is stored in the access table**, note that user access is assigned to template and menu id and user id with a right (new,update,delete,view). This access is linked to user and the user is linked to token. The token is used to check if any action to rest api is allowed like view or deleting. Also keep in mind that this system has no session for the front end but token expiry date. 

![2017-02-01_13-28-02.jpg](https://bitbucket.org/repo/aEK7Ey/images/1871385246-2017-02-01_13-28-02.jpg) 

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
Keep in mind that all instances of this app will have full system admin to login in with.

Usr: fixxit
psw: fix!2

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

Oh no design flaw... If you read to this point I have 1st improvement for you to take on. The login/token auth is done over GET request this needs to be changed to post. Because sending passwords and usernames over open channel is lunacy!

### Who do I talk to? ###
Riaan Schoeman (riaan.schoeman@fixx.it)

![tumblr_mtxyuyRJZZ1s4xenlo1_400.gif](https://bitbucket.org/repo/aEK7Ey/images/1657564402-tumblr_mtxyuyRJZZ1s4xenlo1_400.gif)