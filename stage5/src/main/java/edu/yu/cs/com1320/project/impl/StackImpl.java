package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Stack;

public class StackImpl<T> implements Stack<T>{
    private Node<T> first;
    public StackImpl(){
        this.first = null;
    }

    private class Node<V>{
        private Node<T> next;
        private T cmnd;
        private Node(T cmnd){
            this.cmnd = cmnd;
        }
        public Node<T> getNext(){
            return this.next;
        }
        public void setNext(Node<T> n){
            this.next = n;
        }
        public T getCommand(){
            return this.cmnd;
        }
    }


    @Override
    public void push(T element) {
        if (element == null){
            return;
        }
        Node<T> newNode = new Node<>(element);
        newNode.setNext(first);
        first = newNode;
    }

    @Override
    public T pop() {
        if (first == null){
            return null;
        }
        else{
            Node<T> temp = first;
            first = first.getNext();
            return temp.getCommand();
        }
    }

    @Override
    public T peek() {
        if (first == null){
            return null;
        }
        return first.getCommand();
    }

    @Override
    public int size() {
        int counter = 0;
        Node<T> p = first;
        while (p != null){
            counter++;
            p = p.getNext();
        }
        return counter;
    }
}