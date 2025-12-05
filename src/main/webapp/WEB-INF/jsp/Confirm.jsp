<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注文内容確認</title>
</head>
<body>

	<h2>商品確認</h2>

	<div style="display: flex; gap: 30px;">

		<!-- 左：画像 -->
		<div>
			<img src="assets/images/${menu.img}" width="300">
		</div>

		<!-- 右：商品情報 -->
		<div>
			<h3>${menu.name}</h3>
			<p>価格：${menu.price}円</p>
			<p>カテゴリ：${menu.category}</p>

			<!-- 数量選択 -->
			<form action="CartAddServlet" method="post">
				<input type="hidden" name="id" value="${menu.id}"> <label>数量：</label>
				<select name="quantity">
					<c:forEach var="i" begin="1" end="10">
						<option value="${i}">${i}</option>
					</c:forEach>
				</select> <br>
				<br>

				<button type="submit">カートに追加</button>
			</form>

			<br>
			<form action="ListServlet" method="get">
				<button type="submit">閉じる</button>
			</form>

		</div>

	</div>

</body>
</html>
