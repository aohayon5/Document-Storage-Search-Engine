package edu.yu.cs.com1320.project.stage6.impl;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import edu.yu.cs.com1320.project.stage6.Document;

public class DocumentImpl implements Document{
    private Map<String, String> metaData;
    private Map<String, Integer> wordAndCount;
    private URI uri;
    private long useTime;
    private String txt;
    private byte[] binaryData;
    public DocumentImpl(URI uri, String text, Map<String, Integer> wordCountMap){
        if (uri == null
                || text == null
                || uri.toString().isEmpty()
                || text.isEmpty()){
            throw new IllegalArgumentException();
        }
        this.txt = text;
        this.metaData = new HashMap<>();
        if (wordCountMap == null){
            this.wordAndCount = new HashMap<>();
            this.setUpMap(filter(this.txt)); //set up the map of all of the words and their counts
        }
        else{
            this.wordAndCount = wordCountMap;
        }
        this.uri = uri;
        this.useTime = System.nanoTime();
        
        this.binaryData = null;
        
    }

    private String filter(String key){
        return key.replaceAll("[^a-zA-Z0-9\\s]", "");
    }


    public DocumentImpl(URI uri, byte[] binaryData){
        if (uri == null 
                || binaryData == null
                || uri.toString().isEmpty()
                || binaryData.length == 0){
            throw new IllegalArgumentException();
        }
        this.metaData = new HashMap<>();
        this.wordAndCount = new HashMap<>();
        this.uri = uri;
        this.useTime = System.nanoTime();
        this.binaryData = binaryData;
        this.txt = null;
    }

    @Override
    public String setMetadataValue(String key, String value) {
        if (key == null || key.isEmpty()){
            throw new IllegalArgumentException();
        }
        String returned;
        if(value != null){
            returned = this.metaData.put(key, value);
        }
        else{
            returned = this.metaData.remove(key);
        }
        return returned;
    }

    @Override
    public String getMetadataValue(String key) {
        if (key == null || key.isEmpty()){
            throw new IllegalArgumentException();
        }
        return this.metaData.get(key);
    }

    @Override
    public HashMap<String, String> getMetadata(){
        HashMap<String, String> newHs = new HashMap<>();
        Set<String> ks = metaData.keySet();
        for (String s: ks){
            newHs.put(s, metaData.get(s));
        }
        return newHs;
    }

    @Override
    public String getDocumentTxt() { //returns null if used on binary data
        return this.txt;
    }

    @Override
    public byte[] getDocumentBinaryData() { //returns null if used on text document
        return this.binaryData;
    }

    @Override
    public URI getKey() {
        return this.uri;
    }

    private void setUpMap(String txt){
        String[] aryofWords = txt.split(" ");
        for (String word : aryofWords){
            if(wordAndCount.containsKey(word)){
                wordAndCount.put(word, 1 + wordAndCount.get(word));
            }
            else{
                wordAndCount.put(word, 1);
            }
        }
    }

    @Override
    public void setMetadata(HashMap<String, String> metadata) {
        this.metaData = metadata;
    }

    @Override
    public HashMap<String, Integer> getWordMap() {
        HashMap<String, Integer> copyWordAndCount = new HashMap<>();
        for (String word : this.wordAndCount.keySet()){
            copyWordAndCount.put(word, wordAndCount.get(word).intValue());
        }
        return copyWordAndCount;
    }

    @Override
    public void setWordMap(HashMap<String, Integer> wordMap) {
        this.wordAndCount = wordMap;
    }



    @Override
    public int wordCount(String word){
        if (this.txt == null){
            return 0;
        }
        return wordAndCount.containsKey(word) ? wordAndCount.get(word) : 0;
    }


    @Override
    public Set<String> getWords(){
        if (this.txt == null){
            return new HashSet<>();
        }
        return wordAndCount.keySet();
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

    @Override
    public int compareTo(Document o) {
        if (o == null){
            throw new IllegalArgumentException();
        }
        if (this.useTime < o.getLastUseTime()){
            return -1;
        }
        else if(this.useTime > o.getLastUseTime()){
            return 1;
        }
        else{
            return 0;
        }
    }

    @Override
    public long getLastUseTime() {
        return this.useTime;
    }

    @Override
    public void setLastUseTime(long timeInNanoseconds) {
        this.useTime = timeInNanoseconds;
    }
}