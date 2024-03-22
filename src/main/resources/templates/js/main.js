//     $(document).ready(function() {
//     // 상품 정보 가져오는 함수
//     function getProductInfo(productId) {
//         $.ajax({
//             type: 'GET',
//             url: '/products/' + productId,
//             success: function(response) {
//                 // 상품 정보를 표시하는 부분 업데이트
//                 $('#productInfo_' + productId).html('<p>상품명: ' + response.title + '</p>' +
//                     '<p>가격: ' + response.price + '</p>');
//             },
//             error: function(xhr, status, error) {
//                 if (xhr.status == 404) {
//                     alert('상품을 찾을 수 없습니다.');
//                 } else {
//                     alert('상품 정보를 불러오는 데 실패했습니다.');
//                 }
//             }
//         });
//     }

//     // 페이지 로드시 각 상품의 정보 가져오기
//     $('.product').each(function() {
//     var productId = $(this).attr('id').replace('product', '');
//     getProductInfo(productId);
// });
// });
