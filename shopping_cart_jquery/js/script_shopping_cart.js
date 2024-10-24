// Khi tài liệu load xong sẽ load json bên dưới
$(document).ready(function () {
    // Dùng jquery load json
    $.each(products, function(i, product) {
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
