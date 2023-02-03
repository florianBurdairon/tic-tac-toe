# General informations

The game is a tic-tac-toe with possibility to choose the size and the dimension of the grid.

The game proposes multiple network mode :
- Local (PvP/AI)
- Host
- Client
- Server

The game have a save functionnality build-in.

# Requirements

- OS : Windows/Linux
- Java version : 17.0.6
- Maven version : 3.8.1

Network configuration : use port 9876

# Installation

Clone git repository :

``git clone https://github.com/florianBurdairon/tic-tac-toe.git``

Move into the git folder :

``cd tic-tac-toe``

Build the JAR file :

``mvn install``

Execute the JAR file

``java -jar .\target\Tic-Tac-Toe-1.0-jar-with-dependencies.jar [network mode]``

Network mode :
- ``local``
- ``host``
- ``client``
- ``server``