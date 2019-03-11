function getProductsByRating(rating, count) {
    $.ajax({
        url: "/v1/products/?rating=" + rating + "&count="+count,
        success: function(result) {
            var menu = JSON.parse(result);

            menu.forEach(function(element) {
                addProductToGrid(element);
            });
        }
    });
}

function addProductToGrid(element) {
    var divProducts = document.getElementById('products');

    var templateProduct = divProducts.childNodes[1].cloneNode(true);
    templateProduct.id = element.id;
    templateProduct.removeAttribute('hidden');
    divProducts.appendChild(templateProduct);
    $('#'+element.id).find('#product_name').text(element.name);

    var $link = $('#'+element.id).find('#product_link');
    $link.attr('href', $link.attr('href') + element.id);

    $('#'+element.id).find('#product_price').text(element.price + ' $');
    $('#'+element.id).find('#product_description').text(element.description);
    $('#'+element.id).find('#product_image').attr('src', element.imageLink);
}
