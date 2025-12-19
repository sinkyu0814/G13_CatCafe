<%-- webapp/WEB-INF/jsp/TableSelect.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.Map"%>

<html>
<head>
<title>席選択画面</title>
<style>
.table-layout {
	display: grid;
	grid-template-columns: repeat(5, 1fr);
	gap: 10px;
	width: 800px;
	margin: 50px auto;
}

.table-unit {
	background-color: #e0e0e0;
	padding: 20px;
	text-align: center;
	cursor: pointer;
	border: 2px solid transparent;
	font-size: 1.2em;
}

.selected {
	border: 2px solid #3498db;
	background-color: #c0e0f0;
}

.status {
	margin-top: 5px;
	font-size: 0.9em;
	color: #333;
}

button {
	padding: 10px 20px;
	margin: 5px;
}
</style>
</head>

<body>
	<div style="width: 800px; margin: auto;">
		<h1>席選択</h1>
	</div>

	<form id="accountingForm" action="AccountingServlet" method="POST">
		<input type="hidden" id="selectedTable" name="tableNumber" value="">
		<input type="hidden" name="action" value="startAccounting">

		<%
			Map<Integer, Integer> tablePersonsMap =
				(Map<Integer, Integer>) request.getAttribute("tablePersonsMap");
		%>

		<div class="table-layout">
			<%
			for (int i = 1; i <= 25; i++) {

				String statusText = "空席";

				if (tablePersonsMap != null && tablePersonsMap.containsKey(i)) {
					int p = tablePersonsMap.get(i);
					if (p > 0) {
						statusText = p + "名";
					} else {
						statusText = "？名";
					}
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

		<div style="text-align: center;">
			<button type="button" onclick="submitForm()">会計開始</button>
		</div>
	</form>

	<script>
		let currentSelectedTable = null;

		function selectTable(tableNum) {
			if (currentSelectedTable) {
				document.getElementById(currentSelectedTable).classList
						.remove('selected');
			}

			const tableId = 'table-' + tableNum.replace('番', '');
			const tableElement = document.getElementById(tableId);

			tableElement.classList.add('selected');
			currentSelectedTable = tableId;

			document.getElementById('selectedTable').value = tableNum;
		}

		function submitForm() {
			const tableNumber = document.getElementById('selectedTable').value;
			if (tableNumber) {
				document.getElementById('accountingForm').submit();
			} else {
				alert('テーブルを選択してください。');
			}
		}
	</script>
</body>
</html>
