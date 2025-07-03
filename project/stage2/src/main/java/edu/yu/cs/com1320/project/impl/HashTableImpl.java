package edu.yu.cs.com1320.project.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.yu.cs.com1320.project.HashTable;

public class HashTableImpl<Key, Value> implements HashTable<Key,Value>{
    private Node<Key, Value>[] hashArray;

    public HashTableImpl(){
        this.hashArray = new Node[5];
        for(int i = 0; i <this.hashArray.length; i++){
            this.hashArray[i] = null;
        }
    }

    private class Node<Key, Value>{
        private Key key;
        private Value value;
        private Node next;
        private Node(Key k, Value v){
            this.key = k;
            this.value = v;
        }
        public Node getNext(){
            return this.next;
        }
        public void setNext(Node n){
            this.next = n;
        }
        public Key getKey(){
            return this.key;
        }
        public Value getValue(){
            return this.value;
        }
        public void setValue(Value v){
            this.value = v;
        }

    }
    



    private Value delete(Node<Key, Value> first, Node<Key, Value> delNode) {
        if (first == null){
            return null;
        }
        
        if (first.getKey().equals(delNode.getKey())){
            int keyHashCode = hashFunction(first.getKey());
            Value tempVal = first.getValue();
            this.hashArray[keyHashCode] = first.getNext();
            return tempVal;
        }
        Node q = first;
        Node p = first.getNext();
        while(p != null){
            if (p.getKey().equals(delNode.getKey())){
                Value tempVal = (Value) p.getValue();
                q.setNext(p.getNext());
                return tempVal;
            }
            p = p.getNext();
            q = q.getNext();
        }
        //filler
        return null;
    }



    @Override
     /**
     * @param k the key whose value should be returned
     * @return the value that is stored in the HashTable for k, or null if there is no such key in the table
     */
    public Value get(Key k){
        int keyHashCode = hashFunction(k);
        Node current = this.hashArray[keyHashCode];
        if (current == null){
            return null;
        }
        if (current.getKey().equals(k)){
            return (Value) current.getValue();
        }
        else{
            while(current.getNext() != null){
                if (current.getNext().getKey().equals(k)){
                    return (Value) current.getNext().getValue();
                }
                current = current.getNext();
            }
        }
        return null;
    }


    /**
     * 
     * @param first the first node in the hashed to element of the hashArray
     * @param newNode the Node being modified
     * @see put
     * @return if there is no previous value: null, otherwise previous value
     */

    private Value insert(Node<Key, Value> first, Node<Key, Value> newNode){
        if(first == null){
            int keyHashCode = hashFunction(newNode.getKey());
            this.hashArray[keyHashCode] = newNode;
            return null;
        }
        else{
            Node p = first;
			while (p.getNext() != null) {
				p = p.getNext();
			}
			p.setNext(newNode);
		}
		return null;
     }


     private Value edit(Node<Key, Value> first, Node<Key, Value> editNode){
        Node p= first;
        Value tempVal = null;
        while (p != null){
            if (p.getKey().equals(editNode.getKey())){
                tempVal = (Value) p.getValue();
                p.setValue(editNode.getValue());
            }
            p = p.getNext();
        }
        return tempVal;
     }

    @Override
    /**
     * @param k the key at which to store the value
     * @param v the value to store
     *          To delete an entry, put a null value.
     * @see insert, delete, hashFunction
     * @return if the key was already present in the HashTable, return the previous value stored for the key. If the key was not already present, return null.
     */
    public Value put(Key k, Value v){
        int keyHashCode = hashFunction(k);
        Node first = this.hashArray[keyHashCode];
        Node modNode = new Node<Key,Value>(k, v);
        if (v == null){
            return (Value) delete(first, modNode);
        }
        else if (this.containsKey(k)){
            return (Value) edit(first, modNode);
        }
        return (Value) insert(first, modNode);
    }

    private int hashFunction(Key k){
        return Math.abs(Objects.hash(k)) % 5;
    }

    @Override
    /**
     * @param key the key whose presence in the hashtabe we are inquiring about
     * @return true if the given key is present in the hashtable as a key, false if not
     * @throws NullPointerException if the specified key is null
     */
    public boolean containsKey(Key key){
        if(key == null){
            throw new NullPointerException();
        }
        for (Node p : this.hashArray){
            while (p != null){
                if (p.getKey().equals(key)){
                    return true;
                }
                p = p.getNext();
            }
        }
        return false;
    }

    @Override
    /**
     * @return an unmodifiable set of all the keys in this HashTable
     * @see java.util.Collections#unmodifiableSet(Set)
     */
    public Set<Key> keySet(){
        HashSet<Key> myValueSet = new HashSet();
        for (Node p : this.hashArray){
           while (p != null){
               myValueSet.add((Key) p.getKey());
               p = p.getNext();
            }
        }
        return Collections.unmodifiableSet(myValueSet);
    }

    @Override
    /**
     * @return an unmodifiable collection of all the values in this HashTable
     * @see java.util.Collections#unmodifiableCollection(Collection)
     */
    public Collection<Value> values(){
        ArrayList<Value> myValueList = new ArrayList();
        for (Node p : this.hashArray){
           while (p != null){
               myValueList.add((Value) p.getValue());
               p = p.getNext();
            }
        }
        return Collections.unmodifiableCollection(myValueList);
    }

    @Override
    /**
     * @return how entries there currently are in the HashTable
     */
    public int size(){
        int counter = 0;
        for (Node p : this.hashArray){
            while (p != null){
                counter+= 1;
                p = p.getNext();
            }
        }
        return counter;
    }
    
}
