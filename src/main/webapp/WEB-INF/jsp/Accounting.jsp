<%@ page import="java.util.List, viewmodel.OrderItem"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>個別会計画面</title>
<style>
body {
	font-family: Arial, sans-serif;
	display: flex;
	flex-direction: column;
	align-items: center;
}

.grid-container {
	display: grid;
	grid-template-columns: 2fr 1fr;
	width: 800px;
	gap: 20px;
	margin-top: 20px;
}

.order-panel {
	border: 1px solid #ccc;
	padding: 15px;
}

.keypad {
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 5px;
	margin-top: 10px;
}

.keypad button, #depositInput {
	padding: 10px;
	font-size: 1.2em;
}

.amount-info div {
	margin-bottom: 10px;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-bottom: 20px;
}

th, td {
	border: 1px solid #ddd;
	padding: 8px;
	text-align: left;
}
</style>
</head>
<body>
	<%
	// Servletからデータを受け取る
	String tableNum = (String) request.getAttribute("selectedTable");
	List<OrderItem> orderList = (List<OrderItem>) request.getAttribute("orderList");
	Integer totalAmount = (Integer) request.getAttribute("totalAmount");
	if (totalAmount == null)
		totalAmount = 0;
	%>

	<h1>個別会計画面</h1>
	<div
		style="width: 800px; display: flex; justify-content: space-between;">
		<h2><%=tableNum%>テーブル
		</h2>
		<%-- 戻るボタンでHistoryServletへ戻るように修正（履歴画面へ戻る） --%>
		<form action="TableSelectServlet" method="GET">
			<input type="hidden" name="tableNumber" value="<%=tableNum%>">
			<button type="submit">戻る</button>
		</form>
	</div>

	<div class="grid-container">
		<div class="order-panel">
			<h3>
				合計金額: <span id="totalAmountDisplay"><%=totalAmount%></span> 円
			</h3>
			<input type="hidden" id="totalAmountValue" value="<%=totalAmount%>">

			<table>
				<thead>
					<tr>
						<th>お品物</th>
						<th>数量</th>
						<th>単価</th>
					</tr>
				</thead>
				<tbody>
					<%
					if (orderList != null) {
						for (OrderItem item : orderList) {
					%>
					<tr>
						<td><%=item.getName()%></td>
						<%-- getGoodsName() -> getName() --%>
						<td><%=item.getQuantity()%></td>
						<td><%=item.getPrice()%> 円</td>
					</tr>
					<%
					}
					}
					%>
				</tbody>
			</table>
		</div>

		<div class="accounting-panel">
			<div class="amount-info">
				<div>
					預り金: <input type="text" id="depositInput" readonly value="0">
					円
				</div>
				<div>
					おつり: <span id="changeDisplay">0</span> 円
				</div>
			</div>

			<div class="keypad">
				<%
				for (int i = 1; i <= 9; i++) {
				%>
				<button onclick="inputNumber(<%=i%>)"><%=i%></button>
				<%
				}
				%>
				<button onclick="inputNumber(0)">0</button>
				<button onclick="clearInput()">×</button>
			</div>

			<form id="finishAccountingForm" action="AccountingServlet"
				method="POST">
				<input type="hidden" name="action" value="finishAccounting">
				<input type="hidden" name="tableNumber" value="<%=tableNum%>">
				<input type="hidden" name="totalAmount" value="<%=totalAmount%>">
				<input type="hidden" name="deposit" id="hiddenDeposit" value="0">

				<button type="button" id="finishButton"
					onclick="submitFinishAccounting()"
					style="width: 100%; margin-top: 10px;">会計終了</button>
			</form>
		</div>
	</div>

	<script>
    const depositInput = document.getElementById('depositInput');
    const changeDisplay = document.getElementById('changeDisplay');
    const totalAmountValue = parseInt(document.getElementById('totalAmountValue').value);
    const finishButton = document.getElementById('finishButton');

    function calculateChange() {
        const deposit = parseInt(depositInput.value) || 0;
        const total = totalAmountValue;
        let change = deposit - total;

        changeDisplay.textContent = change >= 0 ? change.toLocaleString() : '金額不足';
        
        // 金額が不足しているか、合計金額が0の場合はボタンを押せないようにする
        if (change < 0 || total === 0) {
            finishButton.disabled = true;
        } else {
            finishButton.disabled = false;
        }
    }
    
    function inputNumber(num) {
        let currentValue = depositInput.value === '0' ? '' : depositInput.value;
        if (currentValue.length < 9) { 
            depositInput.value = currentValue + num;
        }
        calculateChange();
    }

    function clearInput() {
        let currentValue = depositInput.value;
        if (currentValue.length > 1) {
            depositInput.value = currentValue.slice(0, -1);
        } else {
            depositInput.value = '0';
        }
        calculateChange();
    }
    
    function submitFinishAccounting() {
        const deposit = parseInt(depositInput.value) || 0;
        const total = totalAmountValue;
        
        if (deposit >= total && total > 0) {
            document.getElementById('hiddenDeposit').value = deposit;
            document.getElementById('finishAccountingForm').submit();
        } else if (total === 0) {
             alert('会計する商品がありません。');
        } else {
            alert('預り金が不足しています。');
        }
    }

    calculateChange(); // 初期化
</script>
</body>
</html>