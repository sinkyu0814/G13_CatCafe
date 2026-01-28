<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<fmt:setBundle basename="properties.messages" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><fmt:message key="label.shop_name" /></title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/menulist.css">
<style>
.cart-warning {
	color: #e74c3c;
	background-color: #fdf2f2;
	padding: 10px;
	margin: 10px;
	border: 1px solid #e74c3c;
	border-radius: 4px;
	font-size: 0.85em;
	text-align: center;
	font-weight: bold;
}
</style>
</head>
<body>
	<div class="container">
		<main class="main-content">
			<nav class="category-nav">
				<form action="ListServlet" method="get">
					<button name="category" value="Recomend"
						class="${(empty param.category or param.category == 'Recomend') ? 'active' : ''}">
						<fmt:message key="cat.recommend" />
					</button>
					<button name="category" value="Cofe"
						class="${param.category == 'Cofe' ? 'active' : ''}">
						<fmt:message key="cat.coffee" />
					</button>
					<button name="category" value="Tea"
						class="${param.category == 'Tea' ? 'active' : ''}">
						<fmt:message key="cat.tea" />
					</button>
					<button name="category" value="Eat"
						class="${param.category == 'Eat' ? 'active' : ''}">
						<fmt:message key="cat.main" />
					</button>
					<button name="category" value="Sweets"
						class="${param.category == 'Sweets' ? 'active' : ''}">
						<fmt:message key="cat.sweets" />
					</button>
					<button name="category" value="SoftDrink"
						class="${param.category == 'SoftDrink' ? 'active' : ''}">
						<fmt:message key="cat.softdrink" />
					</button>
					<button name="category" value="Morning"
						class="${param.category == 'Morning' ? 'active' : ''}">
						<fmt:message key="cat.morning" />
					</button>
				</form>
			</nav>

			<div class="product-slider-wrapper">
				<button type="button" class="slider-arrow left"
					onclick="scrollGrid(-1)">＜</button>
				<div class="product-grid" id="productGrid">
					<c:forEach var="m" items="${menuList}">
						<div class="menu-item">
							<a href="ConfirmServlet?id=${m.id}&category=${param.category}">
								<div class="img-container">
									<img src="GetImageServlet?id=${m.id}" alt="${m.name}">
								</div>
								<div class="item-info">
									<p class="item-name">${m.name}</p>
									<%-- 単位を label.unit_table (円) に修正 --%>
									<p class="item-price">${m.price}<fmt:message
											key="label.unit_table" />
									</p>
								</div>
							</a>
						</div>
					</c:forEach>
				</div>
				<button type="button" class="slider-arrow right"
					onclick="scrollGrid(1)">＞</button>
			</div>

			<div class="footer-actions">
				<form action="CheckServlet" method="GET">
					<button type="submit" class="checkout-main-button">
						<fmt:message key="button.accounting" />
					</button>
				</form>
			</div>
		</main>

		<aside class="sidebar">
			<div class="cart-header">
				<fmt:message key="label.cart" />
			</div>
			<div class="cart-content">
				<c:if test="${empty cart and not empty error}">
					<div class="cart-warning">${error}</div>
				</c:if>

				<c:forEach var="c" items="${cart}" varStatus="status">
					<div class="cart-row"
						style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
						<div class="cart-info">
							<span class="cart-item-name">${c.goodsName}</span>
							<c:if test="${not empty c.selectedOptions}">
								<div style="font-size: 0.75em; color: #666;">
									<c:forEach var="opt" items="${c.selectedOptions}">+${opt.optionName}</c:forEach>
								</div>
							</c:if>
						</div>

						<div class="cart-qty-area"
							style="display: flex; align-items: center; gap: 5px;">
							<form action="UpdateCartServlet" method="post" style="margin: 0;">
								<input type="hidden" name="index" value="${status.index}">
								<input type="hidden" name="change" value="-1"> <input
									type="hidden" name="category" value="${param.category}">
								<button type="submit" class="qty-btn"
									style="width: 24px; height: 24px; padding: 0;">-</button>
							</form>

							<span class="cart-item-qty">${c.quantity}</span>

							<form action="UpdateCartServlet" method="post" style="margin: 0;">
								<input type="hidden" name="index" value="${status.index}">
								<input type="hidden" name="change" value="1"> <input
									type="hidden" name="category" value="${param.category}">
								<button type="submit" class="qty-btn"
									style="width: 24px; height: 24px; padding: 0;">+</button>
							</form>
						</div>
					</div>
				</c:forEach>
			</div>

			<div class="sidebar-footer">
				<form action="HistoryServlet" method="get">
					<button type="submit" class="side-btn">
						<fmt:message key="button.view_history" />
					</button>
				</form>

				<%-- actionが ConfirmOrderServlet であることを再確認 --%>
				<form action="ConfirmOrderServlet" method="post">
					<button type="submit" class="side-btn order-btn">
						<fmt:message key="button.order_confirm" />
					</button>
				</form>
			</div>
		</aside>
	</div>

	<script>
		function scrollGrid(direction) {
			const grid = document.getElementById('productGrid');
			if (grid) {
				grid.scrollBy({
					left : direction * grid.clientWidth,
					behavior : 'smooth'
				});
			}
		}
	</script>
</body>
</html>