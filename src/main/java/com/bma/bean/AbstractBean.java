package com.bma.bean;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractBean {
	
	protected Integer id;
	private boolean active;
	private Date createdDate;
	private Integer createdBy;
	private Date lastUpdatedDate;
	private Integer lastUpdatedBy;
	
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	@Override
	public String toString() {
		return GSON.toJson(this);
	}
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	
}
