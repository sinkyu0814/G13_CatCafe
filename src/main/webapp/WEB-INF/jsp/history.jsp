<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>shinkyu13-history</title>
<link rel="stylesheet" href="./webapp/assets/history.css">
<style type="text/css">
@charset "UTF-8";
/* --------------------------------------
基本リセットとフォント設定
-------------------------------------- */
body {
    font-family: sans-serif;
    margin: 0;
    padding: 20px;
    background-color: #f0f0f0;
}

/* --------------------------------------
ヘッダーエリア
-------------------------------------- */
.page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid #ccc;
    margin-bottom: 20px;
}

.page-header h1 {
    font-size: 24px;
    margin: 0;
}

.page-header button {
    padding: 5px 15px;
    border: 1px solid #ccc;
    background-color: #eee;
    cursor: pointer;
}

/* --------------------------------------
メインコンテンツ (左右レイアウト)
-------------------------------------- */
.main-content {
    display: flex;
    gap: 20px;
}

/* --------------------------------------
左側: 商品リスト (スクロールエリア)
-------------------------------------- */
.scrollable-area {
    /* 画面全体の幅の約60%を占める */
    flex-grow: 1;
    /* スクロールさせるための高さ指定 */
    max-height: 450px; 
    /* 縦方向にスクロール可能にする */
    overflow-y: auto; 
    padding-right: 15px;
}

.item-list-table {
    width: 100%;
    border-collapse: collapse;
    border: none;
}

.item-list-table th, 
.item-list-table td {
    padding: 10px;
    text-align: left;
    border-bottom: 1px solid #ddd;
}

.item-list-table thead tr {
    /* ヘッダー行の背景色 (画像上部の灰色) */
    background-color: #ccc;
}

.item-list-table tbody tr {
    /* データ行の背景色 */
    background-color: #fff;
}

.item-list-table td {
    color: #666;
}


/* --------------------------------------
右側: サマリーエリア (固定エリア)
-------------------------------------- */
.summary-container {
    width: 300px; 
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    gap: 15px; /* 各ボックス間の間隔 */
}

/* 栄養素合計ボックス */
.nutrition-summary-box {
    /* 合計金額ボックスより大きく見せるため flex-grow: 1 を適用 */
    flex-grow: 1; 
    min-height: 150px; /* 最小高さを確保 */
    padding: 10px;
    background-color: #eee;
    border: 1px solid #ccc;
}

/* 合計金額ボックス */
.total-price-box {
    height: 60px; /* 高さ固定 */
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: flex-end;
    padding: 10px;
    background-color: #eee;
    border: 1px solid #ccc;
}

.summary-label {
    font-weight: bold;
    margin-bottom: 5px;
}

.total-price-value {
    font-size: 18px;
    font-weight: bold;
}

/* アクションボタンエリア */
.action-buttons {
    display: flex;
    flex-direction: column;
    gap: 10px; /* ボタン間の間隔 */
    margin-top: auto; /* 親コンテナの底に配置 */
}



.action-buttons button {
    padding: 10px 0;
    background-color: #eee;
    border: 1px solid #ccc;
    cursor: pointer;
    font-size: 16px;
    text-align: center;
}

/* history.jsp の <style> タグ内に追加 */
.action-buttons a.checkout-button {
    /* action-buttons button のスタイルをコピー */
    display: block; /* リンクをブロック要素にして幅いっぱいに広げる */
    padding: 10px 0;
    background-color: #eee;
    border: 1px solid #ccc;
    cursor: pointer;
    font-size: 16px;
    text-align: center;
    text-decoration: none; /* 下線を消す */
    color: inherit; /* テキスト色を親要素から継承 */
}
</style>

</head>
<body>
 
	<header class="page-header">
    	<a>注文履歴</a>
    	<button>閉じる</button>
	</header>

	 <div class="main-content">

        <div class="scrollable-area">
            <table class="item-list-table">
                <thead>
                    <tr>
                        <th style="width: 10%;">画像</th>
                        <th style="width: 50%;">商品名</th>
                        <th style="width: 20%;">値段</th>
                        <th style="width: 20%;">注文数</th>
                    </tr>
                </thead>
                <tbody>
                	<%-- Servletから渡されたリスト 'orderItems' をJSTLでループして表示 --%>
                	<c:forEach var="item" items="${orderItems}">
                    	<tr>
                    		<td>画像</td>
                    		<td>${item.name}</td>
                    		<td>${item.price}</td>
                    		<td>${item.quantity}個</td>
                    	</tr>
                	</c:forEach>
                </tbody>
            </table>
        </div>

        <div class="summary-container">
            
            <div class="nutrition-summary-box">
                <span class="summary-label">栄養素合計</span>
                <%-- Servletから渡された栄養素データを表示 --%>
                <p>カロリー: ${nutritionData.calories}</p>
                <p>タンパク質: ${nutritionData.protein}</p>
			</div>

            <div class="total-price-box">
                <span class="summary-label">合計金額</span>
                <%-- Servletから渡された合計金額を表示 --%>
                <span class="total-price-value">${totalAmount}円</span>
            </div>
            
            <div class="action-buttons">
    			<button class="call-button">店員呼び出し</button>
    
    			<form action="CheckServlet" method="GET" style="margin: 0; padding: 0; width: 100%;">
        			<button type="submit" class="checkout-button">会計</button>
    			</form>
			</div>
        </div>
        
    </div>
</body>
</html>