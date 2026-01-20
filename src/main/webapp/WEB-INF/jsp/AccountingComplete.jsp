<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.text.NumberFormat"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会計完了</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/AccountingComplete.css">
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
				おつり： <span class="highlight change-amount"> <%=nf.format(change != null ? change : 0)%>
				</span> 円
			</div>
		</div>

		<button type="button" onclick="location.href='TableSelectServlet'">
			席選択画面に戻る</button>
	</div>
</body>
</html>