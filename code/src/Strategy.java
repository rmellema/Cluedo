
import java.util.ArrayList;
import java.util.Random;

/**
* Superclass that implements strategies
* @author lauravdbraak
*/


public abstract class Strategy {
    /**
     * Creates an array of length num, with the index as value
     * @param num the length of the array made
     * @return the new array
     */
    protected int[] arrayTo(int num) {
        int[] ret = new int[num];
        for (int i = 0; i< num; i++) {
            ret[i] = i;
        }
        return ret;
    }

    /**
     * Shuffles an array
     * @param array the array to be shuffled
     * @return a shuffled array
     */
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