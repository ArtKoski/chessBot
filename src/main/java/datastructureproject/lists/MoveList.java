package datastructureproject.lists;

import com.github.bhlangonijr.chesslib.move.Move;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author artkoski
 */
public class MoveList extends LinkedList<Move> /*implements Iterable<Move>*/ {
/*
    private Node head;
    private int n;

    public MoveList() {
        head = null;
        n = 0;
    }

    public void addLast(Move item) {
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

    public void add(Move item) {
        this.addLast(item);
    }

    public void addFirst(Move item) {
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

    public void addAll(MoveList list) {
        for (Move move : list) {
            this.addLast(move);
            increaseSize();
        }
    }

    public void addAllInFront(MoveList list) {
        for (Move move : list) {
            this.addFirst(move);
            increaseSize();
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

    @Override
    public Iterator<Move> iterator() {

        return new LinkedListIterator();
    }

    class LinkedListIterator implements Iterator<Move> {

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
        public Move next() {
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

    public void printList() {
        Node tnode = head;
        while (tnode != null) {
            System.out.print(tnode.getData() + " ");
            tnode = tnode.getNext();
        }
    }

}

class Node {

    private Move data;
    private Node next;

    public Node(Move data) {
        this.data = data;
        this.next = null;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node nextValue) {
        next = nextValue;
    }

    public Move getData() {
        return data;
    }
*/
}
