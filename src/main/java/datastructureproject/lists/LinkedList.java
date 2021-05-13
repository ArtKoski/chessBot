package datastructureproject.lists;

import datastructureproject.Board.Move;
import java.util.Iterator;
import java.util.ListIterator;

/**
 *
 * @author artkoski
 */
public class LinkedList<E> extends java.util.LinkedList {
    /*
    private Node head;
    private int n;

    public LinkedList() {
        head = null;
        n = 0;
    }

    public void addLast(Object item) {
        if (item == null) {
            throw new NullPointerException("Object is null.");
        }
        if (head == null) {
            head = new Node(item);
            increaseSize();
            return;
        }

        Node newNode = new Node(item);

        Node latest = head;
        while (latest.getNext() != null) {
            latest = latest.getNext();
        }
        latest.setNext(newNode);
        increaseSize();
    }

    public void add(Object item) {
        this.addLast(item);
    }

    public void addFirst(Object item) {
        if (item == null) {
            throw new NullPointerException("Object is null.");
        }
        if (head == null) {
            head = new Node(item);
            increaseSize();
            return;
        }

        Node newNode = new Node(item);
        newNode.setNext(head);
        head = newNode;
        increaseSize();

    }

    public void addAll(LinkedList<Object> list) {
        for (Object obj : list) {
            this.addLast(obj);
        }
    }

    public Object get(int index) {
        if (head == null) {
            return null;
        }
        if (index < 0) {
            return null;
        }
        if (index == 0) {
            return head.getData();
        }

        Node currItem = head;
        for (int i = 0; i < index; i++) {
            if (currItem.getNext() == null) {
                return null;
            }

            currItem = currItem.getNext();
        }
        return currItem.getData();

    }

    public Object pollFirst() {
        Object obj = get(0);
        remove(0);
        return obj;
    }

    public boolean remove(int index) {

        if (index == 0) {
            head = head.getNext();
            decreaseSize();
            return true;
        }
        if (index < 0 || index > size()) {
            return false;
        }

        Node prevItem = null;
        Node currItem = head;
        if (head != null) {
            for (int i = 0; i < index; i++) {
                if (currItem.getNext() == null) {
                    return false;
                }
                prevItem = currItem;
                currItem = currItem.getNext();
            }
            prevItem.setNext(currItem.getNext());

            decreaseSize();
            return true;

        }
        return false;
    }

    public int size() {
        return n;
    }

    public void increaseSize() {
        n++;
    }

    public void decreaseSize() {
        n--;
    }

    public boolean isEmpty() {
        return (size() == 0);
    }

    public void clear() {
    }

    /* @Override
    public asdIterator<Object> iterator() {
        return new LinkedListIterator();
    }

    class LinkedListIterator implements Iterator<Object> {

        private Node previous;
        private Node previous2;
        private Node current;
        boolean removeCalled;

        public LinkedListIterator() {
            previous = null;
            previous2 = null;
            current = head;
            removeCalled = false;
        }

        @Override
        public Object next() {
            if (current == null) {
                throw new NullPointerException("Object is null.");
            }

            Node temp = current;
            previous2 = previous;
            previous = current;
            current = current.getNext();
            removeCalled = false;

            return temp.getData();
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public void remove() {
            if (previous == null || removeCalled) {
                throw new NullPointerException("Object is null.");
            }
            if (previous2 == null) {
                head = current;
            } else {
                previous2.setNext(current);
            }
            removeCalled = true;
            decreaseSize();
        }

    }
     */
 /*
    @Override
    public Iterator<Move> iterator() {
        return new MoveListIterator();
    }

    class MoveListIterator implements Iterator<Move> {

        private Node previous;
        private Node previous2;
        private Node current;
        boolean removeCalled;

        public MoveListIterator() {
            previous = null;
            previous2 = null;
            current = head;
            removeCalled = false;
        }

        @Override
        public Move next() {
            if (current == null) {
                throw new NullPointerException("Object is null.");
            }

            Node temp = current;
            previous2 = previous;
            previous = current;
            current = current.getNext();
            removeCalled = false;

            return (Move) temp.getData();
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public void remove() {
            if (previous == null || removeCalled) {
                throw new NullPointerException("Object is null.");
            }
            if (previous2 == null) {
                head = current;
            } else {
                previous2.setNext(current);
            }
            removeCalled = true;
            decreaseSize();
        }

    }
//

    public void printList() {
        Node tnode = head;
        while (tnode != null) {
            System.out.print(tnode.getData() + " ");
            tnode = tnode.getNext();
        }
    }
    
    
}

class Node {

    private Object data;
    private Node next;

    public Node(Object data) {
        this.data = data;
        this.next = null;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node nextValue) {
        next = nextValue;
    }

    public Object getData() {
        return data;
    }*/
}
