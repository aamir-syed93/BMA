package com.bma.util;

	import java.io.File;
	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.io.InputStream;
	import java.io.OutputStream;

	import javax.servlet.ServletContext;

	import org.codehaus.jettison.json.JSONException;
	import org.codehaus.jettison.json.JSONObject;

	public class DataHelperUtils {
		
		public static void writeToFile(InputStream uploadedInputStream,String uploadedFileLocation,ServletContext context,String fileName) {	
				String realPath = context.getRealPath("/");
				try {
					OutputStream out =null;
					int read = 0;
					byte[] bytes = new byte[1024];
					File file = new File(realPath+uploadedFileLocation,fileName);
					File dir = new File(realPath+uploadedFileLocation);
					out = new FileOutputStream(file);
					while ((read = uploadedInputStream.read(bytes)) != -1) {
						out.write(bytes, 0, read);
					}
					out.flush();
					out.close();
				} catch (IOException e) {	 
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}
		 
			}
		
		public static String getStringValueFromJSON(JSONObject json,String parameter) {
			try {
				return json.getString(parameter);
			} catch (JSONException e) {			
				e.printStackTrace();
			}
			return null;
		}
		
		public static void test(File file) {	 
			
				File f = new File(file.getPath());
			System.out.println("FFFFFFFFFFFFFFFFF : "+f.getName());
	 
		}
		

	}

	
