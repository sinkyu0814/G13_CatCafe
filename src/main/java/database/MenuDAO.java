package database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.dto.MenuDTO;
import model.dto.MenuOptionDTO;

public class MenuDAO {

	// IDで検索
	public MenuDTO findById(int id) {
		MenuDTO dto = null;
		String sql = "SELECT m.menu_id, m.name, m.quantity, m.price, m.image, m.category, m.is_visible, "
				+ "mo.option_id, mo.option_name, mo.option_price "
				+ "FROM menus m LEFT JOIN menu_options mo ON m.menu_id = mo.menu_id "
				+ "WHERE m.menu_id = ?";
		try (Connection con = DBManager.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					if (dto == null) {
						dto = mapToDTO(rs);
						dto.setOptions(new ArrayList<>());
					}
					int optId = rs.getInt("option_id");
					if (optId != 0) {
						MenuOptionDTO opt = new MenuOptionDTO();
						opt.setOptionId(optId);
						opt.setOptionName(rs.getString("option_name"));
						opt.setOptionPrice(rs.getInt("option_price"));
						dto.getOptions().add(opt);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	// 全件取得
	public List<MenuDTO> findAll() {
		Map<Integer, MenuDTO> map = new LinkedHashMap<>();
		String sql = "SELECT m.menu_id, m.name, m.quantity, m.price, m.image, m.category, m.is_visible, "
				+ "mo.option_id, mo.option_name, mo.option_price "
				+ "FROM menus m LEFT JOIN menu_options mo ON m.menu_id = mo.menu_id ORDER BY m.menu_id";
		try (Connection con = DBManager.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				int menuId = rs.getInt("menu_id");
				MenuDTO dto = map.get(menuId);
				if (dto == null) {
					dto = mapToDTO(rs);
					dto.setOptions(new ArrayList<>());
					map.put(menuId, dto);
				}
				int optId = rs.getInt("option_id");
				if (optId != 0) {
					MenuOptionDTO opt = new MenuOptionDTO();
					opt.setOptionId(optId);
					opt.setOptionName(rs.getString("option_name"));
					opt.setOptionPrice(rs.getInt("option_price"));
					dto.getOptions().add(opt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>(map.values());
	}

	// カテゴリ検索
	public List<MenuDTO> findByCategory(String category) {
		Map<Integer, MenuDTO> map = new LinkedHashMap<>();
		String sql = "SELECT m.menu_id, m.name, m.quantity, m.price, m.image, m.category, m.is_visible, "
				+ "mo.option_id, mo.option_name, mo.option_price "
				+ "FROM menus m LEFT JOIN menu_options mo ON m.menu_id = mo.menu_id "
				+ "WHERE m.category = ? AND m.is_visible = 1 ORDER BY m.menu_id";
		try (Connection con = DBManager.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, category);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int menuId = rs.getInt("menu_id");
					MenuDTO dto = map.get(menuId);
					if (dto == null) {
						dto = mapToDTO(rs);
						dto.setOptions(new ArrayList<>());
						map.put(menuId, dto);
					}
					int optId = rs.getInt("option_id");
					if (optId != 0) {
						MenuOptionDTO opt = new MenuOptionDTO();
						opt.setOptionId(optId);
						opt.setOptionName(rs.getString("option_name"));
						opt.setOptionPrice(rs.getInt("option_price"));
						dto.getOptions().add(opt);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>(map.values());
	}

	// ★ 追加：カテゴリ一覧を取得する
	public List<String> getCategoryList() {
		List<String> list = new ArrayList<>();
		String sql = "SELECT DISTINCT category FROM menus WHERE is_visible = 1 ORDER BY category";
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
				null,
				rs.getString("category"));
		dto.setIsVisible(rs.getInt("is_visible"));
		return dto;
	}

	public void addMenu(String name, int price, int quantity, String category, InputStream imageStream)
			throws Exception {
		String sql = "INSERT INTO menus (menu_id, name, price, quantity, category, image, is_visible) VALUES (menus_seq.nextval, ?, ?, ?, ?, ?, 1)";
		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, name);
			ps.setInt(2, price);
			ps.setInt(3, quantity);
			ps.setString(4, category);
			ps.setBinaryStream(5, imageStream);
			ps.executeUpdate();
		}
	}

	public void updateVisible(int menuId, int visible) throws Exception {
		String sql = "UPDATE menus SET is_visible = ? WHERE menu_id = ?";
		try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, visible);
			ps.setInt(2, menuId);
			ps.executeUpdate();
		}
	}

	public void deleteMenu(int menuId) throws Exception {
		String sqlOpt = "DELETE FROM menu_options WHERE menu_id = ?";
		String sqlMenu = "DELETE FROM menus WHERE menu_id = ?";
		try (Connection conn = DBManager.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement ps1 = conn.prepareStatement(sqlOpt);
					PreparedStatement ps2 = conn.prepareStatement(sqlMenu)) {
				ps1.setInt(1, menuId);
				ps1.executeUpdate();
				ps2.setInt(1, menuId);
				ps2.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
		}
	}
}