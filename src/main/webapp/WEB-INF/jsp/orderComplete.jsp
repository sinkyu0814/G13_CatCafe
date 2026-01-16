<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ご注文ありがとうございます</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/orderComplete.css">
</head>
<body>

	<div class="container">
		<div class="order-card">
			<header class="order-header">
				<h2>ご注文を承りました</h2>
				<p class="order-id">注文番号：${orderId}</p>
			</header>

			<div class="order-content">
				<h3>承った内容</h3>
				<div class="scroll-area">
					<ul class="order-list">
						<c:forEach var="i" items="${orderItems}">
							<li><span class="item-name">${i.goodsName}</span> <span
								class="item-qty">× ${i.quantity}</span> <span class="item-total">${i.totalPrice}円</span>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>

			<p class="message">
				ただいま心を込めてお作りしております。<br>到着まで少々お待ちくださいませ。
			</p>

			<form action="ListServlet" method="get">
				<button class="return-button">メニューに戻る</button>
			</form>
		</div>
	</div>

</body>
</html>