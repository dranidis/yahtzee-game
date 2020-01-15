[![Actions Status](https://github.com/dranidis/yahtzee/workflows/Java%20CI/badge.svg)](https://github.com/dranidis/yahtzee/actions)

# Yahtzee game

The rules of the Yahtzee game can be found at: https://www.hasbro.com/common/instruct/Yahtzee.pdf

The current implementation is on the terminal.
Players provide their names and play on the same
terminal. 

Bot players can be added. Random bot players have names starting with 'r' and bot players who use a greedy strategy for scoring (still keeping dice randomly) start with 'm'.

### How to install
```
mvn install
```

### How to execute

To run on the console.
```
java -jar target/yahtzee-1.0-SNAPSHOT.jar
```

To start a server at port 3000
```
java -jar target/yahtzee-1.0-SNAPSHOT.jar server 3000
```

To start a client connecting to some host (e.g. localhost) and to port 3000
```
java -jar target/yahtzee-1.0-SNAPSHOT.jar localhost 3000
```
