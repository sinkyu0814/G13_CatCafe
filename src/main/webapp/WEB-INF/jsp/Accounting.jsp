<%@ page import="java.util.List, viewmodel.OrderItem"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>店員用：個別会計画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/Accounting.css">
</head>
<body>

	<%
    Integer orderId = (Integer) request.getAttribute("orderId");
    Integer tableNo = (Integer) request.getAttribute("tableNo");
    List<OrderItem> orderList = (List<OrderItem>) request.getAttribute("orderList");
    Integer totalAmount = (Integer) request.getAttribute("totalAmount");
    if (totalAmount == null) totalAmount = 0;
%>

	<div class="page-container">
		<header class="header-area">
			<h1>
				会計処理：<%=tableNo%>番テーブル
			</h1>
			<form action="TableSelectServlet" method="GET">
				<button type="submit" class="back-btn">← 席選択へ戻る</button>
			</form>
		</header>

		<div class="grid-container">
			<div class="order-panel">
				<div class="total-summary">
					<span class="label">合計金額</span> <span class="amount">¥ <%=String.format("%, d", totalAmount)%></span>
					<input type="hidden" id="totalAmountValue" value="<%=totalAmount%>">
				</div>

				<div class="table-wrapper">
					<table>
						<thead>
							<tr>
								<th>注文内容</th>
								<th>数量</th>
								<th style="text-align: right;">金額</th>
							</tr>
						</thead>
						<tbody>
							<% if (orderList != null && !orderList.isEmpty()) {
                            for (OrderItem item : orderList) { %>
							<tr>
								<td><strong><%=item.getName()%></strong> <% if (item.getSelectedOptions() != null && !item.getSelectedOptions().isEmpty()) { %>
									<ul
										style="margin: 5px 0; padding-left: 15px; font-size: 0.85em; color: #666;">
										<% for (model.dto.MenuOptionDTO opt : item.getSelectedOptions()) { %>
										<li>・<%=opt.getOptionName()%> (+¥<%=opt.getOptionPrice()%>)
										</li>
										<% } %>
									</ul> <% } %></td>
								<td><%=item.getQuantity()%></td>
								<td style="text-align: right;">¥ <%=item.getPrice() + item.getOptionTotalPrice()%>
									<% if (item.getQuantity() > 1) { %>
									<div style="font-size: 0.75em; color: #999;">
										(小計 ¥
										<%=(item.getPrice() + item.getOptionTotalPrice()) * item.getQuantity()%>)
									</div> <% } %>
								</td>
							</tr>
							<% } } else { %>
							<tr>
								<td colspan="3">注文データがありません</td>
							</tr>
							<% } %>
						</tbody>
					</table>
				</div>
			</div>

			<div class="accounting-panel">
				<div class="amount-input-area">
					<div class="input-group">
						<span class="label">預り金額（入力してください）</span> <input type="text"
							id="depositInput" readonly value="0">
					</div>
					<div class="change-area">
						<span class="label" style="color: #bdc3c7;">おつり</span> <span
							id="changeDisplay">¥ 0</span>
					</div>
				</div>

				<div class="keypad">
					<button type="button" onclick="inputNumber(7)">7</button>
					<button type="button" onclick="inputNumber(8)">8</button>
					<button type="button" onclick="inputNumber(9)">9</button>
					<button type="button" onclick="inputNumber(4)">4</button>
					<button type="button" onclick="inputNumber(5)">5</button>
					<button type="button" onclick="inputNumber(6)">6</button>
					<button type="button" onclick="inputNumber(1)">1</button>
					<button type="button" onclick="inputNumber(2)">2</button>
					<button type="button" onclick="inputNumber(3)">3</button>
					<button type="button" onclick="inputNumber(0)">0</button>
					<button type="button" onclick="inputNumber('00')">00</button>
					<button type="button" class="clear" onclick="clearInput()">C</button>
				</div>

				<form id="finishAccountingForm" action="AccountingServlet"
					method="POST">
					<input type="hidden" name="orderId" value="<%=orderId%>"> <input
						type="hidden" name="totalAmount" id="hiddenTotalAmount"
						value="<%=totalAmount%>"> <input type="hidden"
						name="deposit" id="hiddenDeposit" value="0">
					<button type="button" id="finishButton"
						class="finish-btn btn-disabled" onclick="submitFinishAccounting()">会計を完了する</button>
				</form>
			</div>
		</div>
	</div>

	<script>
		function inputNumber(num) {
			const depositInput = document.getElementById('depositInput');
			let current = depositInput.value;
			if (current === "0") {
				depositInput.value = (num === '00') ? "0" : num;
			} else {
				depositInput.value = current + num;
			}
			updateChange();
		}

		function clearInput() {
			document.getElementById('depositInput').value = "0";
			updateChange();
		}

		function updateChange() {
			const total = parseInt(document.getElementById('totalAmountValue').value) || 0;
			const deposit = parseInt(document.getElementById('depositInput').value) || 0;
			const finishButton = document.getElementById('finishButton');
			const changeDisplay = document.getElementById('changeDisplay');
			const hiddenDeposit = document.getElementById('hiddenDeposit');

			const change = deposit - total;
			changeDisplay.innerText = "¥ " + change.toLocaleString();
			hiddenDeposit.value = deposit;

			if (deposit >= total) {
				finishButton.classList.remove('btn-disabled');
				finishButton.classList.add('btn-ready');
				finishButton.disabled = false;
			} else {
				finishButton.classList.remove('btn-ready');
				finishButton.classList.add('btn-disabled');
				finishButton.disabled = true;
			}
		}

		function submitFinishAccounting() {
			if (confirm("会計を確定し、レシートを発行します。よろしいですか？")) {
				document.getElementById('finishAccountingForm').submit();
			}
		}
	</script>
</body>
</html>