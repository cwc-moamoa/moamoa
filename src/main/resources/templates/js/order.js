    $(document).ready(function() {
    // URL에서 상품 ID 가져오기
    var productId = new URLSearchParams(window.location.search).get('productId');
    $('#productId').val(productId);

    // 주문 생성 폼 제출 시
    $('#orderForm').submit(function(event) {
    event.preventDefault(); // 폼 기본 동작 방지

    // 폼 데이터를 객체로 구성
    var formData = {
    userId: $('#userId').val(),
    productId: $('#productId').val(),
    quantity: $('#quantity').val(),
    address: $('#address').val(),
    phoneNumber: $('#phoneNumber').val()
};

    // 서버로 POST 요청 보내기
    $.ajax({
    type: 'POST',
    url: '/api/orders/create',
    data: formData, // 객체 형태로 전송
    success: function(response) {
    alert('주문이 성공적으로 생성되었습니다!');
    // 주문 생성 성공 시 결제 페이지로 이동
    var orderUid = response.orderUid;
    window.location.href = "/payment/" + orderUid;
},
    error: function(xhr, status, error) {
    alert('주문 생성에 실패하였습니다.');
    // 주문 생성 실패 시 처리할 로직 추가 가능
}
});
});
});
