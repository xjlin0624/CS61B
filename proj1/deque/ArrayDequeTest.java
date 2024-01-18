package deque;

import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic array deque tests. */

public class ArrayDequeTest {
    /**
     * You MUST use the variable below for all of your tests. If you test
     * using a local variable, and not this static variable below, the
     * autograder will not grade that test. If you would like to test
     * ArrayDeques with types other than Integer (and you should),
     * you can define a new local variable. However, the autograder will
     * not grade that test.
     */

    public static Deque<Integer> ad = new ArrayDeque<Integer>();

    @Test
    /** Adds a few things to the list, checks that isEmpty() is correct.
     * This is one simple test to remind you how junit tests work. You
     * should write more tests of your own.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {
        assertTrue("A newly initialized deque should be empty", ad.isEmpty());
        ad.addFirst(0);

        assertFalse("ad should now contain 1 item", ad.isEmpty());

        // Reset the linked list deque at the END of the test.
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void addFirstTest() {
        ad.addFirst(7); // ad should be 7
        assertEquals(1, ad.size());
        assertEquals(7, (int) ad.get(0));

        ad.addFirst(3); // now ad should be 3, 7
        assertEquals(2, ad.size());
        assertEquals(3, (int) ad.get(0));

        ad.addFirst(1); // now ad should be 1, 3, 7
        assertEquals(3, ad.size());
        assertEquals(1, (int) ad.get(0));

        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void removeFirstTest() {
        ad.addFirst(7);
        ad.addFirst(3);
        ad.addFirst(1);  // now ad should be 1, 3, 7

        assertEquals(1, (int) ad.removeFirst()); // ad should be 3, 7
        assertEquals(2, ad.size());
        assertEquals(3, (int) ad.get(0));

        assertEquals(3, (int) ad.removeFirst()); // ad should be 7
        assertEquals(7, (int) ad.removeFirst()); // ad should be empty
        assertEquals(0, ad.size());
        assertNull(ad.get(0));
        assertTrue("ad should be empty", ad.isEmpty());

        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void addLastTest() {
        ad.addLast(7); // ad should be 7
        assertEquals(1, ad.size());
        assertEquals(7, (int) ad.get(0));

        ad.addLast(3); // ad should be 7, 3
        assertEquals(2, ad.size());
        assertEquals(3, (int) ad.get(1));

        ad.addLast(1); // ad should be 7, 3, 1
        assertEquals(3, ad.size());
        assertEquals(1, (int) ad.get(2));

        ad = new ArrayDeque<Integer>();

        for (int i = 0; i < 7; i++) {
            ad.addLast(i);
        }   // |  | 0 | 1 | 2 | 3 | 4 | 5 | 6 |
        assertEquals(7, ad.size());
        assertEquals(2, (int) ad.get(2));

        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void removeLastTest() {
        ad.addLast(7);
        ad.addLast(3);
        ad.addLast(1); // ad should be 7, 3, 1

        assertEquals(1, (int) ad.removeLast()); // ad should be 7, 3
        assertEquals(2, ad.size());
        assertEquals(3, (int) ad.get(1));

        ad.removeLast(); // ad should be 7
        ad.removeLast(); // ad should be empty
        assertEquals(0, ad.size());
        assertNull(ad.get(0));
        assertTrue("ad should be empty", ad.isEmpty());

        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void addSpeedTest() {
        int N = 100000;
        for (int i = 0; i < N; i++) {
            ad.addLast(i);
        }
        for (int i = 0; i < N; i++) {
            ad.addLast(ad.get(i));
        }

        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void removeSpeedTest() {
        int N = 100000;
        for (int i = 0; i < N; i++) {
            ad.addLast(i);
        }
        for (int i = 0; i < N; i++) {
            ad.addLast(ad.get(i));
        }

        for (int i = 0; i < 2 * N; i++) {
            ad.removeFirst();
        }

        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void sizeTest() {
        ad.addFirst(2);
        ad.addLast(5);
        ad.removeFirst();
        ad.addFirst(2);
        ad.addFirst(3); // ad: 3, 2, 5
        assertEquals(3, ad.size());

        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void printDequeTest() {
        ad.addFirst(2);
        ad.addLast(5);
        ad.removeFirst();
        ad.addFirst(2);
        ad.addFirst(3); // ad: 3, 2, 5
        String m = "3 2 5\n";  // ad: 3, 2, 5
        System.out.print(m);
        ad.printDeque();

        ad.removeLast(); // ad: 3, 2
        m = "3 2\n";
        System.out.print(m);
        ad.printDeque();

        ad.removeLast(); // ad: 3
        m = "3\n";
        System.out.print(m);
        ad.printDeque();

        ad.removeLast(); // ad:
        m = "\n";
        System.out.println(m);
        ad.printDeque();

        ad = new ArrayDeque<Integer>();
    }


    @Test
    public void getTest() {
        ad.addFirst(5);
        ad.addFirst(4);  // ad: 4, 5
        assertEquals(4, (int) ad.get(0));
        assertEquals(5, (int) ad.get(1));

        ad.removeLast();
        ad.removeLast(); // ad:
        assertNull(ad.get(1));
        assertNull(ad.get(0));

        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void equalsTest() {
        deque.Deque<Integer> test = new ArrayDeque<Integer>();
        deque.Deque<Integer> testLLD = new LinkedListDeque<Integer>();

        assertTrue(ad.equals(ad));
        assertTrue(ad.equals(test));
        assertTrue(ad.equals(testLLD));

        ad.addFirst(1); // lld: 1
        assertFalse(ad.equals(test));
        test.addLast(1); // test: 1
        assertTrue(ad.equals(test));

        ad.addLast(1); // lld: 1 1, test: 1
        assertFalse(ad.equals(test));

        test.addFirst(2); // lld: 1 1, test: 2 1
        assertFalse(ad.equals(test));

        ad.removeFirst();
        test.removeFirst(); // lld: 1, test: 1
        assertTrue(ad.equals(test));

        ad.removeFirst(); // lld: , test: 1
        assertFalse(ad.equals(test));

        test.removeFirst(); // lld: , test:
        assertTrue(ad.equals(test));

        ad = new ArrayDeque<Integer>();
    }

}
