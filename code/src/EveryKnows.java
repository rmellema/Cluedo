import java.util.Set;

/**
 * Implements the E_g operator from S5
 */
public class EveryKnows extends Formula implements GroupKnow {
    private Set<Integer> agents;
    private Formula formula;

    /**
     * Create a new E_agents formula
     * @param agents The agents in the group
     * @param formula The formula which we want to test for general knowledge
     */
    public EveryKnows(Set<Integer> agents, Formula formula) {
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
        Set<Integer> states = model.getReachableStates(state, this.agents);
        for (Integer s : states) {
            if (!this.formula.evaluate(model, s)) {
                return false;
            }
        }
        return true;
    }

    public Formula getFormula() {
        return this.formula;
    }

    public Set<Integer> getAgents() {
        return this.agents;
    }

    /**
     * Return a string representation of this Formula
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return "E_" + agents.toString() + " " + this.formula.toString();
    }
}
