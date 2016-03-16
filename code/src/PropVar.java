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
		return model.getDealing(state).isTrue(this);
	}
}
