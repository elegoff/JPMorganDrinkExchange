# JPMorgan Drink Exchange

Java implementation of JPMorgan Drink Exchange test

Requires Java 1.8

How to use ?
-------------

Start application from command line :

    ./gradlew -q run

Please note that  first run might take some time, since dependencies are downloaded if not already present

It should provide you with a prompt :

    DrinkExchange>

As mentioned just above the prompt, you may get a list of available commands by typing *?list* (or even simply *?l*)

    DrinkExchange> ?list   
    abbrev	name	params
    pr	PE-ratio	(Stock symbol, price)
    bt	buy-trade	(Stock symbol, quantity, price)
    st	sell-trade	(Stock symbol, quantity, price)
    cvw	calculate-volume-weighted	(Stock symbol, past minutes)
    jasi	JPMDE-all-share-index	(past minutes)
    lcs	list-current-stocks	()
    lct	list-current-trades	()
    dy	dividend-yield	(Stock symbol, price)


Each command has a help :
e.g help for the *dy* command 

    DrinkExchange> ?h dy
    Command: dividend-yield
    Abbrev:  dy
    Params:  (Stock symbol, price)
    Description: Calculate dividend yield for a given stock symbol and a price
    Number of parameters: 2
    Stock symbol	String	A string identifying a stock symbol
    price	double	A number corresponding to the price



Some predefined Stocks are loaded at startup from a parsed Stocks.json file



    DrinkExchange> lcs
    TEA | COMMON | 0.0 | 0 | 100.0
    POP | COMMON | 8.0 | 0 | 100.0
    ALE | COMMON | 23.0 | 0 | 60.0
    GIN | PREFERRED | 8.0 | 2 | 100.0
    JOE | COMMON | 13.0 | 0 | 250.0

Each line represents a Stock in memory
On each line, columns represents : Symbol, Stock type, last Dividend, fixed Dividend, Par Value

 - Calculate a dividend yield :

e.g Dividend yield for stock ALE and price 20

    DrinkExchange> dy ALE 20
    Dividend yield is : 
    1.15

* Calculate P/E Ratio

e.g P/E Ratio for Stock GIN with price 20

    DrinkExchange> pr GIN 20
    P/E Ratio is :
    2.5

* Record a trade

e.g Buy a trade TEA with quantity 10 and price 5.20

    DrinkExchange> bt TEA 10 5.2
    Bought trade is :
    TEA | COMMON | 0.0 | 0 | 100.0
     |q= 10 | BUY |p= 5.2 | 2016-03-25 14:24:21:644

The output of a trade is in 2 lines
1st line is the string representation of the stock as described above
2nd line describes the trade : quantity, BUY or SELL, price, timestamp

- Selling trade GIN with quantity 12 and price 51.60

    DrinkExchange> st GIN 12 51.60
    Sold trade is :
    GIN | PREFERRED | 8.0 | 2 | 100.0
     |q= 12 | SELL |p= 51.6 | 2016-03-25 14:28:19:107

* List of trades in memory : just like you could list the various Stocks, you may want to list the current history of trades :

    DrinkExchange> lct
    TEA | COMMON | 0.0 | 0 | 100.0
    |q= 10 | BUY |p= 5.2 | 2016-03-25 14:24:21:644
    GIN | PREFERRED | 8.0 | 2 | 100.0
     |q= 12 | SELL |p= 51.6 | 2016-03-25 14:28:19:107

 
The output above represents the 2 previous trade we recorded

* Volume Weighted Stock Price based on trades in past 5 minutes

e.g for Stock TEA

    DrinkExchange> cvw TEA 5
    Volume Weighted Stock Price is : 
    5.2

Chosen duration could actually different from 5 minutes

e.g for the last minute, it will calculate 0.0

    DrinkExchange> cvw TEA 1
    Volume Weighted Stock Price is : 
    0.0


* JPMDE All Share Index

Just like the Volume Weighted Stock Price, we implemented a variable duration window

e.g JPMDE for the last 15 minutes

    DrinkExchange> jasi 15
    JPMDE All Share Index is :
    16.380476183554617

A console session can be exited via the *exit* keyword or *CTRL+C* keys


HOW TO RUN UNIT TESTS ?
-----------------------
The solution uses Junit
tests can be run with Gradle on command line :

    ./gradlew test

Junit Report can then be viewed by browsing the generated page in  ./build/reports/tests/index.html


EXTERNAL DEPENDENCIES
----------------------
The implementation relies on :

SL4J + logback for logging (http://www.slf4j.org/  , http://logback.qos.ch/)
Cliche shell for command line interactions (https://github.com/budhash/cliche)
Junit for the test driven approach (http://junit.org)
Gradle  (http://gradle.org) as a build tool
Akka for the Actor messaging (http://akka.io)
Json-simple (https://code.google.com/archive/p/json-simple/) for the Stocks.json file parsing

IMPLEMENTATION DETAILS :
------------------------
The '**com.jpmc.elegoff.Console**' class serves as the main class

Command Line interactions are described with annotated methods in
'**com.jpmc.elegoff.Commands**' class

Commands call respective static methods of the
'**com.jpmc.elegoff.Controller**' class
(as the 'C' in the 'MVC' paradigm)

This controller is responsible to initiate an Actor System with 2 actors
For each function the controller is called, it will send a specific message to the dedicated Actor

Actor Messages and Actors are defined in the 
'**com.jpmc.elegoff.actors**' package

**StockActor** handles Stock related messages (which are defined in **StockMsg** class) and keep track of the (non shared) list of **Stocks** 
**TradeActor** handles **Trade** related messages (which are defined in **TradeMsg** class) and keep track of the (non shared) list of Trades 

The reason to use an actor system to handle the different request is in case we imagine it in a production environment where we have to handle concurrent access. This is a computation model known as Actor Model
(https://en.wikipedia.org/wiki/Actor_model)
For example it prevents list of trades to be modified concurrently

Model objects like Trade and Stock are defined in the '**com.jpmc.elegoff.model**' package. We also used *enums* to define some constants

When it comes to calculations, a dedicated **Calculator** class gathers the static functions (with no side-effect)

The **StockHelper** class centralizes the query by symbol in the List of known Stocks (DRY principle , https://en.wikipedia.org/wiki/Don't_repeat_yourself) and is used for the initial loading of the Stock list

We defined a specific exception 
'**com.jpmc.elegoff.DrinkExchangeException**' which is caught and propagated in case something wrong happens and display the message in console

e.g 

    DrinkExchange> bt DUMMY 10 10
    Bought trade is : 
    Exception raised : Unknown stock with symbol : DUMMY
    DrinkExchange> [main] ERROR com.jpmc.elegoff.Commands - Exception raised : Unknown stock with symbol : DUMMY

The logback.xml defines logging configuration. For example when an exception is raised like above, a new error entry is logged in *drinkExchange.log* file


KNOWN LIMITATIONS :
-------------------

The item below would basically serve as a *TODO* list in case we had more time to implement the solution



- The List of Stocks is currently loaded at startup and it was not specified that if would have to manage insertion of deletions in this list

- The timeout for actions (1 second) is hard-coded and would preferably be defined in a configuration file in case the user would like to change it.

- The implementation does not prevent selling Stocks we did not previously buy, not does it check if traded quantities are consistent.





