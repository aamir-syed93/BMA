package com.bma.dao;

import java.util.List;

import com.bma.bean.Carrers;
import com.bma.bean.Feedback;
import com.bma.bean.Search;
import com.bma.bean.User;
import com.bma.service.ApplicationException;


public interface UserDao  {
	
	public User userRegistration(User user) throws DataAccessLayerException;
	
	public User findUserById(int userId) throws DataAccessLayerException;
	
	public User loginUser(User user) throws DataAccessLayerException;
	
	public User getUserByEmail(String email)  throws DataAccessLayerException;
	
	public void changePassword(User user)  throws DataAccessLayerException;
	
	public void changeMobileNo(User user)  throws DataAccessLayerException;
	
	public User resetPassword(User user)  throws DataAccessLayerException;
	
	public void generateOTP(User user)  throws DataAccessLayerException;
	
	public boolean OTP_Validation(User user) throws DataAccessLayerException;
	
	public void addUserInfo(User user) throws DataAccessLayerException;
	
	public User fetchUserInfo(int userId) throws DataAccessLayerException;
	
	public Boolean checkOtp(User user) throws DataAccessLayerException;
	
	public User googlePlusLogin(User user) throws DataAccessLayerException;
	
	public User facebookLogin(User user) throws DataAccessLayerException;
	
	public User findUserBySocialMediaId(String sid) throws DataAccessLayerException;
	
	public User registerUserBySocialMedia(User user) throws DataAccessLayerException;
	public void addUserFeedback(Feedback feedback) throws DataAccessLayerException;
	
	public List<Carrers> fetchCarreerList(String title,int experience) throws DataAccessLayerException;

	public boolean checkOldPassword(User user) throws DataAccessLayerException;
	
	public List<Search> fetchSearchList(String search,int userid) throws DataAccessLayerException;
}

