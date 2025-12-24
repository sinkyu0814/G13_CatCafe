<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>shinkyu13-check</title>
<style>
body {
	font-family: sans-serif;
	margin: 0;
	padding: 20px;
	background-color: #f8f8f8;
}

.page-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	border-bottom: 2px solid #ccc;
	margin-bottom: 20px;
}

.main-content {
	display: flex;
	gap: 30px;
}

.scrollable-area {
	flex-grow: 1;
	max-height: 500px;
	overflow-y: auto;
	background-color: #fff;
	border: 1px solid #ddd;
}

.item-list-table {
	width: 100%;
	border-collapse: collapse;
}

.item-list-table th, .item-list-table td {
	padding: 12px;
	text-align: left;
	border-bottom: 1px solid #eee;
}

.summary-container {
	width: 300px;
	display: flex;
	flex-direction: column;
	gap: 20px;
}

.total-price-box {
	padding: 20px;
	background-color: #FFFACD;
	border: 2px solid #FFD700;
	align-items: flex-end;
	display: flex;
	flex-direction: column;
}

.total-price-value {
	font-size: 36px;
	font-weight: 900;
	color: #D30000;
}

.complete-button {
	width: 100%;
	padding: 15px 0;
	background-color: #28a745;
	color: white;
	border: none;
	font-size: 20px;
	font-weight: bold;
	cursor: pointer;
}

.action-buttons {
	margin-top: auto;
	width: 100%;
}
</style>
</head>
<body>

	<header class="page-header">
		<h1>会計確認（テーブル ${tableNo}）</h1>

		<%-- HistoryServlet（orderId基準）へ戻る --%>
		<form action="HistoryServlet" method="GET" style="margin: 0;">
			<input type="hidden" name="orderId" value="${orderId}">
			<button type="submit">戻る</button>
		</form>
	</header>

	<div class="main-content">

		<div class="scrollable-area">
			<table class="item-list-table">
				<thead>
					<tr>
						<th style="width: 15%;">画像</th>
						<th style="width: 45%;">商品名</th>
						<th style="width: 20%;">値段</th>
						<th style="width: 20%;">注文数</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${items}">
						<tr>
							<td><c:if test="${not empty item.image}">
									<img src="assets/images/${item.image}" width="60" height="60"
										style="object-fit: cover;">
								</c:if> <c:if test="${empty item.image}">No Image</c:if></td>
							<td><strong>${item.name}</strong>
								<ul
									style="list-style: none; padding: 0; margin: 5px 0 0 10px; font-size: 0.9em; color: #555;">
									<c:forEach var="opt" items="${item.selectedOptions}">
										<li>・${opt.optionName} (+${opt.optionPrice}円)</li>
									</c:forEach>
								</ul></td>
							<td>${item.price}円 <c:if
									test="${not empty item.selectedOptions}">
									<div style="font-size: 0.8em; color: #999;">小計:
										${item.price + item.optionTotalPrice}円</div>
								</c:if>
							</td>
							<td>${item.quantity}個</td>
						</tr>
					</c:forEach>

					<c:if test="${empty items}">
						<tr>
							<td colspan="4" style="text-align: center;">注文はありません。</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>

		<div class="summary-container">
			<div class="total-price-box">
				<span class="total-price-label">合計金額</span> <span
					class="total-price-value">${totalAmount}円</span>
			</div>

			<form action="CheckoutServlet" method="POST" class="action-buttons">
				<input type="hidden" name="orderId" value="${orderId}">
				<button type="submit" class="complete-button">会計確定</button>
			</form>
		</div>

	</div>

</body>
</html>
