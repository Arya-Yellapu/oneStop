package com.onestop.restaurantSide.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ReservationJDBCRowMapper implements RowMapper<ReservationJDBC>{

	@Override
	public ReservationJDBC mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReservationJDBC res = new ReservationJDBC();
		res.setId(rs.getInt("id"));
		res.setMailid(rs.getString("mailid"));
		res.setReservationid(rs.getString("reservationid"));
		res.setSlotid(rs.getInt("slotid"));
		res.setSlotsstatus(rs.getString("slotsstatus"));
		return res;
	}

}
