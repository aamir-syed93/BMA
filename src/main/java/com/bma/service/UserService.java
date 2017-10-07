package com.bma.service;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

import com.bma.bean.Carrers;
import com.bma.bean.Feedback;
import com.bma.bean.Search;
import com.bma.bean.User;


public interface UserService {
	
	
	public User userRegistration(User user) throws ApplicationException , IOException;
	
	public User findUserById(int userId) throws ApplicationException;
	
	public User loginUser(User user)  throws EmptyResultDataAccessException , ApplicationException;
	
	public void resetForgottenPassword(String email)  throws ApplicationException, IOException;
	
	public void changePassword(User user)  throws ApplicationException;
	
	public void changeMobileNo(User user)  throws ApplicationException;
	
	public User  getUserByEmail(String email) throws ApplicationException;
	
	public Boolean resetPassword(User user) throws ApplicationException, IOException;
	
	public void generateOTP(User user)  throws ApplicationException;
	
	public void generateNewOTP(User user)  throws ApplicationException;
	
	public boolean OTP_Validation(User user) throws  ApplicationException;
	
	public void addUserInfo(User user) throws ApplicationException;
	
	public User fetchUserInfo(int userId) throws ApplicationException;
	
	public Boolean checkOtp(User user) throws ApplicationException;
	
	public User addUser(User user) throws ApplicationException;
	
	public void sendUserCredentials(String email,String username,String mobile) throws ApplicationException,IOException;
	
	public User googlePlusLogin(User user) throws ApplicationException;
	
	public User facebookLogin(User user) throws ApplicationException;
	
	public User socialLoginDetails(User user) throws ApplicationException;
	
	public User findUserBySocialMediaId(String sid) throws ApplicationException;
	public void addUserFeedback(Feedback feedback) throws ApplicationException;

	public User modifyProfileOtp(User user) throws ApplicationException;
	
	public List<Carrers> fetchCarreerList(String title,int experience) throws ApplicationException;
	public User createUserByAdmin(User user) throws ApplicationException;

	public void updateLocaleFile(String filedata) throws ApplicationException,IOException;
	
	public List<Search> fetchSearchResults(String search,int userid) throws ApplicationException;
	
}
