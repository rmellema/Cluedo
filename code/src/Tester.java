public class Tester {
    public static void main(String[] args)
    {

    	int[][] array = {{0, 1, 1, 2}, {0, 2, 3, 3, 4, 4}}; // For 4 players
    	//int[][] array = {{0, 1, 1, 1}, {0, 2, 2, 2, 3, 3}}; // For 3 players
    	Dealing point = new Dealing(array);
    	System.out.println("Point:");
    	point.print();

    	KripkeModel km = new KripkeModel(point, 4);
    	 
    	//TODO Test the relation query functions
    }
}
