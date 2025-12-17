<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>checkout</title>
<style>
body {
	font-family: sans-serif;
	text-align: center;
	padding: 20px;
}

.header-area {
	display: flex;
	justify-content: space-between;
	align-items: flex-start;
	width: 100%;
	margin-bottom: 50px; /* HACK: 調整 */
}

.title {
	font-size: 30px;
	font-weight: bold;
	margin-left: 20px;
	flex-grow: 1;
	text-align: left;
}

.close-button-container {
	padding: 0;
}

.close-button {
	border: 1px solid #ccc;
	background-color: #f0f0f0;
	padding: 5px 10px;
	cursor: pointer;
	font-size: 14px;
}

.thank-you {
	font-size: 20px;
	margin-top: 50px;
	margin-bottom: 50px;
}

.table-number-line {
	font-size: 20px;
	margin-bottom: 50px;
}

.table-number-box {
	display: inline-block;
	background-color: #e0e0e0; /* テーブル番号の背景色 */
	padding: 5px 15px; /* テーブル番号の余白 */
	border: 1px solid #ccc;
	margin: 0 5px;
}

.instruction {
	font-size: 20px;
}
/* .caption のCSS定義は、今後別の用途で使う可能性もあるため残しておきますが、
     不要であればこの部分も削除して構いません。 */
.caption {
	font-size: 10px;
	text-align: left;
	color: #888;
}
</style>
</head>
<body>

	<div class="header-area">
		<div class="title">ねこまるカフェ</div>
		<div class="close-button-container">
			<a href="${pageContext.request.contextPath}/ToppageServlet">
				<button class="close-button">閉じる</button>
			</a>
		</div>
	</div>

	<div class="thank-you">ありがとうございます。</div>

	<div class="table-number-line">
		テーブル番号 <span class="table-number-box"> ${ tableNo } </span> 番
	</div>

	<div class="instruction">レジにてテーブル番号をお伝えください。</div>
</body>
</html>