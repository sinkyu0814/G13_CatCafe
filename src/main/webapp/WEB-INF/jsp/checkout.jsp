<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>お会計受付完了</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/checkout.css">
</head>
<body>

	<div class="container">
		<header class="header-area">
			<div class="shop-name">ねこまるカフェ</div>
			<div class="close-button-container">
				<a href="${pageContext.request.contextPath}/ToppageServlet">
					<button class="close-button">閉じる</button>
				</a>
			</div>
		</header>

		<main class="main-message">
			<div class="icon-check">✔</div>
			<h2 class="thank-you">ご利用ありがとうございました</h2>

			<div class="table-info">
				<p class="table-label">お客様のテーブル番号</p>
				<div class="table-number-line">
					<span class="table-number-box">${tableNo}</span> 番
				</div>
			</div>

			<div class="instruction-box">
				<p class="instruction">
					こちらの画面をそのままに、<br>
					<span>レジまでお越しください。</span>
				</p>
				<p class="sub-instruction">伝票（テーブル番号）を確認させていただきます。</p>
			</div>
		</main>
	</div>

</body>
</html>