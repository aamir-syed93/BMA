package com.bma.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import com.bma.bean.AbstractBean;
import com.bma.bean.Complain;


public class Util {

	public static String type=null;
	public static String ctype=null;

	public static String category=null;
	public static String cat=null;
	public static String error=null;
	public static String  generateUniqueUUID() {
		UUID uniqueKey = UUID.randomUUID();   
		return uniqueKey.toString();
	} 
	
	public static Properties loadQueries(){
		Properties prop = new Properties();
		try {
			prop.load(AbstractBean.class.getClassLoader().getResourceAsStream("databaseQueries.properties"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return prop;
	}
	
	public static Properties loadConstants(){
		Properties prop = new Properties();
		try {
			prop.load(AbstractBean.class.getClassLoader().getResourceAsStream("constants.properties"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return prop;
	}
	public static Properties loadConfiguration(){
		Properties prop = new Properties();
		try {
			prop.load(AbstractBean.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return prop;
	}
	
	public static Properties getProperties(){
		Properties prop = new Properties();
		try {
			prop.load(AbstractBean.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return prop;
	}
	
	//unique complian id
	public static String  generateComplainUniqueUUID(Complain complainDetails) {
		
	// Integer complainId=complainDetails.getId();
		
		System.out.println("complainDetails.getCategory() in util "+complainDetails.getCategory());
		
		if(complainDetails.getCategory().equals("Other"))
		{
		
			cat="OTR";
		}
		else
		{
			category=complainDetails.getCategory().getName();
			cat=category.substring(0, 3).toUpperCase();
			
		}
		type=complainDetails.getComplainTypeObj().getName();

		ctype=type.substring(0,1).toUpperCase();
		String extraZero="0";
	 int userId=complainDetails.getUserId();
	 
	 
	 String uniqueKey="IRS"+ctype+cat+extraZero+userId+extraZero;
	 System.out.println("cat is "+cat);
	 System.out.println("unique key is "+uniqueKey);
	 return uniqueKey;
	} 
	
	
	//for uploaded docs
	public static String saveFile(InputStream uploadedInputStream,
			String serverLocation,String filename) {
		
		String path=SystemUtils.getProjectPath()+serverLocation;
	
		File targetFolder = new File(path);
		String filenameGeneratd=generateUniqueUUID()+filename;
		if (!targetFolder.exists()) {
			targetFolder.mkdirs();
		}
		
		try {
			OutputStream outpuStream = new FileOutputStream(new File(path+filenameGeneratd));
			int read = 0;
			byte[] bytes = new byte[1024];

			outpuStream = new FileOutputStream(new File(path+filenameGeneratd));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}
			outpuStream.flush();
			outpuStream.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return "..\\IRSdocuments"+serverLocation+filenameGeneratd;
     
	}
	
	//OCR Read
	public static String OCRRead(String filePath) {
		String tempPath=filePath.replace("..\\IRSdocuments", "");
		String path=SystemUtils.getProjectPath()+tempPath;
        //File imageFile = new File(path);
        
        System.out.println("path is "+path);
        File imageFile = new File("D:/pan.jpg");
		Tesseract instance = Tesseract.getInstance(); 

		String result = null;
		try {
			//System.out.println(imageFile);
			//System.out.println(imageFile.getAbsolutePath());
			result = instance.doOCR(imageFile);
			System.out.println(result);

		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}
		imageFile = null;
		return result;
	}
	
	public static String hashPassword(final String password) {
		if(null != password){
			MessageDigest mDigest;
			try {
				mDigest = MessageDigest.getInstance("SHA1");
				byte[] result = mDigest.digest(password.getBytes());
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < result.length; i++) {
					sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16)
							.substring(1));
				}
				return (sb.toString());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return null;
			}
		}else{
			return null;
		}
	}
	
	public static Map<String,String> converMapObjectToMapString(Map<String,Object> mapFrom){		
		Map<String,String> mapTo = new HashMap<String, String>();
		for (Entry<String,Object> entry : mapFrom.entrySet()) {
			if(null != entry.getValue()){	
				System.out.println(entry.getKey());
				if(EmailSender.EMAIL_CONTENT.equals(entry.getKey())){
					String s = new String((byte[])entry.getValue());
					mapTo.put(entry.getKey(),s);
				}else{
					mapTo.put(entry.getKey(), entry.getValue().toString());
				}
			}
		}
		return mapTo;
	} 
	
	
}
