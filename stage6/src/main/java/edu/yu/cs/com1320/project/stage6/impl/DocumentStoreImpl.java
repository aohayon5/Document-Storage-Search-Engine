package edu.yu.cs.com1320.project.stage6.impl;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.yu.cs.com1320.project.BTree;
import edu.yu.cs.com1320.project.MinHeap;
import edu.yu.cs.com1320.project.Stack;
import edu.yu.cs.com1320.project.Trie;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage6.Document;
import edu.yu.cs.com1320.project.stage6.DocumentStore;
import edu.yu.cs.com1320.project.undo.CommandSet;
import edu.yu.cs.com1320.project.undo.GenericCommand;
import edu.yu.cs.com1320.project.undo.Undoable;

public class DocumentStoreImpl implements DocumentStore{
    private MinHeap<HeapDocument> minHeap;
    private BTree<URI, Document> bTree;
    private Stack<Undoable> commandStack;
    private Trie<URI> myTrie;
    private int maxDocumentCount;
    private int maxDocumentBytes;
    //private int currentBytes;

    //Helper data structures
    private Set<URI> inMemory;
    private Set<URI> onDisk;
    private Map<URI, Map<String, String>> uriMetaMap;



    public DocumentStoreImpl(){
        this.minHeap = new MinHeapImpl<>();
        this.bTree = new BTreeImpl<>();
        this.bTree.setPersistenceManager(new DocumentPersistenceManager(null));
        this.commandStack = new StackImpl<>();
        this.myTrie = new TrieImpl<>();
        this.maxDocumentCount = Integer.MAX_VALUE;
        this.maxDocumentBytes = Integer.MAX_VALUE;
        //this.currentBytes = 0;
        //Helper data structures
        this.inMemory = new HashSet<>();
        this.onDisk = new HashSet<>();
        this.uriMetaMap = new HashMap<>();
    }

    public DocumentStoreImpl(File baseDir){
        this.minHeap = new MinHeapImpl<>();
        this.bTree = new BTreeImpl<>();
        this.bTree.setPersistenceManager(new DocumentPersistenceManager(baseDir));
        this.commandStack = new StackImpl<>();
        this.myTrie = new TrieImpl<>();
        this.maxDocumentCount = Integer.MAX_VALUE;
        this.maxDocumentBytes = Integer.MAX_VALUE;
        //this.currentBytes = 0;
        //Helper data structures
        this.inMemory = new HashSet<>();
        this.onDisk = new HashSet<>();
        this.uriMetaMap = new HashMap<>();
    }

    

    @Override
    public String setMetadata(URI uri, String key, String value) throws IOException{
        if (uri == null
                || uri.toString().isEmpty()
                || key == null
                || key.isEmpty()
                || bTree.get(uri) == null){
            throw new IllegalArgumentException();
        }
        //Undo logic
        //If the key was not previously present, that means that this metadata was just fully added, not changed, so delete the key-value pair
        Document associatedDoc = this.bTree.get(uri);
        checkFits(associatedDoc);
        HeapDocument hd = new HeapDocument(uri);
        HashMap<String, String> docsMetaData = associatedDoc.getMetadata();

        if(!docsMetaData.containsKey(key)){
            this.commandStack.push(new GenericCommand<URI>(uri, undoThis -> {
                associatedDoc.setMetadataValue(key, null);
                this.uriMetaMap.get(uri).remove(key);
            }));
        }
        else{ //change it back
            String previousValue = associatedDoc.getMetadataValue(key);
            this.commandStack.push(new GenericCommand<URI>(uri, undoThis -> {
                associatedDoc.setMetadataValue(key, previousValue);
                this.uriMetaMap.get(uri).put(key, previousValue);
            }));
        }
        associatedDoc.setLastUseTime(System.nanoTime());
        hd.setLastUseTime(System.nanoTime());
        minHeap.insert(hd);
        inMemory.add(hd.uri);
        this.minHeap.reHeapify(hd);
        //metaMap
        if(uriMetaMap.get(uri) == null){
            this.uriMetaMap.put(uri, new HashMap<>());
        }
        this.uriMetaMap.get(uri).put(key, value);
        storageControlSendToBTree();
        return associatedDoc.setMetadataValue(key, value);

    }

