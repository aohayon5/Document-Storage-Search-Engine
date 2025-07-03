package edu.yu.cs.com1320.project.stage6.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import edu.yu.cs.com1320.project.stage6.Document;
import edu.yu.cs.com1320.project.stage6.PersistenceManager;

public class DocumentPersistenceManager implements PersistenceManager<URI, Document>{
    private Gson gson;
    private File baseDir;
    public DocumentPersistenceManager(File baseDir){
        this.baseDir = baseDir;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DocumentImpl.class, new DocumentSerializerAndDeserializer());
        gsonBuilder.registerTypeAdapter(new TypeToken<HashMap<String, Integer>>(){}.getType(), new HashMapDeserializer());
        this.gson = gsonBuilder.create();
    }

    @Override
    public void serialize(URI uri, Document doc) throws IOException {
        String json = this.gson.toJson(doc);
        String fileName = uri.toString().replaceFirst("http://", "");
        fileName += ".json";
        File myFile = fileConstructor(fileName);
        
        File parentDir = myFile.getParentFile();
        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Can't create the path for some reason " + parentDir.getPath());
            }
        }
        FileWriter fw = new FileWriter(myFile);
        fw.write(json);
        fw.close();
    }

    private File fileConstructor(String fileName){
        if (baseDir == null){
            return new File(fileName);
        }
        else{
            return new File(baseDir, fileName);
        }
    }

    @Override
    public Document deserialize(URI uri) throws IOException {
        String fileName = uri.toString().replaceFirst("http://", "");
        fileName += ".json";
        File retrievingFile = fileConstructor(fileName);
        FileReader fr = new FileReader(retrievingFile);
        return gson.fromJson(fr, DocumentImpl.class);

    }

    @Override
    public boolean delete(URI uri) throws IOException {
        String fileName = uri.toString().replace("http://", "");
        fileName += ".json";
        File deleteFile = fileConstructor(fileName);
        if (!deleteFile.exists()){
            return false;
        }
        boolean worked = deleteFile.delete();
        //get rid of empty folders/directory
        if (worked){
            delFiles(deleteFile.getParentFile());
        }
        return worked;
    }

    private File delFiles(File file){
        if (file == null){
            return null;
        }
        File[] subFoldersOrFiles = file.listFiles();
        if (subFoldersOrFiles == null || subFoldersOrFiles.length == 0){
            file.delete();
            delFiles(file.getParentFile());
        }
        return file;
    }

    private class HashMapDeserializer implements JsonDeserializer<HashMap<String, Integer>> {
        @Override
        public HashMap<String, Integer> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            HashMap<String, Integer> result = new HashMap<>();
            JsonObject jsonObject = json.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                result.put(entry.getKey(), entry.getValue().getAsInt());
            }
            return result;
        }
    }

    private class DocumentSerializerAndDeserializer implements JsonSerializer<Document>, JsonDeserializer<Document>{
        @Override
        public JsonElement serialize(Document doc, Type typeOfSrc, JsonSerializationContext context) {
            //Stringing the maps
            Gson gson = new Gson();
            //String jsonMetadata = gson.toJson(doc.getMetadata());
            JsonElement jsonMetadata = gson.toJsonTree(doc.getMetadata());
            //String jsonWordAndCount = gson.toJson(doc.getWordMap());
            JsonElement jsonWordAndCount = gson.toJsonTree(doc.getWordMap());
            //add properties to the Json
            JsonObject jsObj = new JsonObject();
            //text or binary
            if (doc.getDocumentTxt() != null){ //it's a text document
                jsObj.addProperty("txtContent", doc.getDocumentTxt());
            }
            else{ //it's binary
                byte[] byteContent = doc.getDocumentBinaryData();
                String base64Encoded = Base64.getEncoder().encodeToString(byteContent);
                jsObj.addProperty("byteContent", base64Encoded);
            }
            jsObj.add("metaData", jsonMetadata);
            jsObj.addProperty("uri", doc.getKey().toString());
            jsObj.add("jsonWordAndCount", jsonWordAndCount);
            //jsObj.addProperty("jsonWordAndCount", jsonWordAndCount);
            return jsObj;
        }

        @Override
        public Document deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsObj = json.getAsJsonObject();;
            String uriString = jsObj.get("uri").getAsString();
            JsonElement metaDataJson = jsObj.get("metaData");
            JsonElement wordAndCountJson = jsObj.get("jsonWordAndCount");
            
            //maps and uri made
            URI uri = null;
            try {
                uri = new URI(uriString);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            HashMap<String, Integer> wordMap = gson.fromJson(wordAndCountJson, new TypeToken<HashMap<String, Integer>>(){}.getType());

            HashMap<String, String> metaData = gson.fromJson(metaDataJson, HashMap.class);
            
            //find and get type of content and then make the doucment
            JsonElement txtContentElement = jsObj.get("txtContent");
            JsonElement byteContentElement = jsObj.get("byteContent");
            if (txtContentElement != null && !txtContentElement.isJsonNull()) {
                String contentTxt = txtContentElement.getAsString();
                Document myDoc = new DocumentImpl(uri, contentTxt, wordMap);
                myDoc.setMetadata(metaData);
                return myDoc;
            } 
            else{
                String base64EncodedContent = byteContentElement.getAsString();
                byte[] contentBin = Base64.getDecoder().decode(base64EncodedContent);
                Document myDoc = new DocumentImpl(uri, contentBin);
                myDoc.setWordMap(wordMap);
                myDoc.setMetadata(metaData);
                return myDoc;
            }
        }
    }
}
