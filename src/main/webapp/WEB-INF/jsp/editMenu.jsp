<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>メニュー編集</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/addMenu.css">
</head>
<body>
	<div class="admin-container">
		<header class="admin-header">
			<h1>メニュー編集 (ID: ${menu.id})</h1>
		</header>

		<section class="form-section card">
			<form action="EditMenuServlet" method="post"
				enctype="multipart/form-data" class="horizontal-form">
				<input type="hidden" name="id" value="${menu.id}">

				<div class="input-field">
					<label>商品名</label> <input type="text" name="name"
						value="${menu.name}" required>
				</div>
				<div class="input-field">
					<label>価格 (円)</label> <input type="number" name="price"
						value="${menu.price}" required>
				</div>
				<div class="input-field">
					<label>在庫数量</label> <input type="number" name="quantity"
						value="${menu.quantity}" required>
				</div>
				<div class="input-field">
					<label>カテゴリ</label> <select name="category">
						<c:forEach var="cat"
							items="Recomend,Eat,Morning,Sweets,Cofe,Tea,SoftDrink">
							<option value="${cat}" ${menu.category == cat ? 'selected' : ''}>${cat}</option>
						</c:forEach>
					</select>
				</div>
				<div class="input-field">
					<label>画像 (変更する場合のみ)</label> <input type="file" name="image">
					<p style="font-size: 0.8em; color: #666;">
						※現在の画像：<img src="GetImageServlet?id=${menu.id}"
							style="height: 30px; vertical-align: middle;">
					</p>
				</div>

				<div style="display: flex; gap: 10px;">
					<button type="submit" class="primary-btn">更新保存</button>
					<button type="button" class="back-btn"
						onclick="location.href='AddMenuServlet'">キャンセル</button>
				</div>
			</form>
		</section>
	</div>
</body>
</html>