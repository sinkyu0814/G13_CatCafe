<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品・オプションランキング</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
body {
	font-family: sans-serif;
	text-align: center;
	padding: 20px;
	background-color: #f4f7f6;
}

.container {
	width: 90%;
	max-width: 1000px;
	margin: auto;
	background: white;
	padding: 30px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.nav-btn {
	text-align: left;
	margin-bottom: 20px;
}

.filter-area {
	margin-bottom: 30px;
}

.filter-links {
	margin: 20px 0;
	display: flex;
	justify-content: center;
	gap: 10px;
}

.filter-links a {
	text-decoration: none;
	padding: 10px 25px;
	background: #fff;
	border: 1px solid #3498db;
	color: #3498db;
	border-radius: 25px;
	transition: 0.3s;
}

.filter-links a.active {
	background: #3498db;
	color: #fff;
}

.filter-box {
	margin-top: 15px;
	background: #f8f9fa;
	padding: 15px;
	border-radius: 8px;
	border: 1px solid #eee;
	display: inline-block;
}

.no-data-box {
	background: #fff5f5;
	border: 1px solid #feb2b2;
	color: #c53030;
	padding: 40px;
	margin-top: 30px;
	border-radius: 8px;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 30px;
}

th, td {
	border-bottom: 1px solid #eee;
	padding: 15px;
}

th {
	background: #f8f9fa;
	color: #555;
}

.chart-container {
	position: relative;
	height: 320px;
	width: 100%;
	margin-top: 20px;
}
</style>
</head>
<body>
	<div class="container">
		<div class="nav-btn">
			<form action="AdminServlet" method="get">
				<button type="submit">← 管理画面に戻る</button>
			</form>
		</div>

		<h1>ランキング分析</h1>

		<div class="filter-area">
			<form action="RankingServlet" method="get">
				<div class="filter-links">
					<a href="RankingServlet?period=day&filterType=${selectedFilter}"
						class="${currentPeriod == 'day' ? 'active' : ''}">本日</a> <a
						href="RankingServlet?period=week&filterType=${selectedFilter}"
						class="${currentPeriod == 'week' ? 'active' : ''}">1週間</a> <a
						href="RankingServlet?period=month&filterType=${selectedFilter}"
						class="${currentPeriod == 'month' ? 'active' : ''}">1ヶ月</a> <a
						href="RankingServlet?period=year&filterType=${selectedFilter}"
						class="${currentPeriod == 'year' ? 'active' : ''}">1年</a>
				</div>

				<input type="hidden" name="period" value="${currentPeriod}">
				<div class="filter-box">
					<strong>集計対象：</strong> <select name="filterType"
						onchange="this.form.submit()"
						style="padding: 5px 10px; cursor: pointer;">
						<option value="all" ${selectedFilter == 'all' ? 'selected' : ''}>商品（セット含む）</option>
						<option value="item" ${selectedFilter == 'item' ? 'selected' : ''}>商品のみ</option>
						<option value="option"
							${selectedFilter == 'option' ? 'selected' : ''}>オプションのみ</option>
					</select>
				</div>
			</form>
		</div>

		<c:choose>
			<c:when test="${empty rankingList}">
				<div class="no-data-box">
					<p>
						<strong>${periodLabel}のデータは見つかりませんでした。</strong>
					</p>
				</div>
			</c:when>
			<c:otherwise>
				<p>${periodLabel}の総
					${selectedFilter == 'option' ? 'オプション' : '注文'} 数: <strong>${totalCount}</strong>
				</p>
				<div class="chart-container">
					<canvas id="rankingChart"></canvas>
				</div>
				<table>
					<thead>
						<tr>
							<th>順位</th>
							<th>${selectedFilter == 'option' ? 'オプション名' : '商品名'}</th>
							<th>注文数</th>
							<th>比率</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${rankingList}" varStatus="s">
							<tr>
								<td>${s.index + 1}</td>
								<td>${item.goodsName}</td>
								<td>${item.orderCount}</td>
								<td>${item.ratio}%</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:otherwise>
		</c:choose>
	</div>

	<script>
        const rankingData = [
            <c:forEach var="i" items="${rankingList}">
                { name: '${i.goodsName}', count: ${i.orderCount} },
            </c:forEach>
        ];

        if (rankingData.length > 0) {
            const ctx = document.getElementById('rankingChart').getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: rankingData.map(d => d.name),
                    datasets: [{
                        label: '注文数',
                        data: rankingData.map(d => d.count),
                        backgroundColor: '${selectedFilter == 'option' ? '#e67e22' : '#3498db'}',
                        borderRadius: 5
                    }]
                },
                options: { 
                    responsive: true, 
                    maintainAspectRatio: false,
                    scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
                }
            });
        }
    </script>
</body>
</html>