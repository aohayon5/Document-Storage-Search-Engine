package edu.yu.cs.com1320.project.stage4.impl;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.Stack;
import edu.yu.cs.com1320.project.Trie;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.DocumentStore;
import edu.yu.cs.com1320.project.undo.CommandSet;
import edu.yu.cs.com1320.project.undo.GenericCommand;
import edu.yu.cs.com1320.project.undo.Undoable;

public class DocumentStoreImpl implements DocumentStore{
    private HashTable<URI, Document> docStore;
    private Stack<Undoable> commandStack;
    private Trie<Document> myTrie;

    public DocumentStoreImpl(){
        this.docStore = new HashTableImpl<>();
        this.commandStack = new StackImpl<>();
        this.myTrie = new TrieImpl<>();
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
        //Undo logic
        GenericCommand<URI> toPush;
        //If the key was not previously present, that means that this metadata was just fully added, not changed, so delete the key-value pair
        HashTable<String, String> docsMetaData = this.docStore.get(uri).getMetadata();
        if(!docsMetaData.containsKey(key)){
            toPush = new GenericCommand<URI>(uri, undoThis -> this.docStore.get(uri).setMetadataValue(key, null));
        }
        else{ //change it back
            String previousValue = this.docStore.get(uri).getMetadataValue(key);
            toPush = new GenericCommand<URI>(uri, undoThis -> this.docStore.get(uri).setMetadataValue(key, previousValue));
        }
        this.commandStack.push(toPush);
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
        if (uri == null || uri.toString().isEmpty() || format == null){
            throw new IllegalArgumentException();
        }
        if (input == null){
            int tempHashCode = docStore.get(uri) == null ? 0 : docStore.get(uri).hashCode();
            return delete(uri) ? tempHashCode : 0;
        }
        byte[] content = null;
        try {   content = input.readAllBytes();} 
        catch (Exception e) {    throw new IOException();}
        finally{    input.close();  }
        Document newDoc = createNewDoc(uri, format, content);
        Document previousDocument = this.docStore.get(uri);
        this.commandStack.push(new GenericCommand<URI>(uri, undoThis -> {
            this.docStore.put(uri, previousDocument);
            if (previousDocument != null){
                addDocumentToTrie(previousDocument);
            }
            removeFromTrie(newDoc);
        }));
        if (format == DocumentFormat.TXT){ //MODIFY TRIE
            if (previousDocument == null){
                removeFromTrie(previousDocument);
            }
            addDocumentToTrie(newDoc);
        }
        Document previous = this.docStore.put(uri, newDoc);
        return previous == null ? 0 : previous.hashCode();
    }


