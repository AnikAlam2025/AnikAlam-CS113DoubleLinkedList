import java.util.*;

public class DoubleLinkedList<E> extends AbstractSequentialList<E>{
    private Node<E> head, tail, topOfStackRef = null;
    private int size = 0;

    /**
     * public class listIterator with implemented interface methods: add, hasNext, hasPrevious, next, nextIndex, previous, previousIndex, remove, set
     */
    public class listIterator implements ListIterator<E> {
        private Node<E> nextItem; //this node references to the next node, last item returned, and initialized index at 0
        private Node<E> lastItemReturned;
        private int index = 0;

        public listIterator(int i) {
            if(i < 0 || i > size) {
                throw new IndexOutOfBoundsException();
            }
            lastItemReturned = null;

            if(i == size) {
                index = size;
                nextItem = null;
            } else {
                nextItem = head;
                for(index = 0; index < i; index++) {
                    nextItem = nextItem.nextNode;
                }
            }

        }

        /**
         * copy constructor
         * @param iteratorOriginal
         */
        public listIterator(listIterator iteratorOriginal) {
            nextItem = iteratorOriginal.nextItem;
            lastItemReturned = iteratorOriginal.lastItemReturned;
            index = iteratorOriginal.index;
        }

        /**
         * method adds new item between item returned by next and item returned by previous(in between the two)
         * If previous is called after add, the new element is returned
         * @param object
         */
        public void add(E object) {
            if(head == null) { //There is no head, list is empty
                head = new Node<>(object);
                tail = head;
            } else if(nextItem == head) {//insertion is at head of the list
                Node<E> newNode = new Node<>(object);
                newNode.nextNode = nextItem;
                nextItem.prevNode = newNode;
                head = newNode;
            } else if(nextItem == null) {//inserting at the tail of list
                Node<E> newNode = new Node<>(object);
                tail.nextNode = newNode;
                newNode.prevNode = tail;
                tail = newNode;
            } else { //inserting into the middle
                Node<E> newNode = new Node<>(object);
                newNode.prevNode = nextItem.prevNode;
                nextItem.prevNode.nextNode = newNode;

                newNode.nextNode = nextItem;
                nextItem.prevNode = newNode;
            }
            //increment the size + index and update lastItemReturned to be null as no item was returned, only added
            size++;
            index++;
        }

        /**
         * method checks to see if there is a next item present in the list
         * @return true if next item is present and will not throw an exception, false otherwise
         */
        public boolean hasNext() {
            return nextItem != null;
        }

        /**
         * method checks to see if there is a prev item in the list
         * @return true if prev item is present and will not throw an exception, false otherwise
         */
        public boolean hasPrevious() {
            //if next item is null and the size isn't 0 OR if the nextItem's previous node isn't null, then prev exists
            if(size == 0) {
                return false;
            }
            return ((nextItem == null && size != 0) || nextItem.prevNode != null);
        }

        /**
         * method checks to see if there is a next item, throws exception if there is not
         * otherwise increments lastItemReturned to current nextItem and nextItem is incremented
         * to the item of the next node
         * @return lastItemReturned.nodeData
         */
        public E next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            //updating positions for each item by shifting them forward
            lastItemReturned = nextItem;
            nextItem = nextItem.nextNode;
            index++;
            return lastItemReturned.nodeData;
        }

        /**
         * method returns the next index spot
         * @return current index spot
         */
        public int nextIndex() {
            return index;
        }

        /**
         * method checks if there is previous data, if so it will check if the iterator is past the last element
         * if no next item, updates it to tail, otherwise updates to previous node
         * @return data from last returned item
         */
        public E previous() {
            if(!hasPrevious()) { //if there is no previous, throw exception
                throw new NoSuchElementException();
            }
            if(nextItem == null) {
                nextItem = tail;
            } else {
                nextItem = nextItem.prevNode;
            }
            lastItemReturned = nextItem;
            index--;
            return lastItemReturned.nodeData;
        }

