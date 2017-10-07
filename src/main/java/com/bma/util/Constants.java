package com.bma.util;

import java.util.Properties;

public final class Constants {

	public static Properties queries;
	public static Properties contants;
	public static Properties config;
	
	
	public final static String LOG_METHOD_ENTRY = ">>>";
	public final static String LOG_METHOD_EXIT = "<<<";
	public final static String APP_DATE_FORMAT = "MM/dd/yyyy";
	
	public final static String APPLICATION = "Application";
	
	//local
	//public static final String APPLICATION_PATH = "\\wtpwebapps\\bmanew\\";

	//namdev
	//public static final String APPLICATION_PATH = "\\wtpwebapps\\bma\\";
	public static final String DOCUMENT_PATH = "\\webapps\\IRSdocuments";
	//server
	public static final String APPLICATION_PATH = "\\webapps\\bma\\";
	
	
	public interface Jasper{
		public final String JRXML_FOLDER = "\\IRSdocuments\\resources\\jasper\\";
		public final String GENERATED_PDFS_FOLDER = "generatedpdfs\\";
		public final String problem_description_statement = "bma_product_driscription.jrxml";
		
	}
	

	/*public final static String VALIDITY_OF_OTP_ms= contants.getProperty("constant.VALIDITY_OF_OTP_ms");	*/
	
	private Constants() {
	}
	public interface SMSText{
		public final String registrationSMS = "Hi $USER_NAME,Thank you for registering complain with www.bma.com."
				+ " Your Complain Id is $COMPLAIN_ID: ";
		public final String edocumentUploadSMS = "Hi $USER_NAME,You have succesfully uploaded $NUMBER_EDOC_UPLOADED in the current session.";
		
	}
	public interface complainStatus{
		public final int OPEN = 1;
		public final int ALLOCATED = 2;
		public final int PDSCONFIRMATIONPENDING=3;
		public final int CONFIRMED = 4;
		public final int PDSMODIFIED=5;
		public final int PAYMENT_PENDING = 6;
		public final int WORKINPROGRESS = 7;
		public final int RESOLVED = 8;
		public final int CLOSED = 9;
		public final int REJECTED = 9;
		
	}
	
	public interface userRole{
		public final int ROLE_CUSTOMER = 2;
		public final int ROLE_RM=1;
		public final int ROLE_RP=4;
	}
	
	
	public interface applicationLanguageConstats{
		public final static String EN_US = "en-us";
		public final static String EN = "en";
	}
	
	public interface VTiger{
		public final String BASE_URL = "http://localhost:8888/";		
		public final static String LEADS_MODULE = "Leads";
		public final static String TICKETS_MODULE = "HelpDesk";
	}
	
	public interface FileUploadFolders{
		public final String COMPLAIN_ATTACHMENT = "\\complain_attachment\\";
		public final String EDOCUMENT = "\\Edocument\\";
		public final String IRSTEMP = "\\IRSTemp\\";
		public final String MAIL_DOC = "\\MailDocument\\";
	}
}
