package com.bma.util;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationLoadListener implements ApplicationListener<ContextRefreshedEvent>{
	//@Autowired private ReminderService reminderService;
	
    public void onApplicationEvent(ContextRefreshedEvent event) {/*
    	try {
   		System.out.println("Reminder content loaded.");
   		reminderService.initReminders();
		} catch (Exception e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}

    */}
}

	
	
	