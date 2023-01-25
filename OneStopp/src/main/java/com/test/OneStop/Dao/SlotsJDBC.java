package com.test.OneStop.Dao;

import java.util.Date;

public class SlotsJDBC {

	private int res_id;
	private Date date_field;
	private String slot;
	private int id;
	private int slotsnumber;
	private int maxnumber;
	public int getRes_id() {
		return res_id;
	}
	public void setRes_id(int res_id) {
		this.res_id = res_id;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	@Override
	public String toString() {
		return this.getSlot()+this.getDate_field()+this.getSlotsnumber();
	}
	
	
}
