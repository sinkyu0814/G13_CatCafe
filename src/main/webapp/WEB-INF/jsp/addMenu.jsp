<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>メニュー管理</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/addMenu.css">
</head>
<body>

	<div class="admin-container">
		<header class="admin-header">
			<h1>メニュー管理</h1>
			<form action="AdminServlet">
				<button type="submit" class="back-btn">管理者メニューへ戻る</button>
			</form>
		</header>

		<section class="form-section card">
			<h2>新規メニュー追加</h2>
			<form action="AddMenuServlet" method="post"
				enctype="multipart/form-data" class="horizontal-form">
				<div class="input-field">
					<label>商品名</label> <input type="text" name="name" required>
				</div>
				<div class="input-field">
					<label>価格 (円)</label> <input type="number" name="price" required>
				</div>
				<div class="input-field">
					<label>初期数量</label> <input type="number" name="quantity"
						value="100">
				</div>
				<div class="input-field">
					<label>カテゴリ</label> <select name="category">
						<option value="Recomend">Recomend</option>
						<option value="Eat">Eat</option>
						<option value="Morning">Morning</option>
						<option value="Sweets">Sweets</option>
						<option value="Cofe">Cofe</option>
						<option value="Tea">Tea</option>
						<option value="SoftDrink">SoftDrink</option>
					</select>
				</div>
				<div class="input-field">
					<label>画像</label> <input type="file" name="image">
				</div>
				<button type="submit" class="primary-btn">追加</button>
			</form>
		</section>

		<section class="list-section card">
			<h2>登録済みメニュー一覧</h2>
			<div class="table-wrapper">
				<table class="admin-table">
					<thead>
						<tr>
							<th>ID</th>
							<th>商品名</th>
							<th>価格</th>
							<th>在庫</th>
							<th>カテゴリ</th>
							<th>画像</th>
							<th>操作</th>
							<th>オプション管理</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="menu" items="${menuList}">
							<tr>
								<td class="center">${menu.id}</td>
								<td><strong>${menu.name}</strong></td>
								<td class="right">${menu.price}円</td>
								<td class="center">${menu.quantity}</td>
								<td class="center"><span class="badge">${menu.category}</span></td>
								<td class="center"><img src="GetImageServlet?id=${menu.id}"
									class="thumb" alt="商品画像"></td>
								<td>
									<div class="action-btns">
										<%-- ★ 修正：編集ボタンをループ内の正しい場所に配置 --%>
										<a href="EditMenuServlet?id=${menu.id}"
											class="btn-sm btn-info"
											style="text-decoration: none; text-align: center; display: inline-block; line-height: 24px; margin-bottom: 5px;">編集</a>

										<form action="ToggleMenuServlet" method="post">
											<input type="hidden" name="menuId" value="${menu.id}">
											<input type="hidden" name="visible"
												value="${menu.isVisible == 1 ? 0 : 1}">
											<button type="submit"
												class="btn-sm ${menu.isVisible == 1 ? 'btn-warn' : 'btn-info'}">
												${menu.isVisible == 1 ? "非表示にする" : "表示する"}</button>
										</form>

										<form action="DeleteMenuServlet" method="post"
											onsubmit="return confirm('メニューを削除しますか？');">
											<input type="hidden" name="menuId" value="${menu.id}">
											<button type="submit" class="btn-sm btn-danger">削除</button>
										</form>
									</div>
								</td>
								<td>
									<div class="option-manager">
										<form action="AddMenuOptionServlet" method="post"
											class="opt-add-form">
											<input type="hidden" name="menuId" value="${menu.id}">
											<input type="text" name="optionName" placeholder="名前"
												required> <input type="number" name="optionPrice"
												placeholder="円" required>
											<button type="submit">+</button>
										</form>
										<ul class="opt-list">
											<c:forEach var="opt" items="${menu.options}">
												<li><span>${opt.optionName} <small>(+${opt.optionPrice})</small></span>
													<form action="DeleteMenuOptionServlet" method="post"
														onsubmit="return confirm('${opt.optionName} を削除しますか？');">
														<input type="hidden" name="optionId"
															value="${opt.optionId}">
														<button type="submit" class="opt-del-btn">×</button>
													</form></li>
											</c:forEach>
										</ul>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</section>
	</div>

</body>
</html>