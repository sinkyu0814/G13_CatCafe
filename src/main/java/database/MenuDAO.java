package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.dto.MenuDTO;

public class MenuDAO {

	public MenuDTO findById(int id) {
		String sql = """
					SELECT menu_id, name, quantity, price, image, category, is_visible
					FROM menus
					WHERE menu_id = ?
				""";

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

	// 管理画面用（全件）
	public List<MenuDTO> findAll() {
		List<MenuDTO> list = new ArrayList<>();
		String sql = """
					SELECT menu_id, name, quantity, price, image, category, is_visible
					FROM menus
					ORDER BY menu_id
				""";

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

	// お客様画面用（表示中のみ）
	public List<MenuDTO> findByCategory(String category) {
		List<MenuDTO> list = new ArrayList<>();
		String sql = """
					SELECT menu_id, name, quantity, price, image, category, is_visible
					FROM menus
					WHERE category = ?
					  AND is_visible = 1
				""";

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

	public List<String> getCategoryList() {
		List<String> list = new ArrayList<>();
		String sql = """
					SELECT DISTINCT category
					FROM menus
					WHERE is_visible = 1
					ORDER BY category
				""";

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

	private MenuDTO mapToDTO(ResultSet rs) throws Exception {
		MenuDTO dto = new MenuDTO(
				rs.getInt("menu_id"),
				rs.getString("name"),
				rs.getInt("quantity"),
				rs.getInt("price"),
				rs.getString("image"),
				rs.getString("category"));
		dto.setIsVisible(rs.getInt("is_visible"));
		return dto;
	}

	public void addMenu(String name, int price, int quantity, String category, String image) throws Exception {
		String sql = """
					INSERT INTO menus
					(menu_id, name, price, quantity, category, image, is_visible)
					VALUES (menus_seq.nextval, ?, ?, ?, ?, ?, 1)
				""";

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

	// ★ 論理削除
	public void updateVisible(int menuId, int visible) throws Exception {
		String sql = "UPDATE menus SET is_visible = ? WHERE menu_id = ?";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, visible);
			ps.setInt(2, menuId);
			ps.executeUpdate();
		}
	}

	// ★ 物理削除
	public void deleteMenu(int menuId) throws Exception {
		String sql = "DELETE FROM menus WHERE menu_id = ?";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, menuId);
			ps.executeUpdate();
		}
	}
}
