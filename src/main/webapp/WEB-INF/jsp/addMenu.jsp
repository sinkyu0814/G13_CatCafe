<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
<title>メニュー管理</title>
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
		</select><br> 画像 <input type="file" name="image"><br>
		<button type="submit">追加</button>
	</form>

	<hr>

	<h2>メニュー一覧</h2>

	<table border="1">
		<tr>
			<th>ID</th>
			<th>名前</th>
			<th>価格</th>
			<th>数量</th>
			<th>カテゴリ</th>
			<th>画像</th>
			<th>表示</th>
			<th>削除</th>
			<th>オプション</th>
		</tr>

		<c:forEach var="menu" items="${menuList}">
			<tr>
				<td>${menu.id}</td>
				<td>${menu.name}</td>
				<td>${menu.price}</td>
				<td>${menu.quantity}</td>
				<td>${menu.category}</td>

				<td><c:if test="${not empty menu.img}">
						<img src="assets/images/${menu.img}" width="50">
					</c:if></td>

				<td>
					<form action="ToggleMenuServlet" method="post">
						<input type="hidden" name="menuId" value="${menu.id}"> <input
							type="hidden" name="visible"
							value="${menu.isVisible == 1 ? 0 : 1}">
						<button type="submit">${menu.isVisible == 1 ? "非表示" : "表示"}
						</button>
					</form>
				</td>

				<td>
					<form action="DeleteMenuServlet" method="post"
						onsubmit="return confirm('削除しますか？');">
						<input type="hidden" name="menuId" value="${menu.id}">
						<button type="submit">削除</button>
					</form>
				</td>

				<!-- ★ オプション管理 -->
				<td>
					<form action="AddMenuOptionServlet" method="post">
						<input type="hidden" name="menuId" value="${menu.id}"> <input
							type="text" name="optionName" placeholder="例：大盛り" required>
						<input type="number" name="optionPrice" placeholder="+100"
							required>
						<button type="submit">追加</button>
					</form> <c:forEach var="opt" items="${menu.options}">
						<div>
							${opt.optionName}(+${opt.optionPrice}円)
							<form action="DeleteMenuOptionServlet" method="post"
								style="display: inline">
								<input type="hidden" name="optionId" value="${opt.optionId}">
								<button type="submit">×</button>
							</form>
						</div>
					</c:forEach>
				</td>
			</tr>
		</c:forEach>
	</table>

	<hr>

	<form action="AdminServlet">
		<button>戻る</button>
	</form>

</body>
</html>
