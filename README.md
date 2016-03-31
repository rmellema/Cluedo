# Running the program
The file 'cluedo.jar' can be run by entering `java -jar cluedo.jar` in the command line.

# Using the program
When the program starts, some buttons appear in the bottom of the window. The functions of these buttons are explained here.

### New game
This button starts a new game of our simplified version of Cluedo. The button **Default settings** initialises the game with 4 agents that all use the default strategy and a default card dealing. The button **Random settings** initialises the game with 4 agents that all use the default strategy and a random card dealing. The button **Done** can be pressed after specifying costum game settings for all tabs. These tabs and the way they have to be filled in are described here.

Note that the **Apply** button has to be pressed to save the input of a tab that has been filled in.

##### Agents
In this tab you can specify the number of agents that will be playing. A minimal number of two agents is needed to play a game. A large number of agents (larger than about 4) in combination with a large number of categories and cards can cause an lengthen the duration of turns in the game significantly.

##### Categories
Here the number of card categories, e.g. weapon in the original game, can be specified. Each category needs to be initialised with at least one card. A large number of categories (larger than about 3) in combination with a large number of cards and agents can cause an lengthen the duration of turns in the game significantly.

##### Cards
Define the number of cards for each card category here. Again, a large number of cards can cause turns to take longer.

##### Envelope
Indicate here for each category which card will be put into the envelope at the beginning of the game.

##### Dealing
Specify here for every agent which cards he/she receives at the beginning of the game. Cards that will be put into the envelope cannot be dealt to any agent. 

Note that the number of cards each agent receives is fixed to simulate the clockwise dealing that is in the rules of the original Cluedo. When an incorrect dealing is applied, the dealing is not saved and the user is notified.

##### Strategies
The strategies that the agents play have to be filled in here. An agent has different strategies for the different situations it gets into during the game. These situations are: Making a suspicion, responding to the suspicions of other players and making an accusation. The strategies for these situations are described in the [Strategies](http://rmellema.github.io/Cluedo/strategies.html) tab on our website. 

When for a specific agent, a checkbox behind a specific situation in the **Manipulate** collumn is selected, the user will get the option to manipulate the actions of that agent when it gets into that situation. 

### Restart
This button can be used to start a game with the same settings as the game that is currently being played or has just been finished.

### Step
When this button is pressed, the next agent makes a suspicion and the other players respond to it. Note that the respond strategy can only be manipulated when an agent has multiple was to respond to a suspicion. When an agent has shown a card to the agent making the suspicion or no agent was able to respond, the agent making the suspicion has the possibility of making an accusation. When an agent gets an accusation wrong, it is no longer allowed to make suspicions or accusations.

### Round
This button performs steps until it is the turn of agent #1 to make a suspicion.

### Game
Pressing this button causes the game to perform rounds until the game is over. 

### Clear output
This button clears the output text panel. 


