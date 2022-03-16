import java.util.*;

public class DoubleLinkedList<E> extends AbstractSequentialList<E>{
    private Node<E> head, tail;
    private int size = 0;



    public class listIterator implements ListIterator<E> {
        private Node<E> nextItem; //this node references to the next node, last item returned, and initialized index at 0
        private Node<E> lastItemReturned;
        private int index = 0;
        /**
         *
         */
        public listIterator(int position) {

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
                head = newNode;
            } else if(nextItem == null) {//inserting at the tail of list
                Node<E> newNode = new Node<>(object);
                tail.nextNode = newNode;
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
            lastItemReturned = null;
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
            return (nextItem == null && size != 0) || nextItem.prevNode != null;
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
         * method returns the next index spot
         * @return current index spot
         */
        public int nextIndex() {
            return index;
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
            if(lastItemReturned == null) { //no last returned item
                throw new NoSuchElementException();
            }
            if(lastItemReturned == head) {

            }
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
                throw new NoSuchElementException();
            }
        }
    }

    /**
     *
     * @return
     */
    public Iterator<E> iterator(){
        return new listIterator(0);
    }

    /**
     *
     * @param
     * @return
     */
    public ListIterator<E> listIterator() {
        return new listIterator(0);
    }

    /**
     *
     * @param index
     * @return
     */
    public ListIterator<E> listIterator(int index) {
        return new listIterator(index);
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
     * method adds item to the front of the list
     * @param object
     */
    public void addFirst(E object) {
        head = new Node<>(object);
        size++;
    }

    /**
     * method adds item to tail of the list, will take existing tail and update its next value to new tail
     * then sets new tail to the tail node
     * @param object
     */
    public void addLast(E object) {
        Node<E> newTail = new Node<>(object);
        newTail.prevNode = tail;
        tail.nextNode = newTail;
        newTail = tail;
        newTail.nextNode = null;
        size++;
    }


    //Get node methods to retrieve node info, getNode is used to get the data from that node
    /**
     * method gets data at the head of list
     * @return data at head
     */
    public E getFirst() {
        return head.nodeData;
    }

    /**
     * method gets data at tail of list
     * @return data at tail
     */
    public E getLast() {
        return tail.nodeData;
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
     * @return
     */
    public E get(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("The index can only be 0 to " + size());
        }
        Node<E> placeHolderNode = getNode(index);
        return placeHolderNode.nodeData;
    }

    /**
     *
     * @return
     */
    public int size() {
        return size;
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
