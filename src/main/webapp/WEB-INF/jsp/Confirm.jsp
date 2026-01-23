<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注文内容確認</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/confirm.css">
</head>
<body>

	<div class="container">
		<h2 class="page-title">商品確認</h2>

		<div class="content-wrapper">

			<div class="product-image-area">
				<img
					src="${pageContext.request.contextPath}/assets/images/${menu.img}"
					alt="${menu.name}">
			</div>

			<div class="product-details-area">
				<h3 class="product-name">${menu.name}</h3>
				<p class="product-price">
					価格：<span>${menu.price}</span>円
				</p>
				<p class="product-category">カテゴリ：${menu.category}</p>

				<%-- ★onsubmitを追加し、JSでバリデーションを行うように修正 --%>
				<form action="CartAddServlet" method="post" class="add-to-cart-form"
					onsubmit="return validateMorningOption()">
					<%-- 商品IDとカテゴリ情報をhiddenで送信 --%>
					<input type="hidden" name="id" value="${menu.id}"> <input
						type="hidden" name="category" value="${param.category}">

					<div class="quantity-selector">
						<label for="quantity">数量：</label> <select name="quantity"
							id="quantity">
							<c:forEach var="i" begin="1" end="10">
								<option value="${i}">${i}</option>
							</c:forEach>
						</select>
					</div>

					<c:if test="${not empty options}">
						<h4 class="option-title">
							オプション（トッピング）
							<%-- ★モーニングの時だけ「必須」と表示してユーザーに知らせる --%>
							<c:if test="${menu.category == 'Morning'}">
								<span style="color: red; font-size: 0.8em; margin-left: 10px;">【必須：1つ以上選択】</span>
							</c:if>
						</h4>
						<div class="options-list">
							<c:forEach var="opt" items="${options}">
								<label class="option-item"> <input type="checkbox"
									name="optionIds" value="${opt.optionId}"> <span
									class="option-name">${opt.optionName}</span> <span
									class="option-price">（+${opt.optionPrice}円）</span>
								</label>
							</c:forEach>
						</div>
					</c:if>

					<button type="submit" class="add-to-cart-button">カートに追加する</button>
				</form>

				<form action="ListServlet" method="get" class="back-form">
					<input type="hidden" name="category" value="${param.category}">
					<button type="submit" class="back-button">戻る</button>
				</form>
			</div>
		</div>
	</div>

	<%-- ★ モーニング専用のオプションチェックJS --%>
	<script>
		function validateMorningOption() {
			// JSPのEL式を使って、サーバー側のカテゴリ名を取得
			const category = "${menu.category}";

			// モーニングカテゴリの場合のみチェックを実行
			if (category === "Morning") {
				// name="optionIds" を持つチェックボックスの中から、チェックされているものを取得
				const selectedOptions = document
						.querySelectorAll('input[name="optionIds"]:checked');

				// 1つも選ばれていない場合
				if (selectedOptions.length === 0) {
					alert("モーニングをご注文の場合は、セット内容（トッピングなど）を必ず1つ以上選択してください。");
					return false; // formの送信をキャンセル
				}
			}
			return true; // 送信を実行
		}
	</script>

</body>
</html>