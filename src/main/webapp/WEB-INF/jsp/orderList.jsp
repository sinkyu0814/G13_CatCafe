<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<title>キッチンオーダー</title>
<style>
.status-READY {
	background-color: #ffcccc;
}

.status-COOKING {
	background-color: #ffffcc;
}

.status-COOKED {
	background-color: #ccffcc;
}

.btn {
	padding: 5px 10px;
	cursor: pointer;
}
</style>
</head>
<body>
	<h2>キッチン - 調理状況管理</h2>
	<p>
		<button onclick="location.href='KitchenHistoryServlet'">提供済み履歴を見る</button>
	</p>

	<table border="1" style="width: 100%; border-collapse: collapse;">
		<tr bgcolor="#eee">
			<th>卓</th>
			<th>日時</th>
			<th>商品名 (オプション)</th>
			<th>数量</th>
			<th>現在の状態</th>
			<th>操作</th>
		</tr>
		<c:forEach var="o" items="${orders}">
			<tr class="status-${o.kitchenStatus}">
				<td align="center">${o.tableNo}</td>
				<td>${o.orderDate}</td>
				<td><strong>${o.goodsName}</strong><br> <small><c:forEach
							var="opt" items="${o.options}">・${opt} </c:forEach></small></td>
				<td align="center">${o.quantity}</td>
				<td align="center"><c:choose>
						<c:when test="${o.kitchenStatus == 'READY'}">調理待ち</c:when>
						<c:when test="${o.kitchenStatus == 'COOKING'}">調理中...</c:when>
						<c:when test="${o.kitchenStatus == 'COOKED'}">調理完了</c:when>
					</c:choose></td>
				<td>
					<form action="KitchenOrderServlet" method="post"
						style="display: inline;">
						<input type="hidden" name="orderItemId" value="${o.orderItemId}">
						<c:if test="${o.kitchenStatus == 'READY'}">
							<button name="status" value="COOKING" class="btn">調理開始</button>
						</c:if>
						<c:if test="${o.kitchenStatus == 'COOKING'}">
							<button name="status" value="COOKED" class="btn">調理完了</button>
						</c:if>
						<c:if test="${o.kitchenStatus == 'COOKED'}">
							<button name="status" value="DELIVERED" class="btn"
								style="background: #4CAF50; color: white;">提供済み</button>
							<button name="status" value="READY" class="btn">待ちに戻す</button>
						</c:if>
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>