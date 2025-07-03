
package edu.yu.cs.com1320.project.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.impl.DocumentImpl;

public class MinHeapImplTest {
    private MinHeapImpl<Document> minHeap;
    URI uri1;
    URI uri2;
    URI uri3;

    URI uri1b;
    URI uri2b;
    URI uri3b;

    Document doc4WordCounts;

    Document doc1_txt;
    Document doc2_txt;
    Document docSTA1_txt;
    Document docSURI1_txt;
    Document docSA1_txt;

    byte[] ba1;
    byte[] ba2;
    byte[] ba3;
    
    Document doc1_b;
    Document doc2_b;
    Document docSTA1_b;
    Document docSURI1_b;
    
    Document docSA1_b;
    
    @BeforeEach
    public void setUp() {
        minHeap = new MinHeapImpl<>();
    }

    @BeforeEach
    void beforeDem() throws URISyntaxException{
        uri1 = new URI("uri1");
        uri2 = new URI("uri2");
        uri3 = new URI("uri3");

        uri1b = new URI("uri1b");
        uri2b = new URI("uri2b");
        uri3b = new URI("uri3b");

        // {and: 3, my: 1, name: 1, is: 2, And: 1, Joe: 1, hi: 1}
        doc4WordCounts = new DocumentImpl(uri1, "and my name is And is Joe and hi and");


        doc1_txt = new DocumentImpl(uri1, "text1");
        doc2_txt = new DocumentImpl(uri2, "text2");
        docSTA1_txt = new DocumentImpl(uri3, "text1");
        docSURI1_txt = new DocumentImpl(uri1, "text3");
        docSA1_txt =  new DocumentImpl(uri1, "text1");

        ba1 = "text1b".getBytes(StandardCharsets.UTF_8);
        ba2 = "text2b".getBytes(StandardCharsets.UTF_8);
        ba3 = "text3b".getBytes(StandardCharsets.UTF_8);

        doc1_b = new DocumentImpl(uri1b, ba1);
        doc2_b = new DocumentImpl(uri2b, ba2);
        docSTA1_b = new DocumentImpl(uri3b, ba1);
        docSURI1_b = new DocumentImpl(uri1b, ba3);
        docSA1_b = new DocumentImpl(uri1b, ba1);
    }


    @Test
    void reheapify(){
        minHeap.insert(doc1_txt);
        minHeap.insert(doc2_b);
        minHeap.insert(doc2_txt);
        assertEquals(doc1_txt, minHeap.remove());
        assertEquals(doc2_txt, minHeap.remove());
        assertEquals(doc2_b, minHeap.remove());

        //reheapify doc2_txt
        minHeap.insert(doc1_txt);
        minHeap.insert(doc2_b);
        minHeap.insert(doc2_txt);

        doc1_txt.setLastUseTime(System.nanoTime());
        minHeap.reHeapify(doc1_txt);
        
        assertEquals(doc2_txt, minHeap.remove());
        assertEquals(doc2_b, minHeap.remove());
        assertEquals(doc1_txt, minHeap.remove());
    }

    @Test
    void getArrayIndex(){
        minHeap.insert(doc1_txt);
        minHeap.insert(doc2_b);
        minHeap.insert(doc2_txt);
        minHeap.insert(doc1_b);
        assert(doc1_txt.getLastUseTime() < doc2_txt.getLastUseTime());
        assert(doc2_txt.getLastUseTime() < doc1_b.getLastUseTime());
        assert(doc1_b.getLastUseTime() < doc2_b.getLastUseTime());
        assertEquals(1, minHeap.getArrayIndex(doc1_txt));
        assertEquals(3, minHeap.getArrayIndex(doc2_txt));
        assertEquals(2, minHeap.getArrayIndex(doc1_b));
        assertEquals(4, minHeap.getArrayIndex(doc2_b));
    }

    @Test
    void doubleArraySize(){
        //obvious
    }
}