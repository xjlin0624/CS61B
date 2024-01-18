package deque;
import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertEquals;

public class MaxArrayDequeTest {

    class NumComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            int a = (int) o1;
            int b = (int) o2;
            if (a >= b) {
                return 1;
            }
            return -1;
        }
    }

    class OppNumComparator implements Comparator {  //Gets minimum
        @Override
        public int compare(Object o1, Object o2) {
            int a = (int) o1;
            int b = (int) o2;
            if (a <= b) {
                return 1;
            }
            return -1;
        }
    }

    @Test
    public void maxTest() {
        Comparator c = new NumComparator();

        MaxArrayDeque mad = new MaxArrayDeque(c);
        mad.addFirst(7);
        mad.addFirst(6);
        mad.addFirst(0); //0 6 7

        assertEquals(7, mad.max());

        Comparator o = new OppNumComparator();
        assertEquals(0, mad.max(o));
    }

}