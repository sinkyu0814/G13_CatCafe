package model.service;

import java.util.List;

import database.MenuDAO;
import model.dto.MenuDTO;

public class MenuService {

	private final MenuDAO dao = new MenuDAO();

	public List<MenuDTO> getMenuList(String category) {
		if ("all".equals(category)) {
			return dao.findAll();
		}
		return dao.findByCategory(category);
	}

	public List<String> getCategoryList() {
		return dao.getCategoryList();
	}

	public MenuDTO getMenuById(int id) {
		return dao.findById(id);
	}

	// 管理画面
	public List<MenuDTO> getAllMenus() {
		return dao.findAll();
	}

	public void updateVisible(int menuId, int visible) throws Exception {
		dao.updateVisible(menuId, visible);
	}

	public void deleteMenu(int menuId) throws Exception {
		dao.deleteMenu(menuId); // ★ 修正
	}
}
