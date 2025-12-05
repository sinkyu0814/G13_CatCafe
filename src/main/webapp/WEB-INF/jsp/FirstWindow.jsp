<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>人数入力</title>
</head>
<body>
	<h1>ねこまるカフェ</h1>
	<form action="ToppageServlet" method="post">
		<p>
			<input type="number" name="quantity" min="1" max="9" value="1">人
		</p>
		<p>テーブル番号 01番</p>
		<button type="submit">注文開始</button>
	</form>
	<br>
	<select name="language">
		<option>日本語</option>
		<option>English</option>
		<option>中文</option>
		<option>Castellano</option>
		<option>عَرَبيّ</option>
	</select>
</body>
</html>