    private void addDocumentToTrie(Document doc){
        if (doc == null){
            return;
        }
        for(String word : doc.getWords()){
            myTrie.put(word, doc);
        }
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
    public Document get(URI url){
        return docStore.get(url);
    }

    @Override
    public boolean delete(URI url) { //removes from the doc store and from the entire Trie
        Document previous = this.docStore.get(url);
        this.commandStack.push(new GenericCommand<URI>(url, undoThis -> {
            this.docStore.put(url, previous);
            addDocumentToTrie(previous);
        }));
        Document deletedDocument = this.docStore.put(url, null);
        if (deletedDocument != null && deletedDocument.getDocumentBinaryData() == null){
            removeFromTrie(deletedDocument);
        }
        return deletedDocument == null ? false : true;
    }

    private void removeFromTrie(Document doc){ //check if I need undo implementation here
        if (doc == null){
            return;
        }
        for(String word : doc.getWords()){
            myTrie.delete(word, doc);
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
            commandStack.pop().undo();
        }
     }


     
    /**
     * undo the last put or delete that was done with the given URI as its key
     * @param url
     * @throws IllegalStateException if there are no actions on the command stack for the given URI
     */
    @Override
    public void undo(URI url) throws IllegalStateException {
        Stack<Undoable> tempStack = new StackImpl<>();
        boolean foundFlag = false;
        GenericCommand<URI> gc; 
        CommandSet<URI> gs;
        while(commandStack.size() != 0){
            Undoable current = commandStack.pop();
            if (current instanceof GenericCommand){
                gc = (GenericCommand<URI>) current;
                if (gc.getTarget().equals(url)){
                    foundFlag = true;
                    current.undo();
                    break;
                }
                else{
                    tempStack.push(current);
                }
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
                else{
                    tempStack.push(current);
                }
            }
        }
        while (tempStack.size() != 0) {
            commandStack.push(tempStack.pop());
        }
        if (!foundFlag){
            throw new IllegalStateException();
        }
    }
    

    /*private class documentComparator implements Comparator<Document>{
        private final String word;
        private documentComparator(String word){
            this.word = word;
        }
    }*/

	@Override
	public List<Document> search(String keyword) {
        final String word = keyword;
        List<Document> sortedDocuments = myTrie.getSorted(word, new Comparator<Document>() {
        @Override
        public int compare(Document d1, Document d2){
            int c1 = d1.wordCount(word);
            int c2 = d2.wordCount(word);
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
        return sortedDocuments;
    }

	@Override
	public List<Document> searchByPrefix(String keywordPrefix) {
        final String wordPrefix = keywordPrefix;
        List<Document> sortedDocuments = myTrie.getAllWithPrefixSorted(wordPrefix, new Comparator<Document>() {
        @Override
        public int compare(Document d1, Document d2){
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
        return sortedDocuments;
	}


    private GenericCommand<URI> deleteForSet(URI url){
        Document previous = this.docStore.get(url);
        GenericCommand<URI> myCommand = new GenericCommand<URI>(url, undoThis -> {
            this.docStore.put(url, previous);
            addDocumentToTrie(previous);
        });
        Document deletedDocument = this.docStore.put(url, null);
        if (deletedDocument != null && deletedDocument.getDocumentBinaryData() == null){
            removeFromTrie(deletedDocument);
        }
        return myCommand;
    }


	@Override
	public Set<URI> deleteAll(String keyword) {
		Set<Document> deleteDocumets = myTrie.deleteAll(keyword);
        Set<URI> deletedUris= new HashSet<>();
        CommandSet<URI> AddbackAll = new CommandSet<>();
        for (Document document : deleteDocumets){
            deletedUris.add(document.getKey());
            AddbackAll.addCommand(this.deleteForSet(document.getKey()));
        }
        this.commandStack.push(AddbackAll);
        return deletedUris;
	}

	@Override
	public Set<URI> deleteAllWithPrefix(String keywordPrefix) {
		Set<Document> deleteDocumets = myTrie.deleteAllWithPrefix(keywordPrefix);
        Set<URI> deletedUris = new HashSet<>();
        CommandSet<URI> addBackAll = new CommandSet<>();
        for (Document document : deleteDocumets){
            deletedUris.add(document.getKey());
            addBackAll.addCommand(this.deleteForSet(document.getKey()));
        }
        this.commandStack.push(addBackAll);
        return deletedUris;
	}

	@Override
	public List<Document> searchByMetadata(Map<String, String> keysValues) { //here
        List<Document> containsMedaData = new ArrayList<>();
        Set<Document> containsMeta = new HashSet<>();
		Collection<Document> allDocs = docStore.values();
        for(Document doc : allDocs){
            HashTable<String, String> docMetaData = doc.getMetadata();
            if (!docMetaData.keySet().containsAll(keysValues.keySet())){ //contain the same keys
                continue;
            }
            boolean metaDataMatchesValues = true;
            for (String metaKey : keysValues.keySet()){ //keys correspond to the right values
                if (!doc.getMetadataValue(metaKey).equals(keysValues.get(metaKey))){
                    metaDataMatchesValues = false;
                    break;
                }
            }
            if (metaDataMatchesValues){
                containsMeta.add(doc);
            }
        }
        containsMedaData.addAll(containsMeta);
        return containsMedaData;
	}
 
	@Override
	public List<Document> searchByKeywordAndMetadata(String keyword, Map<String, String> keysValues) {
        List<Document> containsKeyword = this.search(keyword);
        List<Document> containsMetaData = this.searchByMetadata(keysValues);
        containsKeyword.retainAll(containsMetaData);
        return containsKeyword;
	}

	@Override
	public List<Document> searchByPrefixAndMetadata(String keywordPrefix, Map<String, String> keysValues) {
		List<Document> containsPrefix = this.searchByPrefix(keywordPrefix);
        List<Document> containsMetaData = this.searchByMetadata(keysValues);
        containsPrefix.retainAll(containsMetaData);
        return containsPrefix;
	}

	@Override
	public Set<URI> deleteAllWithMetadata(Map<String, String> keysValues) {
        Set<URI> deletedUris = new HashSet<>();
        List<Document> containsMetaData = searchByMetadata(keysValues);
        CommandSet<URI> addBackAll = new CommandSet<>();
        for (Document doc : containsMetaData){
            deletedUris.add(doc.getKey());
            addBackAll.addCommand(this.deleteForSet(doc.getKey())); 
        }
        this.commandStack.push(addBackAll);
        return deletedUris;
	}

	@Override
	public Set<URI> deleteAllWithKeywordAndMetadata(String keyword, Map<String, String> keysValues) {
        Set<URI> deletedUris= new HashSet<>();
        List<Document> containsAll = searchByKeywordAndMetadata(keyword, keysValues);
		CommandSet<URI> addBackAll = new CommandSet<>();
        for (Document doc : containsAll){
            deletedUris.add(doc.getKey());
            addBackAll.addCommand(this.deleteForSet(doc.getKey()));
        }
        this.commandStack.push(addBackAll);
        return deletedUris;
	}

	@Override
	public Set<URI> deleteAllWithPrefixAndMetadata(String keywordPrefix, Map<String, String> keysValues) {
		Set<URI> deletedUris = new HashSet<>();
        List<Document> containsAll = this.searchByPrefixAndMetadata(keywordPrefix, keysValues);
        CommandSet<URI> addBackAll = new CommandSet<>();
        for (Document doc : containsAll){
            deletedUris.add(doc.getKey());
            addBackAll.addCommand(this.deleteForSet(doc.getKey()));
        }
        this.commandStack.push(addBackAll);
        return deletedUris;
	}




}