<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>提供済み履歴</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/kitchenHistory.css">
</head>
<body>
	<h2>キッチン - 提供済み履歴</h2>

	<p>
		<button class="btn-return"
			onclick="location.href='KitchenOrderServlet'">← キッチン画面に戻る</button>
	</p>

	<table>
		<thead>
			<tr>
				<th width="80" class="center">卓</th>
				<th>商品名</th>
				<th width="80" class="center">数量</th>
				<th width="280" class="center">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="h" items="${history}">
				<tr>
					<td class="center" style="font-size: 1.2rem;">${h.tableNo}</td>
					<td style="font-weight: bold;">${h.goodsName}</td>
					<td class="center">${h.quantity}</td>
					<td class="center">
						<form action="KitchenHistoryServlet" method="post">
							<input type="hidden" name="orderItemId" value="${h.orderItemId}">
							<button type="submit" name="action" value="back" class="btn-back">キッチンに戻す</button>
							<button type="submit" name="action" value="delete"
								class="btn-delete" onclick="return confirm('この履歴を削除しますか？')">削除</button>
						</form>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty history}">
				<tr>
					<td colspan="4" class="center" style="padding: 40px; color: #999;">履歴はありません。</td>
				</tr>
			</c:if>
		</tbody>
	</table>

	<div class="footer-actions">
		<p
			style="color: var(--danger-red); font-weight: bold; margin-bottom: 10px;">【管理者エリア】</p>
		<form action="KitchenHistoryServlet" method="post"
			onsubmit="return confirm('注意：全ての履歴を完全に削除します。この操作は取り消せません。');">
			<input type="hidden" name="action" value="deleteAll">
			<button type="submit" class="btn-delete-all">全履歴を一括削除する</button>
		</form>
	</div>
</body>
</html>