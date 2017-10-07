package com.bma.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bma.bean.Carrers;
import com.bma.bean.Feedback;
import com.bma.bean.Search;
import com.bma.bean.User;
import com.bma.bean.UserRole;
import com.bma.dao.ComplainDao;
import com.bma.dao.DataAccessLayerException;
import com.bma.dao.UserAccessControlDao;
import com.bma.dao.UserDao;
import com.bma.service.ApplicationException;
import com.bma.service.MailingService;
import com.bma.service.UserService;
import com.bma.util.Constants;
import com.bma.util.PropertiFileConnection;
import com.bma.util.Sender;
import com.bma.util.SystemUtils;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{
	
	static PropertiFileConnection connectMessageConfig = new PropertiFileConnection();
	public static Properties messageConfig;
	private String generatedPassword;
	@Autowired  private UserDao userDao;
	@Autowired  private ComplainDao complainDao ;
	@Autowired private MailingService mailingService;
	@Autowired private UserAccessControlServiceImpl userAccessControlServiceImpl;
	@Autowired private UserAccessControlDao userAccessControlDao;
	public Sender sendSms;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	static {
		
		try {
			messageConfig = connectMessageConfig.getPropValues();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public User userRegistration(User user) throws ApplicationException, IOException {
		User userDetails=null;
		try {
			//set Role Customer
			 UserRole userRoleObj=new UserRole();
			 userRoleObj.setId(Constants.userRole.ROLE_CUSTOMER);
			 user.setUserRoleObj(userRoleObj);
			 userDetails=userDao.userRegistration(user);
			 
			if (null == userDetails) {
				throw new ApplicationException("User does not exist", null);
			}
			generatedPassword = generateRandomToken();
			System.out.println("generatedPassword : "+generatedPassword);
			userDetails.setOtp(generatedPassword);
			generateOTP(userDetails);
			
			//EMAIL SEND
			mailingService.sendConfirmRegistrationMail(userDetails);
			
			//SMS SEND
			String registrationSMS="Dear "+userDetails.getUsername()+", thank you for your choosing Insta bma, our Resolution Team shall get in touch with you soon.";
			String mobile = userDetails.getMobNo();
			//SystemUtils.sendSms(userDetails.getMobNo(),registrationSMS);
			sendSms =  new Sender(messageConfig.getProperty("server"),Integer.parseInt(messageConfig.getProperty("port")),messageConfig.getProperty("username"),messageConfig.getProperty("password"),registrationSMS, "0", "0", mobile, messageConfig.getProperty("sender"));
			sendSms.submitMessage();
		
		} catch (DataAccessLayerException exDataAccessLayer) {
			exDataAccessLayer.getCause().printStackTrace();
			throw new ApplicationException("Application error while inserting Registation Info to Database", exDataAccessLayer);
		}
		return userDetails;
	}
	

	@Override
	public User addUser(User user) throws ApplicationException {
	 try {
		 User  userDetails=userDao.userRegistration(user);
		 return userDetails;
	   } catch (DataAccessLayerException exDataAccessLayer) {
		exDataAccessLayer.getCause().printStackTrace();
		throw new ApplicationException("Application error while inserting Registation Info to Database", exDataAccessLayer);
	}
	
	}
	
	
	public static  String generateRandomToken(){
		return RandomStringUtils.random(5, true, true);
	}

	
	@Override
	public void resetForgottenPassword(String email) throws ApplicationException, IOException {	
		//for Forgot Password
		final User user = getUserByEmail(email);			
	
		if (null == user) {
			throw new ApplicationException("User does not exist", null);
		}
		
		generatedPassword = generateRandomToken();
		System.out.println("generatedPassword : "+generatedPassword);
		user.setPassword(generatedPassword);
		user.setPassReset(true);
		changePassword(user);
		
		//EMAIL SEND
		mailingService.sendForgotPasswordMail(user);
		
		//SMS SEND
		//String sendForgotPasswordSMS="Hi "+user.getUsername()+",Your password for the bma account has been changed. Your username is : "+user.getMail()+"and password is : "+user.getPassword();
		String sendForgotPasswordSMS="Dear "+user.getUsername()+", your password has been changed successfully. Please alert Instabma team if you did not initiate this.";
		String mobile = user.getMobNo();
		//SystemUtils.sendSms(user.getMobNo(),registrationSMS);
		sendSms =  new Sender(messageConfig.getProperty("server"),Integer.parseInt(messageConfig.getProperty("port")),messageConfig.getProperty("username"),messageConfig.getProperty("password"),sendForgotPasswordSMS, "0", "0", mobile, messageConfig.getProperty("sender"));
		sendSms.submitMessage();
	}


	@Override
	public void changePassword(User user) throws ApplicationException {
		try {
			userDao.changePassword(user);
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage(),e);
		}
	}


	@Override
	public User loginUser(User user) throws EmptyResultDataAccessException,ApplicationException {
		try {
			User userDetails=userDao.loginUser(user);
			
			
			System.out.println(userDetails.getRoleObj().getId());
			userDetails.getRoleObj().setRoleActions(userAccessControlServiceImpl.fetchActionListByRoleId(userDetails.getRoleObj().getId()).getRoleActions());;
			return userDetails;
	
		   }catch (EmptyResultDataAccessException excpDataAccessLayer) {	
				LOGGER.error("Incorrect username or password.", excpDataAccessLayer);
				throw new EmptyResultDataAccessException("Incorrect username or password.", 1);
			} catch (DataAccessLayerException excpDataAccessLayer) {
				LOGGER.error("Application error while login : " + excpDataAccessLayer.getMessage());
				throw new ApplicationException("Application error while login.", excpDataAccessLayer);
		}
			
	}


	@Override
	public User getUserByEmail(String email) throws ApplicationException {
		try {
			return userDao.getUserByEmail(email);
		} catch (DataAccessLayerException exDataAccessLayer) {
			exDataAccessLayer.getCause().printStackTrace();
			throw new ApplicationException("Application error while retrieving user by email", exDataAccessLayer);
		}
	}


	@Override
	public Boolean resetPassword(User user) throws ApplicationException, IOException {
		try {
				System.out.println("USER OBJECT :"+user);
			
			if(userDao.checkOldPassword(user))
			{
				
				User userDetails= userDao.resetPassword(user);
				 
				//EMAIL SEND
				mailingService.sendChangePasswordMail(userDetails);
				
				//SMS SEND
				String registrationSMS="Hi "+userDetails.getUsername()+",Your password for the bma account has been changed. Your username is : "+userDetails.getMail()+"and password is : "+userDetails.getPassword();
			//	SystemUtils.sendSms(userDetails.getMobNo(),registrationSMS);
				String mobile = user.getMobNo();
				//SystemUtils.sendSms(user.getMobNo(),registrationSMS);
				sendSms =  new Sender(messageConfig.getProperty("server"),Integer.parseInt(messageConfig.getProperty("port")),messageConfig.getProperty("username"),messageConfig.getProperty("password"),registrationSMS, "0", "0", mobile, messageConfig.getProperty("sender"));
				sendSms.submitMessage();
				System.out.println("SMS Sending Finish");
			
			return true;
			}
			else
			{
				//old pwd wrong
				System.out.println("In else of reset password false");
				return false;
			}
			
			//for Change password Password by user
	
			
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage(),e);
		}
	}


	@Override
	public void generateOTP(User user) throws ApplicationException {
		try {
			userDao.generateOTP(user);
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage(),e);
		}
	}


	@Override
	public boolean OTP_Validation(User user) throws ApplicationException {
		boolean OTP_validity;
		try {
			 OTP_validity = userDao.OTP_Validation(user);
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage(),e);
		}
		return OTP_validity;
	}


	@Override
	public void generateNewOTP(User user) throws ApplicationException {
		try {
			final User userDetails = getUserByEmail(user.getMail());			
			
			if (null == userDetails) {
				throw new ApplicationException("User does not exist", null);
			}
			generatedPassword = generateRandomToken();
			System.out.println("generatedPassword : "+generatedPassword);
			userDetails.setPassword(generatedPassword);
			userDao.generateOTP(userDetails);
			
			//EMAIL SEND
			mailingService.sendNewOtpMail(userDetails);
			
			/*//SMS SEND
			String registrationSMS="Hi "+userDetails.getUsername()+",Your new OTP for the bma account has been generated. Your username is : "+userDetails.getMail()+"and password is : "+userDetails.getPassword();
			SystemUtils.sendSms(userDetails.getMobNo(),registrationSMS);*/
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage(),e);
		}
	}


	@Override
	public void addUserInfo(User user) throws ApplicationException {
		try {
			userDao.addUserInfo(user);
			complainDao.makeAllComplainsActive(user);
			//EMAIL SEND
			mailingService.sendProfileUpdateMail(user);
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage(),e);
		}
	}


	@Override
	public Boolean checkOtp(User user) throws ApplicationException {
		boolean checkOtp;
		try {
			checkOtp = userDao.checkOtp(user);
			
			
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage(),e);
		}
		return checkOtp;
	}


	@Override
	public User fetchUserInfo(int userId) throws ApplicationException {
		try {
			return userDao.fetchUserInfo(userId);
		} catch (DataAccessLayerException exDataAccessLayer) {
			exDataAccessLayer.getCause().printStackTrace();
			throw new ApplicationException("Application error while retrieving user info", exDataAccessLayer);
		}
	}


	@Override
	public void changeMobileNo(User user) throws ApplicationException {
		try {
			userDao.changeMobileNo(user);
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage(),e);
		}
	}


	@Override
	public User findUserById(int userId) throws ApplicationException {
		try {
			return userDao.findUserById(userId);
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage(),e);
		}
	}

	@Override
	public void sendUserCredentials(String email,String username,String mobile) throws ApplicationException, IOException {
		final User user = getUserByEmail(email);			
		mailingService.sendUserCredentials(user);
		
		//SMS SEND
		String registrationSMS="Dear "+username+", we have created a new Instabma account for you. Please check your e-mail for further details.";
		String mob = mobile;
		sendSms =  new Sender(messageConfig.getProperty("server"),Integer.parseInt(messageConfig.getProperty("port")),messageConfig.getProperty("username"),messageConfig.getProperty("password"),registrationSMS, "0", "0", mob, messageConfig.getProperty("sender"));
		sendSms.submitMessage();
	}


	@Override
	public User googlePlusLogin(User user) throws ApplicationException {
		User userDetails=null; 
		try {
			
			 User userData = findUserBySocialMediaId(user.getGid());
			 
			 if (null == userData) {
				 UserRole userRoleObj=new UserRole();
				 userRoleObj.setId(Constants.userRole.ROLE_CUSTOMER);
				 user.setUserRoleObj(userRoleObj);
				 userDetails=userDao.googlePlusLogin(user);
			 }
			
		   } catch (DataAccessLayerException exDataAccessLayer) {
			exDataAccessLayer.getCause().printStackTrace();
			throw new ApplicationException("Application error while inserting Registation Info to Database", exDataAccessLayer);
		}
		  return userDetails;
	}


	@Override
	public User facebookLogin(User user) throws ApplicationException {
		User userDetails=null;
		 try {
			
			 User userData = findUserBySocialMediaId(user.getFid());
			 
				if (null == userData) {
					 UserRole userRoleObj=new UserRole();
					 userRoleObj.setId(Constants.userRole.ROLE_CUSTOMER);
					 user.setUserRoleObj(userRoleObj);
					 userDetails=userDao.googlePlusLogin(user);
				}
			
		   } catch (DataAccessLayerException exDataAccessLayer) {
			exDataAccessLayer.getCause().printStackTrace();
			throw new ApplicationException("Application error while inserting Registation Info to Database", exDataAccessLayer);
		}
		 return userDetails;
	}


	@Override
	public User findUserBySocialMediaId(String sid) throws ApplicationException {
		try {
			
			 User userData= userDao.findUserBySocialMediaId(sid);
			 return userData;
		  }catch (EmptyResultDataAccessException excpDataAccessLayer) {	
				LOGGER.error("No User Found.", excpDataAccessLayer);
				throw new EmptyResultDataAccessException("No User Found.", 1);
		 } catch (DataAccessLayerException exDataAccessLayer) {
			exDataAccessLayer.getCause().printStackTrace();
			throw new ApplicationException("Application error while fetching user info", exDataAccessLayer);
		}
	}


	@Override
	public User socialLoginDetails(User user) throws ApplicationException {
		User userDetails=null;
		 try {
			
			 userDetails = findUserBySocialMediaId(user.getSocialId());
			 
				if (null == userDetails) {
					 UserRole userRoleObj=new UserRole();
					 userRoleObj.setId(Constants.userRole.ROLE_CUSTOMER);
					 user.setUserRoleObj(userRoleObj);
					 userDetails=userDao.registerUserBySocialMedia(user);
				}
			
				userDetails.getRoleObj().setRoleActions(userAccessControlServiceImpl.fetchActionListByRoleId(userDetails.getRoleObj().getId()).getRoleActions());
				 return userDetails;
		   } catch (DataAccessLayerException exDataAccessLayer) {
			exDataAccessLayer.getCause().printStackTrace();
			throw new ApplicationException(exDataAccessLayer.getMessage(), exDataAccessLayer);
		}
		
	}


	@Override
	public void addUserFeedback(Feedback feedback) throws ApplicationException {
	
		try {
			
			userDao.addUserFeedback(feedback);
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage(),e);
		}
	}


	@Override
	public User modifyProfileOtp(User user) throws ApplicationException {
		// TODO Auto-generated method stub
		
		try {
			
			System.out.println("boolean value"+userDao.checkOtp(user));
			user.setAuthenticateChangePass(userDao.checkOtp(user));
			
			System.out.println("in impl "+user.isAuthenticateChangePass());
			return user;
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage(),e);
		
	}}


	@Override
	public List<Carrers> fetchCarreerList(String title,int experience) throws ApplicationException {
		// TODO Auto-generated method stub

		 try {
			 return userDao.fetchCarreerList(title,experience);
		   } catch (DataAccessLayerException exDataAccessLayer) {
			exDataAccessLayer.getCause().printStackTrace();
			throw new ApplicationException("Application error while fetching carrer list", exDataAccessLayer);
		}
		
	}

	@Override
	public User createUserByAdmin(User user) throws ApplicationException {
		// TODO Auto-generated method stub

		 try {
	
			User userDetails= userDao.userRegistration(user);
			
			//inser in user supervisor mapping
			
			userDetails.getUserRoleObj().setParentUserId(user.getUserRoleObj().getParentUserId());
			
			
			if(user.getUserRoleObj().getParentUserId()!=null)
			{
				userAccessControlDao.createUserMapping(userDetails);
			}
				generatedPassword = generateRandomToken();
		
			 	userDetails.setOtp(generatedPassword);
				generateOTP(userDetails);
				
				//EMAIL SEND
				mailingService.sendConfirmRegistrationMail(userDetails);
				
			 return userDetails;
		   } catch (DataAccessLayerException exDataAccessLayer) {
			exDataAccessLayer.getCause().printStackTrace();
			throw new ApplicationException("Application error while fetching carrer list", exDataAccessLayer);
		}
	}


	@Override
	public void updateLocaleFile(String filedata) throws ApplicationException,IOException {
		System.out.println(filedata);
		File f = new File("/bmaSVN/src/main/webapp/resources/i18n/locale_default.js");
		System.out.println(f.getPath());
		File f1 = new File("/bmaSVN/src/main/webapp/resources/i18n/locale_default.js");
		System.out.println(f1.getAbsolutePath());
		File f2 = new File("locale_default.js");
		System.out.println(f2.getAbsolutePath());
		
	PrintWriter writer = new PrintWriter("D:\\shital\\workspace\\bmaSVN\\src\\main\\webapp\\resources\\i18n\\locale_default.js", "UTF-8");
	writer.print(filedata);

	writer.close();
		
	}


	@Override
	public List<Search> fetchSearchResults(String search,int userid)
			throws ApplicationException {
		// TODO Auto-generated method stub

		 try {
			 return userDao.fetchSearchList(search,userid);
		   } catch (DataAccessLayerException exDataAccessLayer) {
			exDataAccessLayer.getCause().printStackTrace();
			throw new ApplicationException("Application error while fetching search list", exDataAccessLayer);
		}
	}
}