        /**
         * method returns the previous index spot
         * @return previous index spot
         */
        public int previousIndex() {
            return index - 1;
        }

        /**
         * method removes the last returned element
         */
        public void remove() {
            if (lastItemReturned == null) { //no last returned item
                throw new IllegalStateException();
            }
            if (lastItemReturned == head) { //removing item from head
                head = nextItem;
                head.prevNode = null;
            } else if (lastItemReturned == tail) { //removing item from tail
                tail = lastItemReturned.prevNode;
                tail.nextNode = null;
            } else { //removing item from somewhere in the middle
                lastItemReturned.nextNode.prevNode = lastItemReturned.prevNode;
                lastItemReturned.prevNode.nextNode = lastItemReturned.nextNode;
            }
            size--;
            index--;
        }

        /**
         * method checks if the last returned item isn't null, and if not then will set that data to the inputItem
         * otherwise throws an exception seeing as the element is not present
         * @param inputItem
         */
        public void set(E inputItem) {
            if(lastItemReturned != null) {
                lastItemReturned.nodeData = inputItem;
            } else {
                throw new IllegalStateException();
            }
        }
    }

    /**
     * iterator constructor
     * @return iterator over elements within the list
     */
    public Iterator<E> iterator(){
        return new listIterator(0);
    }

    /**
     *
     * @return list iterator over elements within the list
     */
    public ListIterator<E> listIterator() {
        return new listIterator(0);
    }

    /**
     *
     * @param index
     * @return list iterator over elements within the list starting at the specified index
     */
    public ListIterator<E> listIterator(int index) {
        return new listIterator(index);
    }

    /**
     *
     * @param object
     * @return
     */
    public boolean add(E object) {
        add(size, object);
        return true;
    }

    //add node methods for adding at head, tail, and specified index
    /**
     * method adds the object or piece of data at the given index before the node currently referenced by the iterator
     * @param index
     * @param object
     */
    public void add(int index, E object) {
        listIterator(index).add(object);
    }

    /**
     * method to clear out the linked list by removing
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Equals method to check if the specified object is valid and compares it to another object
     * @param other
     * @return
     */
    public boolean equals(Object other) {
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        } else {
            return other == this;
        }
    }

    /**
     * method returns true if object exists and there has an index location, false otherwise which would return -1
     * @param object
     * @return
     */
    public boolean contains(Object object) {
        return indexOf(object) != -1;
    }

    /**
     * method iterates through list to find node at specific index
     * @param index
     * @return node at selected index
     */
    public Node<E> getNode(int index) {
        Node<E> currentNode = head;
        for(int i = 0; i < index && currentNode != null; i++) {
            currentNode = currentNode.nextNode;
        }
        return currentNode;
    }

    /**
     *
     * @param index
     * @return returns data in the element at position index
     */
    public E get(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("The index can only be 0 to " + size());
        }
        Node<E> placeHolderNode = getNode(index);
        return placeHolderNode.nodeData;
    }

//    public int indexOf(Object obj) {
//        return indexOf(obj);
//    }

//    public int lastIndexOf(Object o) {
//
//    }

    public boolean isEmpty() {
        topOfStackRef = head;
        return topOfStackRef == null;
    }

    /**
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * removes element at specified location within the list
     * @param index
     * @return the value being removed
     */
    public E remove(int index) {
        E valueRemoved;
        ListIterator<E> iterator = listIterator(index); //creates a variable for storage and moves iterator to desired index

        if(iterator.hasNext()) {
            valueRemoved = iterator.next();
            iterator.remove();
        } else {
            throw new IndexOutOfBoundsException();
        }
        return valueRemoved;
    }

    /**
     * Node inner class for double linked list which creates next/previous links and has a constructor
     * for the data
     * @param <E>
     */
    private static class Node<E> {
        private E nodeData;
        private Node<E> nextNode;
        private Node<E> prevNode;

        private Node(E nodeDataPlaceholder) {
            nodeData = nodeDataPlaceholder;
        }
    }
}
