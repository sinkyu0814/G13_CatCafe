<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.Map"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>席選択画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/TableSelect.css">
</head>
<body>
	<div class="container-center">
		<h1>席選択</h1>
	</div>

	<form id="accountingForm" action="AccountingServlet" method="POST">
		<input type="hidden" id="selectedTable" name="tableNumber" value="">
		<input type="hidden" name="action" value="startAccounting">

		<%
        Map<Integer, Integer> tablePersonsMap = (Map<Integer, Integer>) request.getAttribute("tablePersonsMap");
        %>

		<div class="table-layout">
			<%
            for (int i = 1; i <= 25; i++) {
                String statusText = "空席";
                if (tablePersonsMap != null && tablePersonsMap.containsKey(i)) {
                    int p = tablePersonsMap.get(i);
                    statusText = (p > 0) ? p + "名" : "？名";
                }
            %>
			<div class="table-unit" id="table-<%=i%>"
				onclick="selectTable('<%=i%>番')">
				<%=i%>番
				<div class="status"><%=statusText%></div>
			</div>
			<%
            }
            %>
		</div>

		<div class="btn-container">
			<button type="button" onclick="submitForm()">会計開始</button>
		</div>
	</form>

	<div class="admin-section">
		<form action="ResetAllTablesServlet" method="POST"
			onsubmit="return confirmReset()">
			<button type="submit" class="btn-reset">【管理者用】全テーブルを強制空席にする
			</button>
		</form>
	</div>

	<script
		src="${pageContext.request.contextPath}/assets/js/TableSelect.js"></script>
</body>
</html>