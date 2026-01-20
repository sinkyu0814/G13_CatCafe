/**
 * Ranking.js：自律型描画スクリプト
 */
document.addEventListener('DOMContentLoaded', function() {
	// 1. HTMLに埋め込まれたデータと要素を取得
	const dataElement = document.getElementById('rankingDataJson');
	const configElement = document.getElementById('rankingConfig');
	const canvasElement = document.getElementById('rankingChart');

	// 要素が存在しない（データがない）場合は何もしない
	if (!dataElement || !canvasElement) {
		return;
	}

	try {
		// テキスト情報をJSONとしてパース
		const rankingData = JSON.parse(dataElement.textContent.trim());
		const barColor = configElement ? configElement.getAttribute('data-bar-color') : '#3498db';

		if (rankingData.length === 0) return;

		// 2. Chart.js を使用してグラフを描画
		const ctx = canvasElement.getContext('2d');
		new Chart(ctx, {
			type: 'bar',
			data: {
				labels: rankingData.map(d => d.name),
				datasets: [{
					label: '注文数',
					data: rankingData.map(d => d.count),
					backgroundColor: barColor,
					borderRadius: 5
				}]
			},
			options: {
				responsive: true,
				maintainAspectRatio: false,
				scales: {
					y: {
						beginAtZero: true,
						ticks: { stepSize: 1 }
					}
				}
			}
		});
	} catch (e) {
		console.error("ランキングデータの解析に失敗しました:", e);
	}
});