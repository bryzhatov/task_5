function getProduct(id) {
    $.ajax({
        url: "/v1/product/?id=" + id,
        success: function(response) {
             var product = JSON.parse(response);
             $('#product_id').text(product.id);
             $('#product_name').text(product.name);
             $('#product_price').text(product.price+' $');
             $('#product_description').text(product.description);
             $('#product_image').attr('src', product.imageLink);
        }
    });
}
