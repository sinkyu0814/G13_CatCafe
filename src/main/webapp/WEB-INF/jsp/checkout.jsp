<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<fmt:setBundle basename="properties.messages" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><fmt:message key="label.thank_you" /></title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/checkout.css">
<script>
    function checkStatus() {
        fetch('${pageContext.request.contextPath}/CheckStatusServlet')
            .then(response => response.json())
            .then(data => {
                if (data.isCleared) {
                    window.location.href = '${pageContext.request.contextPath}/ToppageServlet';
                }
            })
            .catch(error => console.error('Error:', error));
    }
    setInterval(checkStatus, 3000);
</script>
</head>
<body>
	<div class="container">
		<header class="header-area">
			<div class="shop-name">
				<fmt:message key="label.shop_name" />
			</div>
		</header>

		<main class="main-message">
			<div class="icon-check">✔</div>
			<h2 class="thank-you">
				<fmt:message key="label.thank_you" />
			</h2>
			<div class="table-info">
				<p class="table-label">
					<fmt:message key="label.customer_table_no" />
				</p>
				<div class="table-number-line">
					<span class="table-number-box">${tableNo}</span>
					<fmt:message key="label.unit_table_no" />
				</div>
			</div>
			<div class="instruction-box">
				<p class="instruction">
					<fmt:message key="msg.go_to_register" />
					<br> <span><fmt:message key="msg.screen_will_change" /></span>
				</p>
			</div>
		</main>
	</div>
</body>
</html>