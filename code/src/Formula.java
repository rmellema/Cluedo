/**
 * Superclass for all Formulas
 */
public abstract class Formula {

    /**
     * Evaluate a Formula in a state using the given model
     * @param model The model used for evaluation
     * @param state The state to evaluate the Formula in
     * @return `true` if the Formula holds in state `state`, `false` otherwise
     */
    public abstract boolean evaluate(KripkeModel model, int state);

    /**
     * Evaluate a Formula in state 0 in the given model. State 0 is the true
     * state.
     * @param model The model used for evaluation
     * @return `true` if the Formula evaluate in state 0, `false` otherwise
     */
    public boolean evaluate(KripkeModel model) {
        return this.evaluate(model, 0);
    }

    /**
     * Negate a formula. Preferred way of creating negations
     * @return The negation of a formula
     */
    public Formula negate() {
        return new Neg(this);
    }

    /**
     * Simplify a Formula for easier evaluation
     * @return A logically equivalent, possibly simplified formula
     */
    public Formula simplify() {
        return this;
    }

    /**
     * Make a formula remember its evaluation in a specific state. Do not
     * use if you work with different models
     * @return The memoized version of the formula
     */
    public Formula memoize() {
        return new MemoizationFormula(this);
    }

    /**
     * Return an implication. Since there is no implies class, this convenience
     * method was added.
     * @param premises The premises of the implication
     * @param conclusion The conclusion of the implication
     * @return The new implication
     */
    public static Formula implies(Formula premises, Formula conclusion) {
        return new Or(premises.negate(), conclusion);
    }

    /**
     * Return a string representation of this Formula
     * @return String representation
     */
    abstract public String toString();
}
