<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン</title>
</head>
<body>
	<h2>システムログイン</h2>
	<%
	if (request.getAttribute("error") != null) {
	%>
	<p style="color: red;"><%=request.getAttribute("error")%></p>
	<%
	}
	%>
	<form action="LoginServlet" method="post">
		ユーザー名: <input type="text" name="username" required>G13<br>
		パスワード: <input type="password" name="password" required>2136<br>
		<button type="submit">ログイン</button>
	</form>
</body>
</html>