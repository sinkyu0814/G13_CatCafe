package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.dto.MenuDTO;

public class MenuDAO {

	// -----------------------------------------------------
	// 1件取得
	// -----------------------------------------------------
	public model.dto.MenuDTO findById(int id) {

		String sql = "SELECT menu_id, name, quantity, price, image, category FROM menus WHERE menu_id = ?";

		try (Connection con = DBManager.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return mapToDTO(rs);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// -----------------------------------------------------
	// 全件取得
	// -----------------------------------------------------
	public List<MenuDTO> findAll() {
		List<MenuDTO> list = new ArrayList<>();
		String sql = "SELECT menu_id, name, quantity, price, image, category FROM menus ORDER BY menu_id";

		try (Connection con = DBManager.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				list.add(mapToDTO(rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// -----------------------------------------------------
	// カテゴリ検索
	// -----------------------------------------------------
	public List<MenuDTO> findByCategory(String category) {
		List<MenuDTO> list = new ArrayList<>();
		String sql = "SELECT menu_id, name, quantity, price, image, category FROM menus WHERE category = ?";

		try (Connection con = DBManager.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, category);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapToDTO(rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// -----------------------------------------------------
	// ★カテゴリ一覧 
	// -----------------------------------------------------
	public List<String> getCategoryList() {
		List<String> list = new ArrayList<>();
		String sql = "SELECT DISTINCT category FROM menus ORDER BY category";

		try (Connection con = DBManager.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				list.add(rs.getString("category"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	// -----------------------------------------------------
	// ResultSet → DTO
	// -----------------------------------------------------
	private MenuDTO mapToDTO(ResultSet rs) throws Exception {
		return new MenuDTO(
				rs.getInt("menu_id"),
				rs.getString("name"),
				rs.getInt("quantity"),
				rs.getInt("price"),
				rs.getString("image"),
				rs.getString("category"));
	}

	public void addMenu(String name, int price, int quantity, String category, String image) throws Exception {
		String sql = "INSERT INTO menus (menu_id, name, price, quantity, category, image) " +
				"VALUES (menus_seq.nextval, ?, ?, ?, ?, ?)";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, name);
			ps.setInt(2, price);
			ps.setInt(3, quantity);
			ps.setString(4, category);
			ps.setString(5, image);

			ps.executeUpdate();
		}
	}
}