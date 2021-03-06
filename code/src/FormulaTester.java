import java.util.HashSet;

public class FormulaTester {
    static KripkeModel km;

    public static HashSet<Integer> set(int... agents) {
        HashSet<Integer> ret = new HashSet<>();
        for (int agent : agents) {
            ret.add(agent);
        }
        return ret;
    }

    public static void print(String name, Formula test) {
        String value = test.toString();
        String holds = Boolean.toString(test.evaluate(km));
        while (value.length() > 40) {
            for (int i = 39; i >= 0; i--) {
                if (value.charAt(i) == ' ') {
                    System.out.format("%-30s # %-40s# %5s\n", name,
                            value.substring(0, i), holds);
                    value = value.substring(i);
                    name = "";
                    holds = "";
                    break;
                }
            }
        }
        System.out.format("%-30s # %-40s# %5s\n", name, value, holds);
    }

    public static void init(int[][] array) {
        Dealing point = new Dealing(array);
        System.out.println("Point:");
        point.print();

        km = new KripkeModel(point, 4);
    }
    public static void init() {
        int[][] array = {{0, 1, 1, 2}, {0, 2, 3, 3, 4, 4}};
        init(array);
    }

    public static void main(String[] args) {
        init();
        System.out.println(new String(new char[80]).replace('\0', '='));
		PropVar p00 = new PropVar(new Card(0, 0), 0);
        PropVar p11 = new PropVar(new Card(0, 1), 1);
        PropVar p01 = new PropVar(new Card(0, 0), 1);
        Or test = new Or(new ImplicitKnow(set(1, 3), p00), new EveryKnows(set(1, 3), p11));
        print("Testing MultiEvery", test);
        print("", test.simplify());
        print("p00", p00);
        print("p11", p11);
        print("p01", p01);
        System.out.println(new String(new char[80]).replace('\0', '='));
        print("1 knows p11", new Know(1, p11));
        print("1 knows p00", new Know(1, p00));
        print("1 knows p00", new Neg(new Know(1, p00)));
        print("1 knows p00", new Know(1, new Neg(new Know(1, p00))));
        print("1 knows p01", new Know(1, p01));
        print("1 knows not p10", new Know(1, new Neg(new PropVar(new Card(0, 1), 0))));
        print("1 knows not p10", new Neg(new Know(1, new PropVar(new Card(0, 1), 0))));
        print("1 knows not p10", new Maybe(1, new PropVar(new Card(0, 1), 0)));
        print("1 maybes p11", new Maybe(1, p11));
        print("1 maybes p00", new Maybe(1, p00));
        print("1 maybes p01", new Maybe(1, p01));
        System.out.println(new String(new char[80]).replace('\0', '='));
        Card c00 = new Card(0, 0);
        Or cards = new Or(p00, new PropVar(c00, 1), new PropVar(c00, 2),
                new PropVar(c00, 3), new PropVar(c00, 4));
        print("Someone has card 0, 0", cards);
        print("It is known", new EveryKnows(set(1, 2, 3, 4), cards));
        print("You know nothing", new EveryKnows(set(1, 2, 3, 4), p00));
        print("Even a wise man", new ImplicitKnow(set(1, 2, 3, 4), p00));
        print("Doesn\'t know all", new ImplicitKnow(set(1, 2, 3, 4), p11));
        // Uncomment, run, and go watch a movie. It takes about that long
        //print("Some has card 0, 0", new CommonKnow(set(1,2,3,4), cards));
        print("The envelope is secret", new CommonKnow(set(1, 2, 3, 4), p00));
        System.out.println(new String(new char[80]).replace('\0', '='));
        //print("Public", new PublicAnnouncement(p00,
        //        new CommonKnow(set(1, 2, 3, 4), p00)));
//        print("Untrue announcement", new PublicAnnouncement(p01,
//                new And(p11, p11.negate())));
//        print("True announcement", new PublicAnnouncement(p00,
//                new And(p11, p11.negate())));
//        print("Private", new PrivateAnnouncement(set(1), p00,
//                new CommonKnow(set(1), p00)));
//        print("Private", new PrivateAnnouncement(set(1, 3), p00,
//                new CommonKnow(set(1, 3), p00)));
//        print("Private", new PrivateAnnouncement(set(1), p00,
//                new CommonKnow(set(1, 2, 3, 4), p00)));
//        print("Only one person has a card", new Or(new Neg(p00), new Neg(p01)));
    }
}
