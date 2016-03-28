import java.util.*;

/**
 * Implements the C_g operator from S5
 */
public class CommonKnow extends Formula implements GroupKnow {
    private Set<Integer> agents;
    private Formula formula;

    /**
     * Create a new C_agents formula
     * @param agents The agents in the group
     * @param formula The formula which we want to test for common knowledge
     */
    public CommonKnow(Set<Integer> agents, Formula formula) {
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
        Set<Integer> added = new HashSet<Integer>();
        LinkedList<Integer> todo = new LinkedList<>();
        if (!this.formula.evaluate(model, state)) {
            return false;
        }
        added.add(state);
        todo.push(state);
        while (!todo.isEmpty()) {
            int s = todo.pop();
            Set<Integer> reachable = model.getReachableStates(s, this.agents);
            for (Integer reach : reachable) {
                if (!added.contains(reach)) {
                    if (!this.formula.evaluate(model, reach)) {
                        return false;
                    }
                    todo.add(reach);
                    added.add(reach);
                }
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
        return "C_" + agents.toString() + " " + this.formula.toString();
    }
}
