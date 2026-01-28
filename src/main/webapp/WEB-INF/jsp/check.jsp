<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<fmt:setBundle basename="properties.messages" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><fmt:message key="label.accounting_confirm" /></title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/check.css">

<script>
	function confirmCheckout() {
		const hasUnfinished = ${not empty hasUnfinished && hasUnfinished ? "true" : "false"};
		if (hasUnfinished) {
			return confirm("<fmt:message key='alert.unfinished_items' />");
		} else {
			return confirm("<fmt:message key='alert.confirm_checkout' />");
		}
	}
</script>
</head>
<body>
	<div class="container">
		<header class="page-header">
			<h1>
				<fmt:message key="label.accounting_confirm" />
				<span class="table-no">（<fmt:message key="label.table" />:
					${tableNo}）
				</span>
			</h1>
			<form action="HistoryServlet" method="GET" class="header-form">
				<input type="hidden" name="orderId" value="${orderId}">
				<button type="submit" class="back-link-button">
					<fmt:message key="button.back_to_history" />
				</button>
			</form>
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
						<c:forEach var="item" items="${items}">
							<tr>
								<td class="td-img"><img
									src="GetImageServlet?id=${item.menuId}" class="checkout-img"
									alt="${item.name}"></td>
								<td class="td-details"><strong class="item-name">${item.name}</strong>
									<ul class="option-list">
										<c:forEach var="opt" items="${item.selectedOptions}">
											<li>・${opt.optionName} (+${opt.optionPrice}<fmt:message
													key="label.unit_table" />)
											</li>
										</c:forEach>
									</ul></td>
								<td class="td-price">
									<div class="base-price">${item.price}<fmt:message
											key="label.unit_table" />
									</div> <c:if test="${not empty item.selectedOptions}">
										<div class="sub-total-text">
											<fmt:message key="label.total_amount" />
											: ${item.price + item.optionTotalPrice}
											<fmt:message key="label.unit_table" />
										</div>
									</c:if>
								</td>
								<td class="td-qty">${item.quantity}<fmt:message
										key="label.unit_qty" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<aside class="summary-container">
				<div class="final-info-box">
					<p>
						<fmt:message key="msg.confirm_checkout_desc" />
					</p>
				</div>
				<div class="total-price-box">
					<span class="total-price-label"><fmt:message
							key="label.payment_total" /></span> <span class="total-price-value">${totalAmount}<fmt:message
							key="label.unit_table" /></span> <span class="tax-label"><fmt:message
							key="label.tax_incl" /></span>
				</div>
				<form action="CheckoutServlet" method="POST" class="action-buttons">
					<input type="hidden" name="orderId" value="${orderId}">
					<button type="submit" class="complete-button"
						onclick="return confirmCheckout()">
						<fmt:message key="button.confirm_accounting" />
					</button>
				</form>
			</aside>
		</div>
	</div>
</body>
</html>