<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>端末設定 | テーブル固定</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/AdminTableSet.css">
</head>
<body>
	<div class="admin-box">
		<h2>テーブル番号設定</h2>
		<p>
			この端末を特定のテーブルとして<br>固定するための設定です。
		</p>

		<form action="AdminTableSetServlet" method="post">
			<span class="input-label">固定する番号 (1-25)</span> <input type="number"
				name="fixedTableNo" min="1" max="25" required placeholder="0"
				autofocus> <br>
			<button type="submit">この番号で固定する</button>
		</form>

		<div class="footer-note">※設定後、この端末は指定されたテーブル専用となります。</div>
	</div>
</body>
</html>