$( document ).ready(function() {
    $.ajax({
        url: "/v1/categories/",
        success: function(result) {
            var menu = JSON.parse(result);
            menu.forEach(function(element) {
            addMainCategory(element, 'menu')
            });
        }
    });
});

function addMainCategory(element, id) {

    if (element.childCategory.length) {
        var dropMenu = document.getElementById('dropMenu').cloneNode(true)
        dropMenu.childNodes[3].id = element.category.id;
        dropMenu.removeAttribute('hidden');
        dropMenu.childNodes[1].textContent = element.category.name;
        document.getElementById(id).appendChild(dropMenu);
    } else {
        document.getElementById(id).innerHTML += '<li><a href="#">' + element.category.name+'</a></li>';
    }

    addChildCategory(element);
}

function addChildCategory(element) {
    if (element.childCategory.length) {
        var childCategory = element.childCategory;

        childCategory.forEach(function(entry) {
            addMainCategory(entry, element.category.id);
        });
    }
}
