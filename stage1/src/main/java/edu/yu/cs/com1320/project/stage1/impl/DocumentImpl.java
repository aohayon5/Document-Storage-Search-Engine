package edu.yu.cs.com1320.project.stage1.impl;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;

import edu.yu.cs.com1320.project.stage1.Document;

public class DocumentImpl implements Document{
    private HashMap<String, String> metaData;
    private URI uri;
    private String txt;
    private byte[] binaryData;
    public DocumentImpl(URI uri, String txt){
        if (uri == null
                || txt == null
                || uri.toString().isEmpty()
                || txt.isEmpty()){
            throw new IllegalArgumentException();
        }
        this.metaData = new HashMap<>();
        this.uri = uri;
        this.txt = txt;
        this.binaryData = null;
    }

    public DocumentImpl(URI uri, byte[] binaryData){
        if (uri == null 
                || binaryData == null
                || uri.toString().isEmpty()
                || binaryData.length == 0){
            throw new IllegalArgumentException();
        }
        this.metaData = new HashMap<>();
        this.uri = uri;
        this.binaryData = binaryData;
        this.txt = null;
    }

    @Override
    public String setMetadataValue(String key, String value) {
        if (key == null || key.equals("")){
            throw new IllegalArgumentException();
        }
        return this.metaData.put(key, value);
    }

    @Override
    public String getMetadataValue(String key) {
        if (key == null || key.isEmpty()){
            throw new IllegalArgumentException();
        }
        return this.metaData.get(key);
    }

    @Override
    public HashMap<String, String> getMetadata() {
        return new HashMap<>(metaData);
    }

    @Override
    public String getDocumentTxt() { //reeturn null if used on binary data?
        return this.txt;
    }

    @Override
    public byte[] getDocumentBinaryData() { //return null if used on text document
        return this.binaryData;
    }

    @Override
    public URI getKey() {
        return this.uri;
    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + (txt != null ? txt.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(binaryData);
        return Math.abs(result);
    }


    @Override
    public boolean equals(Object obj){
        if (obj == this){
            return true;
        }
        if (obj == null){
            return false;
        }
        if (obj.getClass() != this.getClass()){
            return false;
        }
        DocumentImpl di = (DocumentImpl)obj;
        return this.hashCode() == di.hashCode();
    }
    
}