package com.bma.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.bma.bean.Carrers;
import com.bma.bean.Feedback;
import com.bma.bean.Role;
import com.bma.bean.Search;
import com.bma.bean.User;
import com.bma.dao.AbstractDao;
import com.bma.dao.DataAccessLayerException;
import com.bma.dao.UserDao;
import com.bma.resources.UserResource;
import com.bma.util.Constants;

public class UserDaoImpl extends AbstractDao implements UserDao  {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
	static DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
	
	@Override
	public User userRegistration(final User user) throws DataAccessLayerException {
	KeyHolder key = new GeneratedKeyHolder(); 
     final String query = Constants.queries.getProperty("dbquery.insert_user");
		try {
			getJdbcTemplate().update(new PreparedStatementCreator(){
				public PreparedStatement createPreparedStatement(Connection connection)throws SQLException{					
					PreparedStatement ps = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, user.getUsername());
					ps.setString(2, user.getPassword());
					ps.setString(3, user.getFirstName());
					ps.setString(4, user.getLastName());
					ps.setObject(5, user.getFid());
					ps.setString(6, user.getFimg());
					ps.setObject(7,user.getGid());
					ps.setString(8, user.getGimg());
					ps.setString(9,user.getMail());
					ps.setString(10,user.getMobNo());
					ps.setObject(11,user.getCreatedBy());
					
					if(user.getCityObj()!=null)
					{
						ps.setObject(12,user.getCityObj().getId());
							
					}
					else
					{
						ps.setObject(12,null);
						
					}
					return ps;
				}
			},key);
			user.setId(key.getKey().intValue());
			addUserRoleEntry(user);
			return user;
		} catch(Exception e) {
			LOGGER.error(e.getMessage());
			throw new DataAccessLayerException("Error while Inserting User", e);
		}	
		
	}

	
	
	@Override
	public boolean checkOldPassword(User user) throws DataAccessLayerException
	{
		StringBuilder query=new StringBuilder();
		
		System.out.println("user.get "+user.getPassword()+" & "+user.getId());
		query.append("SELECT COUNT(1) FROM `rd_usr` WHERE USR_PWD=? AND ID=? ");
		try
		{
			Integer checkoldpwd = getJdbcTemplate().queryForInt(query.toString(), new Object[] {user.getPassword(),user.getId()});	
	    	
	    	
	    		if(checkoldpwd==1){
					
					  return true;
					}
					else{
						return false;
					}
			
			
		} catch (Exception excp) {						
		throw new DataAccessLayerException("Error updating check password ",excp);
		}
	}
	
	@Override
	public void changePassword(final User user) throws DataAccessLayerException {
		
		System.out.println("in change password : "+user.getPassword()+"  "+user.getId()+"  "+user.isPassReset());
	
		final String query = Constants.queries.getProperty("dbquery.update_user_password");
	
		try {
			getJdbcTemplate().update(new PreparedStatementCreator(){
				public PreparedStatement createPreparedStatement(
						Connection connection)throws SQLException{
					PreparedStatement ps = connection.prepareStatement(query);
				/*	String hashPass =Util.hashPassword(user.getPassword());*/
					ps.setString(1,user.getPassword());
					ps.setBoolean(2,user.isPassReset());
					ps.setObject(3,user.getId());
					return ps;
				}
			});
		} catch (Exception excp) {						
			throw new DataAccessLayerException("Error updating user password ",excp);
		}
	}

	public boolean OTP_Validation(User user) throws DataAccessLayerException {
		 boolean OTP_Flag = Constants.queries.getProperty("dbquery.OTP_validation") != null;
		 return OTP_Flag;
	}
	
	@Override
	public User loginUser(User user) throws DataAccessLayerException {
		User userDetails = null;
			
		//final String query = Constants.queries.getProperty("dbquery.login_user");	
		final String query = Constants.queries.getProperty("dbquery.login_user_with_role");	
		
	
			System.out.println("query for login user is "+query);
		
		try {
			userDetails = getJdbcTemplate().queryForObject(query.toString(), new Object[]{user.getUseridentifier(),user.getUseridentifier(),user.getPassword()}, new RowMapper<User>(){
				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User user = new User();
					user.setId(rs.getInt("ID"));
					user.setUsername(rs.getString("USR_NM"));
					user.setPassword(rs.getString("USR_PWD"));
					user.setFirstName(rs.getString("F_NM"));
					user.setLastName(rs.getString("L_NM"));
					user.setOtp(rs.getString("OTP"));
					user.setMail(rs.getString("EML"));
					user.setMobNo(rs.getString("MOB_NO"));
					user.setActive(rs.getBoolean("ACTIVE"));
					user.setAuthenticateChangePass(rs.getBoolean("AUTH_PASS_FLAG"));
					user.setOtpExpiredFlag(rs.getBoolean("otp_expired"));
                    user.setAadharNo(rs.getString("AADHAR_NO"));
                    user.setPanNo(rs.getString("PAN_NO"));
                    //user.setDob(rs.getDate("DOB"));
                   
                    if(rs.getDate("DOB")!=null)
                    {
                    	user.setDobString(dateFormat.format(rs.getDate("DOB")));
                        	
                    }
                    
                    Role roleObj=new Role();
                    roleObj.setId(rs.getInt("role_id"));
                    roleObj.setRoleName(rs.getString("ROLE_NAME"));
                    user.setRoleObj(roleObj);
                    user.setPassReset(rs.getBoolean("PASS_RESET"));
					return user;
				}});			
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			LOGGER.error("No such user exists with username : " + user.getUseridentifier(), emptyResultDataAccessException);
			throw new DataAccessLayerException("No user exists with username : " + user.getUseridentifier(), emptyResultDataAccessException);
		} catch (Exception excp) {	
			LOGGER.error("Error fetching user data with username : " + user.getUseridentifier(), excp);			
			throw new DataAccessLayerException("Error fetching user data with username : " + user.getUseridentifier(), excp);
		}
		System.out.println("userDetails are"+userDetails);
		return userDetails;
	}

	@Override
	public User getUserByEmail(String identifier) throws DataAccessLayerException {
		User user = null;
		//final String query = Constants.queries.getProperty("dbquery.fetch_user_by_mail");
		
		
		final StringBuilder query=new StringBuilder();
		query.append("SELECT ru.*,rur.*,rrm.id AS role_id,rrm.role_name FROM `rd_usr_rls` rur  ");
		query.append(" LEFT JOIN `rd_usr` ru ON ru.id=rur.usr_id ");
		query.append(" LEFT JOIN `rd_role_mstr` rrm ON rrm.id=rur.role_id ");
		query.append(" WHERE eml=? OR USR_NM=?");
		
		
		try{
			user = getJdbcTemplate().queryForObject(query.toString(), 
					new Object[] {identifier,identifier} , new RowMapper<User>(){				
				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User userObj = new User();
					userObj.setId(rs.getInt("ID"));
					userObj.setUsername(rs.getString("USR_NM"));
					userObj.setMail(rs.getString("EML"));
					userObj.setPassword(rs.getString("USR_PWD"));
					userObj.setMobNo(rs.getString("MOB_NO"));
					userObj.setActive(rs.getBoolean("ACTIVE"));
					userObj.setAuthenticateChangePass(rs.getBoolean("AUTH_PASS_FLAG"));
					
					
					Role roleObj=new Role();
					roleObj.setId(rs.getInt("role_id"));
					roleObj.setRoleName(rs.getString("rrm.role_name"));
					
					userObj.setRoleObj(roleObj);
					return userObj;
				}
			});		
		}catch(Exception e) {
			LOGGER.error(e.getMessage());
			throw new DataAccessLayerException("Error getting user with email ", e);
		}
		return user;
	}


	@Override
	public User resetPassword(final User user) throws DataAccessLayerException {
		//update password with user provided password : set auth_flag=true
		System.out.println("In reset Password "+user.getNewPassword()+"  "+user.getPassword());
		final String query = Constants.queries.getProperty("dbquery.reset_password");	
		
		try{
			getJdbcTemplate().update(new PreparedStatementCreator(){
				public PreparedStatement createPreparedStatement(
						Connection connection)throws SQLException{
					PreparedStatement ps = connection.prepareStatement(query.toString());
					ps.setString(1, user.getNewPassword());	
					ps.setBoolean(2, user.isPassReset());
					ps.setObject(3, user.getId());
					ps.setString(4, user.getPassword());
					
					return ps;
				}
			});
		}catch(Exception e) {
			LOGGER.error(e.getMessage());
			throw new DataAccessLayerException("Error updating User password", e);
		}
		return user;
		
	}


	@Override
	public void generateOTP(final User user) throws DataAccessLayerException {
		final String query = Constants.queries.getProperty("dbquery.generate_otp");
		
		System.out.println("user object in generate OTP is "+user);
		System.out.println("**"+user.getOtp()+"   \t "+user.getMail());
		System.out.println(query);
		
		try {
			getJdbcTemplate().update(new PreparedStatementCreator(){
				public PreparedStatement createPreparedStatement(
						Connection connection)throws SQLException{
					PreparedStatement ps = connection.prepareStatement(query);
					//ps.setString(1,user.getPassword());
					
					ps.setString(1,user.getOtp());
					
					ps.setString(2,user.getMail());
					return ps;
				}
			});
		} catch (Exception excp) {						
			throw new DataAccessLayerException("Error in Generating OTP ",excp);
		}
	}

	@Override
	public void addUserInfo(final User user) throws DataAccessLayerException {
	 /*   final String query = Constants.queries.getProperty("dbquery.inset_user_info");*/
		final StringBuffer queryString = new StringBuffer();
		
		System.out.println("User object in daoimpl");
		System.out.println(user);
		queryString.append("INSERT INTO `rd_user_details` ");
		queryString.append("(`ID`,`PAN_NO`,`AADHAR_NO`,`DOB`,`ACTIVE`) VALUES (?,?,?,?,true) ");
		queryString.append(" ON DUPLICATE KEY  UPDATE  `PAN_NO`=?,`AADHAR_NO`=?,`DOB`=?");
		
			try {
				getJdbcTemplate().update(new PreparedStatementCreator(){
					public PreparedStatement createPreparedStatement(Connection connection)throws SQLException{					
						PreparedStatement ps = connection.prepareStatement(queryString.toString());
						ps.setObject(1, user.getId());
						ps.setString(2, user.getPanNo());
						ps.setString(3,user.getAadharNo());
						try {
							ps.setDate(4, new Date((dateFormat.parse(user.getDobString())).getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ps.setString(5, user.getPanNo());
						ps.setString(6,user.getAadharNo());
						try {
							ps.setDate(7, new Date((dateFormat.parse(user.getDobString())).getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//ps.setObject(8,user.getLastUpdatedBy());
						
			
						return ps;
					}
				});
				updatefullNameofUser(user);
			} catch(Exception e) {
				System.out.println("ERORR IN addUserInfo "+e);
				LOGGER.error(e.getMessage());
				throw new DataAccessLayerException("Error while Inserting User Info", e);
			}	
	}

	
	void updatefullNameofUser(final User user)
	{
		
		System.out.println("in update full name "+user);
	    final StringBuilder queryString = new StringBuilder();
	    queryString.append("UPDATE `rd_usr`  ");
		queryString.append("SET `F_NM`=?,`L_NM`=?,`MOB_NO`=? ");
		queryString.append("WHERE ID=?  ;");
	
		getJdbcTemplate().update(new PreparedStatementCreator(){
			public PreparedStatement createPreparedStatement(
					Connection connection)throws SQLException{
				PreparedStatement ps = connection.prepareStatement(queryString.toString());
				ps.setString(1, user.getFirstName());
				ps.setObject(2, user.getLastName());
				ps.setObject(3, user.getMobNo());
				ps.setInt(4,user.getId());
		
				return ps;
			};
		});
		
	}
	@Override
	public Boolean checkOtp(final User user) throws DataAccessLayerException {
		
	    final String query ="SELECT COUNT(1) FROM `rd_usr` WHERE `OTP`=? AND (`EML`=? OR `USR_NM`=?)";	
    	Integer checkOtp = getJdbcTemplate().queryForInt(query, new Object[] {user.getOtp(),user.getUseridentifier(),user.getUseridentifier()});	
    	
    	
    
			if(checkOtp==1){
				
				  setOtpAuthFlag(user);
				  return true;
				}
				else{
					return false;
				}
		}

	public void setOtpAuthFlag(final User user) throws DataAccessLayerException {
		final String query = "UPDATE `rd_usr` SET `AUTH_PASS_FLAG`=TRUE WHERE (`EML`=? OR `USR_NM`=?) ";
		try {
			getJdbcTemplate().update(new PreparedStatementCreator(){
				public PreparedStatement createPreparedStatement(
						Connection connection)throws SQLException{
					PreparedStatement ps = connection.prepareStatement(query);
					ps.setObject(1,user.getUseridentifier());
					ps.setObject(2,user.getUseridentifier());
					return ps;
				}
			});
		} catch (Exception excp) {						
			throw new DataAccessLayerException("Error updating Auth Flag",excp);
		}
	}

	@Override
	public User fetchUserInfo(int userId) throws DataAccessLayerException {
		User userInfo = null;
		final String query ="SELECT ru.* ,rud.`PAN_NO`,rud.`AADHAR_NO`,rud.`DOB`,rud.`ACTIVE` AS ENBL,DATE_FORMAT(rud.dob,'%m-%b-%Y') AS DOBF FROM `rd_usr` ru LEFT JOIN `rd_user_details` rud ON rud.`ID`=ru.`ID` WHERE ru.`ID`=? ";	
		try {
			userInfo = getJdbcTemplate().queryForObject(query.toString(), new Object[]{userId}, new RowMapper<User>(){
				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User user = new User();
					user.setId(rs.getInt("ID"));
					user.setUsername(rs.getString("USR_NM"));
					user.setPassword(rs.getString("USR_PWD"));
					user.setFirstName(rs.getString("F_NM"));
					user.setLastName(rs.getString("L_NM"));
					user.setOtp(rs.getString("OTP"));
					user.setMail(rs.getString("EML"));
					user.setMobNo(rs.getString("MOB_NO"));
				/*	user.setActive(rs.getBoolean("ACTIVE"));*/
					
				
						user.setAuthenticateChangePass(rs.getBoolean("AUTH_PASS_FLAG"));
	                    	
					user.setAadharNo(rs.getString("AADHAR_NO"));
                    user.setPanNo(rs.getString("PAN_NO"));
                  //  user.setDob(rs.getDate("DOB"));
                    
                   
                    user.setDobString(rs.getString("DOBF"));
                    user.setActive(rs.getBoolean("ENBL"));
					return user;
				}});			
		} catch (Exception excp) {						
			throw new DataAccessLayerException("Error updating Auth Flag",excp);
		}
		return userInfo;
	}

	@Override
	public void changeMobileNo(final User user) throws DataAccessLayerException {
		final String query = " UPDATE `rd_usr` SET `MOB_NO`=? WHERE `ID`= ? ";
		try {
			getJdbcTemplate().update(new PreparedStatementCreator(){
				public PreparedStatement createPreparedStatement(
						Connection connection)throws SQLException{
					PreparedStatement ps = connection.prepareStatement(query);
				/*	String hashPass =Util.hashPassword(user.getPassword());*/
					ps.setString(1,user.getMobNo());
					ps.setObject(2,user.getId());
					return ps;
				}
			});
		} catch (Exception excp) {						
			throw new DataAccessLayerException("Error updating user Mob No ",excp);
		}
	}

	@Override
	public User findUserById(int userId) throws DataAccessLayerException {
		User userDetails = null;
		final String query ="SELECT * FROM `rd_usr` WHERE `ID`=? ";	
		try {
			userDetails = getJdbcTemplate().queryForObject(query.toString(), new Object[]{userId}, new RowMapper<User>(){
				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User user = new User();
					user.setId(rs.getInt("ID"));
					user.setUsername(rs.getString("USR_NM"));
					user.setFirstName(rs.getString("F_NM"));
					user.setLastName(rs.getString("L_NM"));
					user.setPassword(rs.getString("USR_PWD"));
					user.setOtp(rs.getString("OTP"));
					user.setMail(rs.getString("EML"));
					user.setMobNo(rs.getString("MOB_NO"));
					user.setActive(rs.getBoolean("ACTIVE"));
					user.setAuthenticateChangePass(rs.getBoolean("AUTH_PASS_FLAG"));
					return user;
				}});			
		} catch (Exception excp) {						
			throw new DataAccessLayerException("Error find user by Id",excp);
		}
		return userDetails;
	}

	
	public User addUserRoleEntry(final User user) throws DataAccessLayerException {
		System.out.println(user.toString());
		final StringBuffer queryString = new StringBuffer();
		queryString.append("INSERT INTO `rd_usr_rls`");
		queryString.append("(`USR_ID`,`ROLE_ID`,`CREATED_BY`,`CREATED_DATE`,`ACTIVE`) VALUES (?,?,?,NOW(),true) ");
			try {
				getJdbcTemplate().update(new PreparedStatementCreator(){
					public PreparedStatement createPreparedStatement(Connection connection)throws SQLException{					
						PreparedStatement ps = connection.prepareStatement(queryString.toString());
						ps.setObject(1, user.getId());
						ps.setObject(2, user.getUserRoleObj().getId());
						ps.setObject(3,user.getCreatedBy());
						return ps;
					}
				});
				return user;
			} catch(Exception e) {
				LOGGER.error(e.getMessage());
				throw new DataAccessLayerException("Error while Inserting User Role", e);
			}		
	}

	@Override
	public User googlePlusLogin(final User user) throws DataAccessLayerException {
		KeyHolder key = new GeneratedKeyHolder(); 
	     final String query = "INSERT INTO `rd_usr` (`USR_NM`,`F_NM`,`L_NM`,`G_ID`,`G_IMG`,`CREATED_DATE`,`CREATED_BY`,`ACTIVE`) "
	     		+ "VALUES (?,?,?,?,?,NOW(),?,TRUE)";
			try {
				getJdbcTemplate().update(new PreparedStatementCreator(){
					public PreparedStatement createPreparedStatement(Connection connection)throws SQLException{					
						PreparedStatement ps = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
						ps.setString(1, user.getUsername());
						ps.setString(2, user.getFirstName());
						ps.setString(3, user.getLastName());
						ps.setObject(4,user.getGid());
						ps.setString(5, user.getGimg());
						ps.setObject(6,user.getCreatedBy());
						return ps;
					}
				},key);
				user.setId(key.getKey().intValue());
				addUserRoleEntry(user);
				return user;
			} catch(Exception e) {
				LOGGER.error(e.getMessage());
				throw new DataAccessLayerException("Error while Inserting Google User", e);
			}	
	}

	@Override
	public User facebookLogin(final User user) throws DataAccessLayerException {
		KeyHolder key = new GeneratedKeyHolder(); 
	     final String query = "INSERT INTO `rd_usr` (`USR_NM`,'EML',`F_NM`,`L_NM`,`F_ID`,`F_IMG`,`CREATED_DATE`,`CREATED_BY`,`ACTIVE`,AUTH_PASS_FLAG) "
	     		+ "VALUES (?,?,?,?,?,?,NOW(),?,TRUE,TRUE)";
			try {
				getJdbcTemplate().update(new PreparedStatementCreator(){
					public PreparedStatement createPreparedStatement(Connection connection)throws SQLException{					
						PreparedStatement ps = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
						ps.setString(1, user.getUsername());
						ps.setString(2, user.getMail());
						ps.setString(3, user.getFirstName());
						ps.setString(4, user.getLastName());
						ps.setObject(5, user.getFid());
						ps.setString(6, user.getFimg());
						ps.setObject(7, user.getCreatedBy());
						return ps;
					}
				},key);
				user.setId(key.getKey().intValue());
				addUserRoleEntry(user);
				return user;
			} catch(Exception e) {
				LOGGER.error(e.getMessage());
				throw new DataAccessLayerException("Error while Inserting Facebook User", e);
			}	
	}

	@Override
	public User findUserBySocialMediaId(String sid)
			throws DataAccessLayerException {
		User userDetails = null;
		final String query ="SELECT TIMEDIFF(NOW(),`OTP_CREATED_DATE`) > '01:00:00'  AS OTP_EXPIRED ,ru.*,rud.*,rrm.`ROLE_NAME`,rrm.`ID` AS role_id FROM `rd_usr` ru LEFT JOIN `rd_user_details` rud ON rud.`ID`=ru.`ID` LEFT JOIN `rd_usr_rls` rur ON rur.`USR_ID`=ru.`ID` LEFT JOIN `rd_role_mstr` rrm ON rrm.`ID`=rur.`ROLE_ID` WHERE `G_ID`=? OR `F_ID`=? OR `SOCIAL_ID`=?";	
		try {
			userDetails = getJdbcTemplate().queryForObject(query.toString(), new Object[]{sid,sid,sid}, new RowMapper<User>(){
				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User user = new User();
					user.setId(rs.getInt("ID"));
					user.setUsername(rs.getString("USR_NM"));
					user.setPassword(rs.getString("USR_PWD"));
					user.setOtp(rs.getString("OTP"));
					user.setMail(rs.getString("EML"));
					user.setMobNo(rs.getString("MOB_NO"));
					user.setActive(rs.getBoolean("ACTIVE"));
					user.setAuthenticateChangePass(rs.getBoolean("AUTH_PASS_FLAG"));
					user.setOtpExpiredFlag(rs.getBoolean("otp_expired"));
                    user.setAadharNo(rs.getString("PAN_NO"));
                    user.setPanNo(rs.getString("AADHAR_NO"));
                    user.setDob(rs.getDate("DOB"));
                    //user.setDobString(dateFormat.format(rs.getDate("DOB")));
                    
                    Role roleObj=new Role();
                    roleObj.setId(rs.getInt("ID"));
                    roleObj.setRoleName(rs.getString("ROLE_NAME"));
                    user.setRoleObj(roleObj);
					return user;
				}});			
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			return null;
		} catch (Exception excp) {						
			throw new DataAccessLayerException("Error find user by social media Id",excp);
		}
		return userDetails;
	}

	@Override
	public User registerUserBySocialMedia(final User user) throws DataAccessLayerException {
		KeyHolder key = new GeneratedKeyHolder(); 
		String tempQuery ="";
		tempQuery = "INSERT INTO `rd_usr` (`USR_NM`,`EML`,`F_NM`,`L_NM`,`SOCIAL_ID`,`SOCIAL_IMG`,`CREATED_DATE`,`CREATED_BY`,`ACTIVE`) VALUES (?,?,?,?,?,?,NOW(),?,TRUE)";
		
		final String query = tempQuery;
		try {
			getJdbcTemplate().update(new PreparedStatementCreator(){
				public PreparedStatement createPreparedStatement(Connection connection)throws SQLException{					
					PreparedStatement ps = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, user.getUsername());
					ps.setString(2, user.getMail());
					ps.setString(3, user.getFirstName());
					ps.setString(4, user.getLastName());
					ps.setObject(5,user.getSocialId());
					ps.setString(6, user.getSocialImg());
					ps.setObject(7,user.getCreatedBy());
					return ps;
				}
			},key);
			user.setId(key.getKey().intValue());
			addUserRoleEntry(user);
			return user;
		} catch(DuplicateKeyException e) {
			LOGGER.error(e.getMessage());
			throw new DataAccessLayerException("User with email : "+user.getMail()+" already exists", e);
		} catch(Exception e) {
			LOGGER.error(e.getMessage());
			throw new DataAccessLayerException("Error while Inserting Social User", e);
		}	
	}

	@Override
	public void addUserFeedback(final Feedback feedback)
			throws DataAccessLayerException {
		// TODO Auto-generated method stub
		
		System.out.println("feedback dao in user dao "+feedback);
		final StringBuffer queryString = new StringBuffer();
		queryString.append("INSERT INTO `rd_feedback`");
		queryString.append("(`USER_ID`,`RATE`,`EXPERIENCE`,`CREATED_DATE`,`ACTIVE`) VALUES (?,?,?,NOW(),true) ");
			try {
				getJdbcTemplate().update(new PreparedStatementCreator(){
					public PreparedStatement createPreparedStatement(Connection connection)throws SQLException{					
						PreparedStatement ps = connection.prepareStatement(queryString.toString());
						ps.setObject(1,UserResource.userdetails.getId());
						ps.setInt(2,feedback.getRate());
						ps.setString(3,feedback.getExperience());
						return ps;
					}
				});
			} catch(Exception e) {
				LOGGER.error(e.getMessage());
				throw new DataAccessLayerException("Error while Inserting User Role", e);
			}	
	}

	@Override
	public List<Carrers> fetchCarreerList(final String title,final int experience) throws DataAccessLayerException {
		// TODO Auto-generated method stub
		

		StringBuilder query=new StringBuilder();
		query.append(" SELECT * FROM `rd_jobs` WHERE active=TRUE ");
		List<Carrers> carrersList = new ArrayList<Carrers>();


		final List<Object> parameters = new ArrayList<Object>();
		
		
		
		if(title!=null)
		{
			query.append("AND JOB_TITLE LIKE ? ");
			parameters.add("%" + title + "%");
	
		}
		if(experience!=0)
		{
			query.append(" AND MIN_EXP<=? ");
			parameters.add(experience);
		}

		System.out.println("job query is "+query);
		Object[] array = parameters.toArray(new Object[parameters.size()]);
		System.out.println("paramerter size "+parameters.size());

		try {
			carrersList = getJdbcTemplate().query(query.toString(),
					array, new RowMapper<Carrers>() {

		
				@Override
						public Carrers mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							Carrers carrerobj=new Carrers();
							carrerobj.setId(rs.getInt("ID"));
							carrerobj.setJobTitle(rs.getString("JOB_TITLE"));
							carrerobj.setLocation(rs.getString("LOCATION"));
							carrerobj.setExpRequired(rs.getString("EXP_REQUIRED"));
							
							System.out.println("career lists : "+carrerobj);
							return carrerobj;					
							}
					});
		} catch (Exception excp) {
			LOGGER.error(
					"DAO - Error fetching carrer list : " + excp.getMessage(),
					excp);
			throw new DataAccessLayerException(
					"DAO - Error fetching Carrer list: " + excp.getMessage(),
					excp);
		}

		return carrersList;

	}



	@Override
	public List<Search> fetchSearchList(String search,int userid)
			throws DataAccessLayerException {
		// TODO Auto-generated method stub
		StringBuilder query=new StringBuilder();
		query.append("  SELECT * FROM rd_usr ru LEFT JOIN rd_complaint rc ON rc.USER_ID=ru.ID LEFT JOIN rd_edocument re ON re.USER_ID=ru.ID WHERE (rc.COMPLAIN_UID LIKE ? OR re.EDOC_TITLE LIKE ? OR TITLE LIKE ?) AND ru.ID=?; ");
		
		final List<Object> parameters = new ArrayList<Object>();
		parameters.add("%" + search + "%");
		parameters.add("%" + search + "%");
		parameters.add("%" + search + "%");
		parameters.add(userid);
		
		List<Search> searchList = new ArrayList<Search>();


		
		Object[] array = parameters.toArray(new Object[parameters.size()]);
		System.out.println("paramerter size "+parameters.size());
		
		try {
			searchList = getJdbcTemplate().query(query.toString(),
					array, new RowMapper<Search>() {

		
				@Override
						public Search mapRow(ResultSet rs, int rowNum)
								throws SQLException {
								Search searchobj=new Search();
								searchobj.setEdoc_id(rs.getInt("re.ID"));
								searchobj.setComplain_id(rs.getString("COMPLAIN_UID"));
								searchobj.setComplain_title(rs.getString("TITLE"));
								searchobj.setEdoc_title(rs.getString("EDOC_TITLE"));
								
							
							System.out.println("search lists : "+searchobj);
							return searchobj;					
							}
					});
		} catch (Exception excp) {
			LOGGER.error(
					"DAO - Error fetching search list : " + excp.getMessage(),
					excp);
			throw new DataAccessLayerException(
					"DAO - Error fetching search list: " + excp.getMessage(),
					excp);
		}

		return searchList;
	}
}

