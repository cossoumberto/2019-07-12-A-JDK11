package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.CoppiaFood;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	
	public void listAllFoods(Map<Integer, Food> idMap){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				try {
					idMap.put(res.getInt("food_code"), new Food(res.getInt("food_code"), res.getString("display_name")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Food> listFoodPortion(int portion, Map<Integer, Food> idMap){
		String sql = "SELECT T.food_code AS FC " +
					"FROM (SELECT food_code, COUNT(portion_id) AS C " +
							"FROM porzioni " +
							"GROUP BY food_code) AS T " +
					"WHERE T.C <= ? ";
		List<Food> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, portion);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				list.add(idMap.get(res.getInt("FC")));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<CoppiaFood> listCoppieFood(int portion, Map<Integer, Food> idMap){
		String sql = "SELECT FC1.food_code AS FCOD1, FC2.food_code AS FCOD2, AVG(C.condiment_calories) AS M " +
					"FROM food_condiment AS FC1, food_condiment AS FC2, condiment AS C, (SELECT food_code, COUNT(portion_id) AS C " +
																						"FROM porzioni " +
																						"GROUP BY food_code) AS T1, " +
																						"(SELECT food_code, COUNT(portion_id) AS C " +
																						"FROM porzioni " +
																						"GROUP BY food_code) AS T2	" +
					"WHERE T1.food_code=FC1.food_code AND T2.food_code=FC2.food_code " +
					"AND T1.C <= ? AND T2.C <= ? " +
					"AND FC1.condiment_code=FC2.condiment_code AND FC1.food_code>FC2.food_code AND FC1.condiment_code=C.condiment_code " +
					"AND T1.food_code=FC1.food_code AND T2.food_code=FC2.food_code " +
					"GROUP BY FC1.food_code, FC2.food_code";
		List<CoppiaFood> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, portion);
			st.setInt(2, portion);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				CoppiaFood c = new CoppiaFood(idMap.get(res.getInt("FCOD1")), idMap.get(res.getInt("FCOD2")), res.getDouble("M"));
				list.add(c);
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
