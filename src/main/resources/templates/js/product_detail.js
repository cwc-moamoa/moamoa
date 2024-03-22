    $(document).ready(function() {
    // URL에서 상품 ID 가져오기
    var urlParams = new URLSearchParams(window.location.search);
    var productId = urlParams.get('productId');

    // 상품 정보 가져오는 함수
    function getProductInfo(productId) {
    $.ajax({
    type: 'GET',
    url: '/products/' + productId,
    success: function(response) {
    // 상품 정보를 표시하는 부분 업데이트
    $('#productInfo').html('<p>상품명: ' + response.title + '</p>' +
    '<p>가격: ' + response.price + '</p>' +
    '<p>내용: ' + response.content + '</p>' +
    '<p>할인: ' + response.discount + "%" + '</p>' +
    '<p>평균 평점: ' + response.ratingAverage + '</p>' +
    '<p>좋아요 수: ' + response.likes + '</p>');
    $('#productImage').html('<img src="' + response.imageUrl + '" alt="Product Image">');
},
    error: function(xhr, status, error) {
    if (xhr.status == 404) {
    alert('상품을 찾을 수 없습니다.');
} else {
    alert('상품 정보를 불러오는 데 실패했습니다.');
}
}
});
}


    // 리뷰 정보 가져오는 함수
    function getReviews(productId) {
    $.ajax({
    type: 'GET',
    url: '/products/' + productId + '/reviews',
    success: function(response) {
    // 리뷰 정보를 표시
    var reviewsHtml = '<h2>상품 리뷰</h2>';
    $.each(response, function(index, review) {
    reviewsHtml += '<div class="review">';
    reviewsHtml += '<p><strong>작성자:</strong> ' + review.userName + '</p>';
    reviewsHtml += '<p><strong>제목:</strong> ' + review.title + '</p>';
    reviewsHtml += '<p><strong>평점:</strong> ' + review.rating + '</p>';
    reviewsHtml += '<p><strong>내용:</strong> ' + review.content + '</p>';
    reviewsHtml += '<p><strong>이미지:</strong> <img src="' + (review.imageUrl || 'https://via.placeholder.com/150') + '" alt="Review Image"></p>';
    reviewsHtml += '<p><strong>좋아요 수:</strong> ' + review.likes + '</p>';
    reviewsHtml += '</div>';
});
    $('#reviews').html(reviewsHtml);
},
    error: function(xhr, status, error) {
    if (xhr.status == 404) {
    $('#reviews').html('<p>리뷰 정보가 없습니다.</p>');
} else {
    $('#reviews').html('<p>리뷰 정보를 불러오는 데 실패했습니다.</p>');
}
}
});
}

    $(document).ready(function() {
    $('#submitReview').click(function() {
    // 폼에서 리뷰 정보 가져오기
    var reviewTitle = $('#reviewTitle').val();
    var reviewContent = $('#reviewContent').val();
    var rating = $('#rating').val();

    // 리뷰 정보를 JSON 형식으로 만듭니다.
    var reviewData = {
    title: reviewTitle,
    content: reviewContent,
    rating: rating
};

    // 서버로 AJAX 요청을 보냅니다.
    $.ajax({
    type: 'POST',
    url: '/reviews/' + productId, // productId를 사용하여 리뷰를 생성하는 엔드포인트에 접근합니다.
    contentType: 'application/json',
    data: JSON.stringify(reviewData),
    success: function(response) {
    // 리뷰가 성공적으로 생성되면 화면에 새로운 리뷰를 추가합니다.
    var newReviewHtml = '<div class="review">';
    newReviewHtml += '<p><strong>작성자:</strong> ' + response.userName + '</p>';
    newReviewHtml += '<p><strong>제목:</strong> ' + response.title + '</p>';
    newReviewHtml += '<p><strong>평점:</strong> ' + response.rating + '</p>';
    newReviewHtml += '<p><strong>내용:</strong> ' + response.content + '</p>';
    newReviewHtml += '</div>';
    $('#reviews').append(newReviewHtml);

    // 리뷰 작성 폼을 초기화합니다.
    $('#reviewTitle').val('');
    $('#reviewContent').val('');
    $('#rating').val('3');
},
    error: function(xhr, status, error) {
    // 오류가 발생하면 알림을 표시합니다.
    alert('리뷰를 생성하는 동안 오류가 발생했습니다.');
}
});
});
});



    $('#submitReview').click(function() {
    var reviewContent = $('#reviewContent').val();
});


    // 페이지 로드시 해당 상품 정보 가져오기
    getProductInfo(productId);
    getReviews(productId);

    // 주문하기 버튼 클릭 시
    $('.order-button').click(function() {
    window.location.href = "/order?productId=" + productId;
});
});
