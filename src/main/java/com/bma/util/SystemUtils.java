package com.bma.util;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.PostMethod;

public class SystemUtils {


	public static String path =null;
	
	public static String getProjectPath(){
	  Map<String, String> env = System.getenv();
		if(path==null)
		{
			path = env.get("bma_TOMCAT");
		}
				return path;
	}
	
	/*public static void main(String args[]){
		Map<String, String> env = System.getenv();
		if(path==null)
			  path = env.get("CONTINENTAL_TOMCAT");
		System.out.println("env values"+env.get("CONTINENTAL_TOMCAT"));
	}
*/
	
	public static void sendSms(String phoneNumber,String message)
	{
		  System.out.println(phoneNumber);
		  System.out.println(message);

		 String twilioHost = "api.twilio.com";
		 final String ACCOUNT_SID = "ACf38638d15310ac74072df44b27da3515";
		 final String AUTH_TOKEN = "c148ee397607a8f8b20bea4d3bd73248";
		  
		/* final String Account_Username = "irtspl";
		 final String Account_Password = "irtspl1234";*/
		
		 org.apache.commons.httpclient.auth.AuthScope authScope = new org.apache.commons.httpclient.auth.AuthScope("api.twilio.com", 443);
		 HostConfiguration hc = new HostConfiguration();
		 hc.setHost(twilioHost, 443, "https");
		// String url = "https://api.twilio.com/2010-04-01/Accounts/ACf38638d15310ac74072df44b27da3515/Messages.json";
		 
		 String url ="http://203.212.70.200/smpp/sendsms?username=irtspl&password=irtspl1234";
		 
		 
		 HttpClient client = new HttpClient();
		 Credentials c = new UsernamePasswordCredentials(ACCOUNT_SID, AUTH_TOKEN);
		 client.getState().setCredentials(authScope, c);
		 
		 PostMethod p = new PostMethod(url);
		 p.addParameter("from","+19073121074");
		 p.addParameter("to","+91"+phoneNumber);
		 p.addParameter("text",message);
		 p.setDoAuthentication(true);
         System.out.println(p.getParameters());
   
		    try {
				client.executeMethod(hc,p);
				p.releaseConnection();
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
