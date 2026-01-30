<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<fmt:setBundle basename="properties.messages" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><fmt:message key="label.product_confirm" /></title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/confirm.css">

<script>
	/**
	 * モーニングメニューのバリデーション
	 * カテゴリが「モーニング」の場合、オプションが1つ以上選択されているかチェックします。
	 */
	function validateMorningOption() {
		const category = document.querySelector('input[name="category"]').value;
		const checkboxes = document.querySelectorAll('input[name="optionIds"]');

		// カテゴリが「モーニング」であるか判定
		if (category === "Morning") {
			let isChecked = false;

			// 1つでもチェックされているか確認
			for (let i = 0; i < checkboxes.length; i++) {
				if (checkboxes[i].checked) {
					isChecked = true;
					break;
				}
			}

			if (!isChecked) {
				alert("モーニングメニューはセットオプション（ドリンク等）の選択が必須です。");
				return false; // 送信をブロック
			}
		}
		return true; // 送信を許可
	}
</script>
</head>
<body>
	<div class="container">
		<h2 class="page-title">
			<fmt:message key="label.product_confirm" />
		</h2>
		<div class="content-wrapper">
			<div class="product-image-area">
				<img src="GetImageServlet?id=${menu.id}" alt="${menu.name}">
			</div>
			<div class="product-details-area">
				<h3 class="product-name">${menu.name}</h3>
				<p class="product-price">
					<fmt:message key="label.price" />
					：<span>${menu.price}</span>
					<fmt:message key="label.unit_table" />
				</p>
				<p class="product-category">
					<fmt:message key="label.category" />
					：${menu.category}
				</p>

				<form action="CartAddServlet" method="post" class="add-to-cart-form"
					onsubmit="return validateMorningOption()">
					<input type="hidden" name="id" value="${menu.id}"> <input
						type="hidden" name="category" value="${menu.category}">

					<div class="quantity-selector">
						<label for="quantity"><fmt:message key="label.quantity" />：</label>
						<select name="quantity" id="quantity">
							<c:forEach var="i" begin="1" end="10">
								<option value="${i}">${i}</option>
							</c:forEach>
						</select>
					</div>

					<c:if test="${not empty options}">
						<h4 class="option-title">オプション（トッピング）</h4>
						<div class="options-list">
							<c:forEach var="opt" items="${options}">
								<label class="option-item"> <input type="checkbox"
									name="optionIds" value="${opt.optionId}"> <span
									class="option-name">${opt.optionName}</span> <span
									class="option-price">（+${opt.optionPrice}<fmt:message
											key="label.unit_table" />）
								</span>
								</label>
							</c:forEach>
						</div>
					</c:if>
					<button type="submit" class="add-to-cart-button">
						<fmt:message key="button.add_to_cart" />
					</button>
				</form>

				<form action="ListServlet" method="get" class="back-form">
					<input type="hidden" name="category" value="${menu.category}">
					<button type="submit" class="back-button">
						<fmt:message key="button.back" />
					</button>
				</form>
			</div>
		</div>
	</div>
</body>
</html>