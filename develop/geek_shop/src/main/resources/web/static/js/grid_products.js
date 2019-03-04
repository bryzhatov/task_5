$( document ).ready(function() {
    $.ajax({
        url: "/v1/products/",
        async: false,
        success: function(result) {
            var menu = JSON.parse(result);
            menu.forEach(function(element) {

                addProductToGrid(element);

            });
        }
    });
});

function addProductToGrid(element) {
    var divProducts = document.getElementById('products');

    var templateProduct = divProducts.childNodes[1].cloneNode(true);
    templateProduct.id = element.id;
    console.log(element.id);
    templateProduct.removeAttribute('hidden');
    divProducts.appendChild(templateProduct);

    $('#'+element.id).find('#product_name').text(element.name);

    $('#'+element.id).find('#product_description').text(description);
}
