<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>会計確認</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/check.css">

<script>
	function confirmCheckout() {
		const hasUnfinished = ${not empty hasUnfinished && hasUnfinished ? "true" : "false"};
		if (hasUnfinished) {
			return confirm("まだ提供されていない商品がありますが、会計を確定してもよろしいですか？");
		} else {
			return confirm("会計を確定します。よろしいですか？");
		}
	}
</script>
</head>
<body>

	<div class="container">
		<header class="page-header">
			<h1>
				会計確認 <span class="table-no">（テーブル: ${tableNo}）</span>
			</h1>
			<form action="HistoryServlet" method="GET" class="header-form">
				<input type="hidden" name="orderId" value="${orderId}">
				<button type="submit" class="back-link-button">注文履歴へ戻る</button>
			</form>
		</header>

		<div class="main-content">
			<div class="scrollable-area">
				<table class="item-list-table">
					<thead>
						<tr>
							<th>商品</th>
							<th>商品名・オプション</th>
							<th>単価</th>
							<th>数量</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${items}">
							<tr>
								<td class="td-img">
									<%-- ★ 修正：item.menuId を使ってGetImageServletから取得 --%> <img
									src="GetImageServlet?id=${item.menuId}" class="checkout-img"
									alt="${item.name}">
								</td>
								<td class="td-details"><strong class="item-name">${item.name}</strong>
									<ul class="option-list">
										<c:forEach var="opt" items="${item.selectedOptions}">
											<li>・${opt.optionName} (+${opt.optionPrice}円)</li>
										</c:forEach>
									</ul></td>
								<td class="td-price">
									<div class="base-price">${item.price}円</div> <c:if
										test="${not empty item.selectedOptions}">
										<div class="sub-total-text">小計: ${item.price + item.optionTotalPrice}円</div>
									</c:if>
								</td>
								<td class="td-qty">${item.quantity}個</td>
							</tr>
						</c:forEach>
						<c:if test="${empty items}">
							<tr>
								<td colspan="4" class="empty-msg">注文内容が見つかりません。</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>

			<aside class="summary-container">
				<div class="final-info-box">
					<p>
						上記の内容で<br>会計を確定しますか？
					</p>
				</div>
				<div class="total-price-box">
					<span class="total-price-label">お支払い合計</span> <span
						class="total-price-value">${totalAmount}円</span> <span
						class="tax-label">(税込)</span>
				</div>
				<form action="CheckoutServlet" method="POST" class="action-buttons">
					<input type="hidden" name="orderId" value="${orderId}">
					<button type="submit" class="complete-button"
						onclick="return confirmCheckout()">会計を確定する</button>
				</form>
			</aside>
		</div>
	</div>
</body>
</html>