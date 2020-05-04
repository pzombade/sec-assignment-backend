
package com.sec.assignment;


import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Component
public class DataInputFileRow {

    String dirPath = "C:\\Users\\pzombade\\Desktop\\sec\\test-SharePoint\\test-SharePoint\\inputFiles\\inputFiles";
    Map<String,Integer> userNameCountMap = new TreeMap<>();
    Map<String,Integer> fileNameCountMap = new TreeMap<>();
    Map<String,Integer> dateActivitCountMap = new TreeMap<>();

    public void process(MultipartFile file){
        FileHandlerUtil.saveFile(file);
        FileHandlerUtil.unzip(file);
    }

    public void parse(){
        try{
            Files.newDirectoryStream(Paths.get(dirPath), path -> true).forEach(row -> parse2(row));
        }catch (Exception e){
            System.out.println("Exception - Wile loadidng folder " + e);
        }
        //System.out.println("userNameMap >> " + userNameCountMap);
       // printTheSort(userNameCountMap);

        //System.out.println("fileNameCountMap >> " + fileNameCountMap);
       // printTheSort();
    }

    public void parse2(Path fileName){
        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(fileName)) {
            //stream.forEach(System.out::println);
            stream.forEach(row->parseRow(row));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseRow(String row){
        Map<String,String> map = new HashMap<>();
        String[] cols = row.split("\\t");

        for(int i=0; i<cols.length; i++){
            if(i==11){
                updateMap(map, cols[i]+ " "+cols[++i]);
              //  System.out.println(i+ " "+cols[i]+ " "+cols[++i]);
            }else {
                updateMap(map, cols[i]);
              //  System.out.println(i+ " "+cols[i]);
            }
        }
    }

    private void updateMap(Map<String,String> map, String col) {
        if(col.contains("=")){
            String[] colArr = col.split("=");
            String key = colArr[0];
            col = col.trim().replaceAll("\\s+",ParseConstants.TIME_SEPERATOR);
            String value = colArr[1].trim();
            processKey(key,value);
            map.put(key,value);
        }
    }

    private void processKey(String key, String value) {
        if (key.equals("USER_NAME")) {
            Integer count = userNameCountMap.get(value);
            count = count == null ? 0 : count;
            userNameCountMap.put(value,++count);
        } else if(key.equals("OBJECTNAME")) {
            Integer count = fileNameCountMap.get(value);
            count = count == null ? 0 : count;
            fileNameCountMap.put(value,++count);
        } else if(key.equals("DATETIME")) {
            Integer count = dateActivitCountMap.get(value);
            count = count == null ? 0 : count;
            dateActivitCountMap.put(value,++count);
        }
    }

    public List<KVPair> getTopFileNames(){
        return printTheSort(fileNameCountMap);
    }

    public List<KVPair> getTopUserNames(){
        return printTheSort(userNameCountMap);
    }

    public List<KVPair> getDateActivitCountMap(){
        return printTheSort(dateActivitCountMap);
    }

    public List<KVPair> printTheSort(Map<String,Integer> map){
        List<Map.Entry> entryList = new LinkedList(map.entrySet());
        Comparator<Map.Entry> com = (e1,e2)->(Integer) e2.getValue()-(Integer)e1.getValue();
        Collections.sort(entryList, com);
        entryList = entryList.subList(0,ParseConstants.LIMIT_TOP_X_RECORDS);
        entryList.stream().forEach(System.out::println);

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