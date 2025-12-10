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
/* ★★ CSSは以前提案したcheck.jsp用のスタイルをここに適用します ★★
    （例として、主要なレイアウトを保つための最小限のスタイルを含めます）
*/
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
		<h1>会計</h1>
		<%-- HistoryServletへ戻る --%>
		<a href="HistoryServlet"><button>戻る</button></a>
	</header>

	<div class="main-content">

		<div class="scrollable-area">
			<table class="item-list-table">
				<thead>
					<tr>
						<th style="width: 10%;">画像</th>
						<th style="width: 50%;">商品名</th>
						<th style="width: 20%;">値段</th>
						<th style="width: 20%;">注文数</th>
					</tr>
				</thead>
				<tbody>
					<%-- ★ 修正: Servletから渡されたリスト 'items' をJSTLでループ ★ --%>
					<c:forEach var="item" items="${items}">
						<tr>
							<td>画像</td>
							<td>${item.name}</td>
							<td>${item.price}円</td>
							<td>${item.quantity}個</td>
						</tr>
					</c:forEach>
					<%-- カートが空の場合はメッセージを表示 --%>
					<c:if test="${empty items}">
						<tr>
							<td colspan="4" style="text-align: center;">注文された商品はありません。</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>

		<div class="summary-container">
			<div class="total-price-box">
				<span class="total-price-label">合計金額</span>
				<%-- ★ 修正: Servletから渡された合計金額を表示 ★ --%>
				<span class="total-price-value">${totalAmount}円</span>
			</div>

			<%-- ★ 修正: CheckoutServlet（会計案内）へ遷移するフォーム ★ --%>
			<form action="checkout" method="GET" class="action-buttons">
				<button type="submit" class="complete-button">確認完了</button>
			</form>
		</div>

	</div>

</body>
</html>