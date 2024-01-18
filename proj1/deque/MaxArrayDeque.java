package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator c;

    public MaxArrayDeque(Comparator<T> c) { // creates a MaxArrayDeque with the given Comparator
        super();
        this.c = c;
    }

    // returns the maximum element in the deque as governed by the
    // previously given Comparator. If the MaxArrayDeque is empty, return null
    public T max() {
        if (this == null) {
            return null;
        }
        T max = super.get(0);
        for (int i = 0; i < super.size(); i++) {
            if (c.compare(max, super.get(i)) < 0) {
                max = super.get(i);
            }
        }
        return max;
    }

    // returns the maximum element in the deque as governed by
    // the parameter Comparator c. If the MaxArrayDeque is empty, return null
    public T max(Comparator<T> c) {
        if (this == null) {
            return null;
        }
        T max = super.get(0);
        for (int i = 0; i < super.size(); i++) {
            if (c.compare(max, super.get(i)) < 0) {
                max = super.get(i);
            }
        }
        return max;
    }

}
