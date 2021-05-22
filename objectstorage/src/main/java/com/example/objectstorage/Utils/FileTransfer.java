//package com.example.objectstorage.Utils;
//
//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.FileItemFactory;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//import org.apache.commons.io.FileUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.commons.CommonsMultipartFile;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//
//
//@Component
//public class FileTransfer {
//    public MultipartFile transfer(String content, String type){
//        try {
//            File temp = File.createTempFile("temp","."+type);
//            FileUtils.writeStringToFile(temp,content);
//            FileItem fileItem = createFileItem(temp);
//            MultipartFile tempMultipartFile = new CommonsMultipartFile(fileItem);
//            return tempMultipartFile;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }
//    private static FileItem createFileItem(File file) {
//        FileItemFactory factory = new DiskFileItemFactory(16, null);
//        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
//        int bytesRead = 0;
//        byte[] buffer = new byte[8192];
//        try {
//            FileInputStream fis = new FileInputStream(file);
//            OutputStream os = item.getOutputStream();
//            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
//            os.close();
//            fis.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return item;
//    }
//}
