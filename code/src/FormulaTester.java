public class FormulaTester {
    static KripkeModel km;
    public static void print(String name, Formula test) {
        System.out.format("%-20s%-54s|%-5s\n", name, test.toString(), test.evaluate(km));

    }

    public static void main(String[] args)
    {

    	int[][] array = {{0, 1, 1, 2}, {0, 2, 3, 3, 4, 4}}; // For 4 players
    	//int[][] array = {{0, 1, 1, 1}, {0, 2, 2, 2, 3, 3}}; // For 3 players
    	Dealing point = new Dealing(array);
    	System.out.println("Point:");
    	point.print();

    	km = new KripkeModel(point, 4);
		PropVar p00 = new PropVar(new Card(0, 0), 0);
        PropVar p11 = new PropVar(new Card(0, 1), 1);
        PropVar p01 = new PropVar(new Card(0, 0), 1);
        print("p00", p00);
        print("p11", p11);
        print("p01", p01);

    	//TODO Test the relation query functions
    }
}
