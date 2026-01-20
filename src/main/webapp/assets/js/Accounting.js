/* Accounting.jsp 専用ロジック */
document.addEventListener('DOMContentLoaded', function() {
	const depositInput = document.getElementById('depositInput');
	const changeDisplay = document.getElementById('changeDisplay');
	const totalAmountValue = parseInt(document.getElementById('totalAmountValue').value) || 0;
	const finishButton = document.getElementById('finishButton');

	// おつり計算
	function calculateChange() {
		const deposit = parseInt(depositInput.value) || 0;
		const change = deposit - totalAmountValue;

		if (change >= 0) {
			changeDisplay.textContent = change.toLocaleString();
			finishButton.disabled = (totalAmountValue === 0);
		} else {
			changeDisplay.textContent = '金額不足';
			finishButton.disabled = true;
		}
	}

	// 数値入力（ボタンのonclickから呼ぶためwindowに紐付け）
	window.inputNumber = function(num) {
		let current = depositInput.value === '0' ? '' : depositInput.value;
		if (current.length < 9) {
			depositInput.value = current + num;
		}
		calculateChange();
	};

	// 1文字クリアまたはリセット
	window.clearInput = function() {
		let current = depositInput.value;
		if (current.length > 1) {
			depositInput.value = current.slice(0, -1);
		} else {
			depositInput.value = '0';
		}
		calculateChange();
	};

	// フォーム送信バリデーション
	window.submitFinishAccounting = function() {
		const deposit = parseInt(depositInput.value) || 0;
		if (deposit >= totalAmountValue && totalAmountValue > 0) {
			document.getElementById('hiddenDeposit').value = deposit;
			document.getElementById('finishAccountingForm').submit();
		} else if (totalAmountValue === 0) {
			alert('会計する商品がありません。');
		} else {
			alert('預り金が不足しています。');
		}
	};

	// 初期化
	calculateChange();
});