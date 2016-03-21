/**
 * Describes a propositional variable as a Card - playerID pair.
 * @author lhboulogne
 *
 */
public class PropVar extends Formula {

	private Card card;
	private int playerID;

	/**
	 * Create a new Propositional variable, mapping a card onto a player.
	 * @param card The card that should be hold by player `playerID`
	 * @param playerID The player that should hold `card`
     */
	public PropVar(Card card, int playerID) {
		this.card = card;
		this.playerID = playerID;
	}

    /**
     * Get the card this propositional variable is about.
     * @return The card
     */
	public Card getCard() {
		return card;
	}

    /**
     * Get the player that the propositional variable is about.
     * @return The player
     */
	public int getPlayer() {
		return playerID;
	}

    /**
     * Evaluate the propositional variable in `state` using `model`
     * @param model The model used for evaluation
     * @param state The state to evaluate the variable in
     * @return `true` if the propositional variable holds, `false` otherwise
     */
	@Override
    public boolean evaluate(KripkeModel model, int state) {
		try {
            return model.getDealing(state).isTrue(this);
        } catch (NullPointerException ex) {
            System.err.println("Trying to evaluate " + this.toString() +
                    " in state " + state + " which does not exist");
            return false;
        }
	}

	/**
	 * Return a string representation of this Formula
	 *
	 * @return String representation
	 */
	@Override
	public String toString() {
		if (this.getCard() == null) {
			System.out.println("Uhhh...");
		}
		return "p(c(" + this.getCard().getCategory() + ", " +
                this.getCard().getNumber() + "), " + this.getPlayer() + ")";
	}
}
