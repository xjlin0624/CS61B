package deque;

import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list deque tests. */

public class LinkedListDequeTest {
    /**
     * You MUST use the variable below for all of your tests. If you test
     * using a local variable, and not this static variable below, the
     * autograder will not grade that test. If you would like to test
     * LinkedListDeques with types other than Integer (and you should),
     * you can define a new local variable. However, the autograder will
     * not grade that test.
     */

    public static deque.Deque<Integer> lld = new LinkedListDeque<Integer>();

    @Test
    /** Adds a few things to the list, checks that isEmpty() is correct.
     * This is one simple test to remind you how junit tests work. You
     * should write more tests of your own.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {
        assertTrue("A newly initialized LLDeque should be empty", lld.isEmpty());
        lld.addFirst(0);

        assertFalse("lld should now contain 1 item", lld.isEmpty());

        // Reset the linked list deque at the END of the test.
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void addFirstTest() {
        lld.addFirst(7); // lld should be 7
        assertEquals(1, lld.size());
        assertEquals(7, (int) lld.get(0));

        lld.addFirst(3); // now lld should be 3, 7
        assertEquals(2, lld.size());
        assertEquals(3, (int) lld.get(0));

        lld.addFirst(1); // now lld should be 1, 3, 7
        assertEquals(3, lld.size());
        assertEquals(1, (int) lld.get(0));

        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void removeFirstTest() {
        lld.addFirst(7);
        lld.addFirst(3);
        lld.addFirst(1); // now lld should be 1, 3, 7

        assertEquals(1, (int) lld.removeFirst()); // lld should be 3, 7
        assertEquals(2, lld.size());
        assertEquals(3, (int) lld.get(0));

        assertEquals(3, (int) lld.removeFirst()); // lld should be 7
        assertEquals(7, (int) lld.removeFirst()); // lld should be empty
        assertEquals(0, lld.size());
        assertNull(lld.get(0));
        assertTrue("lld should be empty", lld.isEmpty());

        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void addLastTest() {
        lld.addLast(7); // lld should be 7
        assertEquals(1, lld.size());
        assertEquals(7, (int) lld.get(0));

        lld.addLast(3); // lld should be 7, 3
        assertEquals(2, lld.size());
        assertEquals(3, (int) lld.get(1));

        lld.addLast(1); // lld should be 7, 3, 1
        assertEquals(3, lld.size());
        assertEquals(1, (int) lld.get(2));

        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void removeLastTest() {
        lld.addLast(7);
        lld.addLast(3);
        lld.addLast(1); // lld should be 7, 3, 1

        assertEquals(1, (int) lld.removeLast()); // lld should be 7, 3
        assertEquals(2, lld.size());
        assertEquals(3, (int) lld.get(1));

        lld.removeLast(); // lld should be 7
        lld.removeLast(); // lld should be empty
        assertEquals(0, lld.size());
        assertNull(lld.get(0));
        assertTrue("lld should be empty", lld.isEmpty());

        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void sizeTest() {
        lld.addFirst(2);
        lld.addLast(5);
        lld.removeFirst();
        lld.addFirst(2);
        lld.addFirst(3); // ad: 3, 2, 5
        assertEquals(3, lld.size());

        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void printDequeTest() {
        lld.addFirst(2);
        lld.addLast(5);
        lld.removeFirst();
        lld.addFirst(2);
        lld.addFirst(3); // lld: 3, 2, 5
        String m = "3 2 5\n"; // lld: 3, 2, 5
        System.out.print(m);
        lld.printDeque();

        lld.removeLast(); // lld: 3, 2
        m = "3 2\n";
        System.out.print(m);
        lld.printDeque();

        lld.removeLast(); // lld: 3
        m = "3\n";
        System.out.print(m);
        lld.printDeque();

        lld.removeLast(); // lld:
        m = "\n";
        System.out.println(m);
        lld.printDeque();

        lld = new LinkedListDeque<Integer>();
    }


    @Test
    public void getTest() {
        lld.addFirst(5);
        lld.addFirst(4); // lld: 4, 5
        assertEquals(4, (int) lld.get(0));
        assertEquals(5, (int) lld.get(1));

        lld.removeLast();
        lld.removeLast(); // lld:
        assertNull(lld.get(1));
        assertNull(lld.get(0));

        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void equalsTest() {
        Deque<Integer> test = new LinkedListDeque<Integer>();
        Deque<Integer> testAD = new ArrayDeque<Integer>();

        assertTrue(lld.equals(lld));
        assertTrue(lld.equals(test));
        assertTrue(lld.equals(testAD));

        lld.addFirst(1); // lld: 1
        assertFalse(lld.equals(test));
        test.addLast(1); // test: 1
        assertTrue(lld.equals(test));

        lld.addLast(1); // lld: 1 1, test: 1
        assertFalse(lld.equals(test));

        test.addFirst(2); // lld: 1 1, test: 2 1
        assertFalse(lld.equals(test));

        lld.removeFirst();
        test.removeFirst(); // lld: 1, test: 1
        assertTrue(lld.equals(test));

        lld.removeFirst(); // lld: , test: 1
        assertFalse(lld.equals(test));

        test.removeFirst(); // lld: , test:
        assertTrue(lld.equals(test));

        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void getRecTest() {
        lld.addFirst(5);
        lld.addFirst(4); // lld: 4, 5
        assertEquals(4, (int) ((LinkedListDeque<Integer>) lld).getRecursive(0));
        assertEquals(5, (int) ((LinkedListDeque<Integer>) lld).getRecursive(1));

        lld.removeLast();
        lld.removeLast(); // lld:
        assertNull(((LinkedListDeque<Integer>) lld).getRecursive(1));
        assertNull(((LinkedListDeque<Integer>) lld).getRecursive(0));

        lld = new LinkedListDeque<Integer>();
    }

}
