package com.onestop.restaurantSide.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RestaurantsJDBCRowMapper implements RowMapper<RestaurantsJDBC>{

	@Override
	public RestaurantsJDBC mapRow(ResultSet rs, int rowNum) throws SQLException {
		RestaurantsJDBC res = new RestaurantsJDBC();
		res.setEmail(rs.getString("email"));
		res.setId(rs.getInt("id"));
		res.setLocation(rs.getString("location"));
		res.setRestaurantName(rs.getString("restaurant_name"));
		return res;
	}

}
