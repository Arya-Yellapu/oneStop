package com.onestop.restaurantSide.dao;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="slots")
public class SlotsEntity {
	@Id
	@Column(name="id")
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

    @Column(name="date_field")
	private Date date;
	
	@Column(name="slot")
	private String slot;
	
	@Column(name="status")
	private String status;
	
	@Column(name="reservationid")
	private String reservationid;

	

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReservationid() {
		return reservationid;
	}

	public void setReservationid(String reservationid) {
		this.reservationid = reservationid;
	}
	
	/*@Column(name="res_id")
	private int restaurantId;

	public int getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}*/

	@Override
	public String toString() {
		return this.getDate()+" ==== "+this.getSlot();
	}
	
}
