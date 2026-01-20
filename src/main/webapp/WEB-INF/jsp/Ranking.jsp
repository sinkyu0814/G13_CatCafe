<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品・オプションランキング</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/Ranking.css">
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

				<div id="rankingDataJson" style="display: none;">
					[
					<c:forEach var="i" items="${rankingList}" varStatus="status">
                            {"name": "${i.goodsName}", "count": ${i.orderCount}}${not status.last ? ',' : ''}
                        </c:forEach>
					]
				</div>
				<div id="rankingConfig" style="display: none;"
					data-bar-color='${selectedFilter == "option" ? "#e67e22" : "#3498db"}'>
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

	<script src="${pageContext.request.contextPath}/assets/js/Ranking.js"></script>
</body>
</html>