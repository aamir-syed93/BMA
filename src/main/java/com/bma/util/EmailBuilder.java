package com.bma.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

public class EmailBuilder implements Runnable {

	MultiPartEmail multiPartEmail;
	
	public EmailBuilder(MultiPartEmail email) {
	    this.multiPartEmail = email;
	}
	
	@Override
	public void run() {
		try {
			multiPartEmail.send();
		} catch (EmailException e) {
			System.out.println("Mail Sending Failed "+e.getMessage());			
		}
	}
	
	
	
}
