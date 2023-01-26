package com.onestop.restaurantSide.dao;

public class ReservationJDBC {

	private int id;
	private String reservationid;
	private String slotsstatus;
	private String mailid;
	private int slotid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReservationid() {
		return reservationid;
	}
	public void setReservationid(String reservationid) {
		this.reservationid = reservationid;
	}
	public String getSlotsstatus() {
		return slotsstatus;
	}
	public void setSlotsstatus(String slotsstatus) {
		this.slotsstatus = slotsstatus;
	}
	public String getMailid() {
		return mailid;
	}
	public void setMailid(String mailid) {
		this.mailid = mailid;
	}
	public int getSlotid() {
		return slotid;
	}
	public void setSlotid(int slotid) {
		this.slotid = slotid;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getMailid()+this.getId()+this.getReservationid()+this.getSlotid()+this.getSlotsstatus();
	}
	
	
	
}
