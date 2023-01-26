package com.onestop.restaurantSide.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class SlotsJDBCRowMapper implements RowMapper<SlotsJDBC>{

	@Override
	public SlotsJDBC mapRow(ResultSet rs, int rowNum) throws SQLException {
		SlotsJDBC slots = new SlotsJDBC();
		slots.setDate_field(rs.getDate("date_field"));
		slots.setId(rs.getInt("id"));
		slots.setMaxnumber(rs.getInt("maxnumber"));
		slots.setRes_id(rs.getInt("res_id"));
		slots.setSlot(rs.getString("slot"));
		slots.setSlotsnumber(rs.getInt("slotsnumber"));
		return slots;
	}

}
