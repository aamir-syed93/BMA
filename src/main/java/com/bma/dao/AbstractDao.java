package com.bma.dao;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.bma.util.Constants;
import com.bma.util.Util;

public class AbstractDao extends JdbcDaoSupport{
	
	public AbstractDao(){
			Constants.queries = Util.loadQueries();
			Constants.contants = Util.loadConstants();
			Constants.config = Util.loadConfiguration();
	}
}
