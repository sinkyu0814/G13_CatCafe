<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>注文履歴</title>

<style type="text/css">
/* 元のスタイルそのまま */
body {
	font-family: sans-serif;
	margin: 0;
	padding: 20px;
	background-color: #f0f0f0;
}

.page-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 10px 0;
	border-bottom: 1px solid #ccc;
	margin-bottom: 20px;
}

.main-content {
	display: flex;
	gap: 20px;
}

.scrollable-area {
	flex-grow: 1;
	max-height: 450px;
	overflow-y: auto;
	padding-right: 15px;
}

.item-list-table {
	width: 100%;
	border-collapse: collapse;
}

.item-list-table th, .item-list-table td {
	padding: 10px;
	text-align: left;
	border-bottom: 1px solid #ddd;
}

.item-list-table thead tr {
	background-color: #ccc;
}

.summary-container {
	width: 300px;
	display: flex;
	flex-direction: column;
	gap: 15px;
}

.nutrition-summary-box {
	flex-grow: 1;
	min-height: 150px;
	padding: 10px;
	background-color: #eee;
	border: 1px solid #ccc;
}

.total-price-box {
	height: 60px;
	display: flex;
	flex-direction: column;
	justify-content: space-around;
	align-items: flex-end;
	padding: 10px;
	background-color: #eee;
	border: 1px solid #ccc;
}

.total-price-value {
	font-size: 18px;
	font-weight: bold;
}

.action-buttons {
	display: flex;
	flex-direction: column;
	gap: 10px;
	margin-top: auto;
}

.checkout-button {
	display: block;
	padding: 10px 0;
	background-color: #eee;
	border: 1px solid #ccc;
	cursor: pointer;
	font-size: 16px;
	text-align: center;
	width: 100%;
}
</style>
</head>

<body>

	<header class="page-header">
		<h1>注文履歴（テーブル ${tableNo} ／ 注文ID ${orderId}）</h1>
		<button onclick="location.href='TableSelectServlet'">閉じる</button>
	</header>

	<div class="main-content">

		<!-- ===== 注文一覧 ===== -->
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
					<c:forEach var="item" items="${orderItems}">
						<tr>
            <td>
                <c:choose>
                    <c:when test="${not empty item.image}">
                        <img src="${pageContext.request.contextPath}/assets/images/${item.image}" width="50" height="50" style="object-fit: cover;">
                    </c:when>
                    <c:otherwise>No Image</c:otherwise>
                </c:choose>
            </td>
            <td>
                <div>${item.name}</div>
                <c:forEach var="opt" items="${item.selectedOptions}">
                    <div style="font-size: 0.8em; color: #666; margin-left: 10px;">
                        + ${opt.optionName} (+${opt.optionPrice}円)
                    </div>
                </c:forEach>
            </td>
            <td>
                ${item.price}円
                <c:if test="${not empty item.selectedOptions}">
									<div style="font-size: 0.8em; color: #888;">(計:
										${item.price + item.optionTotalPrice}円)</div>
								</c:if>
							</td>
            <td>${item.quantity}個</td>
        </tr>
					</c:forEach>

					<c:if test="${empty orderItems}">
						<tr>
							<td colspan="4">注文履歴がありません</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>

		<!-- ===== サマリー ===== -->
		<div class="summary-container">

			<div class="nutrition-summary-box">
				<span class="summary-label">栄養素合計（仮）</span>
				<p>カロリー: ${nutritionData.calories}</p>
				<p>タンパク質: ${nutritionData.protein}</p>
			</div>

			<div class="total-price-box">
				<span class="summary-label">合計金額</span> <span
					class="total-price-value">${totalAmount}円</span>
			</div>

			<div class="action-buttons">
				<button class="call-button">店員呼び出し</button>

				<!-- 会計画面へ（orderIdをセッション利用する想定） -->
				<form action="CheckServlet" method="GET"
					style="margin: 0; padding: 0; width: 100%;">
					<button type="submit" class="checkout-button">会計</button>
				</form>
			</div>
		</div>
	</div>

</body>
</html>
