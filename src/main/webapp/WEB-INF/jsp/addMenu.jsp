<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>メニュー管理</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/addMenu.css">
</head>
<body>

	<h2>メニュー追加</h2>
	<form action="AddMenuServlet" method="post"
		enctype="multipart/form-data">
		名前 <input type="text" name="name"><br> 価格 <input
			type="number" name="price"><br> 数量 <input type="number"
			name="quantity"><br> カテゴリ <select name="category">
			<option value="Recomend">Recomend</option>
			<option value="Eat">Eat</option>
			<option value="Morning">Morning</option>
			<option value="Sweets">Sweets</option>
			<option value="Cofe">Cofe</option>
			<option value="Tea">Tea</option>
			<option value="SoftDrink">SoftDrink</option>
		</select><br> 画像 <input type="file" name="image"> <br>
		<button type="submit">追加</button>
	</form>

	<hr>

	<h2>メニュー一覧</h2>
	<table>
		<thead>
			<tr>
				<th>ID</th>
				<th>名前</th>
				<th>価格</th>
				<th>数量</th>
				<th>カテゴリ</th>
				<th>画像</th>
				<th>表示</th>
				<th>削除</th>
				<th>オプション管理</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="menu" items="${menuList}">
				<tr>
					<td>${menu.id}</td>
					<td><strong>${menu.name}</strong></td>
					<td>${menu.price}円</td>
					<td>${menu.quantity}</td>
					<td>${menu.category}</td>
					<td><c:if test="${not empty menu.img}">
							<img
								src="${pageContext.request.contextPath}/assets/images/${menu.img}"
								width="50">
						</c:if></td>
					<td>
						<form action="ToggleMenuServlet" method="post" style="margin: 0;">
							<input type="hidden" name="menuId" value="${menu.id}"> <input
								type="hidden" name="visible"
								value="${menu.isVisible == 1 ? 0 : 1}">
							<button type="submit">${menu.isVisible == 1 ? "非表示" : "表示"}</button>
						</form>
					</td>
					<td>
						<form action="DeleteMenuServlet" method="post"
							onsubmit="return confirm('メニューを削除しますか？');" style="margin: 0;">
							<input type="hidden" name="menuId" value="${menu.id}">
							<button type="submit">削除</button>
						</form>
					</td>

					<td style="min-width: 220px;">
						<div class="add-opt-box">
							<form action="AddMenuOptionServlet" method="post"
								style="margin: 0; display: flex; gap: 4px;">
								<input type="hidden" name="menuId" value="${menu.id}"> <input
									type="text" name="optionName" placeholder="名前"
									style="width: 80px;" required> <input type="number"
									name="optionPrice" placeholder="￥" style="width: 50px;"
									required>
								<button type="submit" style="font-size: 0.8em;">追加</button>
							</form>
						</div>

						<ul class="option-list">
							<c:forEach var="opt" items="${menu.options}">
								<li class="option-item"><span>${opt.optionName} <small>(+${opt.optionPrice})</small></span>
									<form action="DeleteMenuOptionServlet" method="post"
										style="margin: 0;"
										onsubmit="return confirm('${opt.optionName} を削除しますか？');">
										<input type="hidden" name="optionId" value="${opt.optionId}">
										<button type="submit" class="del-btn">×</button>
									</form></li>
							</c:forEach>
						</ul>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<hr>
	<form action="AdminServlet">
		<button type="submit">戻る</button>
	</form>

</body>
</html>