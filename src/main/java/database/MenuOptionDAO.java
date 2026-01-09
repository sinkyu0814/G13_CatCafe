package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.dto.MenuOptionDTO;

public class MenuOptionDAO {

	public List<MenuOptionDTO> findByMenuId(int menuId) throws Exception {
		List<MenuOptionDTO> list = new ArrayList<>();
		String sql = """
				    SELECT option_id, menu_id, option_name, option_price
				    FROM menu_options
				    WHERE menu_id = ?
				      AND is_deleted = 0
				    ORDER BY option_id
				""";

		try (Connection con = DBManager.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, menuId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				MenuOptionDTO dto = new MenuOptionDTO();
				dto.setOptionId(rs.getInt("option_id"));
				dto.setMenuId(rs.getInt("menu_id"));
				dto.setOptionName(rs.getString("option_name"));
				dto.setOptionPrice(rs.getInt("option_price"));
				list.add(dto);
			}
		}
		return list;
	}
	

	public List<MenuOptionDTO> findByIds(String[] ids) throws Exception {
		List<MenuOptionDTO> list = new ArrayList<>();
		if (ids == null || ids.length == 0)
			return list;

		String sql = """
				    SELECT option_id, option_name, option_price
				    FROM menu_options
				    WHERE option_id = ?
				""";

		try (Connection con = DBManager.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			for (String id : ids) {
				ps.setInt(1, Integer.parseInt(id));
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					MenuOptionDTO dto = new MenuOptionDTO();
					dto.setOptionId(rs.getInt("option_id"));
					dto.setOptionName(rs.getString("option_name"));
					dto.setOptionPrice(rs.getInt("option_price"));
					list.add(dto);
				}
			}
		}
		return list;
	}

	public void insert(int menuId, String name, int price) throws Exception {
		String sql = """
				    INSERT INTO menu_options (menu_id, option_name, option_price)
				    VALUES (?, ?, ?)
				""";

		try (Connection con = DBManager.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, menuId);
			ps.setString(2, name);
			ps.setInt(3, price);
			ps.executeUpdate();
		}
	}

	public void logicalDelete(int optionId) throws Exception {
		String sql = "UPDATE menu_options SET is_deleted = 1 WHERE option_id = ?";

		try (Connection con = DBManager.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, optionId);
			ps.executeUpdate();
		}
	}

	public void delete(int optionId) throws Exception {
		String sql = "DELETE FROM menu_options WHERE option_id = ?";
		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, optionId);
			ps.executeUpdate();
		}
	}
}
