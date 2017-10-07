package com.bma.resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public abstract class AbstractResource {
	

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public String convertToJson(Object obj){
		return GSON.toJson(obj);
	}

}
