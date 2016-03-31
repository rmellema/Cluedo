import java.util.HashMap;

/**
 * Created by rene on 28/03/16.
 */
public class MemoizationFormula extends Formula {
    private Formula form;
    private HashMap<Integer, Boolean> set;

    /**
     * Create a new MemoizationFormula for `form`. This object makes subsequent
     * evaluations of this formula in a given state faster
     * @param form The Formula to memoize
     */
    public MemoizationFormula(Formula form) {
        this.form = form;
        set = new HashMap<>();
    }

    /**
     * Evaluate a Formula in a state using the given model. If this formula
     * has already been evaluated in this state in A model before, return the
     * value of that evaluation
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
     * A memoized version of this formula. Since this formula is already
     * memoized, it returns this formula itself.
     * @return this
     */
    @Override
    public Formula memoize() {
        return this;
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
