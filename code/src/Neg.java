/**
 * Implements the negation of a formula
 */
public class Neg extends Formula{
    private Formula formula;

    /**
     * Negates a formula
     * @param form The formula to negate
     */
    public Neg(Formula form) {
        this.formula = form;
    }

    @Override
    public boolean evaluate(KripkeModel model, int state) {
        return !this.formula.evaluate(model, state);
    }

    /**
     * Negate a formula, e.g. remove the not
     * @return The formula that this object negates
     */
    @Override
    public Formula negate() {
        return this.formula;
    }

    @Override
    public Formula simplify() {
        if (this.formula instanceof Neg) {
            return this.formula.negate();
        }
        return this;
    }
}
