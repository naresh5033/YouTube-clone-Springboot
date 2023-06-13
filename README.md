# youtube-clone-project

This repository contains source code for the Spring Boot Angular Full Stack - Youtube Clone Project
- its a Angular project with Spring Boot for the backend.
- The Front end and the backend are the two separate modules (using maven) Frontend and backend
- The Backend is 3 layer Arch .. Controller --> Service --> Persistence -->DB
- the front end will communicate with the server through REST API(Http) 
- And Mongo DB for the database

<image src = "./images/MongoDb schema.png"></image>

<image src = "./images/High level Arch.png"></image>
- And the video files and thumbnails will be uploaded to AWS S3
- The Authorization will be done through Auth0 Server(we can also use other Auth server like Aws Congnito or Azure AD or our own Auth server like Keycloak) .
# Functional Requirements

- user can upload the video file
- user can upload the thumbnail for the video
- user can view the video
- user can like/dislike video
- user can subscribe to another user to receive notifications about the future video
- user can login/logout using single sign-on
- user can comment on the video
- user can view the history of videos that he watched
- user can view the list of videos that he liked
## Dependencies

- Angular ```ng new youtube-clone-ui --directory```
- Angular Material ```ng add @angular/material``` for the ui/ and the icons, cards.
- front end maven plugin - for building angular proj using maven (copy the build from spring boot and put it in pom)
- Spring Web
- Spring Mongdb
- spring cloud Aws to interact with the Aws 
- spring boot auto configuration to configure spring cloud aws (auto configure)
- Lombok for boilerplate
- ngx-file-drop ```npm i npx-file-drop -D``` is an ang module, has the file drag and drop ui
- Anglar flex layout --```npm i -s @angular/flex-layou @angular/cdk``` ui layout for the angular using flex layout and responsive api.
- ngx-videogular -- ```npm i @videogular/ngx-videogular``` ```npm i @types/core-js --save``` is a video player for angula. this lib also has the icon to font (styles) for the player.
## Video upload
- the way that we managed to upload the file from the user to the s3
- firs we ve to enable the endpt where the user can upload the file to the s3
- the controller (server) will make a REST api call to, and then it delegates the logic to upload the file to the service(class will mainly deals with processsing the file)
- and then it forward to the S3
- the service layer will also ve the repository which stores the metadata about the video files. and the repository can talks to our mongo Db
## Auth0

- for the security/authentication
- our angular client and the backend (spring boot client) will communicate through the Auth0 service.
- our ang client will use auth0 to req new tokens, these tokens are sent to spring boot REST api.
- And our spring boot rest api will validate the the tokens.
- the auth0 domain id is dev-w2xkb2uincv1wiyp.us.auth0.com/
- so the backend will get the tokens and verify them 
- the backend will understand which service it has to make a call is by append the discovery document(/well-known/openid-configuration) link
- the discovery document consists of all the information (about diff endpoint)
- we can use the post to send the post req with the header (give our auth0 --data ), and send it to the https://dev-w2xkb2uincv1wiyp.us.auth0.com/oauth/token 
- that's how we will get the access token 
- now we can make the req to access the video files, with the authorization(as bearer token) and paste the token
- this is how we get the info about the video files with oAuth 

- now from our front end to the backend, ve to enable the authentication.
- we can use http interceptor in our front end, so whenever we make a req to the backend. it will automatically add the token to the http req.
- so in this way we don't ve to repeat the code to get the access token.
- to do that we ve to add the url(which route we wana secure) and the audience (as cust param Auth req) and then define the http interceptor in the providers[] in our app.module.ts

## TODO 
- Implement the home page component
  - create home page component
  - create a featured component (whenever we re in the home page, this feature comp will display) containing all the featured/ suggested video
  - create history component
  - create subscription component
  - create liked video component
  - create sidebar component
  - introduce child routing inside the home page component
  - create video card component
  - display video card in home, history, subscription and liked video components
  - 