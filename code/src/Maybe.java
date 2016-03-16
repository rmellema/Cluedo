/**
 * Implements the M_i operator for S5
 */
public class Maybe extends Formula {
    private int agent;
    private Formula formula;

    public Maybe(int agent, Formula formula) {
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
        return false;
    }
}
