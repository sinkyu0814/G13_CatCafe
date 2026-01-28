<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<fmt:setBundle basename="properties.messages" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><fmt:message key="label.order_history" /></title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/history.css">
</head>
<body>
	<div class="container">
		<header class="page-header">
			<h1>
				<fmt:message key="label.order_history" />
				<span class="header-info">（<fmt:message key="label.table" />:
					${tableNo} ／ <fmt:message key="label.order_id" />: ${orderId}）
				</span>
			</h1>
			<button class="close-button" onclick="location.href='ListServlet'">
				<fmt:message key="button.back" />
			</button>
		</header>
		<div class="main-content">
			<div class="scrollable-area">
				<table class="item-list-table">
					<thead>
						<tr>
							<th><fmt:message key="label.item_name" /></th>
							<th><fmt:message key="label.details" /></th>
							<th><fmt:message key="label.unit_price" /></th>
							<th><fmt:message key="label.quantity" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${orderItems}">
							<tr>
								<td class="td-img"><img
									src="GetImageServlet?id=${item.menuId}" class="history-img"></td>
								<td class="td-details">
									<div class="item-name">${item.name}</div> <c:forEach var="opt"
										items="${item.selectedOptions}">
										<div class="option-text">
											+ ${opt.optionName} (+${opt.optionPrice}
											<fmt:message key="label.unit_table" />
											)
										</div>
									</c:forEach>
								</td>
								<td class="td-price">${item.price}<fmt:message
										key="label.unit_table" />
									<c:if test="${not empty item.selectedOptions}">
										<div class="sub-price">
											(計: ${item.price + item.optionTotalPrice}
											<fmt:message key="label.unit_table" />
											)
										</div>
									</c:if></td>
								<td class="td-qty">${item.quantity}<fmt:message
										key="label.unit_qty" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<aside class="summary-container">
				<div class="info-box">
					<p class="info-text">
						<fmt:message key="label.waiting_msg" />
					</p>
				</div>
				<div class="total-price-box">
					<span class="total-label"><fmt:message
							key="label.total_tax_incl" /></span> <span class="total-price-value">${totalAmount}<fmt:message
							key="label.unit_table" /></span>
				</div>
				<div class="action-buttons">
					<button class="call-button">
						<fmt:message key="button.call_staff" />
					</button>
					<form action="CheckServlet" method="GET" style="width: 100%;">
						<button type="submit" class="checkout-button">
							<fmt:message key="button.proceed_to_checkout" />
						</button>
					</form>
				</div>
			</aside>
		</div>
	</div>
</body>
</html>