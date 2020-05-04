
package com.sec.assignment;


import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Component
public class DataInputFileRow {

    String dirPath = "C:\\Users\\pzombade\\Desktop\\sec\\extracted_files\\inputFiles";
    Map<String,Integer> userNameCountMap = new TreeMap<>();
    Map<String,Integer> fileNameCountMap = new TreeMap<>();
    Map<String,Integer> dateActivitCountMap = new TreeMap<>();

    /**
     * Process the uploaded zipped files.
     * @param file
     */
    public void process(MultipartFile file){
        FileHandlerUtil.saveFile(file);
        FileHandlerUtil.unzip(file);
    }

    /**
     * Start parsing the extracted files one by one
     */
    public void parse(){
        try{
            Files.newDirectoryStream(Paths.get(dirPath), path -> true).forEach(row -> parseFile(row));
        }catch (Exception e){
            System.out.println("Exception - Wile loadidng folder " + e);
        }
    }

    /**
     * Parse the given file
     * @param fileName
     */
    public void parseFile(Path fileName){
        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(fileName)) {
            //stream.forEach(System.out::println);
            stream.forEach(row->parseRow(row));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse the given row and update the records map
     * @param row
     */
    public void parseRow(String row){
        Map<String,String> map = new HashMap<>();
        String[] cols = row.split("\\t");

        for(int i=0; i<cols.length; i++){
            if(i==11 || i==2){
                updateMap(map, cols[i]+ " "+cols[++i]);
              //  System.out.println(i+ " "+cols[i]+ " "+cols[++i]);
            }else {
                updateMap(map, cols[i]);
              //  System.out.println(i+ " "+cols[i]);
            }
        }
    }

    /**
     * Prepare the key - replace empty spaces and trim the blank spaces
     * @param map
     * @param col
     */
    private void updateMap(Map<String,String> map, String col) {
        if(col.contains("=")){
            String[] colArr = col.split("=");
            String key = colArr[0];
            col = col.trim().replaceAll("\\s+",ParseConstants.TIME_SEPERATOR);
            String value = colArr[1].trim();
            processKey(key,value);
           // map.put(key,value);
        }
    }

    /**
     * Update the map with keys and counts
     * @param key
     * @param value
     */
    private void processKey(String key, String value) {
        if (key.equals("USER_NAME")) {
            Integer count = userNameCountMap.get(value);
            count = count == null ? 0 : count;
            userNameCountMap.put(value, ++count);
        } else if(key.equals("OBJECTNAME")) {
            Integer count = fileNameCountMap.get(value);
            count = count == null ? 0 : count;
            fileNameCountMap.put(value, ++count);
        } else if(key.equals("DATETIME")) {
            value = FileHandlerUtil.getHour(value);
            Integer count = dateActivitCountMap.get(value);
            count = count == null ? 0 : count;
            dateActivitCountMap.put(value, ++count);
        }
    }

    public List<KVPair> getTopFileNames(){
        return printTheSort(fileNameCountMap);
    }

    public List<KVPair> getTopUserNames(){
        return printTheSort(userNameCountMap);
    }

    public List<KVPair> getDateActivitCountMap(){
        return populateKVPair(dateActivitCountMap, false);
    }

    public List<KVPair> printTheSort(Map<String,Integer> map){
        return getKvPairsList(new LinkedList(map.entrySet()));
    }

    public List<KVPair> populateKVPair(Map<String,Integer> map, boolean showTopOnly){
        List<Map.Entry> entryList = new LinkedList(map.entrySet());
        Comparator<Map.Entry> com = (e1,e2)->(Integer) e2.getValue()-(Integer)e1.getValue();
        Collections.sort(entryList, com);

        // Get the top 5 records only if requested
        if(showTopOnly) {
            entryList = entryList.subList(0, ParseConstants.LIMIT_TOP_X_RECORDS);
        }

        entryList.stream().forEach(System.out::println);
        List<KVPair> list = getKvPairsList(entryList);
        return list;
    }

    private List<KVPair> getKvPairsList(List<Map.Entry> entryList) {
        List<KVPair> list = new ArrayList();
        entryList.forEach(e->{
            KVPair kvPair = new KVPair();
            kvPair.setKey((String) e.getKey());
            kvPair.setValue((Integer) e.getValue());
            list.add(kvPair);
        });
        return list;
    }
}