#Paystation TDD

##Requirements
We were required to make a Paystation program following the rules of TDD. I believe we were able to use TDD appropriately. We just each worked on a bit of code, committed and pushed it, and then the other person pulled it and worked on a bit. We did this back and forth until we were finished with the program. We were able to make the program completely without missing any of the functions or having many issues along the way. We did alter a few things in the Paystation java file, such as adding final variables, but nothing drastic that changed the intended design of the program. We did not need to add any other test functions to make the program work. 

##Team

#####Paul Hutchings
I began with configuring the IntelliJ project and adding JUnit so that we could run the tests. I created a test, watched it fail, then pushed to github for Liam to implement. He then created a test for me to implement and we went back and forth until all of the tests were complete and had passed. I also created the initial blank readme file for Liam and I to complete.

#####Liam Miller
I wrote the code for the coin Hashmap that made it possible to hold and count the coins entered. I also added the "empty" function and tested it to make sure it cleared out the exact amount of money entered. In addition, I added the final variables for nickel, dime, and quarter to make the code a little bit easier to work with. I made sure the coins are initialized in the beginning and made sure they were never empty, however we reviewed this later and decided to change it up a bit to make it more efficient. I coded a function to make sure the program would return only one coin if one coin was entered. I also altered the cancel function to make sure it removes keys that have zero values and reset the coin map. I added a test function to make sure this works by checking to make sure coins that weren't entered were not returned. Finally I wrote a test to make sure the buy function would clear the coin map.


