<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注文完了</title>
</head>
<body>

	<h2>ご注文ありがとうございました！</h2>

	<p>注文番号：${orderId}</p>

	<h3>注文内容</h3>
	<ul>
		<c:forEach var="i" items="${orderItems}">
			<li>${i.goodsName} × ${i.quantity} = ${i.totalPrice}円</li>
		</c:forEach>
	</ul>

	<form action="ListServlet" method="get">
		<button>メニューに戻る</button>
	</form>

</body>
</html>
