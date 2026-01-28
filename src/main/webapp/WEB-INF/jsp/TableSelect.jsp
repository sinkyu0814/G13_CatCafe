<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.Map"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>店員用：席選択画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/TableSelect.css">
</head>
<body>
	<div class="header-title">
		<h1>席管理・会計</h1>
	</div>

	<div class="legend-container">
		<div class="legend-item">
			<span class="dot empty-dot"></span>空席
		</div>
		<div class="legend-item">
			<span class="dot occupied-dot"></span>食事中
		</div>
		<div class="legend-item">
			<span class="dot checkout-dot"></span>会計待ち
		</div>
	</div>

	<div class="refresh-status">
		<span id="timer-label">5</span>秒後に自動更新...
	</div>

	<form id="accountingForm" action="AccountingServlet" method="POST">
		<input type="hidden" id="selectedTable" name="tableNumber" value="">
		<input type="hidden" name="action" value="startAccounting">

		<%
		Map<Integer, Integer> tablePersonsMap = (Map<Integer, Integer>) request.getAttribute("tablePersonsMap");
		Map<Integer, String> tableStatusMap = (Map<Integer, String>) request.getAttribute("tableStatusMap");
		%>

		<div class="table-layout">
			<%
			for (int i = 1; i <= 25; i++) {
				String status = (tableStatusMap != null) ? tableStatusMap.get(i) : null;
				boolean isOccupied = (status != null);

				// クラス名と表示テキストの判定（元のロジックを維持）
				String tableClass = "table-unit empty";
				String statusText = "空席";

				if ("NEW".equals(status)) {
					tableClass = "table-unit occupied";
					statusText = (tablePersonsMap != null && tablePersonsMap.get(i) != null) ? tablePersonsMap.get(i) + "名" : "食事中";
				} else if ("CHECKOUT_REQUEST".equals(status)) {
					tableClass = "table-unit checkout-request";
					statusText = "会計待ち";
				}
			%>
			<div class="<%=tableClass%>" id="table-<%=i%>"
				onclick="selectTable('<%=i%>', <%=isOccupied%>)">
				<span class="table-number"><%=i%>番</span>
				<div class="status-label"><%=statusText%></div>
			</div>
			<%
			}
			%>
		</div>

		<div class="btn-container">
			<button type="button" class="btn-submit" onclick="submitForm()">会計を開始する</button>
		</div>
	</form>

	<div class="admin-section">
		<form action="ResetAllTablesServlet" method="POST"
			onsubmit="return confirmReset()">
			<button type="submit" class="btn-reset">【管理者】全テーブル一括リセット</button>
		</form>
	</div>

	<script>
        let isCurrentTableOccupied = false;
        let timeLeft = 5;
        let timerPaused = false;

        const timerElement = document.getElementById('timer-label');
        const refreshInterval = setInterval(() => {
            if (!timerPaused) {
                timeLeft--;
                timerElement.innerText = timeLeft;
                if (timeLeft <= 0) {
                    location.reload();
                }
            }
        }, 1000);

        function selectTable(id, occupied) {
            timerPaused = true; // 選択中はリロード停止
            timerElement.innerText = "停止中";

            document.querySelectorAll('.table-unit').forEach(el => {
                el.classList.remove('selected-table');
            });
            
            const target = document.getElementById('table-' + id);
            target.classList.add('selected-table');
            
            document.getElementById('selectedTable').value = id + "番";
            isCurrentTableOccupied = occupied;
        }

        function submitForm() {
            const table = document.getElementById('selectedTable').value;
            if (!table) { alert("テーブルを選択してください"); return; }
            if (!isCurrentTableOccupied) { alert("空席の会計はできません"); return; }
            document.getElementById('accountingForm').submit();
        }

        function confirmReset() {
            return confirm("すべてのテーブル情報をリセットしますか？");
        }
    </script>
</body>
</html>