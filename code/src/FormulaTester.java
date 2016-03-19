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
        while (value.length() > 50) {
            for (int i = 49; i >= 0; i--) {
                if (value.charAt(i) == ' ') {
                    System.out.format("%-20s [ %-50s] %5s\n", name,
                            value.substring(0, i), holds);
                    value = value.substring(i);
                    name = "";
                    holds = "";
                    break;
                }
            }
        }
        System.out.format("%-20s [ %-50s] %5s\n", name, value, holds);
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
        print("p00", p00);
        print("p11", p11);
        print("p01", p01);
        System.out.println(new String(new char[80]).replace('\0', '='));
        print("1 knows p11", new Know(1, p11));
        print("1 knows p00", new Know(1, p00));
        print("1 knows p01", new Know(1, p01));
        print("1 maybes p11", new Maybe(1, p11));
        print("1 maybes p00", new Maybe(1, p00));
        print("1 maybes p01", new Maybe(1, p01));
        System.out.println(new String(new char[80]).replace('\0', '='));
        Card c00 = new Card(0, 0);
        Or cards = new Or(p00, new PropVar(c00, 1), new PropVar(c00, 2), new PropVar(c00, 3), new PropVar(c00, 4));
        print("Some has card 0, 0", cards);
        print("It is known", new EveryKnows(set(1, 2, 3, 4), cards));
        print("You know nothing", new EveryKnows(set(1, 2, 3, 4), p00));
        print("Even a wise man", new ImplicitKnow(set(1, 2, 3, 4), p00));
        print("Doesn\'t know all", new ImplicitKnow(set(1, 2, 3, 4), p11));
        // Uncomment, run, and go watch a movie. It takes about that long
        //print("Some has card 0, 0", new CommonKnow(set(1,2,3,4), cards));

    	//TODO Test the relation query functions
    }
}
