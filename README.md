# SafeBetChecker

SafeBetChecker is a program, whose purpose is to find a safe bet among several bookmakers. A safe bet consists when it is possible to bet on a match in a way that whatever the result of the match, you win.


## Getting Started

SafeBetChecker makes use of an API that provides the odds of the matches. Find this API documentation here 'https://oddsapi.docs.apiary.io/#'.

You must create an account and use the ApiKey of that account as an argument of this program.


### Input format

The program accepts arguments in the following format, the ApiKey as the first argument and the bookmakers you want to search odds on (put '*' if you want all bookmakers). For example:

java -jar ./SafeBetChecker.jar {apiKey} {bookmaker1} {bookmaker2} {bookmaker3} ... {bookmakerN}<br/>
or<br/>
java -jar ./SafeBetChecker.jar {apiKey} *<br/>

### Output format

In case safe bets are found:<br/>
SAFE BET FOUND:<br/>
LEAGUE: {League name}<br/>
MATCH: {teams competing}<br/>
MATCHTIME: {when the match starts}<br/>
{Information on how and what to bet}<br/>

Otherwise, when no safe bets are found, the output is empty.
