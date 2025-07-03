package edu.yu.cs.com1320.project.stage4.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.net.URISyntaxException;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.stage4.Document;

public class DocumentImplTest {
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
    void createNewDocTxt() throws URISyntaxException{
        String txt = "some text";
        String txts1 = null;
        assertThrows(IllegalArgumentException.class,  ()->{
            Document doc = new DocumentImpl(uri1, txts1);
        });
        URI uri = null;
        assertThrows(IllegalArgumentException.class,  ()->{
            Document doc = new DocumentImpl(uri, txt);
        });
        URI uris1 = new URI("");
        assertThrows(IllegalArgumentException.class,  ()->{
            Document doc = new DocumentImpl(uris1, txt);
        });
        String txts2 = "";
        assertThrows(IllegalArgumentException.class,  ()->{
            Document doc = new DocumentImpl(uri1, txts2);
        });        
    }

    @Test
    void createNewDocBin() throws URISyntaxException{
        byte[] bas =  "safe".getBytes(StandardCharsets.UTF_8);
        URI urix = null;
        assertThrows(IllegalArgumentException.class,  ()->{
            Document doc = new DocumentImpl(urix, bas);
        });
        byte[] baw = null;
        assertThrows(IllegalArgumentException.class,  ()->{
            Document doc = new DocumentImpl(uri1b, baw);
        });
        URI urisx1 = new URI("");
        assertThrows(IllegalArgumentException.class,  ()->{
            Document doc = new DocumentImpl(urisx1, bas);
        });
        byte[] ba = "".getBytes(StandardCharsets.UTF_8);
        assertThrows(IllegalArgumentException.class,  ()->{
            Document doc = new DocumentImpl(uri1b, ba);
        });
    }

    @Test
    void setMetadataValue(){
        assertThrows(IllegalArgumentException.class,  ()->{
            doc1_txt.setMetadataValue("", "y");
        });
        assertThrows(IllegalArgumentException.class,  ()->{
            doc1_txt.setMetadataValue(null, "y");
        });
        String r1 = doc1_txt.setMetadataValue("1", "1b");
        String r2 = doc1_txt.setMetadataValue("2", "2b");
        assertEquals(r1, null);
        assertEquals(doc1_txt.getMetadataValue("1"), "1b");
        String r3 = doc1_txt.setMetadataValue("1", "mwaha");
        assertEquals(r3, "1b");
        assertEquals(doc1_txt.getMetadataValue("1"), "mwaha");
    }

    @Test
    void getMetadataValue(){
        assertThrows(IllegalArgumentException.class,  ()->{
            doc1_b.setMetadataValue("", "y");
        });
        assertThrows(IllegalArgumentException.class,  ()->{
            doc1_b.setMetadataValue(null, "y");
        });
        String f1 = doc1_b.getMetadataValue("1");
        assertEquals(f1, null);
        String r1 = doc1_b.setMetadataValue("1", "1b");
        assertEquals("1b", doc1_b.getMetadataValue("1"));
    }

    @Test
    void getMetadata(){
        doc1_b.setMetadataValue("1", "1b");
        doc1_b.setMetadataValue("2", "2b");
        doc1_b.setMetadataValue("1", "new 1b");
        doc1_b.setMetadataValue("3", "3 A");
        HashTable myHm = doc1_b.getMetadata();
        assertEquals(3, myHm.size());
        assertEquals("new 1b", myHm.get("1"));
        assertEquals("3 A", myHm.get("3"));
        assertEquals("2b", myHm.get("2"));
    }

    @Test
    void getDocumentTxt(){
        assertEquals("text1", doc1_txt.getDocumentTxt());
        assertEquals(null, doc1_b.getDocumentTxt());
    }

    @Test
    void getDocumentBinaryData(){
        assertEquals(ba1, doc1_b.getDocumentBinaryData());
        assertEquals(null, doc1_txt.getDocumentBinaryData());
    }

    @Test
    void getKey(){
        assertEquals(uri1, doc1_txt.getKey());
        assertEquals(uri2b, doc2_b.getKey());
    }


    @Test
    void wordCount(){
        assertEquals(3, doc4WordCounts.wordCount("and"));
        assertEquals(1, doc4WordCounts.wordCount("my"));
        assertEquals(1, doc4WordCounts.wordCount("name"));
        assertEquals(2, doc4WordCounts.wordCount("is"));
        assertEquals(1, doc4WordCounts.wordCount("And"));
        assertEquals(1, doc4WordCounts.wordCount("Joe"));
        assertEquals(1, doc4WordCounts.wordCount("hi"));
        assertEquals(0, doc4WordCounts.wordCount("nothing"));
        assertEquals(0, doc1_b.wordCount("and"));
    }

    @Test
    void getWords(){
        System.out.println(doc4WordCounts.getWords());
        System.out.println(doc1_b.getWords());
        System.out.println(doc1_txt.getWords());
    }



    @Test
    void hashCodeCheck(){
        int hc1 = docSA1_txt.hashCode();
        int hc2 = doc1_txt.hashCode();
        assertEquals(hc1, hc2);
        int hc3 = docSA1_b.hashCode();
        int hc4 = doc1_b.hashCode();
        assertEquals(hc3, hc4);
    }

    @Test
    void equalsTest(){
        assertEquals(docSA1_b, doc1_b);
        assertEquals(doc1_txt, docSA1_txt);
        assertNotEquals(doc2_b, doc1_b);
    }
}
