I had an issue during instalation with the dependencies of jakarta, more specifically jakarta.persistance. I solved this by adding a new dependency to gradle "jakarta.persistence:jakarta.persistence-api:3.2.0", this solved the import issue.

- [polls](src/main/java/com.example.dat250_1/jpa.polls)

- [poll test](src/test/java/com/example/dat250_1/PollsTest.java)

This assignment ended up being alot of trying and failing. I had difficulty managing the relationships between the classes. 
It made me miss working with straight databases, so im looking forward to mongoDB this week. 

I added the line ".property("hibernate.show_sql", "true")". This logs the statements being done "behind the scenes.
[Figure 1](images/sql-figure-1.png)
This figure is taken from the usertest part of the pollstest. 
It starts with dropping all tables if they exist and then creating the four tables. 
Then it creates the relationships between the tables with primary/foreign key references. 
It adds our users (alice, bob and eve), to the user table. 
Then it creates the poll, voteoption and votes. 
For this usertest in specific, we can see the last line of figure 1.
The actuall sql-statement being called is to simply select all the columns from the table user, where the user's name is "bob".
On the test-side, we simply check if the output from this statement actually equates to a result. 
