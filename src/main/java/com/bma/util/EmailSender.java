package com.bma.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;

import com.lowagie.text.DocumentException;
public class EmailSender {
		
	public static final String EMAIL_CONTENT = "EMAIL_CONTENT";
	public static final String EMAIL_SUBJECT = "EMAIL_SUBJECT";
	public static final String EMAIL_RECIPIENTS = "RECIPIENT";


	public interface MAIL_TYPE{
		public static String COMPLAIN_REGISTRATION = "COMPLAIN_REGISTRATION";
		public static String COMPLAIN_CONFIRMATION = "COMPLAIN_CONFIRMATION";
		public static String COMPLAIN_PAYMENT_INTIMATION = "COMPLAIN_PAYMENT_INTIMATION";
		public static String COMPLAIN_PAYMENT_SUCCESS = "COMPLAIN_PAYMENT_SUCCESS";
		public static String COMPLAIN_PAYMENT_FAILUAR = "COMPLAIN_PAYMENT_FAILUAR";
		public static String COMPLAIN_PRODUCT_OPTION = "COMPLAIN_PRODUCT_OPTION";
		public static String COMPLAIN_ASK_DOCUMENT_MAIL = "COMPLAIN_ASK_DOCUMENT_MAIL";
		public static String USER_REGISTRATION = "USER_REGISTRATION";
		public static String FORGOT_PASSWORD = "FORGOT_PASSWORD";
		public static String CHANGE_PASSWORD = "CHANGE_PASSWORD";
		public static String NEW_OTP = "NEW_OTP";
		public static String USER_CREDENTIALS = "USER_CREDENTIALS";
		public static String MODIFY_PROFILE="MODIFY_PROFILE";
		public static String UPDATE_PROFILE="UPDATE_PROFILE";
		public static String LOCKER_ACCESS="LOCKER_ACCESS";
		public static String UPLOAD_EDOCUMENT="UPLOAD_EDOCUMENT";
		public static String UPDATE_USER_COMPLAIN_BYADMIN="UPDATE_USER_COMPLAIN_BYADMIN";
		public static String UPDATE_EDOC="UPDATE_EDOC";
		public static String REJECT_BY_ADMIN="REJECT_BY_ADMIN";
	}
		
	private static Email setupEmailSettings() throws EmailException{

		Properties mailConfigProp = Util.getProperties();

		final Email mail = new MultiPartEmail();
		mail.setHostName(mailConfigProp.getProperty("hostname"));
		mail.setSmtpPort(Integer.parseInt(mailConfigProp.getProperty("smtpport")));
		mail.setSSLOnConnect(true);
		mail.setAuthenticator(new DefaultAuthenticator(mailConfigProp.getProperty("authentication.email"), mailConfigProp.getProperty("authentication.password")));
		mail.setFrom(mailConfigProp.getProperty("form"));

		return mail;
	}

	
	public static Email createEMail(Map<String,String> parameters) throws EmailException{		
		final Email mail = setupEmailSettings();		
		
		mail.setSubject(parameters.get(EMAIL_SUBJECT).toString());					
		//mail.setMsg(parameters.get(EMAIL_CONTENT).toString());				
		mail.setContent(parameters.get(EMAIL_CONTENT).toString(),"text/html");
		return mail;		
	}	
	
	public static Email sendEmail(Map<String,String> parameters, String recipients) throws EmailException, FileNotFoundException, DocumentException{			
		final Email mail = createEMail(parameters);		

		mail.addTo(recipients);
		//mail.addTo(recipients);

		return mail;
	}
	
	public static Email sendEmail(Map<String,String> parameters,List<String> recipients) throws EmailException{
		final Email mail = createEMail(parameters);
		
		for (final String recipient : recipients) {
			mail.addTo(recipient);
		}
		
		return mail;
	}
    public static HtmlEmail createhTMLEMail(Map<String,String> parameters) throws EmailException{		
		final HtmlEmail mail = setupHTMLEmailSettings();		
		
		mail.setSubject(parameters.get(EMAIL_SUBJECT).toString());					
		mail.setHtmlMsg(parameters.get(EMAIL_CONTENT).toString());				
		
		return mail;		
	}	
	
    private static HtmlEmail setupHTMLEmailSettings() throws EmailException {

		Properties mailConfigProp = Util.getProperties();

		final HtmlEmail mail = new HtmlEmail();
		mail.setHostName(mailConfigProp.getProperty("hostname"));
		mail.setSmtpPort(Integer.parseInt(mailConfigProp.getProperty("smtpport")));
		mail.setSSLOnConnect(true);
		mail.setAuthenticator(new DefaultAuthenticator(mailConfigProp.getProperty("authentication.email"), mailConfigProp.getProperty("authentication.password")));
		mail.setFrom(mailConfigProp.getProperty("form"));

		return mail;
	}


	public static HtmlEmail sendHTMLEmail(Map<String,String> parameters, String recipients) throws EmailException{			
		final HtmlEmail mail = createhTMLEMail(parameters);		
		
	//	mail.addTo("mahadev.bulusu@planfirma.com");
		System.out.println("email"+recipients);
		mail.addTo(recipients);
		
		return mail;
	}
	
	public static HtmlEmail sendHTMLEmail(Map<String,String> parameters,List<String> recipients) throws EmailException{
		final HtmlEmail mail = createhTMLEMail(parameters);
		
		for (final String recipient : recipients) {
			mail.addTo(recipient);
		}
		return mail;
	}
	
}

