<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>システムログイン</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/Login.css">
</head>
<body>

	<div class="login-container">
		<div class="login-box">
			<header class="login-header">
				<h1>System Login</h1>
				<p>管理システムにログインしてください</p>
			</header>

			<%
			if (request.getAttribute("error") != null) {
			%>
			<div class="error-message">
				<%=request.getAttribute("error")%>
			</div>
			<%
			}
			%>

			<form action="LoginServlet" method="post" class="login-form">
				<div class="input-group">
					<label for="username">ユーザー名</label> <input type="text"
						name="username" id="username" required placeholder="お名前">
				</div>

				<div class="input-group">
					<label for="password">パスワード</label> <input type="password"
						name="password" id="password" required placeholder="数字４桁">
				</div>

				<button type="submit" class="login-button">ログイン</button>
			</form>

			<footer class="login-footer"> &copy; 2026 ねこまるカフェ System
				Management </footer>
		</div>
	</div>

</body>
</html>