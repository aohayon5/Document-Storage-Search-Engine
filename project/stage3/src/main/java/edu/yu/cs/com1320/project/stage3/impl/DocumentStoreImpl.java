package edu.yu.cs.com1320.project.stage3.impl;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.Stack;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.DocumentStore;
import edu.yu.cs.com1320.project.undo.Command;

public class DocumentStoreImpl implements DocumentStore{
    private HashTable<URI, Document> docStore;
    private Stack<Command> commandStack;

    public DocumentStoreImpl(){
        this.docStore = new HashTableImpl<>();
        this.commandStack = new StackImpl<>();
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
        Command toPush;
        //If the key was not previously present, that means that this metadata was just fully added, not changed, so delete the key-value pair
        HashTable<String, String> docsMetaData = this.docStore.get(uri).getMetadata();
        if(!docsMetaData.containsKey(key)){
            toPush = new Command(uri, undoThis -> this.docStore.get(uri).setMetadataValue(key, null));
        }
        else{ //change it back
            String previousValue = this.docStore.get(uri).getMetadataValue(key);
            toPush = new Command(uri, undoThis -> this.docStore.get(uri).setMetadataValue(key, previousValue));
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
        Command toPush;
        Document previousDocument = this.docStore.get(uri);
        toPush = new Command(uri, undoThis -> this.docStore.put(uri, previousDocument));
        commandStack.push(toPush);
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
    public Document get(URI url){
        return docStore.get(url);
    }

    @Override
    public boolean delete(URI url) {
        Document previous = this.docStore.get(url);
        commandStack.push(new Command(url, undoThis -> this.docStore.put(url, previous)));
        Document exists = this.docStore.put(url, null);
        return exists == null ? false : true;
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
        Stack<Command> tempStack = new StackImpl<>();
        boolean foundFlag = false;
        while(commandStack.size() != 0){
            Command current = commandStack.pop();
            if (current.getUri().equals(url)){
                foundFlag = true;
                current.undo();
                break;
            }
            else{
                tempStack.push(current);
            }
        }
        while (tempStack.size() != 0) {
            commandStack.push(tempStack.pop());
        }
        if (!foundFlag){
            throw new IllegalStateException();
        }
    }
}