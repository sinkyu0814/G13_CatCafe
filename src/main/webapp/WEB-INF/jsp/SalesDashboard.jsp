<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>売上閲覧システム</title>
<style>
/* 全体レイアウト */
body {
	font-family: "Helvetica Neue", Arial, "Hiragino Kaku Gothic ProN",
		"Hiragino Sans", Meiryo, sans-serif;
	margin: 0;
	background: #fff;
	color: #333;
}

/* ヘッダーエリア（パンくずとボタンを横並びに） */
.header-nav {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 15px 25px;
}

.breadcrumb {
	border: 1px solid #000;
	padding: 4px 12px;
	font-size: 13px;
	background: #f9f9f9;
}

/* 戻るボタンの装飾 */
.nav-btn button {
	background: #666;
	color: #fff;
	border: none;
	padding: 6px 15px;
	border-radius: 4px;
	cursor: pointer;
	font-size: 13px;
	transition: 0.2s;
}

.nav-btn button:hover {
	background: #333;
}

.main-wrap {
	padding: 0 25px;
}

/* タブデザイン */
.tabs {
	display: flex;
	margin-bottom: -1px;
	padding-left: 10px;
}

.tab {
	padding: 8px 35px;
	background: #e0e0e0;
	border: 1px solid #999;
	text-decoration: none;
	color: #333;
	margin-right: 10px;
	font-size: 14px;
	border-top-left-radius: 4px;
	border-top-right-radius: 4px;
}

.tab.active {
	background: #cccccc;
	border-bottom: 1px solid #cccccc;
	font-weight: bold;
}

/* レポート表示エリア（灰色背景） */
.report-bg {
	background-color: #cccccc;
	border: 1px solid #999;
	padding: 25px;
	min-height: 600px;
	box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1);
}

/* フィルター操作エリア */
.filter-area {
	margin-bottom: 20px;
	display: flex;
	align-items: center;
	gap: 15px;
}

.filter-box {
	background: #fff;
	border: 1px solid #999;
	padding: 5px 15px;
	display: inline-block;
	border-radius: 3px;
}

.filter-box select {
	border: none;
	outline: none;
	font-size: 16px;
	cursor: pointer;
	padding: 2px;
}

.filter-label {
	font-size: 14px;
	font-weight: bold;
}

/* テーブルデザイン */
.sales-table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 10px;
}

.sales-table th {
	border: 1px solid #000;
	background: #d9d9d9;
	padding: 10px;
	font-weight: bold;
	text-align: center;
	font-size: 14px;
}

.sales-table td {
	border: 1px solid #000;
	padding: 8px;
	vertical-align: middle;
}

/* 数値入力風の白枠デザイン */
.white-box {
	background: white;
	border: 1px solid #999;
	height: 28px;
	display: flex;
	align-items: center;
	padding: 0 10px;
	width: 90%;
	margin: 0 auto;
	font-size: 15px;
	font-family: monospace;
}

.text-right {
	justify-content: flex-end;
}

.text-center {
	justify-content: center;
}

/* 文字色設定 */
.zero-data {
	color: #999 !important;
}

.sat-color {
	color: #0000ff !important;
	font-weight: bold;
}

.sun-color {
	color: #ff0000 !important;
	font-weight: bold;
}
</style>
</head>
<body>
	<div class="header-nav">
		<div class="breadcrumb">ホーム ＞ 売上閲覧</div>
		<div class="nav-btn">
			<form action="AdminServlet" method="get">
				<button type="submit">← 管理画面に戻る</button>
			</form>
		</div>
	</div>

	<div class="main-wrap">
		<div class="tabs">
			<a href="SalesServlet?type=year"
				class="tab ${currentType == 'year' ? 'active' : ''}">年間</a> <a
				href="SalesServlet?type=month"
				class="tab ${currentType == 'month' ? 'active' : ''}">月間</a> <a
				href="SalesServlet?type=day"
				class="tab ${currentType == 'day' ? 'active' : ''}">日間</a>
		</div>

		<div class="report-bg">
			<form action="SalesServlet" method="get" id="filterForm">
				<input type="hidden" name="type" value="${currentType}">
				<div class="filter-area">
					<span class="filter-label">表示範囲：</span>
					<div class="filter-box">
						<c:choose>
							<c:when test="${currentType == 'year'}">
								<span>全期間集計</span>
							</c:when>
							<c:when test="${currentType == 'month'}">
								<select name="year" onchange="this.form.submit()">
									<c:forEach var="y" begin="2020" end="2026">
										<option value="${y}" ${selectedYear == y ? 'selected' : ''}>${y}年</option>
									</c:forEach>
								</select>
							</c:when>
							<c:otherwise>
								<select name="year" onchange="this.form.submit()">
									<c:forEach var="y" begin="2020" end="2026">
										<option value="${y}" ${selectedYear == y ? 'selected' : ''}>${y}年</option>
									</c:forEach>
								</select>
								<select name="month" onchange="this.form.submit()">
									<c:forEach var="m" begin="1" end="12">
										<fmt:formatNumber var="mm" value="${m}" pattern="00" />
										<option value="${mm}" ${selectedMonth == mm ? 'selected' : ''}>${m}月</option>
									</c:forEach>
								</select>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</form>

			<table class="sales-table">
				<thead>
					<tr>
						<th style="width: 20%;">日付</th>
						<th style="width: 25%;">売上合計(オプション込)</th>
						<th style="width: 25%;">来店人数合計</th>
						<th style="width: 25%;">一人当たり単価</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="s" items="${salesList}">
						<c:set var="dowClass" value="" />
						<c:if test="${currentType == 'day'}">
							<c:choose>
								<c:when test="${s.dayOfWeek == 6}">
									<c:set var="dowClass" value="sat-color" />
								</c:when>
								<c:when test="${s.dayOfWeek == 7}">
									<c:set var="dowClass" value="sun-color" />
								</c:when>
							</c:choose>
						</c:if>

						<tr class="${s.totalAmount == 0 ? 'zero-data' : ''} ${dowClass}">
							<td><div class="white-box text-center ${dowClass}">${s.displayDate}</div></td>
							<td><div class="white-box text-right ${dowClass}">
									¥
									<fmt:formatNumber value="${s.totalAmount}" pattern="#,###" />
								</div></td>
							<td><div class="white-box text-right ${dowClass}">${s.visitorCount}
									名</div></td>
							<td><div class="white-box text-right ${dowClass}">
									¥
									<fmt:formatNumber value="${s.getAveragePrice()}"
										pattern="#,###" />
								</div></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>