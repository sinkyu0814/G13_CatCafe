/**
 * AdminDashboard.js
 * リアルタイム時計更新ロジック
 */
function updateClock() {
	const now = new Date();
	const hours = String(now.getHours()).padStart(2, '0');
	const minutes = String(now.getMinutes()).padStart(2, '0');

	const clockElement = document.getElementById('realTimeClock');
	if (clockElement) {
		clockElement.textContent = hours + ":" + minutes;
	}
}

// ページの読み込みが完了したら実行
document.addEventListener('DOMContentLoaded', function() {
	updateClock();
	setInterval(updateClock, 1000);
});