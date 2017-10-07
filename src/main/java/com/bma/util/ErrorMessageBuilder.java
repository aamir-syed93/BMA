package com.remedy.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.Date;

public class ErrorMessageBuilder {
	
	public static String buildJsonMessage(final Exception exception) {
		final ServerException serverException = new ServerException(Calendar.getInstance().getTime(), exception.getClass().getName(), exception.getMessage());
		
		return serverException.toString();
	}
	
	@SuppressWarnings("unused")
	private static class ServerException {

		private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
		
		protected Date time;
		protected String name;
		protected String message;

		public ServerException(final Date timeOfException, final String className, final String messageException) {
			this.time = timeOfException;
			this.name = className;
			this.message = messageException;
		}
		
		@Override public String toString() {
			return GSON.toJson(this);
		}
	}

}
