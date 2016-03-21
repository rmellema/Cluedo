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
     * Evaluate a disjunction in a state using the given model
     *
     * @param model The model used for evaluation
     * @param state The state to evaluate the disjunction in
     * @return `true` if the disjunction holds in state `state`, `false` otherwise
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
     * Return a string representation of this disjunction
     *
     * @return String representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(this.getDisjuncts()[0]);
        for (int i = 1; i < this.getDisjuncts().length; i++) {
            sb.append(" | ");
            sb.append(this.getDisjuncts()[i]);
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Get the disjuncts in this disjunction
     * @return The disjuncts in this disjunction
     */
    public Formula[] getDisjuncts() {
        return this.formulas;
    }
}
