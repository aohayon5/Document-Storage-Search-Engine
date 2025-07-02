package edu.yu.cs.com1320.project.stage2.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.stage2.DocumentStore;
import edu.yu.cs.com1320.project.stage2.DocumentStore.DocumentFormat;

class DocumentStoreImplTest {
 	URI uri1;
    URI uri2;
    URI uri3;
    URI uri4;
    URI uri5;
    URI uri6;
    URI uri7;
    URI uri8;
    URI uri9;
    URI uri10;
    URI uri11;
    URI uri12;
    URI uri13;
    URI uri14;
    URI uri15;


    URI uri1b;
    URI uri2b;
    URI uri3b;

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

    InputStream inputStream1;
    InputStream inputStream2;
    InputStream inputStream3;
    InputStream inputStream4;
    InputStream inputStream5;
    InputStream inputStream6;
    InputStream inputStream7;
    InputStream inputStream8;
    InputStream inputStream9;
    InputStream inputStream10;
    InputStream inputStream11;
    InputStream inputStream12;
    InputStream inputStream13;
    InputStream inputStream14;
    InputStream inputStream15;

    //Storage
    DocumentStore myDocStore;

    @BeforeEach
    void beforeDem() throws URISyntaxException{
        uri1 = new URI("uri1");
        uri2 = new URI("uri2");
        uri3 = new URI("uri3");
        uri4 = new URI("uri4");
        uri5 = new URI("uri5");
        uri6 = new URI("uri6");
        uri7 = new URI("uri7");
        uri8 = new URI("uri8");
        uri9 = new URI("uri9");
        uri10 = new URI("uri10");
        uri11 = new URI("uri11");
        uri12 = new URI("uri12");
        uri13 = new URI("uri13");
        uri14 = new URI("uri14");
        uri15 = new URI("uri15");    



        uri1b = new URI("uri1b");
        uri2b = new URI("uri2b");
        uri3b = new URI("uri3b");

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

        //Storage
        myDocStore = new DocumentStoreImpl();

        //inputstream
        inputStream1 = new ByteArrayInputStream("document1Content".getBytes());
        inputStream2 = new ByteArrayInputStream("document2Content".getBytes());
        inputStream3 = new ByteArrayInputStream("document3Content".getBytes());
        inputStream4 = new ByteArrayInputStream("document4Content".getBytes());
        inputStream5 = new ByteArrayInputStream("document5Content".getBytes());
        inputStream6 = new ByteArrayInputStream("document6Content".getBytes());
        inputStream7 = new ByteArrayInputStream("document7Content".getBytes());
        inputStream8 = new ByteArrayInputStream("document8Content".getBytes());
        inputStream9 = new ByteArrayInputStream("document9Content".getBytes());
        inputStream10 = new ByteArrayInputStream("document10Content".getBytes());
        inputStream11 = new ByteArrayInputStream("document11Content".getBytes());
        inputStream12 = new ByteArrayInputStream("document12Content".getBytes());
        inputStream13 = new ByteArrayInputStream("document13Content".getBytes());
        inputStream14 = new ByteArrayInputStream("document14Content".getBytes());
        inputStream15 = new ByteArrayInputStream("document15Content".getBytes());
    }

    @Test
    void put() throws IOException, URISyntaxException{
        InputStream inputStreamx = new ByteArrayInputStream("document1Content".getBytes());
        
        URI urixNull = null;
        assertThrows(IllegalArgumentException.class,  ()->{
            myDocStore.put(inputStreamx, urixNull, DocumentFormat.BINARY);
        });

        URI urix = new URI("");
        assertThrows(IllegalArgumentException.class,  ()->{
            myDocStore.put(inputStreamx, urix, DocumentFormat.BINARY);
        });

        assertThrows(IllegalArgumentException.class,  ()->{
            myDocStore.put(inputStreamx, uri1, null);
        });
        //checking that it returns right stuff
        int check1 = myDocStore.put(inputStream1, uri1, DocumentFormat.TXT);
        assertEquals(0, check1);
        Document temp1 = myDocStore.get(uri1);
        int hc1 = temp1.hashCode();
        int check2 = myDocStore.put(inputStream2, uri1, DocumentFormat.BINARY);
        assertEquals(hc1, check2);

        //add something else to the docStore
        myDocStore.put(inputStream3, uri3, DocumentFormat.TXT);

        //checking delete
        //assertEquals(2, DocumentStoreImpl.docStore.size());

        int delMiss = myDocStore.put(null, uri2, DocumentFormat.TXT);
        assertEquals(0, delMiss);
        //assertEquals(2, DocumentStoreImpl.docStore.size());

        int tempHC = myDocStore.get(uri1).hashCode();
        int del = myDocStore.put(null, uri1, DocumentFormat.TXT);
        assertEquals(del, tempHC);
        //assertEquals(1, DocumentStoreImpl.docStore.size());

        String third_document_text = myDocStore.get(uri3).getDocumentTxt();
        assertEquals("document3Content", third_document_text);
    }
    