    @Override
    public String getMetadata(URI uri, String key) throws IOException{
        if (uri == null
                || uri.toString().isEmpty()
                || key == null
                || key.isEmpty()
                || bTree.get(uri) == null){
            throw new IllegalArgumentException();
        }
        Document associatedDoc = this.bTree.get(uri);
        checkFits(associatedDoc);
        HeapDocument hd = new HeapDocument(uri);
        associatedDoc.setLastUseTime(System.nanoTime());
        hd.setLastUseTime(System.nanoTime());
        minHeap.insert(hd);
        inMemory.add(hd.uri);
        this.minHeap.reHeapify(hd);
        storageControlSendToBTree();
        return associatedDoc.getMetadataValue(key);
    }

    private class HeapDocument implements Comparable<HeapDocument>{
        private long useTime;
        private URI uri;
        public HeapDocument(URI uri){
            this.uri = uri;
            this.useTime = System.nanoTime();
        }

        public long getLastUseTime(){
            return this.useTime;
        }
        public void setLastUseTime(Long v){
            this.useTime = v;
        }
        @Override
        public int hashCode() {
            int result = uri.hashCode();;
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
            HeapDocument di = (HeapDocument)obj;
            return this.hashCode() == di.hashCode();
        }

        @Override
        public int compareTo(HeapDocument o) {
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
    }


