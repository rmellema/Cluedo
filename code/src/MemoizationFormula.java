import java.util.HashMap;

/**
 * Created by rene on 28/03/16.
 */
public class MemoizationFormula extends Formula {
    private Formula form;
    private HashMap<Integer, Boolean> set;

    public MemoizationFormula(Formula form) {
        this.form = form;
        set = new HashMap<>();
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
        if (!set.containsKey(state)) {
            this.set.put(state, this.form.evaluate(model, state));
        }
        return this.set.get(state);
    }

    /**
     * Return a string representation of this Formula
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return "Mem{" + this.form.toString() + "}";
    }
}
