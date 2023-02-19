Authors: Jimmy Hypi, Mirko Mantovani, Matteo Marziali.

Welcome to the `Lorenzo il Magnifico` game by _**MJM**_ wiki!
In this wiki you will be able to find some general information and requirements for our project.

## INDEX
1. [How to start the application](#how-to-start-our-application)
2. [Fifth player solution](#fifth-player-solution)
3. [Game Customizability](#game-customizability)
4. [Specifications Covered](#specifications-covered)
5. [Server Implementation](#server-implementation)


# High level UML

![Image]({{site.url}}{{site.baseurl}}/assets/viewNetworkingPackagesFinalUML.png)


## How to start our application:
   In order to start our application you should follow these few upcoming steps:
* At first you should find the `it.polimi.ingsw.ps19.launchers` package where the launchers are stored
* You must run the **Server** before: select the `ServerLauncher` class from the previous package and run it.
* Now it's time for the **Client**: select the `ClientLauncher` class and run it
* You should be now asked to insert the mode connection and the User Interface, insert them and you'll be ready to play!

N.B. You can modify the max number of players by going to it.polimi.ingsw.ps19.constant.NetworkConstants and modifying the MAXPLAYERS constant up to 5 (to activate the fifth player). The server will enqueue the waiting players and it will wait a certaint time (taken from file) until all the players are connected or the timeout expires as requested by the specifications.

## Fifth Player Solution:
We decided to add a little "spicy flavour" to the game by adding the figure of Satan into the game: his goal is to punish other players by subtracting them victory points. He has 99 victory points, and once per round he decides which player to punish preventing them to overcome him.

# Game Customizability

**Customizability of development cards**
============

In our game, development cards have a limited customizability range based on the card Type:
  * **Territory cards**:
       * As all cards you can change id, name and period to the card.
       * They don't require any cost, so we don't take the cost into consideration for this type of card
         and so we don't allow a territory card with a cost.
       * Their effects (both immediate and harvest) can only consist of a resource chest where the amount of each resource  
         in the chest can be arbitrarily changed.
       * Each territory card has a Harvest Cost that can be changed as well
  * **Building cards**:
       * As all cards you can change id, name and period to the card.
       * A building card cost can be a certain amount of **coins**, **woods**, **stones** and **servants**.
       * Its immediate effect can be to instantly get **faith points** and/or **victory points**.
       * Arbitrary production activation cost.
       * Its permanent effect can be one of three main kinds:
            1. Get some **privilege councils**, **coins**, **military points** and **victory points**.
            2. Receive _n_ **coins** or **military points** for every card of a specific type _t_ in possession.
            3. Pay _n1_ of resource _r1_ and (optional) _n2_ of resource _r2_ and (optional) _n3_ of resource _r3_ to get 
               _n4_ of resource _r4_ and (optional) _n2_ of resource _r4_ (r3 and r4 can be **privilege councils**).

  * **Character cards**:
       * As all cards you can change id, name and period to the card.
       * They only cost **coins**, so we don't consider character cards with a different cost.
       * Their effects are various and we gave them the following customizability:
            * Get an amount of **military  points** or **faith points** or **council privileges**
            * Sum n to player's harvest / production values.
            * Sum n to player's action value when taking a card of a specific type t.
            * Take with a n action value a card of type t
            * Get a discount (and an alternative discount) of n on a certain resource of type t (and optionally on another 
              resource of type t)
            * Prevent from taking the action space bonus related to the third and the fourth floor of a tower
            * Take n **victory points**, foreach m **military points**
            * Take n **victory points**, foreach card of type t
            * Activate a Harvest or Production action with a value of n
  * **Venture cards**:
       * As all cards you can change id, name and period to the card.
       * A Venture card cost can be a certain amount of **coins**, **woods**, **stones** and **servants** and 
         **militaryPoints** if you have a specific **militaryPoints** required amount, also variable in the file.
       * Venture cards can have two alternatives cost, one of these "prices" is always based on **militaryPoint** and the 
         player should have the faculty to choose which cost he wants to pay. 
       * Its immediate effect can be to instantly get a various amount and type of all the in game resources: 
         **coin**,**wood**,**stone**,**servant**,**faithPoint**,**victoryPoint**,**militaryPoint**,**councilPrivilege**.
       * The immediate effect could also be to activate an instant production or harvest action with a certain value. 
       * Its permanent effect can be only to receive a variable amount of **victoryPoints**
 

# Board customizability
*  We covered all the requirements about the customizability of the board, so we use file (.txt format) data to store 
   and fetch all the data about Development Cards, Excomunication Cards, Action Space Bonuses, and all the other board 
   (except for the market) elements and moreover we take from file the timeout called for accessing a new 
   game or for finishing your turn too.


**Customizability of development cards**
============

In our game, development cards have a limited customizability range based on the card Type:
  * **Territory cards**:
       * As all cards you can change id, name and period to the card.
       * They don't require any cost, so we don't take the cost into consideration for this type of card
         and so we don't allow a territory card with a cost.
       * Their effects (both immediate and harvest) can only consist of a resource chest where the amount of each resource  
         in the chest can be arbitrarily changed.
       * Each territory card has a Harvest Cost that can be changed as well
  * **Building cards**:
       * As all cards you can change id, name and period to the card.
       * A building card cost can be a certain amount of **coins**, **woods**, **stones** and **servants**.
       * Its immediate effect can be to instantly get **faith points** and/or **victory points**.
       * Arbitrary production activation cost.
       * Its permanent effect can be one of three main kinds:
            1. Get some **privilege councils**, **coins**, **military points** and **victory points**.
            2. Receive _n_ **coins** or **military points** for every card of a specific type _t_ in possession.
            3. Pay _n1_ of resource _r1_ and (optional) _n2_ of resource _r2_ and (optional) _n3_ of resource _r3_ to get 
               _n4_ of resource _r4_ and (optional) _n2_ of resource _r4_ (r3 and r4 can be **privilege councils**).

  * **Character cards**:
       * As all cards you can change id, name and period to the card.
       * They only cost **coins**, so we don't consider character cards with a different cost.
       * Their effects are various and we gave them the following customizability:
            * Get an amount of **military  points** or **faith points** or **council privileges**
            * Sum n to player's harvest / production values.
            * Sum n to player's action value when taking a card of a specific type t.
            * Take with a n action value a card of type t
            * Get a discount (and an alternative discount) of n on a certain resource of type t (and optionally on another 
              resource of type t)
            * Prevent from taking the action space bonus related to the third and the fourth floor of a tower
            * Take n **victory points**, foreach m **military points**
            * Take n **victory points**, foreach card of type t
            * Activate a Harvest or Production action with a value of n
  * **Venture cards**:
       * As all cards you can change id, name and period to the card.
       * A Venture card cost can be a certain amount of **coins**, **woods**, **stones** and **servants** and 
         **militaryPoints** if you have a specific **militaryPoints** required amount, also variable in the file.
       * Venture cards can have two alternatives cost, one of these "prices" is always based on **militaryPoint** and the 
         player should have the faculty to choose which cost he wants to pay. 
       * Its immediate effect can be to instantly get a various amount and type of all the in game resources: 
         **coin**,**wood**,**stone**,**servant**,**faithPoint**,**victoryPoint**,**militaryPoint**,**councilPrivilege**.
       * The immediate effect could also be to activate an instant production or harvest action with a certain value. 
       * Its permanent effect can be only to receive a variable amount of **victoryPoints**
 
   
# Specifications covered

***
## Game specific requirements:
   We implemented the complete set of rules, including the advanced rules. Then we tried out our personal idea of a fifth 
   player and implemented it. So generally we should have covered all the game specific requirements.

## Game agnostic requirements:
   We used the MVC pattern to make a neater distributed system structure with one **server** handling many **clients**, 
   implementing both socket and rmi and being able to manage these two different technologies at the same time exploiting 
   Java functionalities. We then implemented the CLI and GUI in order to interact with the user, and this again is 
   completely transparent to the user who can freely choose one from the other before starting to play. About the optional 
   game-agnostic requirements we implemented the login based section, but unfortunately we didn't have the opportunity to 
   finish the part regarding the statistics so we didn't add this functionality.
   
## Server implementation
**Singleton pattern** with private constructor and getInstance() method to prevent any other instantiation of the server.
When created the Server starts the listening on both Socket port and RMI.

* **Observer pattern** to let the listeners notify the Server that a new client has connected passing the abstract Runnable object ClientHandler (implemented by concrete socket and RMI handlers) to the server.
* A ClientHandler is a Thread that is gonna handle a single Client for the entire game, filtering and allowing messages from the client and sending them over the right Network Interface.
* When reaching the [`NetworkConstants.MINPLAYERS`](https://github.com/mirkomantovani/ProjectMJM/blob/master/src/main/java/it/polimi/ingsw/ps19/constant/NetworkConstants.java) a timer starts, and if the timer expires or the number of clients connected reaches [`NetworkConstants.MAXPLAYERS`](https://github.com/mirkomantovani/ProjectMJM/blob/master/src/main/java/it/polimi/ingsw/ps19/constant/NetworkConstants.java) the match starts.
* ExecutorService Thread Pool used to Run MatchHandlers and a HashMap to store futures return by the executes, they’ll be needed to be able to stop the execution when the Match is finished.

## MatchHandler – the Controller - MVC
* The **MVC** controller of the game, given a set of ClientHandlers that it’s going to map to players, it acts based on users inputs notified by the ServerCommandHandler, the MatchHandler decides what to send to whom for every phase of the game.
* It modifies and calls methods of **model**, contained in a single object called Match, where all the game-needed data are ‘stored’.
* A timer Thread dictates the round time every player has to decide the action to perform, if the timer expires the player is automatically disconnected from the game and the other players are notified.
* The player can however reconnect and go on playing whenever he wants.
* **Model-view** interactions: the model notifies the view that the state has changed (e.g. [`PlayerStatusChangeCommand`](https://github.com/mirkomantovani/ProjectMJM/blob/master/src/main/java/it/polimi/ingsw/ps19/command/toclient/PlayerStatusChangeCommand.java), [`OpponentStatusChangeCommand`](https://github.com/mirkomantovani/ProjectMJM/blob/master/src/main/java/it/polimi/ingsw/ps19/command/toclient/OpponentStatusChangeCommand.java)
## Authentication - Reconnection
* A mandatory **authentication** is required to start playing.
* This is needed for both saving player stats and let users **reconnect** into the game demonstrating they were actually playing in that game.
*  A standard String hashCode() was used to make the authentication possible without having to save sensitive information. 
* A Thread saves updates the file containing Users information (played – won - lost matches, gameplay time) saving the on a File.


