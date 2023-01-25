package com.test.OneStop.Dao;

import java.util.Date;

public class FinalSlots {

	private int restaurantId;
	private Date date_field;
	private String slot;
	private int slotId;
	private int slotsnumber;
	private int maxnumber;
	
	private String reservationId;
	private String slotsstatus;
	
	private String restaurantName;
	private String location;
	public int getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}
	public Date getDate_field() {
		return date_field;
	}
	public void setDate_field(Date date_field) {
		this.date_field = date_field;
	}
	public String getSlot() {
		return slot;
	}
	public void setSlot(String slot) {
		this.slot = slot;
	}
	public int getSlotId() {
		return slotId;
	}
	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}
	public int getSlotsnumber() {
		return slotsnumber;
	}
	public void setSlotsnumber(int slotsnumber) {
		this.slotsnumber = slotsnumber;
	}
	public int getMaxnumber() {
		return maxnumber;
	}
	public void setMaxnumber(int maxnumber) {
		this.maxnumber = maxnumber;
	}
	public String getReservationId() {
		return reservationId;
	}
	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}
	public String getSlotsstatus() {
		return slotsstatus;
	}
	public void setSlotsstatus(String slotsstatus) {
		this.slotsstatus = slotsstatus;
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
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getReservationId();
	}
	
	
	
}
