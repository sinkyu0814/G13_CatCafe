<%@ page import="java.util.List, viewmodel.OrderItem"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>個別会計画面</title>
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

	<h1>個別会計画面</h1>

	<div
		style="width: 800px; display: flex; justify-content: space-between;">
		<h2>
			テーブル
			<%=tableNo%>（注文ID:
			<%=orderId%>）
		</h2>

		<form action="TableSelectServlet" method="GET">
			<input type="hidden" name="orderId" value="<%=orderId%>">
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
						<th>商品名</th>
						<th>数量</th>
						<th>金額（単価+オプション）</th>
					</tr>
				</thead>
				<tbody>
					<% if (orderList != null && !orderList.isEmpty()) {
                        for (OrderItem item : orderList) { %>
					<tr>
						<td><strong><%=item.getName()%></strong> <% if (item.getSelectedOptions() != null && !item.getSelectedOptions().isEmpty()) { %>
							<ul
								style="list-style: none; padding: 0; margin: 5px 0 0 10px; font-size: 0.85em; color: #666;">
								<% for (model.dto.MenuOptionDTO opt : item.getSelectedOptions()) { %>
								<li>・<%=opt.getOptionName()%> (+<%=opt.getOptionPrice()%>円)
								</li>
								<% } %>
							</ul> <% } %></td>
						<td><%=item.getQuantity()%></td>
						<td><%=item.getPrice() + item.getOptionTotalPrice()%> 円 <% if (item.getQuantity() > 1) { %>
							<div style="font-size: 0.8em; color: #999;">
								(小計:
								<%= (item.getPrice() + item.getOptionTotalPrice()) * item.getQuantity() %>
								円)
							</div> <% } %></td>
					</tr>
					<% } } else { %>
					<tr>
						<td colspan="3">注文がありません</td>
					</tr>
					<% } %>
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
				<% for (int i = 1; i <= 9; i++) { %>
				<button type="button" onclick="inputNumber(<%=i%>)"><%=i%></button>
				<% } %>
				<button type="button" onclick="inputNumber(0)">0</button>
				<button type="button" onclick="clearInput()">×</button>
			</div>

			<form id="finishAccountingForm" action="AccountingServlet"
				method="POST">
				<input type="hidden" name="orderId" value="<%=orderId%>"> <input
					type="hidden" name="totalAmount" value="<%=totalAmount%>">
				<input type="hidden" name="deposit" id="hiddenDeposit" value="0">

				<button type="button" id="finishButton"
					onclick="submitFinishAccounting()"
					style="width: 100%; margin-top: 10px;">会計終了</button>
			</form>
		</div>
	</div>

	<script
		src="${pageContext.request.contextPath}/assets/js/Accounting.js"></script>
</body>
</html>