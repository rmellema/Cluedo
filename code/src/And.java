import java.text.Normalizer;

/**
 * Implements the conjunction of two or more formulas
 */
public class And extends Formula {
    private Formula[] formulas;

    /**
     * Initialize a new conjunction
     * @param formulas The conjuncts of this conjunction
     */
    public And(Formula... formulas) {
        if (formulas.length <= 1) {
            throw new IllegalArgumentException("Conjunction should have at least 2 conjuncts");
        }
        this.formulas = formulas;
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
        for (Formula form : this.formulas) {
            if (!form.evaluate(model, state)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns an array of the conjuncts
     * @return Conjuncts in this conjunction
     */
    public Formula[] getConjuncts() {
        return this.formulas;
    }
}
