package com.sec.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class FileHandlerUtil {

    @Autowired
    private DataInputFileRow fileHandler;
    private static String EXTRACTED_FOLDER = "C:\\Users\\pzombade\\Desktop\\sec\\extracted_files";
    private static String UPLOADED_FOLDER = "C:\\Users\\pzombade\\Desktop\\sec\\uploaded_files";

    /**
     * Save the uploaded files to a temporary location on the server
     * @param file
     * @return
     */
    public static String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            System.out.println("Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + File.separator + file.getOriginalFilename());
            Files.write(path, bytes);
            System.out.println("You successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Ok"; // fileHandler.getTopFileNames().toString();
    }


    /**
     * Unzip the zipped files
     * @param file
     */
    public static void unzip(MultipartFile file) {
        File dir = new File(EXTRACTED_FOLDER);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        FileSystem fileSystem = FileSystems.getDefault();
        try {
            String zipFilePath = UPLOADED_FOLDER + File.separator + file.getOriginalFilename();
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                if (ze.isDirectory())
                {
                    System.out.println("Creating Directory:" + EXTRACTED_FOLDER + File.separator + ze.getName());
                    Files.createDirectories(fileSystem.getPath(EXTRACTED_FOLDER + File.separator + ze.getName()));
                    ze = zis.getNextEntry();
                }
                else {
                    String fileName = ze.getName();
                    File newFile = new File(EXTRACTED_FOLDER + File.separator + fileName);
                    System.out.println("Unzipping to " + newFile.getAbsolutePath());
                    //create directories for sub directories in zip
                    new File(newFile.getParent()).mkdirs();
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    //close this ZipEntry
                    zis.closeEntry();
                    ze = zis.getNextEntry();
                }
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done Unzipping the files...");
    }


    /**
     * Get the hour digit from the date
     * @param dateTime - The date time
     * @return the digit
     */
    public static String getHour(String dateTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(dateTime));
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        return hours+"";
    }
}
