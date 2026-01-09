<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<title>提供済み履歴</title>
</head>
<body>
	<h2>キッチン - 提供済み履歴</h2>
	<p>
		<button onclick="location.href='KitchenOrderServlet'">キッチン画面に戻る</button>
	</p>

	<table border="1" style="width: 100%; border-collapse: collapse;">
		<tr bgcolor="#eee">
			<th>卓</th>
			<th>商品名</th>
			<th>数量</th>
			<th>操作</th>
		</tr>
		<c:forEach var="h" items="${history}">
			<tr>
				<td align="center">${h.tableNo}</td>
				<td>${h.goodsName}</td>
				<td align="center">${h.quantity}</td>
				<td align="center">
					<form action="KitchenHistoryServlet" method="post"
						style="display: inline;">
						<input type="hidden" name="orderItemId" value="${h.orderItemId}">
						<button name="action" value="back" style="background: #ff9800;">キッチンに戻す</button>
						<button name="action" value="delete"
							onclick="return confirm('履歴を削除しますか？')"
							style="background: #f44336; color: white;">削除</button>
					</form>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${empty history}">
			<tr>
				<td colspan="4" align="center">履歴はありません。</td>
			</tr>
		</c:if>
	</table>
	<form action="KitchenHistoryServlet" method="post"
		style="display: inline;"
		onsubmit="return confirm('全ての履歴を完全に削除しますか？');">
		<input type="hidden" name="action" value="deleteAll">
		<button type="submit" style="background: #333; color: white;">全履歴を削除</button>
	</form>
</body>
</html>