    @Test
    void get() throws IOException{
        myDocStore.put(inputStream1, uri1, DocumentFormat.TXT);
        myDocStore.put(inputStream2, uri2, DocumentFormat.BINARY);
        assertEquals("document1Content", myDocStore.get(uri1).getDocumentTxt());
    }


    @Test
    void delete() throws IOException{
        myDocStore.put(inputStream1, uri1, DocumentFormat.TXT);
        myDocStore.put(inputStream2, uri2, DocumentFormat.BINARY);
        boolean wasDeleted = myDocStore.delete(uri1);
        assertTrue(wasDeleted);
        boolean wasDeleted2 = myDocStore.delete(uri3);
        assertFalse(wasDeleted2);
    }
    @Test
    void revisedDelete() throws IOException{
        myDocStore.put(inputStream1, uri1, DocumentFormat.TXT);
        myDocStore.put(inputStream2, uri2, DocumentFormat.BINARY);
        myDocStore.put(inputStream3, uri3, DocumentFormat.TXT);
        myDocStore.put(inputStream4, uri4, DocumentFormat.BINARY);
        myDocStore.put(inputStream5, uri5, DocumentFormat.TXT);
        myDocStore.put(inputStream6, uri6, DocumentFormat.BINARY);
        myDocStore.put(inputStream7, uri7, DocumentFormat.TXT);
        myDocStore.put(inputStream8, uri8, DocumentFormat.BINARY);
        myDocStore.put(inputStream9, uri9, DocumentFormat.TXT);
        myDocStore.put(inputStream10, uri10, DocumentFormat.BINARY);
        myDocStore.put(inputStream11, uri11, DocumentFormat.TXT);
        myDocStore.put(inputStream12, uri12, DocumentFormat.BINARY);
        myDocStore.put(inputStream13, uri13, DocumentFormat.TXT);
        myDocStore.put(inputStream14, uri14, DocumentFormat.BINARY);
        myDocStore.put(inputStream15, uri15, DocumentFormat.TXT);
        assertNotNull(myDocStore.get(uri1));
        myDocStore.delete(uri1);
        assertNull(myDocStore.get(uri1));

        assertTrue(myDocStore.delete(uri10));
        assertNull(myDocStore.get(uri10));
        assertFalse(myDocStore.delete(uri10));

        
        //System.out.println(DocumentStoreImpl.docStore.keySet());
        assertFalse(myDocStore.delete(uri10));

    }

    @Test
    void setMetadata() throws IOException{
        myDocStore.put(inputStream1, uri1, DocumentFormat.TXT);
        myDocStore.put(inputStream2, uri2, DocumentFormat.BINARY);
        myDocStore.get(uri1).setMetadataValue("first", "firstb");
        String temp1 = myDocStore.get(uri1).setMetadataValue("second", "secondb");
        assertNull(temp1);
        assertEquals("firstb", myDocStore.get(uri1).getMetadataValue("first"));
        assertEquals("secondb", myDocStore.get(uri1).getMetadataValue("second"));
        String temp2 = myDocStore.get(uri1).setMetadataValue("second", "secondNew");
        assertEquals("secondb", temp2);
        assertEquals("secondNew", myDocStore.get(uri1).getMetadataValue("second"));
    }   

    //repeated for the purpose of design, essentially tested within the same stanza
    @Test
    void getMetadata() throws IOException{
        myDocStore.put(inputStream1, uri1, DocumentFormat.TXT);
        myDocStore.put(inputStream2, uri2, DocumentFormat.BINARY);
        myDocStore.get(uri1).setMetadataValue("first", "firstb");
        myDocStore.get(uri1).setMetadataValue("second", "secondb");
        assertEquals("firstb", myDocStore.get(uri1).getMetadataValue("first"));
        assertEquals("secondb", myDocStore.get(uri1).getMetadataValue("second"));
    }




	





    //assertThrows(IllegalArgumentException.class,  ()->{
            
    //});
}
