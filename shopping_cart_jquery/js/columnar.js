// Khi tài liệu load xong sẽ load json bên dưới
$(document).ready(function () {
    // Dùng jquery load json
    $.getJSON("products.json", function (products) {

        // lặp từng phần tử trong json
        products.forEach(product => {
            $("#product-list").append(`
                <div class="col-md-3 col-sm-6 col-lg-3 p-2">
                    <div class="card" style="width: 16rem;">
                        <img src="${product.img}" class="card-img-top" alt="img_product">
                        <div class="card-body p-2">
                            <p class="card-text">${product.name}</p>
                            <p class="card-text">${product.category}</p>
                            <p class="card-text">${product.price.toLocaleString('vi', { style: 'currency', currency: 'VND' })}</p>
                            <button class="add-to-cart btn btn-danger btn-sm" data-id="${product.id}" data-img="${product.img}" data-name="${product.name}" data-category="${product.category}" data-price="${product.price}">Thêm</button>
                        </div>
                    </div>
                </div>
            `);
        });
    });


    let jsonCart = [];

    function updateCart() {
        $("#cart-list").empty(); // Xóa dữ liệu cũ trước khi cập nhật

        let totalAllProduct = 0; // Lưu kết quả tổng tất cả sản phẩm trong giỏ

        if (jsonCart.length === 0) {
            totalAllProduct = 0;
            $("#total-price").text(totalAllProduct.toLocaleString('vi', { style: 'currency', currency: 'VND' }));
        }

        // Lặp từng phần từ trong cart
        jsonCart.forEach(function (product) {
            let sumPrice = product.price * product.quantity; // Tổng thành tiền

            // toLocaleString chuyển đơn vị tiền tệ từ 100000 -> 100.000 đ
            $("#cart-list").append(`
                <div class="col-md-3 col-sm-6 col-lg-3 p-2">
                    <div class="card" style="width: 16rem;">
                        <img src="${product.img}" class="card-img-top" alt="img_product">
                        <div class="card-body p-2">
                            <p class="card-text">${product.name}</p>
                            <p class="card-text">${product.category}</p>
                            <p class="card-text">${product.price.toLocaleString('vi', { style: 'currency', currency: 'VND' })}</p>
                            <button class="down-quantity btn btn-danger btn-sm" data-id="${product.id}"> - </button><span id ="quantity" class="m-3">${product.quantity}</span><button class="up-quantity btn btn-danger btn-sm" data-id="${product.id}"> + </button>

                            <button class="delete-product btn btn-danger btn-sm" data-id="${product.id}">Xóa</button>
                        </div>
                    </div>
                </div>
            `);

            totalAllProduct += sumPrice; // Tính tổng tất cả sản phẩm

            $("#total-price").text(totalAllProduct.toLocaleString('vi', { style: 'currency', currency: 'VND' }));
        });
    }

    $("#product-list").on("click", ".add-to-cart", function () {
        let productId = $(this).data("id");
        let productImg = $(this).data("img");
        let productCategory = $(this).data("category");
        let productName = $(this).data("name");
        let productPrice = $(this).data("price");

        let validateId = jsonCart.find(item => item.id === productId);

        // Kiểm tra trong giỏ hàng đã tồn tại chưa, nếu chưa push vào jsonCart, ngược lại thêm số lượng
        if (validateId) {
            validateId.quantity += 1;
        } else {
            jsonCart.push({
                id: productId,
                img: productImg,
                category: productCategory,
                name: productName,
                price: productPrice,
                quantity: 1
            });
        }

        updateCart(); // // Cập nhật lại giỏ hàng lên view 

        // alert("Đã thêm sản phẩm thành công");
    });

    // Xóa khỏi giỏ. Sử dụng event để xử lý sự kiện clicknút  delete
    $("#cart-list").on("click", ".delete-product", function () {
        let productId = $(this).data("id");

        jsonCart = jsonCart.filter(item => item.id != productId);
        
        updateCart(); // Cập nhật lại giỏ hàng lên view 
        // alert("Đã xoá sản phẩm thành công");
    });

    // Giảm số lượng ... 
    $("#cart-list").on("click", ".down-quantity", function () {
        let productId = $(this).data("id");

        // Phương thức map() trả về một mảng mới
        jsonCart = jsonCart.map(item => {
            if (item.quantity > 1 && item.id === productId) {
                item.quantity -= 1; // Giảm số lượng sản phẩm
            }

            return item; // trả về item
        });

        updateCart(); // Cập nhật lại giỏ hàng lên view 
    });

    // Tăng số lượng ...
    $("#cart-list").on("click", ".up-quantity", function () {
        let productId = $(this).data("id"); 

        jsonCart = jsonCart.map(item => {
            if (item.id === productId) {
                item.quantity += 1; // Tăng số lượng sản phẩm
            }
            return item;
        });
        updateCart();

    });
});