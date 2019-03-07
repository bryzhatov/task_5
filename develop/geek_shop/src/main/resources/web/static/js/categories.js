function requestCategories(deep) {
    $.ajax({
        url: "/v1/categories/?deep="+deep,
        async: false,
        success: function(result) {
            var menu = JSON.parse(result);
            menu.forEach(function(element) {
            addMainCategory(element, 'menu')
            });
        }
    });
}

function addMainCategory(element, id) {

    if (element.childCategory) {
        var dropMenu = document.getElementById('dropMenu').cloneNode(true)
        dropMenu.childNodes[3].id = element.id;
        dropMenu.removeAttribute('hidden');
        dropMenu.childNodes[1].textContent = element.name;
        document.getElementById(id).appendChild(dropMenu);
    } else {
        document.getElementById(id).innerHTML += '<li><a href="#">' + element.name+'</a></li>';
    }

    addChildCategory(element);
}

function addChildCategory(element) {
    if (element.childCategory) {
        var childCategory = element.childCategory;

        childCategory.forEach(function(entry) {
            addMainCategory(entry, element.id);
        });
    }
}
