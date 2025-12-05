<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>キッチンオーダー</title>
</head>
<body>

	<h2>キッチン - オーダー一覧</h2>

	<table border="1" cellpadding="5">
		<tr>
			<th>注文番号</th>
			<th>テーブル</th>
			<th>注文日時</th>
			<th>商品名</th>
			<th>数量</th>
		</tr>

		<c:forEach var="o" items="${orders}">
			<tr>
				<td>${o.orderId}</td>
				<td>${o.tableNo}</td>
				<td>${o.orderDate}</td>
				<td>${o.goodsName}</td>
				<td>${o.quantity}</td>
			</tr>
		</c:forEach>

	</table>

</body>
</html>
