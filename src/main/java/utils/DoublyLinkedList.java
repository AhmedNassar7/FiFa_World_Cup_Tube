package utils;

import java.util.Iterator;

public class DoublyLinkedList<T> implements Iterable<T> {
    private int size = 0;
    private Node<T> head = null;
    private Node<T> tail = null;

    public boolean isEmpty() {
        return size() == 0;
    }


    public void addToHead(T elem) {
        if (isEmpty()) {
            head = tail = new Node<T>(elem, null, null);
        } else {
            head.prev = new Node<T>(elem, null, head);
            head = head.prev;
        }
        size++;
    }

    public void add(T elem) {
        addToTail(elem);
    }

    public void add(int index, T elem) {
        if (index < 0 || index > size) throw new IllegalArgumentException("Illegal index: " + index);
        if (index == 0) addToHead(elem);
        else if (index == size) addToTail(elem);
        else {
            Node<T> current;
            int i = 0;
            if (index < size / 2) {
                for (i = 0, current = head; i < index - 1; i++)
                    current = current.next;
            } else {
                for (i = size - 1, current = tail; i > index - 1; i--)
                    current = current.prev;
            }
            Node<T> newNode = new Node<>(elem, current, current.next);
            current.next.prev = newNode;
            current.next = newNode;
            size++;
        }
    }

    public void addToTail(T elem) {
        if (isEmpty()) {
            head = tail = new Node<T>(elem, null, null);
        } else {
            tail.next = new Node<T>(elem, tail, null);
            tail = tail.next;
        }
        size++;
    }

    public T peekHead() {
        if (isEmpty()) throw new RuntimeException("Empty Linked List!");
        return head.data;
    }

    public T peekTail() {
        if (isEmpty()) throw new RuntimeException("Empty Linked List!");
        return tail.data;
    }

    public T removeHead() {
        if (isEmpty()) throw new RuntimeException("Empty Linked List!");
        T removed = head.data;
        head = head.next;
        size--;
        if (isEmpty()) tail = null;
        else head.prev = null;
        return removed;

    }

    public T removeTail() {
        if (isEmpty()) throw new RuntimeException("Empty Linked List!");
        T removed = tail.data;
        tail = tail.prev;
        size--;
        if (isEmpty()) head = null;
        else tail.next = null;
        return removed;

    }

    private T remove(Node<T> node) {
        if (node.prev == null) return removeHead();
        if (node.next == null) return removeTail();
        node.next.prev = node.prev;
        node.prev.next = node.next;

        T removed = node.data;

        node = node.next = node.prev = null;

        size--;

        return removed;
    }

    public T removeAt(int index) {
        if (index < 0 || index >= size) throw new IllegalArgumentException();
        int i = 0;
        Node<T> current;
        if (index < size / 2) {
            for (i = 0, current = head; i < index; i++)
                current = current.next;
        } else {
            for (i = size - 1, current = tail; i > index; i--)
                current = current.prev;
        }
        return remove(current);
    }

    public boolean remove(Object obj) {
        Node<T> current = head;
        if (obj == null) {
            while (current != null) {
                if (current.data == null) {
                    remove(current);
                    return true;
                }
                current = current.next;
            }
        } else {
            while (current != null) {
                if (obj.equals(current.data)) {
                    remove(current);
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }


    public int indexOf(Object obj) {
        Node<T> current = head;
        if (obj == null) {
            for (int i = 0; current != null; current = current.next, i++)
                if (current.data == null) return i;

        } else {
            for (int i = 0; current != null; current = current.next, i++)
                if (obj.equals(current.data)) return i;
        }
        return -1;
    }

    public T get(int index) {
        if (index < 0 || index >= size) throw new IllegalArgumentException();
        int i = 0;
        Node<T> current;
        if (index < size / 2) {
            for (i = 0, current = head; i < index; i++)
                current = current.next;
        } else {
            for (i = size - 1, current = tail; i > index; i--)
                current = current.prev;
        }
        return current.data;
    }

    public boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    public void clear() {
        Node<T> current = head;
        while (current != null) {
            Node<T> next = current.next;
            current.prev = current.next = null;
            current.data = null;
            current = next;
        }
        head = tail = current = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append(" ]");
        return sb.toString();
    }

    private class Node<T> {
        T data;
        Node<T> prev, next;

        public Node(T data, Node<T> prev, Node<T> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }
}
