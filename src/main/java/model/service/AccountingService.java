package model.service;

import java.util.List;

import viewmodel.OrderItem;

public class AccountingService {
	public int calculateTotal(List<OrderItem> items) {
		return items.stream()
				.mapToInt(item -> (item.getPrice() + item.getOptionTotalPrice()) * item.getQuantity())
				.sum();
	}

	public int calculateChange(int deposit, int total) {
		return deposit - total;
	}
}