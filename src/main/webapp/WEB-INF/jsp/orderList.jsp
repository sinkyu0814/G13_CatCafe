<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>キッチンオーダー管理</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/orderList.css">
</head>
<body>
	<h2>キッチン - 調理状況管理</h2>

	<div class="header-nav">
		<button class="btn"
			style="background-color: #fff; border: 1px solid #ccc; color: #333;"
			onclick="location.href='KitchenHistoryServlet'">提供済み履歴を見る</button>
	</div>

	<table>
		<thead>
			<tr>
				<th width="80">卓</th>
				<th width="120">注文時刻</th>
				<th>商品名 (オプション)</th>
				<th width="80">数量</th>
				<th width="150">現在の状態</th>
				<th width="250">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="o" items="${orders}">
				<tr class="status-${o.kitchenStatus}">
					<td class="center"><span class="table-no">${o.tableNo}</span></td>
					<td class="center" style="font-size: 0.9rem; color: #666;">${o.orderDate}</td>
					<td><strong style="font-size: 1.2rem;">${o.goodsName}</strong><br>
						<div style="margin-top: 5px; color: #555;">
							<c:forEach var="opt" items="${o.options}">
								<span
									style="background: rgba(0, 0, 0, 0.05); padding: 2px 6px; border-radius: 4px; font-size: 0.85rem; margin-right: 5px;">・${opt}</span>
							</c:forEach>
						</div></td>
					<td class="center"><span
						style="font-size: 1.3rem; font-weight: bold;">${o.quantity}</span></td>
					<td class="center"><c:choose>
							<c:when test="${o.kitchenStatus == 'READY'}">
								<span class="badge badge-ready">調理待ち</span>
							</c:when>
							<c:when test="${o.kitchenStatus == 'COOKING'}">
								<span class="badge badge-badge-cooking">調理中...</span>
							</c:when>
							<c:when test="${o.kitchenStatus == 'COOKED'}">
								<span class="badge badge-cooked">調理完了</span>
							</c:when>
						</c:choose></td>
					<td class="center">
						<form action="KitchenOrderServlet" method="post">
							<input type="hidden" name="orderItemId" value="${o.orderItemId}">

							<c:if test="${o.kitchenStatus == 'READY'}">
								<button type="submit" name="status" value="COOKING"
									class="btn btn-start">調理開始</button>
							</c:if>

							<c:if test="${o.kitchenStatus == 'COOKING'}">
								<button type="submit" name="status" value="COOKED"
									class="btn btn-complete">調理完了</button>
							</c:if>

							<c:if test="${o.kitchenStatus == 'COOKED'}">
								<button type="submit" name="status" value="DELIVERED"
									class="btn btn-delivered">提供済み</button>
								<button type="submit" name="status" value="READY"
									class="btn btn-return">戻す</button>
							</c:if>
						</form>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty orders}">
				<tr>
					<td colspan="6" class="center"
						style="padding: 50px; font-size: 1.2rem; color: #999;">
						現在、調理待ちの注文はありません。</td>
				</tr>
			</c:if>
		</tbody>
	</table>
</body>
</html>