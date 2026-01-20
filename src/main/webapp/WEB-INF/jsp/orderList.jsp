<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>キッチンオーダー</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/orderList.css">
</head>
<body>
	<h2>キッチン - 調理状況管理</h2>
	<p>
		<button class="btn" onclick="location.href='KitchenHistoryServlet'">提供済み履歴を見る</button>
	</p>

	<table>
		<thead>
			<tr>
				<th>卓</th>
				<th>日時</th>
				<th>商品名 (オプション)</th>
				<th>数量</th>
				<th>現在の状態</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="o" items="${orders}">
				<tr class="status-${o.kitchenStatus}">
					<td class="center">${o.tableNo}</td>
					<td>${o.orderDate}</td>
					<td><strong>${o.goodsName}</strong><br> <small> <c:forEach
								var="opt" items="${o.options}">・${opt} </c:forEach>
					</small></td>
					<td class="center">${o.quantity}</td>
					<td class="center"><c:choose>
							<c:when test="${o.kitchenStatus == 'READY'}">調理待ち</c:when>
							<c:when test="${o.kitchenStatus == 'COOKING'}">調理中...</c:when>
							<c:when test="${o.kitchenStatus == 'COOKED'}">調理完了</c:when>
						</c:choose></td>
					<td class="center">
						<form action="KitchenOrderServlet" method="post">
							<input type="hidden" name="orderItemId" value="${o.orderItemId}">

							<c:if test="${o.kitchenStatus == 'READY'}">
								<button type="submit" name="status" value="COOKING" class="btn">調理開始</button>
							</c:if>

							<c:if test="${o.kitchenStatus == 'COOKING'}">
								<button type="submit" name="status" value="COOKED" class="btn">調理完了</button>
							</c:if>

							<c:if test="${o.kitchenStatus == 'COOKED'}">
								<button type="submit" name="status" value="DELIVERED"
									class="btn btn-delivered">提供済み</button>
								<button type="submit" name="status" value="READY" class="btn">待ちに戻す</button>
							</c:if>
						</form>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty orders}">
				<tr>
					<td colspan="6" class="center">現在、調理待ちの注文はありません。</td>
				</tr>
			</c:if>
		</tbody>
	</table>
</body>
</html>