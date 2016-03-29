/**
 * Created by rene on 29/03/16.
 */
public abstract class AbstractPlayer {
    private KripkeModel model;
    private int         number;

    /**
     * Creates a new AbstractPlayer. To be used to set the model and agent number
     * @param model The model this agent should use
     * @param number The number for this agent
     */
    public AbstractPlayer(KripkeModel model, int number) {
        this.model = model;
        this.number = number;
    }

    /**
     * Check to see if this agent knows a formula
     * @param formula The formula to check
     * @return `true` if the agent knows the formula, `false` otherwise
     */
    public boolean doesKnow(Formula formula) {
        return (new Know(this.number, formula)).evaluate(model);
    }

    /**
     * Check to see if this agent considers a formula possible
     * @param formula The formula to check
     * @return `true` if the agent considers this formula, `false` otherwise
     */
    public boolean doesConsider(Formula formula) {
        return (new Maybe(this.number, formula)).evaluate(model);
    }

    /**
     * The agent number for this agent
     * @return The agent number
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * The number of card categories this agent knows of
     * @return Number of card categories
     */
    public int getCardCategories() {
        return this.model.point().getCategories();
    }

    /**
     * The number of cards for a certain category this agent knows of
     * @param category The category to get the number of cards for
     * @return The number of cards in `category`
     */
    public int getCardsForCategory(int category) {
        return this.model.point().numberOfCards(category);
    }
}
