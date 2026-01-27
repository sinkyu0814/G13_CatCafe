<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>注文履歴</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/history.css">
</head>

<body>

	<div class="container">
		<header class="page-header">
			<h1>
				注文履歴 <span class="header-info">（テーブル: ${tableNo} ／ 注文ID:
					${orderId}）</span>
			</h1>
			<button class="close-button" onclick="location.href='ListServlet'">戻る</button>
		</header>

		<div class="main-content">
			<div class="scrollable-area">
				<table class="item-list-table">
					<thead>
						<tr>
							<th>商品</th>
							<th>詳細</th>
							<th>単価</th>
							<th>数量</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${orderItems}">
							<tr>
								<td class="td-img">
									<%-- ★ 修正：item.menuId を使ってDBから画像を取得 --%> <img
									src="GetImageServlet?id=${item.menuId}" class="history-img"
									alt="${item.name}">
								</td>
								<td class="td-details">
									<div class="item-name">${item.name}</div> <c:forEach var="opt"
										items="${item.selectedOptions}">
										<div class="option-text">+ ${opt.optionName}
											(+${opt.optionPrice}円)</div>
									</c:forEach>
								</td>
								<td class="td-price">${item.price}円<c:if
										test="${not empty item.selectedOptions}">
										<div class="sub-price">(計: ${item.price + item.optionTotalPrice}円)</div>
									</c:if>
								</td>
								<td class="td-qty">${item.quantity}個</td>
							</tr>
						</c:forEach>
						<c:if test="${empty orderItems}">
							<tr>
								<td colspan="4" class="empty-msg">注文履歴がありません</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>

			<aside class="summary-container">
				<div class="info-box">
					<p class="info-text">
						ご注文ありがとうございます。<br>内容をご確認ください。
					</p>
				</div>
				<div class="total-price-box">
					<span class="total-label">合計金額（税込）</span> <span
						class="total-price-value">${totalAmount}円</span>
				</div>
				<div class="action-buttons">
					<button class="call-button">店員を呼ぶ</button>
					<form action="CheckServlet" method="GET" style="width: 100%;">
						<button type="submit" class="checkout-button">お会計へ進む</button>
					</form>
				</div>
			</aside>
		</div>
	</div>

</body>
</html>