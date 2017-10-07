package com.bma.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

public class SocialMediaOneAllUtil {
	
	// Your Site Settings
	public static String site_subdomain = "firstoneall";
	public static String site_public_key = "4bc2d686-d332-4339-94e6-98d4bc9dd84a";
	public static String site_private_key = "9232f88d-cf54-4319-9837-d8f0f678c8c1";
	
	public static String readUserDetails(String userToken,String format){
		
		// API Access Domain
		String site_domain = site_subdomain + ".api.oneall.com";
		 
		// Connection Resource
		String resource_uri = "https://" + site_domain + "/users/"+userToken+"."+format;
		 
		  // Result Container
		String result_json = "";
		String result = null;
		
		try
		{
		  // Forge authentication string username:password
		  String site_authentication = site_public_key + ":" + site_private_key;
		  String encoded_site_authentication = new String(new Base64().encode(site_authentication.getBytes())).replaceAll("[\n\r]", "");
		        
		  // Setup connection
		  URL url = new URL (resource_uri);
		  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		   
		  // Connect using basic auth
		  connection.setRequestMethod("GET");   
		  connection.setRequestProperty("Authorization", "Basic " +  encoded_site_authentication);
		  connection.setDoOutput(true);
		  connection.setReadTimeout(10000);
		  connection.connect();
		  connection.getInputStream();
		   
		  StringBuilder sb = new StringBuilder();
		  String line = null;
		 
		  // Read result
		  BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		  while ((line = rd.readLine()) != null) {
		    sb.append(line);
		  }
		  result = sb.toString();
		 
		} 
		catch (Exception e)
		{
		  e.printStackTrace();
		}
		 
		// Done
		System.out.println (result);
		return result;
	
	}
	

}