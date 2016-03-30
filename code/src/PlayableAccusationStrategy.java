import javax.swing.JOptionPane;


public class PlayableAccusationStrategy extends AccusationStrategy {
	public PlayableAccusationStrategy(String strategy) {
		super(strategy);
	}

	@Override 
	public CardSet strategy(Player agent){
        if(manualInput(agent.getNumber())){
            return manualStrategy(agent);
        }
        return super.strategy(agent);
    }

	private boolean manualInput(int agent) {
		int selectedOption = JOptionPane.showConfirmDialog(null, 
                "Do you want to do an accusation for agent #"+ agent +"?", 
                "It's your turn!", 
                JOptionPane.YES_NO_OPTION); 
		if (selectedOption == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}


	private CardSet manualStrategy(Player agent) {
		// TODO Auto-generated method stub
		return null;
	}
}
