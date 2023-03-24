package utils;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.LinkedList;

// com.ahmedhatem.collections.Stack implemented with LinkedList
public class Stack<T> implements Iterable<T> {
    private final LinkedList<T> list = new LinkedList<>();

    public Stack(T elem) {
        push(elem);
    }

    public Stack() {
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void push(T elem) {
        list.addLast(elem);
    }

    public T pop() {
        if (isEmpty()) throw new EmptyStackException();
        return list.removeLast();
    }

    public T peek() {
        if (isEmpty()) throw new EmptyStackException();
        return list.peekLast();
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }
}