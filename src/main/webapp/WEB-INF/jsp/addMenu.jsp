<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>新しいメニュー追加</title>
</head>
<body>
	<h2>新しいメニューを追加</h2>

	<c:if test="${not empty error}">
		<p style="color: red;">${error}</p>
	</c:if>
	<c:if test="${not empty success}">
		<p style="color: green;">${success}</p>
	</c:if>
	<form action="${pageContext.request.contextPath}/AddMenuServlet"
		method="post" enctype="multipart/form-data">
		<label>メニュー名: <input type="text" name="name" required></label><br>
		<label>価格: <input type="number" name="price" required></label><br>
		<label>数量: <input type="number" name="quantity" required></label><br>
		<label>カテゴリ: <select name="category">
				<option value="Recomend">Recomend</option>
				<option value="Eat">Eat</option>
				<option value="Morning">Morning</option>
				<option value="Sweets">Sweets</option>
				<option value="Cofe">Cofe</option>
				<option value="Tea">Tea</option>
				<option value="SoftDrink">SoftDrink</option>
		</select>
		</label><br> <label>画像ファイル: <input type="file" name="image"
			accept="image/*"></label><br>
		<button type="submit">追加</button>
	</form>
</body>
</html>
