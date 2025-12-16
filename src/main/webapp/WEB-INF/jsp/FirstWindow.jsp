<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>人数・テーブル入力</title>

<style>
.keypad {
	display: grid;
	grid-template-columns: repeat(3, 80px);
	gap: 10px;
	margin-top: 20px;
}

.keypad button {
	font-size: 20px;
	padding: 20px;
}

input[type=text] {
	font-size: 20px;
	width: 120px;
	text-align: right;
}
</style>

<script>
	let activeInput = null;

	function setActive(id) {
		activeInput = document.getElementById(id);
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
</script>

</head>
<body>

	<h1>ねこまるカフェ</h1>

	<form action="ToppageServlet" method="post">

		<p>
			人数： <input type="text" name="persons" id="persons" readonly
				onclick="setActive('persons')">
		</p>

		<p>
			テーブル番号： <input type="text" name="tableNo" id="tableNo" readonly
				onclick="setActive('tableNo')">
		</p>

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

			<button type="button" onclick="clearInput()">C</button>
			<button type="button" onclick="press(0)">0</button>
			<button type="button" onclick="backspace()">←</button>
		</div>

		<br>
		<button type="submit">注文開始</button>

	</form>

</body>
</html>
