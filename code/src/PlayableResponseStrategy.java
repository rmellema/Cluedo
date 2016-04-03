import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;


public class PlayableResponseStrategy extends ResponseStrategy {
	public PlayableResponseStrategy(String strategy) {
		super(strategy);
	}

	/**
	 * If there are multiple responses possible, the user is asked whether he/she wants to play this strategy manually. If so, a GUI pops up in which the user can specify a response. Otherwise, the response is obtained by calling the strategy function of the ResponseStrategy class.
	 * @param agent The agent that plays this strategy
	 * @return A card that represents the response if an the agent can respond. Null if this is not possible. 
	 */
	@Override 
	public Card strategy(Player agent, CardSet query, CardSet hand, int other){
		// Follow the rules: 
		int counter = 0, i;
        ArrayList<Card> found = new ArrayList<>();
        
        for(i = 0; i < query.size(); i++){
            if (hand.contains(query.getCard(i))){
                System.out.println("Have card: " + query.getCard(i).toString());
                counter++;
                found.add(query.getCard(i));
            }
        }

        /* 
        If a player has only one of the cards queried, return that one
        else return null.
        */
        if (counter == 1){
            return found.get(0);
        } else if (counter == 0) {
            return null;
        }
		
        // Choose:
		if(manualInput(agent.getNumber())){
            return manualStrategy(agent, found, other, query, hand);
        }
        return super.strategy(agent, query, hand, other);
    }

	private boolean manualInput(int agent) {
		int selectedOption = JOptionPane.showConfirmDialog(null, 
                "Do you want to manipulate the response of agent #"+ agent +"?", 
                "It's your turn!", 
                JOptionPane.YES_NO_OPTION); 
		if (selectedOption == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}


	private Card manualStrategy(Player agent, ArrayList<Card> found, int other, CardSet query, CardSet hand) {
		String[] showAbles = new String[found.size()];
		for (int idx = 0; idx != found.size(); ++idx) {
			showAbles[idx] = found.get(idx).toString();
		}
		int response = JOptionPane.showOptionDialog(null, 
				"What card should agent #" + agent.getNumber() + " show to agent #" + other + "?", 
				"It's your turn!",
			    JOptionPane.DEFAULT_OPTION, 
			    JOptionPane.QUESTION_MESSAGE,
			    null, showAbles, showAbles[0]);
		
		if (response < -1)
			return super.strategy(agent, query, hand, other);
		return found.get(response);
	}
}
