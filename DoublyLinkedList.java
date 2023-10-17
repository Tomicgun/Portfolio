package cse214hw1;

import java.util.NoSuchElementException;

public class DoublyLinkedList<E> implements ListAbstractType<E>{
    private int length = 0;
    private Node<E> head;
    private Node<E> tail;
    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
    }

    public int getLength(){
        return length;
    }


    private static class Node<E> {
    public Node<E> next;
    public Node<E> prev;
    public E data;

    public Node(E e){
        this.next = null;
        this.prev = null;
        this.data = e;
    }
}

private class DoublyLinkedListIterator implements TwoWayListIterator<E>{
        Node<E> current; //= new Node<>(null);
    @Override
    public boolean hasPrevious() {
        return (current.prev != null);
    }
    @Override
    public E previous() throws NoSuchElementException {
        current = current.prev;
        return current.data;
    }
    @Override
    public void add(E element) throws UnsupportedOperationException{
        Node<E> newNode = new Node<>(element);
        if(isEmpty()){
            head = newNode;
        } else {
            newNode.next = current.next;
            newNode.prev = current;
            current.next = newNode;
        }
        length++;
    }
    @Override
    public void set(E element) throws UnsupportedOperationException, IllegalStateException {
        Node<E> currentNode = current;
        currentNode = new Node<>(element);
    }
    @Override
    public boolean hasNext() {
        if(current != null){
            return current.next != null;
        }
        else{
            return true;
        }
    }
    @Override
    public E next() {
        if (current != null) {
            current = current.next;
        } else {
            current = head;
        }
        return current.data;
    }
}
    public TwoWayListIterator<E> iterator(){
        return new DoublyLinkedListIterator();
    }

//DoublyLinked list methods (ListAbstract and Collection type interfaces)
    @Override
    public boolean add(E element) {
        Node<E> temp = new Node<>(element);
        if(head == null) {
            head = tail = temp;
        } else {
            tail.next = temp;
            temp.prev = tail;
            tail = temp;
        }
        length++;
        return true;
    }
    @Override
    public boolean remove(E element) {
        Node<E> traverse = head;
        if(head.data == element){
            head = head.next;
            length--;
            return true;
        }
        if(tail.data == element){
            tail = tail.prev;
            tail.next = null;
        } else {
            while(traverse.next != null){
                if(traverse.next.data == element){
                    traverse.next = traverse.next.next;
                }
                traverse = traverse.next;
            }
        }
        length--;
        return true;
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public boolean isEmpty() {
        return this.head == null;
    }

    @Override
    public boolean contains(E element) {
        Node<E> step = head;
        while(step != null){
            if(step.data == element){return true;}
            step = step.next;
        }
        return false;
    }


    //ListAbstract type methods
    @Override
    public E get(int index) {
        Node<E> temp = head;
        for(int i = 0; i <= length-1;i++){
            if(i == index){
                return temp.data;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public E set(int index, E element) {
        Node<E> temp = head;
        Node<E> replace = new Node<>(element);
        for(int i = 0; i < length;i++){
            if(i == index){
                temp.data = replace.data;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public void add(int index, E element) {
        Node<E> temp = new Node<>(element);
        if(index == 0){
            temp.next = head;
            head.prev = temp;
            head = temp;
            head.prev = null;
        } else if(index == length-1){
            tail.next = temp;
            temp.prev = tail;
            tail = temp;
            tail.next = null;
        } else {
            Node<E> traverse = head;
            for(int i = 0; i <= length-1;i++){
                traverse = traverse.next;
                if(i == index){
                    temp.next = traverse.next;
                    temp.prev = traverse;
                    traverse.next = temp;
                }
            }
        }

    }

    @Override
    public void remove(int index) {
        if(index == 0 & length > 1){
            head = head.next;
            length--;
        }
        else if(index == length-1){
            tail = tail.prev;
            tail.next = null;
            length--;
        }
        else if(length == 1) {
            head.next = null;
            head.prev = null;
            length--;
        } else if (length == 2) {
            head = head.next;
            length--;
        } else{
            Node<E> traverse = head;
            for(int i = 0; i < length; i++){
                if(i == index-1){
                    traverse.next = traverse.next.next;
                    length--;
                }
                traverse = traverse.next;
            }
        }
    }
    @Override
    public String toString() {
        TwoWayListIterator<E> it = this.iterator();
        if (!it.hasNext())
            return "[]";
        StringBuilder builder = new StringBuilder("[");
        while (it.hasNext()) {
            E e = it.next();
            builder.append(e.toString());
            if (!it.hasNext())
                return builder.append("]").toString();
            builder.append(", ");
        }
// code execution should never reach this line
        return null;
    }

}
