package com.test.OneStop.Dao;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="res_id")
	private List<Slots> slotsList;

	public List<Slots> getSlotsList() {
		return slotsList;
	}

	public void setSlotsList(List<Slots> slotsList) {
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

