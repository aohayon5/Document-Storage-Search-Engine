package edu.yu.cs.com1320.project.stage6.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.yu.cs.com1320.project.stage6.Document;
import edu.yu.cs.com1320.project.stage6.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage6.PersistenceManager;

public class DocumentPersistenceManagerTest {
    DocumentPersistenceManager pm;
    Document doc1_txt;
    String txt1;
    Document doc1_b;
    byte[] ba1;
    URI uri1;
    URI uri2;

    @BeforeEach
    void b4() throws URISyntaxException{
        this.txt1 = "myTest LOL LOL";
        this.ba1 = txt1.getBytes(StandardCharsets.UTF_8);
        uri1 = new URI("http://www.yu.edu/documents/doc1");
        uri2 = new URI("http://www.yu.edu/notDocument/doc2");
        doc1_txt = new DocumentImpl(uri1, txt1, null);
        doc1_b = new DocumentImpl(uri2, ba1);

        HashMap<String,String> myMetadata = new HashMap<>();
        myMetadata.put("m1", "v1");
        myMetadata.put("m2", "v2");

        //set metaData doc1_txt
        
        doc1_txt.setMetadata(myMetadata);

        //set metaData doc1_b
        doc1_b.setMetadata(myMetadata);


        String baseDir = "C:/Users/flash/.appearMe";
        this.pm = new DocumentPersistenceManager(new File(baseDir));
    }

    @Test
    void serialize() throws IOException{
        pm.serialize(uri2, doc1_b);
        pm.serialize(uri1, doc1_txt);
    }

    @Test
    void deserialize() throws IOException{
        DocumentImpl d1d = (DocumentImpl) pm.deserialize(uri1);
        System.out.println(d1d.getKey());
        System.out.println(d1d.getDocumentTxt());
        System.out.println(d1d.getMetadata());
        System.out.println(d1d.getWordMap());
        DocumentImpl d2d = (DocumentImpl) pm.deserialize(uri2);
        System.out.println(d2d.getKey());
        System.out.println(new String(d2d.getDocumentBinaryData()));
        System.out.println(d2d.getMetadata());
        System.out.println(d2d.getWordMap());
    }

    @Test
    void delete() throws IOException{
        this.pm.delete(uri2);
    }
}
