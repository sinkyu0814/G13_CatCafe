<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<fmt:setBundle basename="properties.messages" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><fmt:message key="label.order_received" /></title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/orderComplete.css">
</head>
<body>
	<div class="container">
		<div class="order-card">
			<header class="order-header">
				<h2>
					<fmt:message key="label.order_received" />
				</h2>
				<p class="order-id">
					<fmt:message key="label.order_id" />
					：${orderId}
				</p>
			</header>
			<div class="order-content">
				<h3>
					<fmt:message key="label.order_details" />
				</h3>
				<div class="scroll-area">
					<ul class="order-list">
						<c:forEach var="i" items="${orderItems}">
							<li><span class="item-name">${i.goodsName}</span> <span
								class="item-qty">× ${i.quantity}</span> <span class="item-total">${i.totalPrice}<fmt:message
										key="label.unit_table" /></span></li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<p class="message">
				<fmt:message key="label.waiting_msg" />
			</p>
			<form action="ListServlet" method="get">
				<button class="return-button">
					<fmt:message key="button.back_to_menu" />
				</button>
			</form>
		</div>
	</div>
</body>
</html>