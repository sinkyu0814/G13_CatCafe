<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.Map"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>席選択画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/TableSelect.css">
<style>
.error-message {
	color: white;
	background-color: #ff4d4d;
	padding: 10px;
	text-align: center;
	border-radius: 5px;
	margin: 10px auto;
	width: 80%;
}

.table-unit.occupied {
	background-color: #ffcccc;
	cursor: pointer;
}

.table-unit.empty {
	background-color: #e0e0e0;
	cursor: pointer;
}
</style>
</head>
<body>
	<div class="container-center">
		<h1>席選択</h1>
	</div>

	<%
	String error = (String) request.getAttribute("error");
	if (error != null) {
	%>
	<div class="error-message"><%=error%></div>
	<%
	}
	%>

	<form id="accountingForm" action="AccountingServlet" method="POST">
		<input type="hidden" id="selectedTable" name="tableNumber" value="">
		<input type="hidden" name="action" value="startAccounting">

		<%
		Map<Integer, Integer> tablePersonsMap = (Map<Integer, Integer>) request.getAttribute("tablePersonsMap");
		%>

		<div class="table-layout">
			<%
			for (int i = 1; i <= 25; i++) {
				boolean isOccupied = (tablePersonsMap != null && tablePersonsMap.containsKey(i));
				String statusText = isOccupied ? tablePersonsMap.get(i) + "名" : "空席";
				String tableClass = isOccupied ? "table-unit occupied" : "table-unit empty";
			%>
			<div class="<%=tableClass%>" id="table-<%=i%>"
				onclick="selectTable('<%=i%>', <%=isOccupied%>)">
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
			<button type="submit" class="btn-reset">【管理者用】全テーブルを強制空席にする</button>
		</form>
	</div>

	<script>
        let isCurrentTableOccupied = false;

        function selectTable(id, occupied) {
            // 選択状態の見た目をリセット
            document.querySelectorAll('.table-unit').forEach(el => {
                el.style.border = "1px solid #ccc";
                el.style.boxShadow = "none";
            });
            
            // 選択したテーブルを強調
            const target = document.getElementById('table-' + id);
            target.style.border = "3px solid #007bff";
            target.style.boxShadow = "0 0 10px rgba(0,123,255,0.5)";
            
            // 値をセット
            document.getElementById('selectedTable').value = id + "番";
            isCurrentTableOccupied = occupied;
        }

        function submitForm() {
            const table = document.getElementById('selectedTable').value;
            if (!table) {
                alert("テーブルを選択してください");
                return;
            }
            if (!isCurrentTableOccupied) {
                alert("選択された " + table + " は現在空席です。会計処理は行えません。");
                return;
            }
            document.getElementById('accountingForm').submit();
        }

        function confirmReset() {
            return confirm("本当にすべてのテーブルを空席にしますか？");
        }
    </script>
</body>
</html>