package com.bma.resources;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.bma.util.Sender;
import com.bma.util.PropertiFileConnection;
import com.bma.bean.BaseBean;
import com.bma.bean.Carrers;
import com.bma.bean.Feedback;
import com.bma.bean.Search;
import com.bma.bean.User;
import com.bma.service.ApplicationException;
import com.bma.service.MailingService;
import com.bma.service.UserAccessControlService;
import com.bma.service.UserService;
import com.bma.service.impl.UserServiceImpl;
import com.bma.util.Constants;
import com.bma.util.ErrorMessageBuilder;

@Component
@Path("/user")
public class UserResource extends AbstractResource{
	private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);
	
	static PropertiFileConnection connectMessageConfig = new PropertiFileConnection();
	public static Properties messageConfig;
	
	@Autowired private UserService userService;
	@Autowired private MailingService mailingService;
	public Sender sendSms;
	@Autowired private UserAccessControlService userAccessControlService;
	static {
		
		try {
			messageConfig = connectMessageConfig.getPropValues();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Path("/registration")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userRegistration(User user) throws IOException{
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);		
		Response response = null;
		try{				
		   	  User userdetails= userService.userRegistration(user);
		   	  
		   	  System.out.println("at time of registration "+user);
			  response = Response.ok(userdetails).build();
		}catch(ApplicationException exApplication){
			LOGGER.error(exApplication.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(ErrorMessageBuilder.buildJsonMessage(exApplication)).build();
		}
		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;
	}
	
	@Path("/search/{search}/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)	
	public Response fetchSearch(@PathParam("search") final String search,@PathParam("userId") final int userId){
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		Response response = null;
		try {
			List<Search> searchInfo= userService.fetchSearchResults(search,userId);
			 response = Response.ok(convertToJson(searchInfo)).build();
			}
			
		catch (ApplicationException e) {
			response = Response.status(Status.BAD_REQUEST).build();
		}

		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;		
	}
	
	
	@Path("/googlepluslogindetails")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response googlePlusLogin(User user){
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);		
		Response response = null;
		try{				
		   	  User userdetails= userService.googlePlusLogin(user);
			  response = Response.ok(userdetails).build();
		}catch(ApplicationException exApplication){
			LOGGER.error(exApplication.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(ErrorMessageBuilder.buildJsonMessage(exApplication)).build();
		}
		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;
	}
	
	@Path("/facebooklogindetails")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response facebookLogin(User user){
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);		
		Response response = null;
		try{				
		   	  User userdetails= userService.facebookLogin(user);
			  response = Response.ok(userdetails).build();
		}catch(ApplicationException exApplication){
			LOGGER.error(exApplication.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(ErrorMessageBuilder.buildJsonMessage(exApplication)).build();
		}
		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;
	}
	
	@Path("/socialLoginDetails")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response socialLoginDetails(User user){
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);		
		Response response = null;
		try{				
		   	  User userdetails= userService.socialLoginDetails(user);
		   	  
		   	  System.out.println("socialLoginDetails are "+userdetails);
			  response = Response.ok(userdetails.toString()).build();
		}catch(ApplicationException exApplication){
			LOGGER.error(exApplication.getMessage());
			response = Response.status(Status.CONFLICT).entity(ErrorMessageBuilder.buildJsonMessage(exApplication)).build();
		}
		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;
	}
	
	@Path("/addUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(User user){
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);		
		Response response = null;
		try{				
		   	  User userdetails= userService.addUser(user);
			  response = Response.ok(userdetails).build();
		}catch(ApplicationException exApplication){
			LOGGER.error(exApplication.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(ErrorMessageBuilder.buildJsonMessage(exApplication)).build();
		}
		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;
	}
	
	public static  User userdetails;
	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)	
	@Produces(MediaType.APPLICATION_JSON)	
	public Response loginUser(User user){
		
		System.out.println("user login object in resource "+user);
		System.out.println(user.getUseridentifier());
		System.out.println(user.getPassword());
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		Response response = null;
		try {
			
			   userdetails = userService.loginUser(user);
			     
			     System.out.println("userdetails in login resource "+userdetails);
			 
			     
			       if(userdetails!=null)
			       {
			    	   
			    	   if(!userdetails.isOtpExpiredFlag()||userdetails.isAuthenticateChangePass())
			    	   {
			    	  //to check OTP entered correctly or not
			    		   if(userdetails.isAuthenticateChangePass())
			    		   {
			    			   response = Response.ok(userdetails).build();
			    			   
			    		   }
			    		   else
			    		   {
			    			   response = Response.status(Status.UNAUTHORIZED).build();
			    		   }
			     	
			    	  }else
			    	  {
			    	  //invalid user
			    		  	response = Response.status(Status.BAD_REQUEST).build();
			    	  }	
			       }
			  
		} catch (EmptyResultDataAccessException excp) {
			LOGGER.error(excp.getMessage());
			response = Response.status(Status.NOT_FOUND).entity(ErrorMessageBuilder.buildJsonMessage(excp)).build();
		} catch (ApplicationException excp) {
			LOGGER.error(excp.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(ErrorMessageBuilder.buildJsonMessage(excp)).build();
		}
		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;		
	}
	
	@Path("/forgotpassword")
	@POST
	@Produces(MediaType.APPLICATION_JSON)	
	public Response resetForgottenPassword(User user) throws IOException{
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		Response response = null;
		
		System.out.println("in forgot password "+user);
		try {
			 userService.resetForgottenPassword(user.getMail());
			 response = Response.ok().build();
			}
			
		catch (ApplicationException e) {
			response = Response.status(Status.BAD_REQUEST).build();
		}

		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;		
	}
	
	@Path("/changepassword")
	@POST
	@Produces(MediaType.APPLICATION_JSON)	
	public Response changepassword(User user) throws IOException{
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		Response response = null;
		try {
			
			BaseBean baseBean=new BaseBean();
			
			baseBean.setActive(userService.resetPassword(user));
			 response = Response.ok(baseBean.toString()).build();
			}
			
		catch (ApplicationException e) {
			response = Response.status(Status.BAD_REQUEST).build();
		}

		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;		
	}
	
	@Path("/generatenewotp")
	@POST	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateNewOTP(User user) throws IOException{
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		Response response = null;
		System.out.println(user.toString());
		try {
			
			
			System.out.println("in generatenewotp "+user);
			final User userDetails = userService.getUserByEmail(user.getMail());			
			System.out.println("USerDatails in Generate OTP : "+userDetails);
			if(userDetails!=null)
			{
			 
				String generatedPassword = UserServiceImpl.generateRandomToken();
				
				System.out.println("generatedPassword : "+generatedPassword);
				userDetails.setOtp(generatedPassword); //set new otp for modify profile
				this.userService.generateOTP(userDetails);
				//EMAIL SENDs
				mailingService.sendModifyProfileOtp(userDetails);
				
				//SMS SEND
				System.out.println("Modify profile sms  "+userDetails.getUsername()+"  "+userDetails.getOtp());
				String modifyprofotpSMS="Dear "+userDetails.getUsername()+", "+userDetails.getOtp()+ " is the one time password (OTP) for modifying your profile, valid for 30 min. Please do not share it with anyone.";
				String mobile = userDetails.getMobNo();
				//SystemUtils.sendSms(userDetails.getMobNo(),registrationSMS);
				sendSms =  new Sender(messageConfig.getProperty("server"),Integer.parseInt(messageConfig.getProperty("port")),messageConfig.getProperty("username"),messageConfig.getProperty("password"),modifyprofotpSMS, "0", "0", mobile, messageConfig.getProperty("sender"));
				sendSms.submitMessage();
				
			}
			 
			 response = Response.ok().build();
		}catch (ApplicationException e) {
			response = Response.status(Status.BAD_REQUEST).build();
		}

		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;		
	}
	
	@Path("/regeneratenewotp")
	@POST	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response regenerateNewOTP(User user) throws IOException{
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		Response response = null;
		System.out.println(user.toString());
		try {
			
			
			System.out.println("in regeneratenewotp "+user);
			final User userDetails = userService.getUserByEmail(user.getMail());			
			
			if(userDetails!=null)
			{
			 
				String generatedPassword = UserServiceImpl.generateRandomToken();
				
				System.out.println("regeneratedPassword : "+generatedPassword);
				userDetails.setOtp(generatedPassword); //set new otp for modify profile
				this.userService.generateOTP(userDetails);
				//EMAIL SENDs
				//mailingService.sendLockerAccessOtp(userDetails);
				mailingService.sendConfirmRegistrationMail(userDetails);
				//SMS SEND
				String registrationSMS="Dear "+userDetails.getUsername()+", "+userDetails.getOtp()+ " is the one time password (OTP) for your transaction, valid for 30 min. Please do not share it with anyone.";
				String mobile = userDetails.getMobNo();
				//SystemUtils.sendSms(userDetails.getMobNo(),registrationSMS);
				sendSms =  new Sender(messageConfig.getProperty("server"),Integer.parseInt(messageConfig.getProperty("port")),messageConfig.getProperty("username"),messageConfig.getProperty("password"),registrationSMS, "0", "0", mobile, messageConfig.getProperty("sender"));
				sendSms.submitMessage();
			}
			 
			 response = Response.ok().build();
		}catch (ApplicationException e) {
			response = Response.status(Status.BAD_REQUEST).build();
		}

		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;		
	}
	
	@Path("/generatelockerotp")
	@POST	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateLockerOTP(User user) throws IOException{
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		Response response = null;
		System.out.println(user.toString());
		try {
			
			
			System.out.println("in generatenewotp "+user);
			final User userDetails = userService.getUserByEmail(user.getMail());			
			
			if(userDetails!=null)
			{
			 
				String generatedPassword = UserServiceImpl.generateRandomToken();
				
				System.out.println("generatedPassword : "+generatedPassword);
				userDetails.setOtp(generatedPassword); //set new otp for modify profile
				this.userService.generateOTP(userDetails);
				//EMAIL SENDs
				mailingService.sendLockerAccessOtp(userDetails);
			   //SEND SMS
				String mobile = userDetails.getMobNo();
				String newOtpMessage = "Dear "+userDetails.getUsername()+", "+userDetails.getOtp()+ " is the one time password (OTP) for your InstaLocker, valid for 30 min. Please do not share it with anyone.";
				Sender sendSms =  new Sender(messageConfig.getProperty("server"),Integer.parseInt(messageConfig.getProperty("port")),messageConfig.getProperty("username"),	messageConfig.getProperty("password"),newOtpMessage, "0", "0", mobile, messageConfig.getProperty("sender"));
				sendSms.submitMessage();
			}
			 
			 response = Response.ok().build();
		}catch (ApplicationException e) {
			response = Response.status(Status.BAD_REQUEST).build();
		}

		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;		
	}
	
	@Path("/adduserinfo")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUserInfo(User user){
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);		
		Response response = null;
		
		
		System.out.println("in /adduserinfo .."+user);
		try{				
			userService.addUserInfo(user);
			response = Response.status(Status.OK).build();
		}catch(ApplicationException exApplication){
			LOGGER.error(exApplication.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(ErrorMessageBuilder.buildJsonMessage(exApplication)).build();
		}

		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;
	}
	
	@Path("/fetchuserinfo/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)	
	public Response fetchUserInfo(@PathParam("userId") final int userId){
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		Response response = null;
		try {
			User userInfo= userService.fetchUserInfo(userId);
			 response = Response.ok(userInfo.toString()).build();
			}
			
		catch (ApplicationException e) {
			response = Response.status(Status.BAD_REQUEST).build();
		}

		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;		
	}
	
	@Path("/checkotp")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response checkOtp(User user){
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		
		
		Response response = null;
		try {
			
			User userDetail=this.userService.getUserByEmail(user.getUseridentifier());
			
			
			userDetail.setActive(userService.checkOtp(user));
			
			 if(userService.checkOtp(user))
			 {
				 userDetail.getRoleObj().setRoleActions(userAccessControlService.fetchActionListByRoleId(userDetail.getRoleObj().getId()).getRoleActions());
			 }
			 
				System.out.println("user object in resource check otp i s"+userDetail);
				System.out.println("check otp boolean" +userService.checkOtp(user));
				response = Response.ok(userDetail.toString()).build();
			}
			
		catch (ApplicationException e) {
			response = Response.status(Status.BAD_REQUEST).build();
		}

		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;		
	}
	
	@Path("/checkmodifyprofileotp")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response checkModifyProfiletOtp(User user){
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		
		 
   	 System.out.println("in checkmodifyprofileotp ..."+user);
		   
		Response response = null;
		try {
			 
			 User modifyProfile=userService.modifyProfileOtp(user);
		  
			 
			 
			 
			 if(modifyProfile!=null){
		    	 
		    	 	 
		    	   if(modifyProfile.isAuthenticateChangePass()){
		    	
		    		   System.out.println("yes true");
		    		   response = Response.ok(modifyProfile).build();
		    	  }else{
		    		 System.out.println("not correct");
		    		  response = Response.status(Status.UNAUTHORIZED).build();
		    	  }
		     	
		      }else{
		    	  //invalid user
			    response = Response.status(Status.BAD_REQUEST).build();
		     }	
			 
			  response = Response.ok(modifyProfile).build();
			}catch(ApplicationException excp){
				LOGGER.error(excp.getMessage());
				response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(ErrorMessageBuilder.buildJsonMessage(excp)).build();
			
			}
			LOGGER.debug(Constants.LOG_METHOD_EXIT);
			return response;	
	}
	
	
	@Path("/changemobileno")
	@POST
	@Produces(MediaType.APPLICATION_JSON)	
	public Response changeMobileNo(User user){
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		Response response = null;
		try {
			 userService.changeMobileNo(user);
			 response = Response.ok().build();
			}
			
		catch (ApplicationException e) {
			response = Response.status(Status.BAD_REQUEST).build();
		}

		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;		
	}
	

	@Path("/sendusercredentials/{email}/{username}/{mobile}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)	
	public Response sendUserCredentials(@PathParam("email")  String email,@PathParam("username")  String username,@PathParam("mobile")  String mobile) throws IOException{
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		Response response = null;
		try {
			System.out.println(email);
			userService.sendUserCredentials(email,username,mobile);
			 response = Response.ok().build();
			}
			
		catch (ApplicationException e) {
			response = Response.status(Status.BAD_REQUEST).build();
		}

		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;		
	}
	
	@Path("/userbyemailid/{email}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getUserByEmail(@PathParam("email")  String email){
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		Response response = null;
		try {
			System.out.println(email);
			User userDetails=userService.getUserByEmail(email);
			 response = Response.ok(userDetails.toString()).build();
			}
			
		catch (ApplicationException e) {
			response = Response.status(Status.BAD_REQUEST).build();
		}

		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;		
	}
	@Path("/userfeedback")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response checkOtp(Feedback feedback){
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		
		
		System.out.println("in user feedback "+feedback);
		
		Response response = null;
		try {

			userService.addUserFeedback(feedback);
			response = Response.ok().build();			
	}
		catch (ApplicationException e) {
			response = Response.status(Status.BAD_REQUEST).build();
		}

		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;		
	}
	
	@Path("/carrers/{jobTitle}/{expRequired}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response fetchCarrers(@PathParam("jobTitle") final String jobtitle,@PathParam("expRequired") Integer exprequired){
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);
		Response response = null;
		System.out.println("params : "+jobtitle+" "+exprequired);
		String jobTitle=jobtitle.equalsIgnoreCase("undefined")?null:jobtitle;
		//int exp = exprequired.equals(null)?0:exprequired;
		
		System.out.println("in resource of career");
		try {
			List<Carrers> career= userService.fetchCarreerList(jobTitle,exprequired);
			 response = Response.ok(career.toString()).build();
			 System.out.println("in resource response");
			}
			
		catch (ApplicationException e) {
			response = Response.status(Status.BAD_REQUEST).build();
		}
		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;		
	}
	
	@Path("/createUserByAdmin")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response createUserByAdmin(User user){
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);		
		Response response = null;
		try{				

			System.out.println("create user "+user);
			userService.createUserByAdmin(user);

			  response = Response.ok(userdetails).build();
		}catch(ApplicationException exApplication){
			LOGGER.error(exApplication.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(ErrorMessageBuilder.buildJsonMessage(exApplication)).build();
		}
		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;	
	}
	
	@Path("/updatelocalefile")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response updateLocaleFile(String filedata) throws IOException{
		LOGGER.debug(Constants.LOG_METHOD_ENTRY);		
		Response response = null;
		try{				

			userService.updateLocaleFile(filedata);

			  response = Response.ok().build();
		}catch(ApplicationException exApplication){
			LOGGER.error(exApplication.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(ErrorMessageBuilder.buildJsonMessage(exApplication)).build();
		}
		LOGGER.debug(Constants.LOG_METHOD_EXIT);
		return response;	
	}
	
}
