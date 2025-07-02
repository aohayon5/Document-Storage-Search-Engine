package edu.yu.cs.com1320.project.stage6.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.stage6.Document;
import edu.yu.cs.com1320.project.stage6.DocumentStore;
import edu.yu.cs.com1320.project.stage6.DocumentStore.DocumentFormat;

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


    InputStream is1;
    InputStream is2;
    InputStream is3;
    InputStream is4;


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
    void beforeDem() throws URISyntaxException, IOException{
        uri1 = new URI("http://www.yu.edu/documents/doc1");
        uri2 = new URI("http://www.yu.edu/notDocument/doc2");
        uri3 = new URI("http://www.yu.edu/documents/doc3");
        uri4 = new URI("http://www.yu.edu/notDocument/doc4");
        uri5 = new URI("http://www.yu.edu/documents/doc5");
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

        doc1_txt = new DocumentImpl(uri1, "text1", null);
        doc2_txt = new DocumentImpl(uri2, "text2", null);
        docSTA1_txt = new DocumentImpl(uri3, "text1", null);
        docSURI1_txt = new DocumentImpl(uri1, "text3", null);
        docSA1_txt =  new DocumentImpl(uri1, "text1", null);

        ba1 = "text1b".getBytes(StandardCharsets.UTF_8);
        ba2 = "text2b".getBytes(StandardCharsets.UTF_8);
        ba3 = "text3b".getBytes(StandardCharsets.UTF_8);

        doc1_b = new DocumentImpl(uri1b, ba1);
        doc2_b = new DocumentImpl(uri2b, ba2);
        docSTA1_b = new DocumentImpl(uri3b, ba1);
        docSURI1_b = new DocumentImpl(uri1b, ba3);
        docSA1_b = new DocumentImpl(uri1b, ba1);

        //Storage
        myDocStore = new DocumentStoreImpl(new File("C:/Users/flash/.appearMe"));

        String word1 = "apple ";
        String word2 = "app ";
        String word3 = "appearance ";
        String word4 = "apply ";
        String word5 = "air ";
        String word6 = "no ";
        String word7 = "nope ";
        String word8 = "note ";
        String word9 = "not ";
        String word10 = "knot ";
        String word11 = "noting ";
        String word12 = "date ";
        String word13 = "apps ";
        String word14 = "Air ";
        String word15 = "17Mi ";
        String word16 = "17Miami ";


        //combos
        //3 apple, 1 app, 1 appearance, 2 air     5 words with "app"
        String combo1 = word1 + word1 + word1 + word2 + word3 + word5 + word5;
        //2 apple, 2 app, 2 17Miami,            3 words with "app"
        String combo2 = word1 + word1+  word2 + word2 + word16 + word16; 
        //1 nope, 1 note, 1 knot, 1 Air         0 words with "app"
        String combo3 = word7 + word8 + word10 + word14;
        //3 apply, 1 no, 1 not, 1 note, 1 noting, 1 date, 
        //2 apps, 1 17Miami, 1 nope, 1 apple     6 words with app
        String combo4 = word4 + word6 + word9 + word11 + word12 + 
        word13 + word16 + word13 + word4 + word7 + word1 + word4;

        //word 15 does not appear in any

        //inputStream4Stage4
        is1 = new ByteArrayInputStream(combo1.getBytes());
        is2 = new ByteArrayInputStream(combo2.getBytes());
        is3 = new ByteArrayInputStream(combo3.getBytes());
        is4 = new ByteArrayInputStream(combo4.getBytes());

        
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

    void resetDocStore() throws IOException{
        this.myDocStore = new DocumentStoreImpl();
        is1.reset();
        is2.reset();
        is3.reset();
        is4.reset();
        inputStream1.reset();
        this.myDocStore.put(is1, uri1, DocumentFormat.TXT);
        this.myDocStore.put(is2, uri2, DocumentFormat.TXT);
        this.myDocStore.put(is3, uri3, DocumentFormat.TXT);
        this.myDocStore.put(is4, uri4, DocumentFormat.TXT);
        
        //setMetadata
        //doc 2 and 4 will be the same, 1 will have a wrong key, 3 will have wrong value
        //need another document
        this.myDocStore.put(inputStream1, uri5, DocumentFormat.TXT); // wrong amount

        //wrong second key
        this.myDocStore.setMetadata(uri1, "kR1", "vR1");
        this.myDocStore.setMetadata(uri1, "kW2", "vR2");

        //matches 4
        this.myDocStore.setMetadata(uri2, "kR1", "vR1");
        this.myDocStore.setMetadata(uri2, "kR2", "vR2");

        //wrong second value
        this.myDocStore.setMetadata(uri3, "kR1", "vR1");
        this.myDocStore.setMetadata(uri3, "kR2", "vW2");

        //matches 2nd
        this.myDocStore.setMetadata(uri4, "kR1", "vR1");
        this.myDocStore.setMetadata(uri4, "kR2", "vR2");

        //wrong amount
        this.myDocStore.setMetadata(uri5, "kR2", "vR2");
    }


    //@BeforeEach
    void initializeDocStore() throws IOException{
        this.myDocStore.put(is1, uri1, DocumentFormat.TXT);
        this.myDocStore.put(is2, uri2, DocumentFormat.TXT);
        this.myDocStore.put(is3, uri3, DocumentFormat.TXT);
        this.myDocStore.put(is4, uri4, DocumentFormat.TXT);

        //setMetadata
        //doc 2 and 4 will be the same, 1 will have a wrong key, 3 will have wrong value
        //need another document
        this.myDocStore.put(inputStream1, uri5, DocumentFormat.TXT); // wrong amount

        //wrong second key
        this.myDocStore.setMetadata(uri1, "kR1", "vR1");
        this.myDocStore.setMetadata(uri1, "kW2", "vR2");

        //matches 4
        this.myDocStore.setMetadata(uri2, "kR1", "vR1");
        this.myDocStore.setMetadata(uri2, "kR2", "vR2");

        //wrong second value
        this.myDocStore.setMetadata(uri3, "kR1", "vR1");
        this.myDocStore.setMetadata(uri3, "kR2", "vW2");

        //matches 2nd
        this.myDocStore.setMetadata(uri4, "kR1", "vR1");
        this.myDocStore.setMetadata(uri4, "kR2", "vR2");

        //wrong amount
        this.myDocStore.setMetadata(uri5, "kR2", "vR2");
    }


    @Test
    void stage6PushOverLimitDelete() throws IOException{
        Document d1 = new DocumentImpl(uri1, "document1Content", null);
        this.myDocStore.put(is1, uri1, DocumentFormat.TXT);
        this.myDocStore.put(is2, uri2, DocumentFormat.TXT);
        this.myDocStore.put(is3, uri3, DocumentFormat.TXT);
        this.myDocStore.setMaxDocumentCount(2);
        this.myDocStore.delete(uri2);
        this.myDocStore.put(is4, uri4, DocumentFormat.TXT);
        this.myDocStore.undo(uri2);
        
    }


    @Test
    void testingMyHeap() throws IOException{
        resetDocStore();
        //oldest (Min) uri1, uri2, uri3, uri4, uri5
        myDocStore.setMaxDocumentCount(3);
        assertNull(myDocStore.get(uri1));
        assertNull(myDocStore.get(uri2));
        assertNotNull(myDocStore.get(uri3));
        assertNotNull(myDocStore.get(uri4));
        assertNotNull(myDocStore.get(uri5));

        resetDocStore();
        myDocStore.search("apple");
        myDocStore.setMaxDocumentCount(3);
        assertNotNull(myDocStore.get(uri1));
        assertNotNull(myDocStore.get(uri2));
        assertNotNull(myDocStore.get(uri4));
        assertNull(myDocStore.get(uri3));
        assertNull(myDocStore.get(uri5));
        myDocStore.put(inputStream2, uri6, DocumentFormat.TXT);
        assertNull(myDocStore.get(uri1));

        resetDocStore();
        myDocStore.getMetadata(uri1, "kR1");
        myDocStore.setMaxDocumentCount(4);
        assertNotNull(myDocStore.get(uri1));
        assertNull(myDocStore.get(uri2));

        resetDocStore();
        myDocStore.searchByPrefix("app");
        myDocStore.setMaxDocumentCount(3);
        assertNotNull(myDocStore.get(uri1));
        assertNotNull(myDocStore.get(uri2));
        assertNotNull(myDocStore.get(uri4));
        assertNull(myDocStore.get(uri3));
        assertNull(myDocStore.get(uri5));

        //Metadata search
        resetDocStore();
        Map <String, String> kv = new HashMap<>();
        kv.put("kR1", "vR1");
        List<Document> v = myDocStore.searchByMetadata(kv);
        myDocStore.setMaxDocumentCount(4);
        for (Document doc : v){
            //System.out.println(doc.getKey());
        }
        assertNotNull(myDocStore.get(uri1));
        assertNotNull(myDocStore.get(uri2));
        assertNotNull(myDocStore.get(uri3));
        assertNotNull(myDocStore.get(uri4));
        assertNull(myDocStore.get(uri5));

        //more metadata test
        resetDocStore();
        Map <String, String> ab = new HashMap<>();
        ab.put("a", "b");
        this.myDocStore.setMetadata(uri1, "a", "b");
        this.myDocStore.setMetadata(uri3, "a", "b");
        //reset pointers
        myDocStore.get(uri1); myDocStore.get(uri2); myDocStore.get(uri3);
        myDocStore.get(uri4); myDocStore.get(uri5);
        this.myDocStore.searchByMetadata(ab);
        myDocStore.setMaxDocumentCount(4);
        assertNotNull(myDocStore.get(uri1));
        assertNull(myDocStore.get(uri2));
        assertNotNull(myDocStore.get(uri3));
        assertNotNull(myDocStore.get(uri4));
        assertNotNull(myDocStore.get(uri5));


        //keyword and metadata
        resetDocStore();
        Map <String, String> myKvMap = new HashMap<>();
        myKvMap.put("kR1", "vR1");
        //System.out.println("Minheap in test: " + DocumentStoreImpl.minHeap.peek().getDocumentTxt());
        List<Document> YZ = myDocStore.searchByKeywordAndMetadata("apple", myKvMap);
       
        for (Document doc :YZ){
            System.out.println(doc.getKey());
        }
       
        myDocStore.setMaxDocumentCount(4);
        assertNotNull(myDocStore.get(uri1));
        assertNotNull(myDocStore.get(uri2));
        assertNull(myDocStore.get(uri3));
        assertNotNull(myDocStore.get(uri4));
        assertNotNull(myDocStore.get(uri5));
    }

    @Test
    void metaBrokenSearches() throws IOException{
        resetDocStore();
    }

    @Test
    void search() throws IOException{
        //miss
        List<Document> matchesDocsSorted = this.myDocStore.search("17Mi");
        assert (matchesDocsSorted.isEmpty());

        //apple
        matchesDocsSorted = this.myDocStore.search("apple");
        System.out.print("uri1, uri2, uri4 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }

        //app
        matchesDocsSorted = this.myDocStore.search("app");
        System.out.print("\nuri2, uri1 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }

        //apply
        matchesDocsSorted = this.myDocStore.search("apply");
        System.out.print("\nuri4 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }

        //air
        matchesDocsSorted = this.myDocStore.search("air");
        System.out.print("\nuri1 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }

        //"nope ";
        matchesDocsSorted = this.myDocStore.search("nope");
        System.out.print("\nuri3 || uri4 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }
        
        //"knot ";
        List<Document> matchesDocsSorted3 = this.myDocStore.search("knot");
        assertEquals(1, matchesDocsSorted3.size());;
        assertEquals(uri3, matchesDocsSorted3.get(0).getKey());

        //"apps ";
        matchesDocsSorted = this.myDocStore.search("apps");
        System.out.print("\nuri4 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }
        
        //"Air ";
        matchesDocsSorted = this.myDocStore.search("Air");
        System.out.print("\nuri3 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }
        
        //"17Mi ";
        matchesDocsSorted = this.myDocStore.search("17Mi");
        System.out.print("\n[] -> ");
        assertNotNull(matchesDocsSorted);
        if (matchesDocsSorted.isEmpty()) System.out.print("empty");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }
        
        //"17Miami ";
        matchesDocsSorted = this.myDocStore.search("17Miami");
        System.out.print("\nuri2, uri4 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }
    }
    

    @Test
    void searchByPrefix() throws IOException{
        List<Document> matchesDocsSorted;
        //miss
        matchesDocsSorted = this.myDocStore.searchByPrefix("17Mime");
        assert (matchesDocsSorted.isEmpty());

        //apple
        matchesDocsSorted = this.myDocStore.searchByPrefix("apple");
        System.out.print("uri1, uri2, uri4 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }

        //app
        matchesDocsSorted = this.myDocStore.searchByPrefix("app");
        System.out.print("\nuri4, uri1, uri2 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }

        //apply
        matchesDocsSorted = this.myDocStore.searchByPrefix("apply");
        System.out.print("\nuri4 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }

        //air
        matchesDocsSorted = this.myDocStore.searchByPrefix("air");
        System.out.print("\nuri1 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }


        //"nope ";
        matchesDocsSorted = this.myDocStore.searchByPrefix("nope");
        System.out.print("\nuri3 || uri4 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }
        
        //"knot ";
        List<Document> matchesDocsSorted3 = this.myDocStore.searchByPrefix("knot");
        assertEquals(1, matchesDocsSorted3.size());;
        assertEquals(uri3, matchesDocsSorted3.get(0).getKey());

        //"apps ";
        matchesDocsSorted = this.myDocStore.searchByPrefix("apps");
        System.out.print("\nuri4 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }
        
        //"Air ";
        matchesDocsSorted = this.myDocStore.searchByPrefix("Air");
        System.out.print("\nuri3 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }
        
        //"17Mi ";
        matchesDocsSorted = this.myDocStore.searchByPrefix("17Mi");
        System.out.print("\nuri2, uri4-> ");
        assertNotNull(matchesDocsSorted);
        if (matchesDocsSorted.isEmpty()) System.out.print("empty");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }
        
        //"17Miami ";
        matchesDocsSorted = this.myDocStore.searchByPrefix("17Miami");
        System.out.print("\nuri2, uri4 -> ");
        for (Document doc : matchesDocsSorted){
            System.out.print(doc.getKey()+ ", ");
        }
    }

    @Test
    void deleteAll() throws IOException{       
        Set <URI> deleted = this.myDocStore.deleteAll("17Mime");
        //search();
        assertNotNull(deleted);
        assert (deleted.isEmpty());

        //apple
        deleted = this.myDocStore.deleteAll("apple");
        //search();

        resetDocStore();
        deleted = this.myDocStore.deleteAll("app");
        assert(deleted.contains(uri1) && deleted.contains(uri2) && deleted.size() == 2);
        assertNull(this.myDocStore.get(uri1));
        assertNotNull(this.myDocStore.get(uri4));
        //search();

        resetDocStore();
        deleted = this.myDocStore.deleteAll("apply");
        //search();

        resetDocStore();
        deleted = this.myDocStore.deleteAll("air");
        //search();

        resetDocStore();
        deleted = this.myDocStore.deleteAll("nope");
        //search();

        resetDocStore();
        //deleted = this.myDocStore.deleteAll("knot");
        //search();

        resetDocStore();
        deleted = this.myDocStore.deleteAll("apps");
        //search();

        resetDocStore();
        deleted = this.myDocStore.deleteAll("Air");
        //search();

        resetDocStore();
        deleted = this.myDocStore.deleteAll("17Mi");
        //search();
        
        resetDocStore();
        deleted = this.myDocStore.deleteAll("17Miami");
        //search();
    }

    @Test
    void deleteAllWithPrefix() throws IOException{
        Set <URI> deleted = this.myDocStore.deleteAllWithPrefix("17Mime");
        //searchByPrefix();
        assertNotNull(deleted);
        assert (deleted.isEmpty());

        //apple
        deleted = this.myDocStore.deleteAllWithPrefix("apple");
        //searchByPrefix();

        resetDocStore();
        deleted = this.myDocStore.deleteAllWithPrefix("app");
        assert(deleted.contains(uri1) && deleted.contains(uri2) && deleted.contains(uri4) &&deleted.size() == 3);
        assertNull(this.myDocStore.get(uri1));
        assertNotNull(this.myDocStore.get(uri3));
        //searchByPrefix();

        resetDocStore();
        deleted = this.myDocStore.deleteAllWithPrefix("apply");
        //searchByPrefix();

        resetDocStore();
        deleted = this.myDocStore.deleteAllWithPrefix("air");
        //searchByPrefix();

        resetDocStore();
        //deleted = this.myDocStore.deleteAllWithPrefix("nope");
        //searchByPrefix();

        resetDocStore();
        //deleted = this.myDocStore.deleteAllWithPrefix("knot");
        //searchByPrefix();

        resetDocStore();
        deleted = this.myDocStore.deleteAllWithPrefix("apps");
        //searchByPrefix();

        resetDocStore();
        //deleted = this.myDocStore.deleteAllWithPrefix("Air");
        //searchByPrefix();

        resetDocStore();
        deleted = this.myDocStore.deleteAllWithPrefix("17Mi");
        //searchByPrefix();
        
        resetDocStore();
        deleted = this.myDocStore.deleteAllWithPrefix("17Miami");
        //searchByPrefix();
        resetDocStore();
    }

    @Test
    void searchByMetadata()throws IOException{
        List<Document> matchesDocs;
        Map<String, String> metaData;

        //miss
        metaData = new HashMap<>();
        metaData.put("miss", "miss");
        matchesDocs = this.myDocStore.searchByMetadata(metaData);
        assert (matchesDocs.isEmpty());

        // kR1, vR1
        metaData = new HashMap<>();
        metaData.put("kR1", "vR1");
        matchesDocs = this.myDocStore.searchByMetadata(metaData);
        System.out.print("uri4, uri1, uri3, uri2 -> ");
        for (Document doc : matchesDocs){
            System.out.print(doc.getKey()+ ", ");
        }

        // kR2, vR2
        metaData = new HashMap<>();
        metaData.put("kR2", "vR2");
        matchesDocs = this.myDocStore.searchByMetadata(metaData);
        System.out.print("\nuri4, uri2, uri5 -> ");
        for (Document doc : matchesDocs){
            System.out.print(doc.getKey()+ ", ");
        }

        // kR1, vR1, and kR2, VR2
        metaData = new HashMap<>();
        metaData.put("kR1", "vR1");
        metaData.put("kR2", "vR2");
        matchesDocs = this.myDocStore.searchByMetadata(metaData);
        System.out.print("\nuri4, uri2 -> ");
        for (Document doc : matchesDocs){
            System.out.print(doc.getKey()+ ", ");
        }
    }

    @Test
    void searchByKeywordAndMetadata()throws IOException{
        List<Document> matchesDocs;
        Map<String, String> metaData;


        // Apple, kR1, vR1
        metaData = new HashMap<>();
        metaData.put("kR1", "vR1");
        matchesDocs = this.myDocStore.searchByKeywordAndMetadata("apple", metaData);
        System.out.print("uri1, uri2, uri4 -> ");
        for (Document doc : matchesDocs){
            System.out.print(doc.getKey()+ ", ");
        }

        // kR2, vR2
        metaData = new HashMap<>();
        metaData.put("kR2", "vR2");
        matchesDocs = this.myDocStore.searchByKeywordAndMetadata("apple", metaData);
        System.out.print("\nuri2, uri4 -> ");
        for (Document doc : matchesDocs){
            System.out.print(doc.getKey()+ ", ");
        }

        // kR1, vR1, and kR2, VR2
        metaData = new HashMap<>();
        metaData.put("kR1", "vR1");
        metaData.put("kR2", "vR2");
        matchesDocs = this.myDocStore.searchByKeywordAndMetadata("apple", metaData);
        System.out.print("\nuri2, uri4 -> ");
        for (Document doc : matchesDocs){
            System.out.print(doc.getKey()+ ", ");
        }
    }

    @Test
    void searchByPrefixAndMetadata()throws IOException{
        List<Document> matchesDocs;
        Map<String, String> metaData;

        // Apple, kR1, vR1
        metaData = new HashMap<>();
        metaData.put("kR1", "vR1");
        matchesDocs = this.myDocStore.searchByPrefixAndMetadata("app", metaData);
        System.out.print("uri4, uri1, uri2 -> ");
        for (Document doc : matchesDocs){
            System.out.print(doc.getKey()+ ", ");
        }

        // kR2, vR2
        metaData = new HashMap<>();
        metaData.put("kR2", "vR2");
        matchesDocs = this.myDocStore.searchByPrefixAndMetadata("apple", metaData);
        System.out.print("\nuri2, uri4 -> ");
        for (Document doc : matchesDocs){
            System.out.print(doc.getKey()+ ", ");
        }

        // kR1, vR1, and kR2, VR2
        metaData = new HashMap<>();
        metaData.put("kR1", "vR1");
        metaData.put("kR2", "vR2");
        matchesDocs = this.myDocStore.searchByPrefixAndMetadata("apply", metaData);
        System.out.print("\nuri4 -> ");
        for (Document doc : matchesDocs){
            System.out.print(doc.getKey()+ ", ");
        }
    }

    @Test
    void deleteAllWithMetadata() throws IOException{
        Map<String, String> metaData;
        Set <URI> deleted;

        //miss
        metaData = new HashMap<>();
        metaData.put("miss", "miss");
        deleted = this.myDocStore.deleteAllWithMetadata(metaData);
        //searchByMetadata();
        assertNotNull(deleted);
        assert (deleted.isEmpty());

        //kR2, vW2
        metaData = new HashMap<>();
        metaData.put("kR2", "vW2");
        deleted = this.myDocStore.deleteAllWithMetadata(metaData);
        //searchByMetadata();

        resetDocStore();
        //kR2, vR2
        metaData = new HashMap<>();
        metaData.put("kR2", "vR2");
        deleted = this.myDocStore.deleteAllWithMetadata(metaData);
        assert(deleted.contains(uri4) && deleted.contains(uri5) && deleted.contains(uri2) &&deleted.size() == 3);
        assertNull(this.myDocStore.get(uri2));
        assertNotNull(this.myDocStore.get(uri3));
        //searchByMetadata();

        resetDocStore();
        metaData = new HashMap<>();
        metaData.put("kR1", "vR1");
        metaData.put("kR2", "vR2");
        deleted = this.myDocStore.deleteAllWithMetadata(metaData); //should have 1, 3, 5
        //searchByMetadata();
    }

    @Test
    void deleteAllWithKeywordAndMetadata() throws IOException{
        Map<String, String> metaData;
        Set <URI> deleted;

        //miss
        metaData = new HashMap<>();
        metaData.put("miss", "miss");
        deleted = this.myDocStore.deleteAllWithKeywordAndMetadata("apple", metaData);
        //searchByKeywordAndMetadata();
        assertNotNull(deleted);
        assert (deleted.isEmpty());

        //kR2, vW2
        metaData = new HashMap<>();
        metaData.put("kR2", "vW2");
        deleted = this.myDocStore.deleteAllWithKeywordAndMetadata("note", metaData); //should have 1, 3, 5
        searchByKeywordAndMetadata();
        assertEquals(1, deleted.size());
    
        //kR1, vR1
        resetDocStore();
        metaData = new HashMap<>();
        metaData.put("kR1", "vR1");
        deleted = this.myDocStore.deleteAllWithKeywordAndMetadata("apple", metaData);
        //searchByKeywordAndMetadata();


        resetDocStore();
        //kR2, vR2
        metaData = new HashMap<>();
        metaData.put("kR2", "vR2");
        deleted = this.myDocStore.deleteAllWithKeywordAndMetadata("apple", metaData);
        assert(deleted.contains(uri4) && deleted.contains(uri2) &&deleted.size() == 2);
        assertNull(this.myDocStore.get(uri2));
        assertNotNull(this.myDocStore.get(uri3));
        //searchByKeywordAndMetadata();

        resetDocStore();
        metaData = new HashMap<>();
        metaData.put("kR1", "vR1");
        metaData.put("kR2", "vR2");
        deleted = this.myDocStore.deleteAllWithKeywordAndMetadata("apple", metaData); //should have 1, 3, 5
        //searchByKeywordAndMetadata();
    }
    

    @Test
    void deleteAllWithPrefixAndMetadata() throws IOException{
        Map<String, String> metaData;
        Set <URI> deleted;

        //miss
        metaData = new HashMap<>();
        metaData.put("miss", "miss");
        deleted = this.myDocStore.deleteAllWithPrefixAndMetadata("something", metaData);
        //searchByPrefixAndMetadata();
        assertNotNull(deleted);
        assert (deleted.isEmpty());

        //kR2, vW2
        metaData = new HashMap<>();
        metaData.put("kR1", "vR1");
        deleted = this.myDocStore.deleteAllWithPrefixAndMetadata("app", metaData);
        assertNull(this.myDocStore.get(uri2));
        assertNotNull(this.myDocStore.get(uri3));
        //searchByPrefixAndMetadata();

        resetDocStore();
        //kR2, vR2
        metaData = new HashMap<>();
        metaData.put("kR2", "vR2");
        deleted = this.myDocStore.deleteAllWithPrefixAndMetadata("apple", metaData);
        assert(deleted.contains(uri4) && deleted.contains(uri2) &&deleted.size() == 2);
        assertNull(this.myDocStore.get(uri2));
        assertNotNull(this.myDocStore.get(uri3));
        //searchByPrefixAndMetadata();

        resetDocStore();
        metaData = new HashMap<>();
        metaData.put("kR1", "vR1");
        metaData.put("kR2", "vR2");
        deleted = this.myDocStore.deleteAllWithPrefixAndMetadata("apply", metaData); //should have 1, 3, 5
        //searchByPrefixAndMetadata();
    }
    

    @Test
    void undoDeleteALL() throws IOException{
        Set <URI> deleted = this.myDocStore.deleteAll("17Mime");
        //search();
        assertNotNull(deleted);
        assert (deleted.isEmpty());

        //apple
        deleted = this.myDocStore.deleteAll("apple");
        this.myDocStore.undo(uri2);
        this.myDocStore.undo(uri1);
        search();
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
        //assertEquals(0, check1);
        Document temp1 = myDocStore.get(uri1);
        int hc1 = temp1.hashCode();
        int check2 = myDocStore.put(inputStream2, uri1, DocumentFormat.BINARY);
        assertEquals(hc1, check2);

        //add something else to the docStore
        myDocStore.put(inputStream3, uri3, DocumentFormat.TXT);

        //checking delete
        //assertEquals(2, DocumentStoreImpl.docStore.size());

        int delMiss = myDocStore.put(null, uri2, DocumentFormat.TXT);
        //assertEquals(0, delMiss);
        //assertEquals(2, DocumentStoreImpl.docStore.size());

        int tempHC = myDocStore.get(uri1).hashCode();
        int del = myDocStore.put(null, uri1, DocumentFormat.TXT);
        assertEquals(del, tempHC);
        //assertEquals(1, DocumentStoreImpl.docStore.size());

        String third_document_text = myDocStore.get(uri3).getDocumentTxt();
        assertEquals("document3Content", third_document_text);
    }
    

    @Test
    void setMetadataUndo() throws IOException{
        //metadata undo
       // assertThrows(IllegalStateException.class, ()->{
       //     myDocStore.undo();
        //});
        myDocStore.put(inputStream1, uri1, DocumentFormat.TXT);
        myDocStore.put(inputStream2, uri2, DocumentFormat.BINARY);
        myDocStore.setMetadata(uri1, "first", "firstb");
        myDocStore.setMetadata(uri1, "second", "secondb");
        myDocStore.undo();
        assertEquals("firstb", myDocStore.getMetadata(uri1, "first"));
        assertEquals(null, myDocStore.getMetadata(uri1, "second"));
        myDocStore.setMetadata(uri1, "third", "thirdb");
        myDocStore.setMetadata(uri1, "fourth", "example");
        myDocStore.setMetadata(uri1, "third", "newthird");
        assertEquals("newthird", myDocStore.getMetadata(uri1, "third"));
        myDocStore.undo();
        assertEquals("thirdb", myDocStore.getMetadata(uri1, "third"));

        
    }

    @Test
    void putUndo() throws IOException{
        Document doc3Same = new DocumentImpl(uri3, "document3Content", null);
        myDocStore.put(inputStream3, uri3, DocumentFormat.TXT);
        assertEquals(doc3Same, myDocStore.get(uri3));
        myDocStore.undo();
        //assertNull(myDocStore.get(uri3));

        myDocStore.put(inputStream1, uri1, DocumentFormat.TXT);
        myDocStore.put(inputStream2, uri2, DocumentFormat.TXT);

        myDocStore.put(inputStream5, uri1, DocumentFormat.TXT);
        Document oldDocument = new DocumentImpl(uri1, "document1Content", null);
        Document newDocument = new DocumentImpl(uri1, "document5Content", null);
        assertEquals(newDocument, myDocStore.get(uri1));
        myDocStore.undo();
        assertEquals(oldDocument, myDocStore.get(uri1));
    }

    @Test
    void deleteUndo() throws IOException{
        myDocStore.put(inputStream1, uri1, DocumentFormat.TXT);
        myDocStore.put(inputStream2, uri2, DocumentFormat.TXT);
        myDocStore.put(inputStream5, uri1, DocumentFormat.TXT);
        myDocStore.delete(uri1);
        Document doc1Same = new DocumentImpl(uri1, "document5Content", null);
        assertNull(myDocStore.get(uri1));
        myDocStore.undo();
        assertEquals(doc1Same, myDocStore.get(uri1));
    }

    @Test
    void undoURI() throws IOException{
        myDocStore.put(inputStream1, uri1, DocumentFormat.TXT);
        myDocStore.put(inputStream2, uri2, DocumentFormat.TXT);
        myDocStore.put(inputStream3, uri3, DocumentFormat.TXT);
        //assertThrows(IllegalStateException.class, ()->{
        //    myDocStore.undo(uri4);
       // });
        myDocStore.setMetadata(uri1, "first", "firstb");
        myDocStore.setMetadata(uri1, "second", "secondb");
        myDocStore.setMetadata(uri2, "random1", "randoma");
        myDocStore.setMetadata(uri3, "random2", "randoma2");
        myDocStore.undo(uri1);
        assertEquals(null, myDocStore.getMetadata(uri1, "second"));
        myDocStore.setMetadata(uri1, "seconds", "oldseconda");
        myDocStore.setMetadata(uri1, "seconds", "s1234");
        myDocStore.delete(uri2);
        myDocStore.undo(uri1);
        assertEquals("oldseconda", myDocStore.getMetadata(uri1, "seconds"));
        assertNull(myDocStore.get(uri2));
        myDocStore.undo(uri2);
        assertNotNull(myDocStore.get(uri2));
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
        //assertFalse(wasDeleted2);
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
        myDocStore.setMetadata(uri1, "first", "firstb");
        String temp1 = myDocStore.setMetadata(uri1, "second", "secondb");
        assertNull(temp1);
        assertEquals("firstb", myDocStore.getMetadata(uri1, "first"));
        assertEquals("secondb", myDocStore.getMetadata(uri1, "second"));
        String temp2 = myDocStore.setMetadata(uri1, "second", "secondNew");
        assertEquals("secondb", temp2);
        assertEquals("secondNew", myDocStore.getMetadata(uri1, "second"));
    }   

    //repeated for the purpose of design, essentially tested within the same stanza
    @Test
    void getMetadata() throws IOException{
        myDocStore.put(inputStream1, uri1, DocumentFormat.TXT);
        myDocStore.put(inputStream2, uri2, DocumentFormat.BINARY);
        myDocStore.setMetadata(uri1, "first", "firstb");
        myDocStore.setMetadata(uri1, "second", "secondb");
        assertEquals("firstb", myDocStore.getMetadata(uri1, "first"));
        assertEquals("secondb", myDocStore.getMetadata(uri1, "second"));
    }




	





    //assertThrows(IllegalArgumentException.class,  ()->{
            
    //});
    
}
