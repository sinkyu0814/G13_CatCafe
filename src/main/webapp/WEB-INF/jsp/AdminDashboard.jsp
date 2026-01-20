<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>店舗管理システム</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/AdminDashboard.css">
</head>
<body>

	<div class="clock-outer-container">
		<span class="home-label">ホーム</span>
		<div style="text-align: left; margin-left: 20px;">
			<div id="realTimeClock" class="clock-box">00:00</div>
		</div>
	</div>

	<div class="quote-text">An eye for an eye, a tooth for a tooth</div>

	<div class="button-grid">
		<button disabled>勤怠画面</button>

		<form action="SalesServlet" method="get">
			<input type="submit" value="売上管理">
		</form>

		<button disabled>W/S登録</button>

		<form action="AddMenuServlet" method="get">
			<input type="submit" value="メニュー管理">
		</form>

		<button disabled>W/S表</button>

		<form action="RankingServlet" method="get">
			<input type="submit" value="商品ランキング">
		</form>
	</div>

	<div class="quote-text">The customer is always right!</div>

	<script
		src="${pageContext.request.contextPath}/assets/js/AdminDashboard.js"></script>
</body>
</html>