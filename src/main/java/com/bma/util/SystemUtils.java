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
	
	
}
