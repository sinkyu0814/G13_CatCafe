<%-- webapp/WEB-INF/jsp/AccountingComplete.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.text.NumberFormat"%>
<html>
<head>
<title>会計完了</title>
<style>
body {
	font-family: Arial, sans-serif;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	height: 100vh;
	margin: 0;
	background-color: #f0f0f0;
}

.complete-box {
	background-color: white;
	padding: 40px;
	border-radius: 8px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	text-align: center;
	width: 420px;
}

h1 {
	color: #2ecc71;
}

.amount-detail {
	margin-top: 20px;
	text-align: left;
	font-size: 1.1em;
}

.amount-detail div {
	margin-bottom: 8px;
}

.highlight {
	font-weight: bold;
	color: #333;
}

button {
	padding: 15px 30px;
	font-size: 1.2em;
	margin-top: 30px;
	cursor: pointer;
	background-color: #3498db;
	color: white;
	border: none;
	border-radius: 5px;
}

button:hover {
	background-color: #2980b9;
}
</style>
</head>
<body>
	<%
	Integer tableNo = (Integer) request.getAttribute("tableNo");
	Integer totalAmount = (Integer) request.getAttribute("totalAmount");
	Integer deposit = (Integer) request.getAttribute("deposit");
	Integer change = (Integer) request.getAttribute("change");

	NumberFormat nf = NumberFormat.getNumberInstance();
	%>

	<div class="complete-box">
		<h1>✅ お会計が完了しました</h1>

		<div class="amount-detail">
			<div>
				テーブル番号： <span class="highlight"> <%=tableNo != null ? tableNo : "-"%>
				</span>
			</div>

			<hr>

			<div>
				合計金額： <span class="highlight"> <%=nf.format(totalAmount != null ? totalAmount : 0)%>
				</span> 円
			</div>

			<div>
				預り金： <span class="highlight"> <%=nf.format(deposit != null ? deposit : 0)%>
				</span> 円
			</div>

			<hr>

			<div>
				おつり： <span class="highlight" style="font-size: 1.3em;"> <%=nf.format(change != null ? change : 0)%>
				</span> 円
			</div>
		</div>

		<button onclick="location.href='TableSelectServlet'">
			席選択画面に戻る</button>
	</div>

</body>
</html>
