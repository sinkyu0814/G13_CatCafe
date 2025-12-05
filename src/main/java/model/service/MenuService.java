package model.service;

import java.util.List;

import database.MenuDAO;
import model.dto.MenuDTO;

public class MenuService {
	private final MenuDAO dao = new MenuDAO();

	// 商品情報取得
	public List<MenuDTO> getMenuList(String category) {
		if (category.equals("all")) {
			return dao.findAll();
		} else {
			return dao.findByCategory(category);
		}
	}

	// カテゴリ一覧の取得
	public List<String> getCategoryList() {
		return dao.getCategoryList();
	}

    public MenuDTO getMenuById(int id) {
        return dao.findById(id);
    }
}
