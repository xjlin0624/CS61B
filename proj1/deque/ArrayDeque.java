package deque;

public class ArrayDeque<T> implements Deque<T> {

    private int size;
    private int nextFirst;
    private int nextLast;
    private T[] items;

    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[8];
        nextFirst = 0;
        nextLast = 1;
    }

    // 0 1 2 3 4 5 6 7
    // 3             9
    //   l         f

    private void resize() {
        if (size == items.length - 1) {
            T[] a = (T[]) new Object[items.length * 2];
            toResize(a);
        }
        if (size < items.length * 0.25 && items.length > 16) {
            T[] a = (T[]) new Object[items.length / 2];
            toResize(a);
        }
    }

    private void toResize(T[] a) {
        for (int i = 0; i < size; i++) {
            a[i] = get(i);
        }
        items = a;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    private int goR(int index) {
        if (index == items.length - 1) {
            return 0;
        } else {
            return index + 1;
        }
    }

    private int goL(int index) {
        if (index == 0) {
            return items.length - 1;
        } else {
            return index - 1;
        }
    }

    @Override
    public void addFirst(T item) {
        resize();
        items[nextFirst] = item;
        nextFirst = goL(nextFirst);
        size++;
    }

    @Override
    public void addLast(T item) {
        resize();
        items[nextLast] = item;
        nextLast = goR(nextLast);
        size++;
    }

    @Override
    public T removeFirst() {
        resize();
        if (size == 0) {
            return null;
        }
        T p = get(0);
        nextFirst = goR(nextFirst);
        items[nextFirst] = null;
        size--;
        return p;
    }

    @Override
    public T removeLast() {
        resize();
        if (size == 0) {
            return null;
        }
        T p = get(size - 1);
        nextLast = goL(nextLast);
        items[nextLast] = null;
        size--;
        return p;
    }

    @Override
    public T get(int index) {
        if (index >= items.length) {
            return null;
        } else if (nextFirst + 1 + index >= items.length) {
            return items[nextFirst + index + 1 - items.length];
        } else {
            return items[nextFirst + 1 + index];
        }
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
    public boolean equals(Object o) {
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

}
