package model;

public class Range<T extends Comparable<T>> {
    private final T start;
    private final T end;

    public Range(T start, T end) {
        this.start = start;
        this.end = end;
    }

    public boolean contains(T value) {
        return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
    }

    public T getStart() {
        return start;
    }

    public T getEnd() {
        return end;
    }
}

