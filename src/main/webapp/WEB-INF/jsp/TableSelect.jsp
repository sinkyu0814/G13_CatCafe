<%-- webapp/WEB-INF/jsp/TableSelect.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
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

		<div class="table-layout">
			<%-- テーブルを25卓分作成 --%>
			<%
			for (int i = 1; i <= 25; i++) {
			%>
			<div class="table-unit" id="table-<%=i%>"
				onclick="selectTable('<%=i%>番')">
				<%=i%>番
				<div class="status">○名</div>
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
			// 既存の選択を解除
			if (currentSelectedTable) {
				document.getElementById(currentSelectedTable).classList
						.remove('selected');
			}

			// 新しい選択を設定
			const tableElement = document.getElementById('table-'
					+ tableNum.replace('番', ''));
			tableElement.classList.add('selected');
			currentSelectedTable = 'table-' + tableNum.replace('番', '');

			// 隠しフィールドにテーブル番号をセット
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