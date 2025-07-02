package edu.yu.cs.com1320.project.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.yu.cs.com1320.project.HashTable;


public class HashTableImplTest {

    HashTable<String, String> myStrHashTable1;
    String mdk1;
    String mdv1;
    
    String mdk2;
    String mdv2;
   
    String mdk3;
    String mdv3;

    String mdk4;
    String mdv4;

    String mdk5;
    String mdv5;

    String mdk6;
    String mdv6;

    String mdk7;
    String mdv7;

    String mdk8;
    String mdv8;





    @BeforeEach
    void BeforeDem(){
        myStrHashTable1 = new HashTableImpl();
        mdk1 = "a";
        mdv1 = "av";
    
        mdk2 = "b";
        mdv2 = "bv";
   
        mdk3 = "c";
        mdv3 = "cv";

        mdk4 = "d";
        mdv4 = "dv";

        mdk5 = "e";
        mdv5 = "ev";

        mdk6 = "f";
        mdv6 = "fv";

        mdk7 = "g";
        mdv7 = "gv";

        mdk8 = "h";
        mdv8 = "hv";
    }
   

    @Test
    void put(){
        assertEquals(null, myStrHashTable1.put(mdk1, mdv1));
        myStrHashTable1.put(mdk2, mdv2);
        myStrHashTable1.put(mdk3, mdv3);
        myStrHashTable1.put(mdk4, mdv4);
        myStrHashTable1.put(mdk5, mdv5);
        myStrHashTable1.put(mdk6, mdv6);
        myStrHashTable1.put(mdk7, mdv7);
        myStrHashTable1.put(mdk8, mdv8);

        //replace d valuew and g
        assertEquals("dv", myStrHashTable1.put(mdk4, "newdv4"));
        assertEquals("gv", myStrHashTable1.put(mdk7, "newgv7"));


        //tests size
        assertEquals(8, myStrHashTable1.size());

        //delete values
        assertEquals("av", myStrHashTable1.put(mdk1, null));
        assertNull(myStrHashTable1.get(mdk1));
        assertEquals(null, myStrHashTable1.put(mdk1, null));

        assertEquals("newgv7", myStrHashTable1.put(mdk7, null));
        
        //assertNull(myStrHashTable1.get);
        System.out.println(myStrHashTable1.keySet());
        System.out.println(myStrHashTable1.values());
    }

    @Test
    void retryingDelete(){
        myStrHashTable1.put(mdk2, mdv2);
        myStrHashTable1.put(mdk3, mdv3);
        myStrHashTable1.put(mdk4, mdv4);
        myStrHashTable1.put(mdk5, mdv5);
        myStrHashTable1.put(mdk6, mdv6);
        myStrHashTable1.put(mdk7, mdv7);
        myStrHashTable1.put(mdk8, mdv8);
        myStrHashTable1.put(mdk8, null);
        assertNull(myStrHashTable1.get(mdk8));
        myStrHashTable1.put(mdk3, null);
        assertNull(myStrHashTable1.get(mdk3));
        assertEquals(null, myStrHashTable1.get(mdk3));
    }

    @Test
    void retryingDeleteV2(){
        // Put 15 strings into the hash table
        for (int i = 0; i < 15; i++) {
            myStrHashTable1.put("Key" + i, i + "");
        }

        // Delete one of the keys
        assertNotNull(myStrHashTable1.put("Key5", null));

        // Attempt to delete the same key again
        assertNull(myStrHashTable1.put("Key5", null));
    }
 

    @Test
    void get(){
        myStrHashTable1.put(mdk2, mdv2);
        myStrHashTable1.put(mdk3, mdv3);
        myStrHashTable1.put(mdk4, mdv4);
        myStrHashTable1.put(mdk5, mdv5);
        myStrHashTable1.put(mdk6, mdv6);
        myStrHashTable1.put(mdk7, mdv7);
        myStrHashTable1.put(mdk8, mdv8);
        assertNull(myStrHashTable1.get(mdk1));
        assertEquals(mdv4, myStrHashTable1.get(mdk4));
        assertEquals("dv", myStrHashTable1.put(mdk4, null));
        assertNull(myStrHashTable1.get(mdk4));
    }

    @Test
    void containsKey(){
        myStrHashTable1.put(mdk1, mdv1);
        myStrHashTable1.put(mdk2, mdv2);
        myStrHashTable1.put(mdk3, mdv3);
        myStrHashTable1.put(mdk4, mdv4);
        myStrHashTable1.put(mdk5, mdv5);
        assertFalse(myStrHashTable1.containsKey(mdk6));
        assertTrue(myStrHashTable1.containsKey(mdk1));
        assertFalse(myStrHashTable1.containsKey("arwa"));
    }

    @Test
    void keySet(){
        myStrHashTable1.put(mdk2, mdv2);
        myStrHashTable1.put(mdk3, mdv3);
        myStrHashTable1.put(mdk4, mdv4);
        myStrHashTable1.put(mdk5, mdv5);
        myStrHashTable1.put(mdk6, mdv6);
        myStrHashTable1.put(mdk7, mdv7);
        myStrHashTable1.put(mdk8, mdv8);
        myStrHashTable1.put(mdk8, "randomc hange");
        Set<String> myKeys = myStrHashTable1.keySet();
        assertEquals(7, myKeys.size());
        System.out.println(myKeys);
    }

    @Test
    void values(){
        myStrHashTable1.put(mdk2, mdv2);
        myStrHashTable1.put(mdk3, mdv3);
        myStrHashTable1.put(mdk4, mdv4);
        myStrHashTable1.put(mdk5, mdv5);
        myStrHashTable1.put(mdk6, mdv6);
        myStrHashTable1.put(mdk7, mdv7);
        myStrHashTable1.put(mdk8, mdv8);
        myStrHashTable1.put(mdk8, "randomc change");
        myStrHashTable1.put(mdk3, null);
        Collection<String> myCollection = myStrHashTable1.values();
        assertEquals(6, myCollection.size());
        System.out.println(myCollection);
    }


    @Test
    void size(){
        assertEquals(0, myStrHashTable1.size());
        myStrHashTable1.put(mdk2, mdv2);
        myStrHashTable1.put(mdk3, mdv3);
        myStrHashTable1.put(mdk4, mdv4);
        myStrHashTable1.put(mdk5, mdv5);
        myStrHashTable1.put(mdk6, mdv6);
        assertEquals(5, myStrHashTable1.size());
        myStrHashTable1.put(mdk7, mdv7);
        myStrHashTable1.put(mdk8, mdv8);
        assertEquals(7, myStrHashTable1.size());
        myStrHashTable1.put(mdk8, "randomc change");
        assertEquals(7, myStrHashTable1.size());
        assertEquals(mdv3, myStrHashTable1.put(mdk3, null));
        assertEquals(6, myStrHashTable1.size());
        Collection<String> myCollection = myStrHashTable1.values();
        assertEquals(6, myCollection.size());
        System.out.println(myCollection);
    }
    
}
