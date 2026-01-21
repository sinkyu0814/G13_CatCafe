<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>お会計受付完了</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/checkout.css">
<script>
    // 3秒ごとに会計状態をチェックする関数
    function checkStatus() {
        // サーバーに「セッション(orderId)が消えたか」を確認しに行く
        fetch('${pageContext.request.contextPath}/CheckStatusServlet')
            .then(response => response.json())
            .then(data => {
                if (data.isCleared) {
                    // 店員が会計を確定（セッションクリア）させていたら、自動でトップページへ
                    window.location.href = '${pageContext.request.contextPath}/ToppageServlet';
                }
            })
            .catch(error => console.error('状態チェックエラー:', error));
    }

    // 画面を開いた後、3秒おきにチェックを実行
    setInterval(checkStatus, 3000);
</script>
</head>
<body>
	<div class="container">
		<header class="header-area">
			<div class="shop-name">ねこまるカフェ</div>
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
					そのままレジまでお越しください。<br> <span>お会計が完了すると画面が切り替わります。</span>
				</p>
			</div>
		</main>
	</div>
</body>
</html>