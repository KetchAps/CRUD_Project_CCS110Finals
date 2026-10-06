package spearsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseHandler {
	
	
	// END - LOGIN HANDLER
	
	public String[][] getEquipmentList() {
		
		
		try(Connection conn = ConnectionHandler.tryConnect();
			PreparedStatement st = conn.prepareStatement("SELECT * from equipment_items")) {
			ArrayList<String[]> list = new ArrayList<>();
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				String[] temp = new String[13];
				
				for(int i = 1; i <= 13; i++) {
				temp[i - 1] = rs.getString(i);
			
				}
				
				list.add(temp);
				
			}
			String[][] array = list.toArray(new String[0][]);
			return array;
			
		} catch (SQLException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public String[] getInventoryFilters() {
		
		try(Connection conn = ConnectionHandler.tryConnect();
			PreparedStatement cat = conn.prepareStatement("select distinct category from equipment_items");
			PreparedStatement catC = conn.prepareStatement("select count(distinct category) from equipment_items")) {
			
			String[] values;
			int categorySize = 1;
			
			ResultSet cs = catC.executeQuery();
			if(cs.next()) categorySize = cs.getInt(1);
			
			values = new String[categorySize];
			ResultSet rs = cat.executeQuery();
			
			for (int i = 0; i < values.length; i++) {
				if(rs.next()) 
				values[i] = rs.getString(1);
				
				else break;
			}
			
			cs.close();
			rs.close();
			return values;
			
		} catch(SQLException e) {
			
			e.printStackTrace();
			return null;
			
		}
		
	}
	
	public String[] fetchEquipmentDetails(int id) {
		
		try (Connection conn = ConnectionHandler.tryConnect();
			PreparedStatement st = conn.prepareStatement("select * from equipment_items WHERE equipmentID = ? LIMIT 1")) {
			
			st.setInt(1, id);
			
			ResultSet rs = st.executeQuery();
			
			ArrayList<String> list = new ArrayList<>();
			if(rs.next()) {
				
				for ( int i = 1; i <= 13; i++)
					list.add(rs.getString(i));			
			}
			
			return list.toArray(new String[0]);
			
		}
	 catch (SQLException e) {
		
		e.printStackTrace();
		return null;
	}
		
	}
}
