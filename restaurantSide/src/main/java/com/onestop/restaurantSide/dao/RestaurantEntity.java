package com.onestop.restaurantSide.dao;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="restaurants")
public class RestaurantEntity {

	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="restaurant_name")
	private String restaurantName;
	
	@Column(name="location")
	private String location;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="email")
	private LoginEntity loginEntity;
	
	public LoginEntity getLoginEntity() {
		return loginEntity;
	}

	public void setLoginEntity(LoginEntity loginEntity) {
		this.loginEntity = loginEntity;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="res_id")
	private List<SlotsEntity> slotsList;

	public List<SlotsEntity> getSlotsList() {
		return slotsList;
	}

	public void setSlotsList(List<SlotsEntity> slotsList) {
		this.slotsList = slotsList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
}


