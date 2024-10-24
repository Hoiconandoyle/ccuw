// Khi tài liệu load xong sẽ load json bên dưới
$(document).ready(function () {
    
    // Dùng jquery load json
    $.getJSON("products.json", function(products) {

        // For each products trong json
        products.forEach(product => {
            $("#product-list").append(`
               <tr>
                   <td>${product.name}</td> 
                   <td>${product.category}</td>
                   <td>${product.price.toLocaleString('vi', { style: 'currency', currency: 'VND' })}</td>
                   <td><button class="add-to-cart btn btn-danger btn-sm" data-id="${product.id}" data-name="${product.name}" data-category="${product.category}" data-price="${product.price}">Thêm</button></td>
               </tr>
           `);
       });
    });

    // Biến lưu sản phẩm
    let jsonCart= [];

    function addToCart(productId, productCategory, productName, productPrice) {

        let validateId = jsonCart.find(item => item.id  === productId); 

        // Kiểm tra trong giỏ hàng đã tồn tại chưa, nếu chưa push vào jsonCart, ngược lại thêm số lượng
        if (validateId) {
            validateId.quantity += 1;
        } else {
            jsonCart.push({
                id: productId,
                category: productCategory,
                name: productName,
                price: productPrice,
                quantity: 1
            });
        }
    }

    // Update giỏ hàng lên view khi thêm
    function updateCart() {
        $("#cart-list").empty(); // Xóa dữ liệu cũ trước khi cập nhật
        let totalAllProduct = 0; // Lưu kết quả tổng tất cả sản phẩm trong giỏ
        let sumPrice = 0; // Tổng thành tiền
        $.each(jsonCart, function (i, item) {
            sumPrice += item.price * item.quantity; // thành tiền = số dượng * đơn giá
            
            // toLocaleString chuyển đơn vị tiền tệ từ 100000 -> 100.000 đ
            $("#cart-list").append(`
                <tr>
                    <td>${item.name}</td>
                    <td>${item.category}</td>
                    <td>${item.quantity}</td>
                    <td>${item.price.toLocaleString('vi', { style: 'currency', currency: 'VND' })}</td>
                    <td>${sumPrice.toLocaleString('vi', { style: 'currency', currency: 'VND' })}</td>
                </tr>
            `);

            totalAllProduct += sumPrice; // Tính tổng tất cả sản phẩm

            $("#total-price").text(totalAllProduct.toLocaleString('vi', { style: 'currency', currency: 'VND' }));
        });
    }

    // Sử dụng event delegation để xử lý sự kiện click trên các phần tử
    $("#product-list").on("click", ".add-to-cart", function () {
        let productId = $(this).data("id");
        let productCategory = $(this).data("category");
        let productName = $(this).data("name");
        let productPrice = $(this).data("price");

        addToCart(productId, productCategory, productName, productPrice);
        updateCart(); // Cập nhật lại giỏ hàng lên view 
    });

});
