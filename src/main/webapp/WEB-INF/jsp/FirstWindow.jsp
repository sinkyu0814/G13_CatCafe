<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ご来店ありがとうございます</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/FirstWindow.css">
<script>
	let activeInput = null;

	function setActive(id) {
		// activeクラスの付け替え
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

	// 送信時のチェック
	//function checkSpecialCode(event) {
	//	const val = document.getElementById('persons').value;
	//	if (val === "2136") {
	//		event.preventDefault();
	//		location.href = "AdminTableSet.jsp"; // 店員用設定画面へ
	//		return false;
	//	}
	//	return true;
	//}

	window.onload = function() {
		setActive('persons');
	};
</script>
</head>

<body>
	<div class="container">
		<header class="welcome-header">
			<h1 class="shop-name">ねこまるカフェ</h1>
			<p class="welcome-msg">
				いらっしゃいませ！<br>ご来店人数を入力してください。
			</p>
		</header>

		<form action="ToppageServlet" method="post" class="input-form"
			onsubmit="return checkSpecialCode(event)">
			<div class="input-area">
				<div class="input-group">
					<label for="persons">ご来店人数</label>
					<div class="field-wrapper" id="wrapper-persons">
						<input type="text" name="persons" id="persons" readonly
							onclick="setActive('persons')" value="${param.persons}"
							placeholder="0"> <span class="unit">名様</span>
					</div>
				</div>

				<div class="input-group display-only">
					<label>テーブル番号</label>
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

				<button type="submit" class="start-button">注文を開始する</button>
			</div>
		</form>

		<c:if test="${not empty error}">
			<div class="error-banner">${error}</div>
		</c:if>
	</div>
</body>
</html>