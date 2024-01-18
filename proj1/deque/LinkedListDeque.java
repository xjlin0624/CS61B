package deque;

public class LinkedListDeque<T> implements Deque<T> {

    private int size;
    private Node sentinel;

    private class Node {
        public T item;
        public Node next;
        public Node prev;

        public Node(T i, Node n, Node p) {
            item = i;
            next = n;
            prev = p;
        }
    }

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        size = 0;
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }


    @Override
    public void addFirst(T item) {
        Node p = sentinel.next;
        sentinel.next = new Node(item, p, sentinel);
        p.prev = sentinel.next;
        size++;
    }

    @Override
    public void addLast(T item) {
        Node p = sentinel.prev;
        sentinel.prev = new Node(item, sentinel, p);
        p.next = sentinel.prev;
        size++;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T p = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size--;
        return p;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T p = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size--;
        return p;
    }

    @Override
    public T get(int index) {
        Node p = sentinel.next;
        for (int i = 0; i < size; i++) {
            if (index == i) {
                return p.item;
            }
            p = p.next;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }

    @Override
    public boolean equals(Object o) { // from lab06
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque deq = (Deque) o;
        if (size != deq.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!get(i).equals(deq.get(i))) {
                return false;
            }
        }
        return true;
    }


    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        Node p = getRecHelp(sentinel.next, index);
        return p.item;
    }

    private Node getRecHelp(Node n, int index) {
        if (index == 0) {
            return n;
        }
        return getRecHelp(n.next, index - 1);
    }

}
