
import java.util.ArrayList;
import java.util.Random;

/**
* Class that implements strategies
* @author lauravdbraak
*/


public class Strategy {
    protected int[] arrayTo(int num) {
        int[] ret = new int[num];
        for (int i = 0; i< num; i++) {
            ret[i] = i;
        }
        return ret;
    }

    public int[] shuffle(int[] array) {
        Random rn = new Random();
        for (int i = array.length - 1; i> 0; i--) {
            int idx = rn.nextInt(i + 1);
            int a = array[idx];
            array[idx] = array[i];
            array[i] = a;
        }
        return array;
    }

}