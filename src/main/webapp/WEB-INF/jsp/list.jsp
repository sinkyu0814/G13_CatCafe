<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>メニュー画面</title>
</head>
<body>

	<!-- 上部カテゴリボタン -->
	<form action="ListServlet" method="get">
		<button name="category" value="Recomend">おすすめ</button>
		<button name="category" value="Cofe">珈琲</button>
		<button name="category" value="Tea">紅茶</button>
		<button name="category" value="Eat">主食</button>
		<button name="category" value="Sweets">スイーツ</button>
		<button name="category" value="SoftDrink">ソフトドリンク</button>
		<button name="category" value="Morning">モーニング</button>
		<button name="category" value="all">すべて</button>
	</form>
	<hr>

	<!-- 左：商品一覧 -->
	<div>
		${menuList.size() }
		<c:forEach var="m" items="${menuList}">
			<div class="menu-item">
				<a href="ConfirmServlet?id=${m.id}"> <img
					src="${pageContext.request.contextPath}/assets/images/${m.img}">
					<p>${m.name}</p>
					<p>${m.price}円</p>
				</a>
			</div>
		</c:forEach>
	</div>

	<!-- 右：カート表示 -->
	<div>
		<h3>カート</h3>
		<c:if test="${not empty cart}">
			<c:forEach var="c" items="${cart}">
				<div>${c.goodsName}× ${c.quantity} (${c.totalPrice}円)</div>
			</c:forEach>
		</c:if>
		<hr>

		<form action="ConfirmOrderServlet" method="post">
			<button>注文確定</button>
		</form>
	</div>

</body>
</html>
