package edu.yu.cs.com1320.project.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Comparator;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.yu.cs.com1320.project.Trie;

public class TrieImplTest {
    Trie<Integer> myTrie;
    String word1 = "apple";
    String word2 = "app";
    String word3 = "appearance";
    String word4 = "apply";
    String word5 = "air";
    String word6 = "no";
    String word7 = "nope";
    String word8 = "note";
    String word9 = "not";
    String word10 = "knot";
    String word11 = "noting";
    String word12 = "date";
    String word13 = "apps";
    String word14 = "Air";
    String word15 = "17Mi";
    String word16 = "17Miami";
    int incrementValue = 0;
    int incrementMultiValues = 100;

    
    @BeforeEach
    void b4(){
        myTrie = new TrieImpl<>();        
    }


    @Test
    void put(){
        //adding words
        myTrie.put(word1, ++incrementValue);
        myTrie.put(word2, ++incrementValue);
        myTrie.put(word3, ++incrementValue);
        myTrie.put(word4, ++incrementValue);
        myTrie.put(word5, ++incrementValue);
        myTrie.put(word6, ++incrementValue);
        myTrie.put(word7, ++incrementValue);
        myTrie.put(word8, ++incrementValue);
        myTrie.put(word9, ++incrementValue);
        myTrie.put(word10, ++incrementValue);
        myTrie.put(word11, ++incrementValue);
        myTrie.put(word12, ++incrementValue);
        myTrie.put(word13, ++incrementValue);
        myTrie.put(word14, ++incrementValue);
        myTrie.put(word15, ++incrementValue);
        myTrie.put(word16, ++incrementValue);
        //double up
        myTrie.put(word2, ++incrementMultiValues); //101
        myTrie.put(word2, ++incrementMultiValues); //102
        myTrie.put(word2, ++incrementMultiValues); //103
        myTrie.put(word5, ++incrementMultiValues); //104
    }

    @Test
    void get(){
        this.put();

        Set<Integer> s1 = myTrie.get(word1);
        assertEquals(1, s1.toArray().length);
        assertEquals(1, s1.toArray()[0]);

        Set<Integer> s2 = myTrie.get(word2);
        assertEquals(4, s2.toArray().length);
        System.out.println(s2); // prints int[] a81 = {2, 101, 102, 103};

        Set s3 = myTrie.get(word12);
        assertEquals(1, s3.toArray().length);
        assertEquals(12 ,s3.toArray()[0]);

        Set s4 = myTrie.get(word5);
        System.out.println(s4); // {5, 104}

        Set s5 = myTrie.get(word15);
        System.out.println(s5); // {15}

        Set s6 = myTrie.get("Date"); //miss
        assertNotNull(s6);
        assert(s6.isEmpty());

    }


    @Test
    void getSorted(){
        // tested
    }

    @Test
    void getAllWithPrefixSorted(){
        // tested
    }


    @Test
    void deleteAllWithPrefix(){
        this.put();
        myTrie.deleteAllWithPrefix("apple");

        Set sp1 = myTrie.get("apply");
        assert(!sp1.isEmpty());
        
        Set<Integer> s2 = myTrie.get("app");
        assertEquals(4, s2.toArray().length);

        Set<Integer> s1 = myTrie.get("apple");
        assert(s1.isEmpty());

        Set d1 = myTrie.deleteAllWithPrefix("17Miami");
        System.out.println("16 -> "+ d1);
        Integer tier2 = (Integer) myTrie.get("17Mi").toArray()[0];
        assertEquals(15, tier2);

        myTrie.put("17MiAmiChange", 999);
        assertEquals(999, myTrie.get("17MiAmiChange").toArray()[0]);

        Set d2 = myTrie.deleteAllWithPrefix("app");
        System.out.println("2, 3, 4, 101, 102, 103, 13  --> " + d2);
        Set d2retrievedapp = myTrie.get("app");
        assert(d2retrievedapp.isEmpty());
        Set d2retrievedapps = myTrie.get("apps");
        assert(d2retrievedapps.isEmpty());
        Set d2retrievedappearance = myTrie.get("appearance");
        assert(d2retrievedappearance.isEmpty());

    }


    @Test
    void deleteAll(){
        this.put();
        Set d1 = myTrie.deleteAll("17Miami");
        System.out.println("16 -> "+ d1);
        Integer tier2 = (Integer) myTrie.get("17Mi").toArray()[0];
        assertEquals(15, tier2);

        Set d2 = myTrie.deleteAll("app");
        System.out.println("2, 101, 102, 103  --> " + d2);
        assert(myTrie.get("app").isEmpty());
        Integer checkAbove = (Integer) myTrie.get("apps").toArray()[0];
        assertEquals(13, checkAbove);
    }

    @Test
    void delete(){
        this.put();

        Set<Integer> s7 = myTrie.get("note"); //initially here
        Integer temps7 = (Integer) s7.toArray()[0];
        assertEquals(1, s7.size());
        Integer s7a = myTrie.delete("note", 8);
        Set<Integer> s7b = myTrie.get("note"); //no longer here
        assert(s7b.isEmpty());
        assertEquals(s7a, temps7);

        Set s2 = myTrie.get(word2);
        assertEquals(4, s2.toArray().length);
        Integer ds2 = myTrie.delete("app", 102);
        System.out.println("{2, 101, 103} = " + myTrie.get("app"));
        assertEquals(102, ds2);

        Integer checkAbove = (Integer) myTrie.get("apps").toArray()[0];
        assertEquals(13, checkAbove);


        Integer sNull = myTrie.delete("17Miami", 18); //shouldn't delete anything
        assertNull(sNull);
        Integer t1 = (Integer) myTrie.get("17Miami").toArray()[0];
        assertEquals(16, t1);
        Integer sNotNull = myTrie.delete("17Miami", 16);
        assertNotNull(sNotNull);
        Integer tier2 = (Integer) myTrie.get("17Mi").toArray()[0];
        assertEquals(15, tier2);
    }


}
