import java.text.Normalizer;

/**
 * Implements the disjunction of two or more Formulas
 */
public class Or extends Formula {
    private Formula[] formulas;

    /**
     * Initialize a disjunction of two or more disjuncts
     * @param formulas The disjuncts of the disjunction
     */
    public Or(Formula... formulas) {
        if (formulas.length <= 1) {
            throw new IllegalArgumentException("Disjunction should have at least 2 disjuncts");
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
        for (Formula form : formulas) {
            if (form.evaluate(model, state)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the disjuncts in this disjunction
     * @return The disjuncts in this disjunction
     */
    public Formula[] getDisjuncts() {
        return this.formulas;
    }
}
