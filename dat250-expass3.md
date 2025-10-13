Assignment 3.

The only technical problem i had with the setup of the frontend project was some issues with node, and its version.
Luckily, this was not a big issue and just needed an upgrade.

I struggled with this assignment, i enjoyed trying to play with styling and css and got abit carried away in that sense.
I managed to get the frontend and server to interact, when this happened i started to play with creating a user.
This is working, and a user can create an account, and login with registered email.
Some issues with login:
- my implementation makes it so that if you refresh the page, you are redirected to the login page
- When creating a poll, the poll will not be visible until user has logged in again.

I managed to get users to be able to create polls, with options. But this is where i struggeled abit.
I thought the voting system was hard to implement, and after trying and failing at implementing it i'm left with just some "vote"-buttons that does nothing.
Everything else beside the voting works, but that was where i didn't get it.

The relevant files for the backend can be found in the [Controller]("src/main/java/com/example/dat250_1/controller"), [Model]("src/main/java/com/example/dat250_1/model"), as well as the [Application](src/main/java/com/example/dat250_1/dat2501Application.java).
For the frontend, the relevant files are [Components]("app/components") and the [App]("app/src/App.tsx") itself.

