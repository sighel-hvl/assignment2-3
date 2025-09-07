### dat250-expass2
The installation was ok, no real issues occured. 
The application seems to work ok.

I ended up making all the tests in intelliJ's http client, and found that to be very helpful:
- Ensure that your path works and you get a 200 code.
- Quick way to check if you have broken something during implementation of features.
- Easy to understand.

PollManager.java is the controller for the project and uses hashmaps and the model classes to handle the required operations.
All the routes are created in the application "dat2501Application.java".
Swagger-ui and openapi is implemented, for now the application is managed there through "http://localhost:8080/swagger-ui.html".

*Sigvard Helsingen, DAT250, H25.* 