/**
 * Describes a propositional variable as a Card - playerID pair.
 * @author lhboulogne
 *
 */
public class PropVar extends Formula {

	private Card card;
	private int playerID;
	
	public PropVar(Card card, int playerID) {
		this.card = card;
		this.playerID = playerID;
	}

	public Card getCard() {
		return card;
	}

	public int getPlayer() {
		return playerID;
	}

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
		return "p(c(" + this.getCard().getCategory() + ", " +
                this.getCard().getNumber() + "), " + this.getPlayer() + ")";
	}
}
