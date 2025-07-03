package edu.yu.cs.com1320.project.stage2.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.stage2.DocumentStore;

public class DocumentStoreImpl implements DocumentStore{
    private HashTable<URI, Document> docStore;

    public DocumentStoreImpl(){
        this.docStore = new HashTableImpl<>();
    }

    @Override
    public String setMetadata(URI uri, String key, String value) {
        if (uri == null
                || uri.toString().isEmpty()
                || key == null
                || key.isEmpty()
                || docStore.get(uri) == null){
            throw new IllegalArgumentException();
        }
        return docStore.get(uri).setMetadataValue(key, value);
    }

    @Override
    public String getMetadata(URI uri, String key) {
        if (uri == null
                || uri.toString().isEmpty()
                || key == null
                || key.isEmpty()
                || docStore.get(uri) == null){
            throw new IllegalArgumentException();
        }
        return docStore.get(uri).getMetadataValue(key);
    }

    @Override
    public int put(InputStream input, URI uri, DocumentFormat format) throws IOException {        

        if (uri == null
                || uri.toString().isEmpty()
                || format == null){
            throw new IllegalArgumentException();
        }

        if (input == null){
            int tempHashCode = docStore.get(uri) == null ? 0 : docStore.get(uri).hashCode();
            return delete(uri) ? tempHashCode : 0;
        }

        byte[] content = null;
        try {
            content = input.readAllBytes();
        } catch (Exception e) {
            throw new IOException();
        }
        finally{
            input.close();
        }
        Document newDoc = createNewDoc(uri, format, content);
        Document previous = this.docStore.put(uri, newDoc);
        return previous == null ? 0 : previous.hashCode();
    }

    

    private Document createNewDoc(URI uri, DocumentFormat format, byte[] ba){
        switch (format) {
            case BINARY:
                return new DocumentImpl(uri, ba);
            //break
            case TXT:
                String sFormat = new String(ba);
                return new DocumentImpl(uri, sFormat);
            //break
            //unreachable
            default:
                return null;
        }
    }

    @Override
    public Document get(URI url) {
        return docStore.get(url);
    }

    @Override
    public boolean delete(URI url) {
       Document exists = this.docStore.put(url, null);
       return exists == null ? false : true;
    }
}
