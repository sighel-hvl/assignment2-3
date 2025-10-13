##### Assignment 6.
I wanted to continue to play with redis. So I chose to use pubsub to implement messaging. 
I had to use some time to read the documentation of redis messaging. 
More particularly; the fact that a user can subscribe to multiple channels by denoting them. I want my user, the controller, to subscribe to them all. 
So my controller uses the pattern poll:*:vote to subscribe to any poll and any votes.

I created small standalone application to represent a user subscribing to a poll of choice.
Throughout this semester of DAT250 tasks i have updated and implemented more IntellijHttp tests, and this is a nice way to initialize the database with users, polls and votes.
I run the tests, and then run the subscriber. The subscriber is able to get changes that happens to the subscribed poll. 