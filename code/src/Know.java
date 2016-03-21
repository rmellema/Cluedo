import java.util.Set;

/**
 * Implements the K_i operator for S5
 */
public class Know extends Formula {
    private int agent;
    private Formula formula;

    /**
     * Create a new K-formula
     * @param agent The agent to test the knowledge of
     * @param formula The formula of which we want to test if the agent knows it
     */
    public Know(int agent, Formula formula) {
        this.agent = agent;
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
        Set<Integer> states = model.getReachableStates(state, this.agent);
        for (Integer s : states) {
            if (!this.formula.evaluate(model, s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return a string representation of this Formula
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return "K_" + this.agent + " " + this.formula.toString();
    }
}
