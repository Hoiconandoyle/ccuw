// Khi tài liệu load xong sẽ load json bên dưới
$(document).ready(function () {

    let allProducts = []; // Lưu trữ toàn bộ sản phẩm

    // Bắt sự kiện thay đổi của thẻ select
    $("#categoryFilter").on("change", function () {
        let selectedCategory = $(this).val(); // Lấy giá trị được chọn trong bộ lọc

        filterProducts(selectedCategory); // Gọi hàm lọc sản phẩm dựa trên danh mục đã chọn
    });
    
    // Dùng jquery load json
    $.getJSON("products.json", function (products) {
        allProducts = products; // Gán toàn bộ sản phẩm vào biến allProducts

        // Hiển thị toàn bộ sản phẩm khi tải trang
        displayFilteredProducts(allProducts);
    });


    // Hàm lọc sản phẩm theo danh mục
    function filterProducts(category) {
        let filteredProducts;

        if (category === "all") {
            // Nếu người dùng chọn "All", hiển thị toàn bộ sản phẩm
            filteredProducts = allProducts;
        } else {
            // Lọc sản phẩm theo danh mục (hãng) được chọn
            filteredProducts = allProducts.filter(product => product.category.toLowerCase() === category.toLowerCase());
        }

        // Sau khi lọc, hiển thị lại danh sách sản phẩm
        displayFilteredProducts(filteredProducts);
    }

    // Hàm hiển thị sản phẩm (sau khi đã lọc hoặc hiển thị tất cả)
    function displayFilteredProducts(products) {
        $("#product-list").empty(); // Xóa dữ liệu cũ trước khi cập nhật

        let totalAllProduct = 0;

        if (products.length === 0) {
            // Nếu không có sản phẩm nào trong bộ lọc, hiển thị thông báo
            $("#total-price").text('0 đ');
            return; // Dừng hàm tại đây
        }

        // Duyệt qua danh sách sản phẩm đã lọc và hiển thị lại

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

        // Cập nhật tổng thành tiền cho tất cả sản phẩm đã lọc
        $("#total-price").text(totalAllProduct.toLocaleString('vi', { style: 'currency', currency: 'VND' }));
    }

    let jsonCart = [];


    // Update giỏ hàng lên view khi thêm
    function updateCart() {
        $("#cart-list").empty(); // Xóa dữ liệu cũ trước khi cập nhật

        let totalAllProduct = 0; // Lưu kết quả tổng tất cả sản phẩm trong giỏ

        if (jsonCart.length === 0) {
            totalAllProduct = 0;
            $("#total-price").text(totalAllProduct.toLocaleString('vi', { style: 'currency', currency: 'VND' }));
        }

        // Lặp từng phần từ trong cart
        $.each(jsonCart, function (i, product) {

            let sumPrice = product.price * product.quantity; // Tổng thành tiền

            // toLocaleString chuyển đơn vị tiền tệ từ 100000 -> 100.000 đ
            $("#cart-list").append(`
                <tr>
                    <td>${product.name}</td>
                    <td>${product.category}</td>
                    <td><button class="down-quantity btn btn-danger btn-sm" data-id="${product.id}"> - </button><span id ="quantity" class="m-3">${product.quantity}</span><button class="up-quantity btn btn-danger btn-sm" data-id="${product.id}"> + </button></td>
                    <td>${product.price.toLocaleString('vi', { style: 'currency', currency: 'VND' })}</td>
                    <td>${sumPrice.toLocaleString('vi', { style: 'currency', currency: 'VND' })}</td>
                    <td><button class="delete-product btn btn-danger btn-sm" data-id="${product.id}">Xóa</button></td>
                </tr>
            `);

            totalAllProduct += sumPrice; // Tính tổng tất cả sản phẩm

            $("#total-price").text(totalAllProduct.toLocaleString('vi', { style: 'currency', currency: 'VND' }));
        });
    }


    // Thêm vào giỏ. Sử dụng event để xử lý sự kiện click nút add-to-cart
    $("#product-list").on("click", ".add-to-cart", function () {
        let productId = $(this).data("id");
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
                category: productCategory,
                name: productName,
                price: productPrice,
                quantity: 1
            });
        }

        updateCart(); // Cập nhật lại giỏ hàng lên view 
    });


    // Xóa khỏi giỏ. Sử dụng event để xử lý sự kiện clicknút  delete
    $("#cart-list").on("click", ".delete-product", function () {
        let productId = $(this).data("id");

        jsonCart = jsonCart.filter(item => item.id != productId);

        updateCart(); // Cập nhật lại giỏ hàng lên view 
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
