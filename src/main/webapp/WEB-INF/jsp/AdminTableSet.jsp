<%-- webapp/WEB-INF/jsp/AdminTableSet.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>【店員用】テーブル固定設定</title>
<style>
.admin-box {
	width: 300px;
	margin: 100px auto;
	padding: 20px;
	border: 2px solid #333;
	text-align: center;
}

input {
	width: 80%;
	padding: 10px;
	font-size: 1.2em;
	margin-bottom: 10px;
}

button {
	padding: 10px 20px;
	cursor: pointer;
	background: #333;
	color: white;
}
</style>
</head>
<body>
	<div class="admin-box">
		<h2>テーブル番号設定</h2>
		<p>この端末のテーブル番号を入力してください。</p>
		<form action="AdminTableSetServlet" method="post">
			<input type="number" name="fixedTableNo" min="1" max="25" required
				placeholder="例: 5"> <br>
			<button type="submit">この番号で固定する</button>
		</form>
	</div>
</body>
</html>