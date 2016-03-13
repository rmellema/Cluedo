import java.util.Arrays;


public class RelationMatrix {
	private char[][] jaggedMatrix;
	
	public RelationMatrix(int size) {
		jaggedMatrix = new char[size][];
		for (int it = 0; it != size; ++it){
			System.out.println(it);
			jaggedMatrix[it] = new char[it];
			Arrays.fill(jaggedMatrix[it], (char)1);
		}
	}

}
