import java.util.HashSet;

/**
 * Implements the E_g operator from S5
 */
public class EveryKnows extends Formula {
    private HashSet<Integer> agents;
    private Formula formula;

    /**
     * Create a new formula E_agents formula
     * @param agents The agents in the group
     * @param formula The formula which they all should know
     */
    public EveryKnows(HashSet<Integer> agents, Formula formula) {
        this.agents = agents;
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
