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

	// 入力欄を選択した時の処理
	function setActive(id) {
		// 全ての入力欄からactiveクラスを外す
		document.getElementById('persons').classList.remove('active-field');
		document.getElementById('tableNo').classList.remove('active-field');

		// 選択されたフィールドをactiveにする
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

	// 初期状態で「人数」を選択状態にする
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
				いらっしゃいませ！<br>人数とテーブル番号を入力してください。
			</p>
		</header>

		<form action="ToppageServlet" method="post" class="input-form">
			<div class="input-area">
				<div class="input-group">
					<label for="persons">ご来店人数</label>
					<div class="field-wrapper">
						<input type="text" name="persons" id="persons" readonly
							onclick="setActive('persons')" value="${param.persons}"
							placeholder="0"> <span class="unit">名様</span>
					</div>
				</div>

				<div class="input-group">
					<label for="tableNo">テーブル番号</label>
					<div class="field-wrapper">
						<input type="text" name="tableNo" id="tableNo" readonly
							onclick="setActive('tableNo')" value="${param.tableNo}"
							placeholder="0"> <span class="unit">番</span>
					</div>
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