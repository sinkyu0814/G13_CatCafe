<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<fmt:setBundle basename="messages" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><fmt:message key="label.welcome" /></title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/FirstWindow.css">
<script>
	let activeInput = null;
	function setActive(id) {
		document.getElementById('persons').classList.remove('active-field');
		activeInput = document.getElementById(id);
		activeInput.classList.add('active-field');
	}
	function press(num) {
		if (!activeInput)
			return;
		activeInput.value += num;
	}
	function clearInput() {
		if (activeInput)
			activeInput.value = "";
	}
	function backspace() {
		if (!activeInput)
			return;
		activeInput.value = activeInput.value.slice(0, -1);
	}
	window.onload = function() {
		setActive('persons');
	};
</script>
</head>

<body>
	<div class="container">
		<header class="welcome-header">
			<%-- 言語選択セレクトボックス --%>
			<div class="lang-switcher"
				style="text-align: right; margin-bottom: 10px;">
				<select name="lang" form="orderForm"
					style="padding: 5px; border-radius: 5px; cursor: pointer;">
					<option value="ja">日本語 / Japanese</option>
					<option value="en">English / 英語</option>
				</select>
			</div>

			<h1 class="shop-name">
				<fmt:message key="label.shop_name" />
			</h1>
			<p class="welcome-msg">
				<fmt:message key="label.welcome" />
				<br>
				<fmt:message key="label.enter_persons" />
			</p>
		</header>

		<form action="ToppageServlet" method="post" class="input-form"
			id="orderForm">
			<div class="input-area">
				<div class="input-group">
					<label for="persons"><fmt:message key="label.persons" /></label>
					<div class="field-wrapper" id="wrapper-persons">
						<input type="text" name="persons" id="persons" readonly
							onclick="setActive('persons')" value="${param.persons}"
							placeholder="0"> <span class="unit"><fmt:message
								key="label.unit_persons" /></span>
					</div>
				</div>

				<div class="input-group display-only">
					<label><fmt:message key="label.table_no" /></label>
					<div class="field-wrapper readonly-wrapper">
						<input type="text" value="${sessionScope.fixedTableNo}" readonly
							placeholder="未設定"> <span class="unit">番</span>
					</div>
					<input type="hidden" name="tableNo"
						value="${sessionScope.fixedTableNo}">
				</div>
			</div>

			<div class="keypad-container">
				<div class="keypad">
					<button type="button" onclick="press(1)">1</button>
					<button type="button" onclick="press(2)">2</button>
					<button type="button" onclick="press(3)">3</button>
					<button type="button" onclick="press(4)">4</button>
					<button type="button" onclick="press(5)">5</button>
					<button type="button" onclick="press(6)">6</button>
					<button type="button" onclick="press(7)">7</button>
					<button type="button" onclick="press(8)">8</button>
					<button type="button" onclick="press(9)">9</button>
					<button type="button" class="btn-clear" onclick="clearInput()">C</button>
					<button type="button" onclick="press(0)">0</button>
					<button type="button" class="btn-back" onclick="backspace()">←</button>
				</div>
				<button type="submit" class="start-button">
					<fmt:message key="label.start_order" />
				</button>
			</div>
		</form>

		<c:if test="${not empty error}">
			<div class="error-banner">${error}</div>
		</c:if>
	</div>
</body>
</html>