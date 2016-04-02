/**
 * Formula to evaluate the Public Announcement operator from PAC
 * @author rmellema
 */
public class PublicAnnouncement extends Formula {
    private Formula announcement;
    private Formula formula;

    /**
     * Create a new public announcement
     * @param announcement The announcement that is made
     * @param formula The formula which we want to test in the new model
     */
    public PublicAnnouncement(Formula announcement, Formula formula) {
        this.announcement = announcement;
        this.formula = formula;
    }

    /**
     * Evaluate a Formula in a state using the given model
     *
     * @param model The model used for evaluation
     * @param state The state to evaluate the Formula in
     * @return `true` if the Formula evaluate in state `state`, `false` otherwise
     */
    @Override
    public boolean evaluate(KripkeModel model, int state) {
        KripkeModel copy = new KripkeModel(model);
        copy.publicAnnouncement(this.announcement);
        if (copy.hasState(state))
            return this.formula.evaluate(copy, state);
        else
            return true; //State is gone, so true by default
    }

    /**
     * Return a string representation of this Formula
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return "[" + this.announcement.toString() + "] " +
                this.formula.toString();
    }
}
