function getCategory(id) {
    $.ajax({
        url: "/v1/category/id=" + id,
        success: function(result) {
            var menu = JSON.parse(result);
            menu.forEach(function(element) {
                addMainCategory(element, 'menu')
            });
        }
    });
}