    @Override
    public int put(InputStream input, URI uri, DocumentFormat format) throws IOException {
        if (uri == null || uri.toString().isEmpty() || format == null){ throw new IllegalArgumentException();   }
        if (input == null){
            int tempHashCode = bTree.get(uri) == null ? 0 : bTree.get(uri).hashCode();
            return delete(uri) ? tempHashCode : 0;
        }
        //
        byte[] content = null;
        try { 
            content = input.readAllBytes();
        } 
        catch (Exception e) {    
            throw new IOException();}
        finally{    
            input.close();  
        }
        Document newDoc = createNewDoc(uri, format, content);
        checkFits(newDoc);
        //FIX THIS
        Document previousDocument = this.bTree.get(uri); //never null if the document exists
        HeapDocument hd = new HeapDocument(uri);
        this.commandStack.push(new GenericCommand<URI>(uri, undoThis -> {
            this.bTree.put(uri, previousDocument);
            if (previousDocument != null){ //means there was a previous document
                //this means the uri is already in the trie and minheap, and 
                //the only concern is if it needs to be moved to disk or not
                if (!inMemory.contains(uri)){ //if it was originally on disk, place it on disk
                    try {
                        bTree.moveToDisk(uri);
                        onDisk.add(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }  
                }
                else{ //place it back into memory
                    inMemory.add(uri);
                    minHeap.insert(hd);
                }
                //addDocumentToTrie(uri);
            }
            else{ //there was no document here originally
                removeFromTrie(newDoc);
                if (inMemory.contains(uri)){ //remove memory, disk
                    removeFromHeap(hd);
                    inMemory.remove(uri);
                    onDisk.remove(uri);
                }
            }

        }));
        if (format == DocumentFormat.TXT){ //MODIFY TRIE AND BYTE COUNTS
            //removeFromTrie(previousDocument); //if previously null does nothing
            //removeFromHeap(previousDocument); //same thing
            //this.currentBytes += newDoc.getDocumentTxt().getBytes().length;
            addDocumentToTrie(newDoc);
        }
        else{
            //this.currentBytes += newDoc.getDocumentBinaryData().length;
        }
        minHeap.insert(hd);
        Document previous = this.bTree.put(uri, newDoc);
        inMemory.add(uri);
        if (this.bTree.get(uri) != null){    
            this.bTree.get(uri).setLastUseTime(System.nanoTime());
            hd.setLastUseTime(System.nanoTime());
        } //be careful here
        minHeap.reHeapify(hd);
        storageControlSendToBTree();
        return previous == null ? 0 : previous.hashCode();
    }


    private void addDocumentToTrie(Document doc){
        if (doc == null){
            return;
        }
        for(String word : doc.getWords()){
            myTrie.put(word, doc.getKey());
        }
    }

    private void checkFits(Document doc){
        if(doc == null){
            return;
        }
        if (doc.getDocumentBinaryData() == null){ //it is a text document
            byte[] lengthTxtDoc = doc.getDocumentTxt().getBytes();
            if (lengthTxtDoc.length > this.maxDocumentBytes){
                throw new IllegalArgumentException();
            }
        }
        else{ //it is a binary document
            byte[] lengthBinDoc = doc.getDocumentBinaryData();
            if (lengthBinDoc.length > this.maxDocumentBytes){
                throw new IllegalArgumentException();
            }
        }
    }


    private Document createNewDoc(URI uri, DocumentFormat format, byte[] ba){
        switch (format) {
            case BINARY:
                return new DocumentImpl(uri, ba);
            //break
            case TXT:
                String sFormat = new String(ba);
                return new DocumentImpl(uri, sFormat, null);
            //break
            //unreachable
            default:
                return null;
        }
    } 

    @Override
    public Document get(URI url) throws IOException{
        Document gotten = this.bTree.get(url);
        checkFits(gotten);
        HeapDocument hd = new HeapDocument(url);
        if (gotten != null){
            gotten.setLastUseTime(System.nanoTime());
            hd.setLastUseTime(System.nanoTime());
            if (!inMemory.contains(hd.uri)){
                minHeap.insert(hd);
                inMemory.add(url);
            }
            onDisk.remove(url);
            minHeap.insert(hd);
            inMemory.add(url);
            this.minHeap.reHeapify(hd); //fix this
        }
        storageControlSendToBTree();
        return gotten;
    }


    private void removeFromHeap(HeapDocument deletedDocument){
        if (deletedDocument == null){
            return;
        }
        minHeap.insert(deletedDocument);
        inMemory.add(deletedDocument.uri);
        deletedDocument.setLastUseTime(Long.MIN_VALUE);
        minHeap.reHeapify(deletedDocument);
        minHeap.remove();
        inMemory.remove(deletedDocument.uri);
    }

    @Override
    public boolean delete(URI url) { //removes from the doc store and from the entire Trie and Heap
        Document previous = this.bTree.get(url);
        if (previous == null){
            return false;
        }
        HeapDocument hd = new HeapDocument(url);
        boolean wasInMemory = inMemory.contains(url);
        this.commandStack.push(new GenericCommand<URI>(url, undoThis -> {
            this.bTree.put(url, previous);
            addDocumentToTrie(previous);
            if (wasInMemory){
                minHeap.insert(hd);
                inMemory.add(url);
            }
            else{ // was not in memory
                try {
                    onDisk.add(url);
                    this.bTree.moveToDisk(url);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }));
        Document deletedDocument = this.bTree.put(url, null);

        if (deletedDocument != null){
            if(deletedDocument.getDocumentBinaryData() == null){
                //currentBytes -= deletedDocument.getDocumentTxt().getBytes().length;
                removeFromTrie(deletedDocument);
            }
            else{
                //currentBytes -= deletedDocument.getDocumentBinaryData().length;
            }

        }
        if (deletedDocument != null){
            if (inMemory.contains(url)){
                removeFromHeap(hd);
                inMemory.remove(url);
            }
            else{
                onDisk.remove(url);
            }
        }
        
        //if (this.bTree.get(url) != null){
        //    this.docStore.get(url).setLastUseTime(System.nanoTime());
        //    if (docStore.containsKey(url)){
        //        this.minHeap.reHeapify(this.docStore.get(url));
        //    }
        //}
        return deletedDocument == null ? false : true; //returns true
    }

    private void removeFromTrie(Document doc){ //check if I need undo implementation here
        if (doc == null){
            return;
        }
        for(String word : doc.getWords()){
            myTrie.delete(word, doc.getKey());
        }
    }

    //**********STAGE 3 ADDITIONS

    /**
     * undo the last put or delete command
     * @throws IllegalStateException if there are no actions to be undone, i.e. the command stack is empty
     */
    @Override
    public void undo() throws IllegalStateException {
        if (this.commandStack.size() == 0){
            throw new IllegalStateException();
        }
        else{
            Undoable undoing = commandStack.pop();
            this.nanoTimeUndo(undoing);//has to be called twice
            undoing.undo();
            this.nanoTimeUndo(undoing);
        }
        storageControlSendToBTree();
     }

     private void nanoTimeUndo(Undoable undoing){
        if (undoing instanceof CommandSet){
            Iterator<GenericCommand<URI>> myItr = ((CommandSet<URI>)undoing).iterator();
            while (myItr.hasNext()){
                GenericCommand<URI> ge = myItr.next();
                Document myDoc = this.bTree.get(ge.getTarget());
                if (myDoc != null){
                    HeapDocument hd = new HeapDocument(myDoc.getKey());
                    minHeap.insert(hd);
                    inMemory.add(hd.uri);
                    myDoc.setLastUseTime(System.nanoTime());
                    hd.setLastUseTime(System.nanoTime());
                    this.minHeap.reHeapify(hd);
                }  
            }
        }
        else{ //generic command
            GenericCommand<URI> ge = (GenericCommand<URI>)undoing;
            Document myDoc = this.bTree.get(ge.getTarget());
            if (myDoc != null){
                HeapDocument hd = new HeapDocument(myDoc.getKey());
                minHeap.insert(hd);
                inMemory.add(hd.uri);
                myDoc.setLastUseTime(System.nanoTime());
                hd.setLastUseTime(System.nanoTime());
                this.minHeap.reHeapify(hd);
            }
        }
    }

     
    /**
     * undo the last put or delete that was done with the given URI as its key
     * @param url
     * @throws IllegalStateException if there are no actions on the command stack for the given URI
     */
    @Override
    public void undo(URI url) throws IllegalStateException {
        Stack<Undoable> tempStack = new StackImpl<>(); boolean foundFlag = false; GenericCommand<URI> gc; CommandSet<URI> gs;
        while(commandStack.size() != 0){
            Undoable current = commandStack.pop();
            if (current instanceof GenericCommand){
                gc = (GenericCommand<URI>) current;
                if (gc.getTarget().equals(url)){
                    foundFlag = true;
                    current.undo();
                    break;
                }
                else{tempStack.push(current);}
            }
            if(current instanceof CommandSet){
                gs = (CommandSet<URI>) current;
                if (gs.containsTarget(url)){
                    foundFlag = true;
                    gs.undo(url);
                    if (gs.size()!= 0){
                        tempStack.push(current);
                    }
                    break;
                }
                else{tempStack.push(current);}
            }
        }
        while (tempStack.size() != 0) { commandStack.push(tempStack.pop());}
        if (!foundFlag){ throw new IllegalStateException();}
        Document doc = this.bTree.get(url);
        if (doc != null){    
            HeapDocument hd = new HeapDocument(url);
            checkFits(doc);
            doc.setLastUseTime(System.nanoTime());
            hd.setLastUseTime(System.nanoTime());
            if (doc != null) {
                minHeap.insert(hd);
                inMemory.add(hd.uri);
                this.minHeap.reHeapify(hd);
            }
        }
        storageControlSendToBTree();
    }    
    
    
    private void massSegregationNanoTime(Set<Document> nanoSet){
        for (Document nanoDoc : nanoSet){
            HeapDocument hd = new HeapDocument(nanoDoc.getKey());
            nanoDoc.setLastUseTime(System.nanoTime());
            hd.setLastUseTime(System.nanoTime());
            if (nanoDoc != null && this.bTree.get(nanoDoc.getKey()) != null){
                checkFits(nanoDoc);
                minHeap.insert(hd);
                inMemory.add(hd.uri);
                this.minHeap.reHeapify(hd);
            }
        }
    }

    private void massSegregationNanoTimeURI(Set<URI> nanoSet){
        for (URI uri : nanoSet){
            HeapDocument hd = new HeapDocument(uri);
            hd.setLastUseTime(System.nanoTime());
            Document tempo = this.bTree.get(uri);
            if (uri != null && tempo != null){
                checkFits(tempo);
                minHeap.insert(hd);
                inMemory.add(hd.uri);
                this.minHeap.reHeapify(hd);
            }
        }
    }
    
    
    /*private class documentComparator implements Comparator<Document>{
        private final String word;
        private documentComparator(String word){
            this.word = word;
        }
    }*/
    private List<Document>uriToDocumentList(List<URI> uriList){
        List<Document> docList= new ArrayList<>();
        for(URI uri : uriList){
            docList.add(bTree.get(uri));
        }
        return docList;
    }

    private List<URI>documentToUriList(List<Document> docList){
        List<URI> uriList= new ArrayList<>();
        for(Document doc : docList){
            uriList.add(doc.getKey());
        }
        return uriList;
    }


	@Override
	public List<Document> search(String keyword) throws IOException{
        final String word = keyword;
        List<URI> sortedURIS = myTrie.getSorted(word, new Comparator<URI>() {
        @Override
        public int compare(URI u1, URI u2){
            int c1 = bTree.get(u1).wordCount(word);
            int c2 = bTree.get(u2).wordCount(word);
            if(c1 > c2){
                return -1;
            }
            else if(c1 < c2){
                return 1;
            }
            else{
                return 0;
            }
        }
        });
        List<Document> sortedDocuments = this.uriToDocumentList(sortedURIS);
        massSegregationNanoTime(new HashSet<>(sortedDocuments));
        storageControlSendToBTree();
        return sortedDocuments;
    }

	@Override
	public List<Document> searchByPrefix(String keywordPrefix) throws IOException{
        final String wordPrefix = keywordPrefix;
        List<URI> sortedURIS = myTrie.getAllWithPrefixSorted(wordPrefix, new Comparator<URI>() {
        @Override
        public int compare(URI u1, URI u2){
            Document d1 = bTree.get(u1);
            Document d2 = bTree.get(u2);
            int c1 = 0, c2 = 0;
            for(String word: d1.getWords()){
                if(word.startsWith(wordPrefix)){
                    c1+= d1.wordCount(word);
                }
            }
            for(String word: d2.getWords()){
                if(word.startsWith(wordPrefix)){
                    c2+= d2.wordCount(word);
                }
            }
            if(c1 > c2){
                return -1;
            }
            else if(c1 < c2){
                return 1;
            }
            else{
                return 0;
            }
        }
        });
        List<Document> sortedDocuments = this.uriToDocumentList(sortedURIS);
        massSegregationNanoTime(new HashSet<>(sortedDocuments));
        storageControlSendToBTree();
        return sortedDocuments;
	}


    private GenericCommand<URI> deleteForSet(URI url){
        Document previous = this.bTree.get(url);
        HeapDocument hd = new HeapDocument(url);
        boolean wasInMemory = inMemory.contains(url);
        GenericCommand<URI> myCommand = new GenericCommand<URI>(url, undoThis -> {
            this.bTree.put(url, previous);
            addDocumentToTrie(previous);
            if (wasInMemory){
                minHeap.insert(hd);
                inMemory.add(url);
            }
            else{ // was not in memory
                onDisk.add(url);
                try {
                    this.bTree.moveToDisk(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Document deletedDocument = this.bTree.put(url, null);
        if (deletedDocument != null && deletedDocument.getDocumentBinaryData() == null){
            removeFromTrie(deletedDocument);
        }
        return myCommand;
    }


	@Override
	public Set<URI> deleteAll(String keyword) {
		Set<URI> deletedUris = myTrie.deleteAll(keyword);
        //Set<URI> deletedUris= new HashSet<>();
        CommandSet<URI> AddbackAll = new CommandSet<>();
        for (URI uri : deletedUris){
            HeapDocument hd = new HeapDocument(uri);
            //deletedUris.add(document.getKey());
            removeFromHeap(hd);
            AddbackAll.addCommand(this.deleteForSet(uri));
        }
        this.commandStack.push(AddbackAll);
        massSegregationNanoTimeURI(deletedUris);
        return deletedUris;
	}

	@Override
	public Set<URI> deleteAllWithPrefix(String keywordPrefix) {
		Set<URI> deletedUris = myTrie.deleteAllWithPrefix(keywordPrefix);
        //Set<URI> deletedUris = new HashSet<>();
        CommandSet<URI> addBackAll = new CommandSet<>();
        for (URI uri : deletedUris){
            HeapDocument hd = new HeapDocument(uri);
            //deletedUris.add(document.getKey());
            removeFromHeap(hd);
            addBackAll.addCommand(this.deleteForSet(uri));
        }
        this.commandStack.push(addBackAll);
        massSegregationNanoTimeURI(deletedUris);
        return deletedUris;
	}


	@Override
	public List<Document> searchByMetadata(Map<String, String> keysValues) throws IOException{ //here
        List<Document> containsMedaData = new ArrayList<>();
        List<URI> containsMeta = new ArrayList<>();
        for(URI uri : this.uriMetaMap.keySet()){
            Map<String, String> docMetaData = this.uriMetaMap.get(uri);
            if (!docMetaData.keySet().containsAll(keysValues.keySet())){ //contain the same keys
                continue;
            }
            boolean metaDataMatchesValues = true;
            for (String metaKey : keysValues.keySet()){ //keys correspond to the right values
                if (!this.uriMetaMap.get(uri).get(metaKey).equals(keysValues.get(metaKey))){
                    metaDataMatchesValues = false;
                    break;
                }
            }
            if (metaDataMatchesValues){
                containsMeta.add(uri);
            }
        }
        containsMedaData = this.uriToDocumentList(containsMeta);
        massSegregationNanoTimeURI(new HashSet<>(containsMeta));
        storageControlSendToBTree();
        return containsMedaData;
	}
 


    private List<URI> normalKeywordSearch(String keyword){
        return new ArrayList<>(myTrie.get(keyword));
    }

    private List<URI> normalPrefixSearch(String prefix){
        List<URI> sortedURIS = myTrie.getAllWithPrefixSorted(prefix, new Comparator<URI>() {
            @Override
            public int compare(URI u1, URI u2){
                int x = 4;
                if(x > 2){
                    return -1;
                }
                else if(x < 2){
                    return 1;
                }
                else{
                    return 0;
                }
            }
            });
        return sortedURIS;
    }

    private List<URI> sortedPrefixMeta(List<URI> first, Comparator<URI> comparator){
        Collections.sort(first, comparator);
        return first;
    }

	@Override
	public List<Document> searchByKeywordAndMetadata(String keyword, Map<String, String> keysValues) throws IOException{
        List<URI> containsKeyword = this.normalKeywordSearch(keyword);
        List<Document> fraud = this.searchByMetadata(keysValues);
        List<URI> containsMetaData = documentToUriList(fraud);
        containsKeyword.retainAll(containsMetaData);
        containsKeyword = sortedPrefixMeta(containsKeyword, new Comparator<URI>() {
            @Override
        public int compare(URI u1, URI u2){
            int c1 = bTree.get(u1).wordCount(keyword);
            int c2 = bTree.get(u2).wordCount(keyword);
            if(c1 > c2){
                return -1;
            }
            else if(c1 < c2){
                return 1;
            }
            else{
                return 0;
            }
        }
        });
        //System.out.println(minHeap.peek().getDocumentTxt());
        List<Document> everythingReturn = uriToDocumentList(containsKeyword);
        massSegregationNanoTime(new HashSet<>(everythingReturn));
        storageControlSendToBTree();
        return everythingReturn;
	}

	@Override
	public List<Document> searchByPrefixAndMetadata(String keywordPrefix, Map<String, String> keysValues) throws IOException{
        List<URI> containsPrefix = this.normalPrefixSearch(keywordPrefix);
        List<Document> fraud = this.searchByMetadata(keysValues);
        List<URI> containsMetaData = documentToUriList(fraud);
        containsPrefix.retainAll(containsMetaData);
        containsPrefix = sortedPrefixMeta(containsPrefix, new Comparator<URI>() {
            @Override
            public int compare(URI u1, URI u2){
                Document d1 = bTree.get(u1);
                Document d2 = bTree.get(u2);
                int c1 = 0, c2 = 0;
                for(String word: d1.getWords()){
                    if(word.startsWith(keywordPrefix)){
                        c1+= d1.wordCount(word);
                    }
                }
                for(String word: d2.getWords()){
                    if(word.startsWith(keywordPrefix)){
                        c2+= d2.wordCount(word);
                    }
                }
                if(c1 > c2){
                    return -1;
                }
                else if(c1 < c2){
                    return 1;
                }
                else{
                    return 0;
                }
            }
            });
        List<Document> everythingReturn = uriToDocumentList(containsPrefix);
        massSegregationNanoTime(new HashSet<>(everythingReturn));
        storageControlSendToBTree();
        return everythingReturn;
	}

	@Override
	public Set<URI> deleteAllWithMetadata(Map<String, String> keysValues) throws IOException{
        Set<URI> deletedUris = new HashSet<>();
        List<Document> containsMetaData = searchByMetadata(keysValues);
        CommandSet<URI> addBackAll = new CommandSet<>();
        for (Document doc : containsMetaData){
            HeapDocument hd = new HeapDocument(doc.getKey());
            deletedUris.add(doc.getKey());
            removeFromHeap(hd);
            addBackAll.addCommand(this.deleteForSet(doc.getKey())); 
        }
        this.commandStack.push(addBackAll);
        massSegregationNanoTime(new HashSet<>(containsMetaData));
        return deletedUris;
	}

	@Override
	public Set<URI> deleteAllWithKeywordAndMetadata(String keyword, Map<String, String> keysValues) throws IOException{
        Set<URI> deletedUris= new HashSet<>();
        List<Document> containsAll = searchByKeywordAndMetadata(keyword, keysValues);
		CommandSet<URI> addBackAll = new CommandSet<>();
        for (Document doc : containsAll){
            HeapDocument hd = new HeapDocument(doc.getKey());
            deletedUris.add(doc.getKey());
            removeFromHeap(hd);
            addBackAll.addCommand(this.deleteForSet(doc.getKey()));
        }
        this.commandStack.push(addBackAll);
        massSegregationNanoTime(new HashSet<>(containsAll));
        return deletedUris;
	}

	@Override
	public Set<URI> deleteAllWithPrefixAndMetadata(String keywordPrefix, Map<String, String> keysValues) throws IOException{
		Set<URI> deletedUris = new HashSet<>();
        List<Document> containsAll = this.searchByPrefixAndMetadata(keywordPrefix, keysValues);
        CommandSet<URI> addBackAll = new CommandSet<>();
        for (Document doc : containsAll){
            HeapDocument hd = new HeapDocument(doc.getKey());
            deletedUris.add(doc.getKey());
            removeFromHeap(hd);
            addBackAll.addCommand(this.deleteForSet(doc.getKey()));
        }
        this.commandStack.push(addBackAll);
        massSegregationNanoTime(new HashSet<>(containsAll));
        return deletedUris;
	}
    
    private int getTotalStorageBytes(){
       int totalLength = 0;
        for (URI myURI : inMemory){
            Document myDoc = bTree.get(myURI);
            if (myDoc.getDocumentBinaryData() == null){ //it is a text document
                byte[] lengthTxtDoc = myDoc.getDocumentTxt().getBytes();
                totalLength += lengthTxtDoc.length;
            }
            else{ //it is a binary document
                byte[] lengthBinDoc = myDoc.getDocumentBinaryData();
                totalLength += lengthBinDoc.length;
            }
        }
        return totalLength;
    }
    
    private int getTotalStorageCounts(){
        return inMemory.size();
    }
    
    

    private void removeFromStack(Document deleteDocument){
        URI url = deleteDocument.getKey();
        Stack<Undoable> tempStack = new StackImpl<>();
        GenericCommand<URI> gc; 
        CommandSet<URI> gs;
        while(commandStack.size() != 0){
            Undoable current = commandStack.pop();
            if (current instanceof GenericCommand){
                gc = (GenericCommand<URI>) current;
                if (gc.getTarget().equals(url)){ //current.undo();    
                }
                else{
                    tempStack.push(current);
                }
            }
            if(current instanceof CommandSet){
                gs = (CommandSet<URI>) current;
                if (gs.containsTarget(url)){ //gs.undo(url);
                    if (gs.size()!= 0){
                        tempStack.push(current);
                    }
                }
                else{
                    tempStack.push(current);
                }
            }
        }
        while (tempStack.size() != 0){     commandStack.push(tempStack.pop());     }
    }
    
    
    
    private void storageControlSendToBTree(){
        while (this.getTotalStorageCounts() > this.maxDocumentCount || (this.getTotalStorageBytes() > this.maxDocumentBytes)){
            HeapDocument removed = minHeap.remove();
            inMemory.remove(removed.uri);
            //we know the document removed will be in memory, so freely call BTree.get
            Document documentRemoved = bTree.get(removed.uri);
            //removeFromStack(removed);
            //subtract bytes
            //if (documentRemoved.getDocumentBinaryData()== null){
            //    currentBytes -= documentRemoved.getDocumentTxt().getBytes().length;
          //  }
           // else{
             //   currentBytes -= documentRemoved.getDocumentBinaryData().length;
           // }
            try { 
                bTree.moveToDisk(removed.uri);
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void setMaxDocumentCount(int limit) {
        if (limit < 1){
            throw new IllegalArgumentException();
        }
        this.maxDocumentCount = limit;
        storageControlSendToBTree();
    }

    @Override
    public void setMaxDocumentBytes(int limit) {
        if (limit < 1){
            throw new IllegalArgumentException();
        }
        this.maxDocumentBytes = limit;
        storageControlSendToBTree();
    }

}