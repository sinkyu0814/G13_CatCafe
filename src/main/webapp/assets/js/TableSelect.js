/**
 * TableSelect.jsp 専用：席選択とフォーム制御
 */
let currentSelectedTableId = null;

function confirmReset() {
	return confirm("すべてのテーブルの注文を終了し、空席に戻しますか？\nこの操作は取り消せません。");
}

function selectTable(tableNum) {
	// 以前の選択を解除
	if (currentSelectedTableId) {
		const prevElement = document.getElementById(currentSelectedTableId);
		if (prevElement) prevElement.classList.remove('selected');
	}

	// 新しい選択を適用
	const tableId = 'table-' + tableNum.replace('番', '');
	const tableElement = document.getElementById(tableId);

	if (tableElement) {
		tableElement.classList.add('selected');
		currentSelectedTableId = tableId;
		// 隠しフィールドに値をセット
		document.getElementById('selectedTable').value = tableNum;
	}
}

function submitForm() {
	const tableNumber = document.getElementById('selectedTable').value;
	if (tableNumber) {
		document.getElementById('accountingForm').submit();
	} else {
		alert('テーブルを選択してください。');
	}
}