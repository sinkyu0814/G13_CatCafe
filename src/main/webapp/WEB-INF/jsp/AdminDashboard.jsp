<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>店舗管理システム</title>
<style>
body {
	font-family: "Helvetica Neue", Arial, sans-serif;
	text-align: center;
	margin: 0;
	padding: 20px;
	background-color: #ffffff;
}

/* 時計部分のコンテナ：画像のような枠線と背景 */
.clock-outer-container {
	border: 2px solid #3498db;
	padding: 40px;
	margin: 20px auto;
	width: 90%;
	max-width: 800px;
	text-align: left; /* 「ホーム」ラベルのため */
}

.home-label {
	background-color: #ccc;
	padding: 2px 10px;
	font-size: 14px;
	border: 1px solid #999;
}

/* デジタル時計：グレーの背景ボックス */
.clock-box {
	background-color: #dcdcdc;
	display: inline-block;
	padding: 20px 40px;
	margin-top: 20px;
	font-size: 80px;
	font-family: "Courier New", Courier, monospace;
	letter-spacing: 15px;
	font-weight: bold;
	color: #000;
	min-width: 250px;
	text-align: center;
}

.quote-text {
	font-size: 32px;
	margin: 40px 0;
}

/* ボタンのレイアウト */
.button-grid {
	display: grid;
	grid-template-columns: repeat(2, 220px);
	gap: 20px;
	justify-content: center;
	margin-bottom: 40px;
}

.button-grid button, .button-grid input[type="submit"] {
	width: 100%;
	padding: 15px;
	font-size: 18px;
	background-color: #dcdcdc;
	border: 1px solid #bbb;
	cursor: pointer;
	color: #333;
}

.button-grid button:disabled {
	opacity: 0.6;
	cursor: not-allowed;
}
</style>
</head>
<body>

	<div class="clock-outer-container">
		<span class="home-label">ホーム</span>
		<div style="text-align: left; margin-left: 20px;">
			<div id="realTimeClock" class="clock-box">00:00</div>
		</div>
	</div>

	<div class="quote-text">An eye for an eye,a tooth for a tooth</div>

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

	<script>
		function updateClock() {
			const now = new Date();
			const hours = String(now.getHours()).padStart(2, '0');
			const minutes = String(now.getMinutes()).padStart(2, '0');

			// 画面上のテキストを更新
			const clockElement = document.getElementById('realTimeClock');
			if (clockElement) {
				clockElement.textContent = hours + ":" + minutes;
			}
		}

		// ページが読み込まれたら即実行
		window.onload = function() {
			updateClock();
			// 1秒(1000ミリ秒)ごとに updateClock を呼び出す
			setInterval(updateClock, 1000);
		};
	</script>

</body>
</html>