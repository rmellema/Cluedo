import java.util.HashSet;

public class KripkeModel {
	private HashSet<Integer> stateNumbers = new HashSet<Integer>();
	private StateDealingMap sdMap;
	private RelationMatrix relations;
	
	public KripkeModel(Dealing dealing, int players) {
		sdMap = new StateDealingMap(dealing, players);
		System.out.println("Total nr of states: " + sdMap.size());
		
		for (int stateNumber = 0; stateNumber != sdMap.size(); ++stateNumber) 
			stateNumbers.add(stateNumber);
		relations = new RelationMatrix(sdMap.size());
	}
	
	public final Dealing point() {
		return sdMap.point();
	}

	public Dealing getDealing(int state) {
        return sdMap.getValuation(state);
    }

}
