
public class Tester {
    public static void main(String[] args)
    {
    	int[][] array = {{0, 1, 1, 2}, {0, 2, 3, 3, 4, 4}};
    	Dealing point = new Dealing(array);
    	
    	KripkeModel km = new KripkeModel(point, 4);
    	
    	km.point().print();
    }
}
