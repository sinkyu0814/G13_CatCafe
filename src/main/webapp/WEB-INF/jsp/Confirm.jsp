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

		<div>
			<img src="assets/images/${menu.img}" width="300">
		</div>

		<div>
			<h3>${menu.name}</h3>
			<p>価格：${menu.price}円</p>
			<p>カテゴリ：${menu.category}</p>

			<form action="CartAddServlet" method="post">
				<input type="hidden" name="id" value="${menu.id}"> <label>数量：</label>
				<select name="quantity">
					<c:forEach var="i" begin="1" end="10">
						<option value="${i}">${i}</option>
					</c:forEach>
				</select> <br> <br>

				<c:if test="${not empty options}">
					<h4>オプション（トッピング）</h4>
					<div
						style="background: #f9f9f9; padding: 10px; border-radius: 5px;">
						<c:forEach var="opt" items="${options}">
							<label
								style="display: block; margin-bottom: 5px; cursor: pointer;">
								<input type="checkbox" name="optionIds" value="${opt.optionId}">
								${opt.optionName}（+${opt.optionPrice}円）
							</label>
						</c:forEach>
					</div>
					<br>
				</c:if>

				<button type="submit">カートに追加する</button>
			</form>

			<br>
			<form action="ListServlet" method="get">
				<button type="submit">戻る</button>
			</form>
		</div>
	</div>

</body>
</html>