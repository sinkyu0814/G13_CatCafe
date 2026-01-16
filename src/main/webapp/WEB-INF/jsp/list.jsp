<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>メニュー画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/menulist.css">
</head>
<body>

	<div class="container">
		<main class="main-content">
			<nav class="category-nav">
				<form action="ListServlet" method="get">
					<button name="category" value="Recomend" class="active">おすすめ</button>
					<button name="category" value="Cofe">珈琲</button>
					<button name="category" value="Tea">紅茶</button>
					<button name="category" value="Eat">主食</button>
					<button name="category" value="Sweets">スイーツ</button>
					<button name="category" value="SoftDrink">ソフトドリンク</button>
					<button name="category" value="Morning">モーニング</button>
				</form>
			</nav>

			<div class="product-slider-wrapper">
				<button type="button" class="slider-arrow left"
					onclick="scrollGrid(-1)">＜</button>

				<div class="product-grid" id="productGrid">
					<c:forEach var="m" items="${menuList}">
						<div class="menu-item">
							<a href="ConfirmServlet?id=${m.id}">
								<div class="img-container">
									<img
										src="${pageContext.request.contextPath}/assets/images/${m.img}"
										alt="${m.name}">
								</div>
								<div class="item-info">
									<p class="item-name">${m.name}</p>
									<p class="item-price">${m.price}円</p>
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
					<button type="submit" class="checkout-main-button">会計</button>
				</form>
			</div>
		</main>

		<aside class="sidebar">
			<div class="cart-header">カート</div>

			<div class="cart-content">
				<c:if test="${not empty cart}">
					<c:forEach var="c" items="${cart}">
						<div class="cart-row">
							<span class="cart-item-name">${c.goodsName}</span> <span
								class="cart-item-qty">× ${c.quantity}</span>
						</div>
					</c:forEach>
				</c:if>
			</div>

			<div class="sidebar-footer">
				<form action="HistoryServlet" method="get">
					<button class="side-btn">注文履歴を見る</button>
				</form>
				<form action="ConfirmOrderServlet" method="post">
					<button class="side-btn order-btn">注文確定</button>
				</form>
			</div>
		</aside>
	</div>

	<script>
		function scrollGrid(direction) {
			const grid = document.getElementById('productGrid');
			const scrollAmount = grid.clientWidth;
			grid.scrollBy({
				left : direction * scrollAmount,
				behavior : 'smooth'
			});
		}
	</script>

</body>
</html>