package com.bma.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

//import com.planfirma.continental.resources.QuotationResource;

public class Sender {
	
	// Username that is to be used for submission
	String username;
	// password that is to be used along with username
	String password;
	// Message content that is to be transmitted
	String message;
	/**
	* What type of the message that is to be sent
	* <ul>
	* <li>0:means plain text</li>
	* <li>1:means flash</li>
	* <li>2:means Unicode (Message content should be in Hex)</li>
	* <li>6:means Unicode Flash (Message content should be in Hex)</li>
	* </ul>
	*/
	String type;
	/**
	* Require DLR or not
	* <ul>
	* <li>0:means DLR is not Required</li>
	* <li>1:means DLR is Required</li>
	* </ul>
	*/
	String dlr;
	/**
	* Destinations to which message is to be sent For submitting more than one
	* destination at once destinations should be comma separated Like
	* 91999000123,91999000124
	*/
	String destination;
	// Sender Id to be used for submitting the message
	String source;
	// To what server you need to connect to for submission
	String server;
	// Port that is to be used like 8080 or 8000
	int port;
	public Sender(String server, int port, String username, String password,String message, String dlr, String type, String destination,String source) {
	System.out.println("Getting data");
	this.username = username;
	this.password = password;
	this.message = message;
	this.dlr = "1";
	this.type = type;
	this.destination = destination;
	this.source = source;
	this.server = server;
	this.port = port;
	}
	

	
	public void sendMessage() throws IOException{
		URL sendUrl = new URL("http://"+this.server);
		
		HttpURLConnection httpConnection = (HttpURLConnection) sendUrl
		.openConnection();
	}
	
	public void submitMessage() throws IOException {
	
		
		final String USER_AGENT = "Chrome/41.0.2227.1";
		String url = "http://"+this.server+"/smpp/sendsms?username="+this.username+"&password="+this.password+"&to="+this.destination+"&from="+this.source+"&text=";
		String message= this.message;
		String encodedURL=java.net.URLEncoder.encode(message,"UTF-8");
		String entireURL = url+encodedURL;
		System.out.println("\n Entire URL :  "+entireURL);
		URL obj = new URL(url+encodedURL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("\nSending 'GET' request to URL : " + encodedURL);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
		

		
 
}
	public static void main(String[] args) {
	try {
	// Below exmaple is for sending Plain text
	/*Sender s = new Sender("server", 8080, "xxxx",
	"xxxx", "test for unicode", "1", "0", "440000xxx",
	"Update");
	s.submitMessage();
	// Below exmaple is for sending unicode
	Sender s1 = new Sender("server", 8080, "xxxx",	"xxx", convertToUnicode("test for unicode").toString(),	"1", "2", "44000xx", "Update");
	s1.submitMessage();*/
	} catch (Exception ex) {
	}
	}
	/**
	* Below method converts the unicode to hex value
	* @param regText
	* @return
	*/
	public static StringBuffer convertToUnicode(String regText) {
	char[] chars = regText.toCharArray();
	StringBuffer hexString = new StringBuffer();
	for (int i = 0; i < chars.length; i++) {
	String iniHexString = Integer.toHexString((int) chars[i]);
	if (iniHexString.length() == 1)
	iniHexString = "000" + iniHexString;
	else if (iniHexString.length() == 2)
	iniHexString = "00" + iniHexString;
	else if (iniHexString.length() == 3)
	iniHexString = "0" + iniHexString;
	hexString.append(iniHexString);
	}
	System.out.println(hexString);
	return hexString;
	}
}	