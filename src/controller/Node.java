package controller;

public class Node<T> {

    private Node<T> prev;
    private Node<T> next;
    private T date;

    public Node(Node<T> prev, Node<T> next, T date) {
        this.prev = prev;
        this.next = next;
        this.date = date;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }
}
