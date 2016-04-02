public class CardTest {
    public static void main(String[] args) {
        Card c1 = new Card(1, 1);
        Card c2 = new Card(1, 1);
        Card c3 = new Card(2, 1);
        Card c4 = new Card(1, 2);
        System.out.println(c1.equals(c2));
        System.out.println(c1.equals(c3));
        System.out.println(c1.equals(c4));
        System.out.println(c2.equals(c3));
        System.out.println(c2.equals(c4));
        System.out.println(c3.equals(c4));
        CardSet set = new CardSet(c1, c3);
        System.out.println(set.contains(c1));
        System.out.println(set.contains(c2));
        System.out.println(set.contains(c4));

    }
}
