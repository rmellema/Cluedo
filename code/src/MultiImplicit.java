import java.util.Set;

/**
 * A class to make it faster to evaluate disjunctions or conjunctions of
 * Implicit Knowledge for the same group of agents by evaluating multiple
 * formulas at the same time
 */
public class MultiImplicit extends Formula {
    private Set<Integer> agents;
    private Formula[]    formulas;
    private boolean      conjunction;

    public MultiImplicit(Set<Integer> agents, boolean conjunction, Formula... forms) {
        this.agents = agents;
        this.formulas = forms;
        this.conjunction = conjunction;
    }

    /**
     * Evaluate a Formula in a state using the given model
     *
     * @param model The model used for evaluation
     * @param state The state to evaluate the Formula in
     * @return `true` if the Formula holds in state `state`, `false` otherwise
     */
    @Override
    public boolean evaluate(KripkeModel model, int state) {
        boolean[] holds = new boolean[this.formulas.length];
        boolean ret = true;
        for (int i = 0; i < holds.length; i++) {
            holds[i] = true;
        }
        Set<Integer> states = model.getReachableStatesForAll(state, this.agents);
        for (int s : states) {
            for (int i = 0; i < holds.length; i ++) {
                holds[i] = holds[i] && this.formulas[i].evaluate(model, s);
            }
            if (this.conjunction) {
                ret = true;
                for (boolean hold : holds) {
                    ret = ret && hold;
                }
            } else {
                ret = false;
                for (boolean hold : holds) {
                    ret = ret || hold;
                }
            }
            if (!ret) {
                return false;
            }
        }
        return ret;
    }

    /**
     * Return a string representation of this Formula
     *
     * @return String representation
     */
    @Override
    public String toString() {
        Formula[] forms = new Formula[this.formulas.length];
        for (int i = 0; i < this.formulas.length; i++) {
            forms[i] = new ImplicitKnow(this.agents, this.formulas[i]);
        }
        if (this.conjunction) {
            return "Multi" + (new And(forms)).toString();
        } else {
            return "Multi" + (new Or(forms)).toString();
        }
    }
}
