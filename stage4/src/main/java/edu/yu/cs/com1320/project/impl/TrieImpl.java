package edu.yu.cs.com1320.project.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.yu.cs.com1320.project.Trie;
import edu.yu.cs.com1320.project.stage4.Document;

public class TrieImpl<Value> implements Trie<Value>{
    private Node<Value> root;
    private Set<Value> cheatSetForDeletedNodesValues; //used for deletes to keep track of teh values associated with deleted Node
    public TrieImpl(){
        this.root = new Node<>();
    }


    private class Node<Value>{
        private Set<Value> allVals;
        private Node<Value>[] links;
        private Node(){
            this.allVals = new HashSet<>();
            this.links = new Node[62];
        }
    }

    private String filter(String key){
        return key.replaceAll("[^a-zA-Z0-9\\s]", "");
    }

    @Override
    public void put(String key, Value val) {
        key = this.filter(key);
        if (val == null){
            //this.deleteAll(key); no need to delete here
        }
        else{
            this.root = this.put(this.root,key,val, 0);
        }
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d){
        if (x == null){
            x = new Node<>();
        }

        //add this value
        if (d == key.length()){
            x.allVals.add(val);
            return x;
        }
        
        char c = key.charAt(d);
        int index = this.charToIndex(c);
        x.links[index] = this.put(x.links[index], key, val, d + 1);
        return x;
    }

    private int charToIndex(char c){
        if (c >= 'A' && c <= 'Z'){
            return c - 'A'; // Uppercase letters
        } 
        else if (c >= 'a' && c <= 'z'){
            return 26 + (c - 'a'); // Lowercase letters
        } 
        else{ // Digits (c >= '0' && c <= '9')
            return 52 + (c - '0'); 
        }
    }

    private char IndexToChar(int index){
        if (index >=0 && index <= 25){
            return (char) (65 + index);
        }
        else if (index >=26 && index <= 51){
            index -= 26;
            return (char) (97 + index);
        }
        else{
            index -= 52;
            return (char) (48 + index);
        }
    }



    @Override
    public List<Value> getSorted(String key, Comparator<Value> comparator) {
       List<Value> listing = new ArrayList<>();
       listing.addAll(this.get(key));
       Collections.sort(listing, comparator);
       return listing;
    }

    @Override
    public Set<Value> get(String key) {
        Node<Value> targetNode = this.get(this.root, key, 0);
        if (targetNode == null){
            return new HashSet<>();
        }
        else{
            return targetNode.allVals;
        }
        
    }

    private Node<Value> get(Node<Value> x, String key, int d){
        if (x == null){
            return null;
        }

        if (d == key.length()){
            return x;
        }

        char c = key.charAt(d);
        int index = this.charToIndex(c);
        return this.get(x.links[index], key, d+1);
    }


    @Override
    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator) {
        //everything from asbase Node and onn will need all Values gathered
        List<Value> listings = new ArrayList<>();
        Set<Value> valuesAll = new HashSet<>();
        Node<Value> abase = this.get(this.root, prefix, 0);
        getAllWithPrefixSet(abase, prefix, valuesAll);
        listings.addAll(valuesAll);
        /*for (Value doc : listings){
            System.out.println(((Document) doc).getKey());
        }*/

        Collections.sort(listings, comparator);
        return listings;
    }

    private void getAllWithPrefixSet(Node<Value> x, String prefix, Set<Value> ValuesAll){
        if(x == null){
            return;
        }       
        for (int i = 0; i < 62; i++){
            if (x.links[i] != null){
                getAllWithPrefixSet(x.links[i], prefix + IndexToChar(i), ValuesAll);
            }
        }
        ValuesAll.addAll(x.allVals);
    }



    @Override
    public Set<Value> deleteAllWithPrefix(String prefix) {
        Node<Value> abase = this.get(this.root, prefix, 0);
        Set<Value> valuesAll = new HashSet<>();
        getAllWithPrefixSet(abase, prefix, valuesAll);
        Set<Value> Alldeleted = new HashSet<>();
        Alldeleted.addAll(valuesAll);
        if (abase == null){
            return Alldeleted;
        }
        //up until here I have the base and all of the values with those prefixes
        for (int i = 0; i < 62; i++){
            abase.links[i] = null;
        }
        deleteAll(prefix);
        return Alldeleted;
    }
    

    @Override
    public Set<Value> deleteAll(String key) {
        cheatSetForDeletedNodesValues = new HashSet<>();
        this.root = deleteAll(this.root,key, 0);
        return cheatSetForDeletedNodesValues;
    }

    private Node<Value> deleteAll(Node<Value> x, String key, int d){
        if (x == null){
            return null;
        }
        if (d == key.length()){
            cheatSetForDeletedNodesValues.addAll(x.allVals);
            x.allVals = new HashSet<>(); //removes all of the values
        }
        else{
            char c = key.charAt(d);
            int index = this.charToIndex(c);
            x.links[index] = this.deleteAll(x.links[index], key, d + 1);
        }

        if (!x.allVals.isEmpty()){
            return x;   //has a value associated with it
        }

        for (int i = 0; i < 62; i++){
            if (x.links[i] != null){
                return x;
            }
        }
        return null; // removes / nulls the link as it does not have any continuation Nodes
    }

    @Override
    public Value delete(String key, Value val) {
        cheatSetForDeletedNodesValues = new HashSet<>();
        this.root = delete(this.root, key, val, 0);
        return cheatSetForDeletedNodesValues.isEmpty() ? null: (Value) cheatSetForDeletedNodesValues.toArray()[0];
    }

    private Node<Value> delete(Node<Value> x, String key, Value val, int d){
        
        if (x == null){ // miss
            return null;
        }

        if (d == key.length()){
            boolean deleted = x.allVals.remove(val);
            if (deleted){
                cheatSetForDeletedNodesValues.add(val);
            }
            else{
                cheatSetForDeletedNodesValues.add(null);
            }
        }
        else{
            char c = key.charAt(d);
            int index = this.charToIndex(c);
            x.links[index] = this.delete(x.links[index], key, val, d+1); //point of setting link, should already be connected
        }
        if (!x.allVals.isEmpty()){
            return x;
        }

        for (int i = 0; i <62; i++){
            if (x.links[i] != null){
                return x;
            }
        }
        return null;
    }

}
