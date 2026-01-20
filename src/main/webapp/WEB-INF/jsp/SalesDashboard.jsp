<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>売上閲覧システム</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/SalesDashboard.css">
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
							<c:when test="${currentType == 'year'}">全期間集計</c:when>
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
</html>