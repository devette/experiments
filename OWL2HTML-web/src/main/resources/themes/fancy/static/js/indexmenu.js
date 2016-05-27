$.getJSON("owl/menu.json", function(json) {
    console.log(json); // this will show the info it in firebug console

    var getMenuItem = function (itemData) {
        var item = $("<li>")
            .append(
        $("<a>", {
            href: itemData.link,
            html: eval("itemData.name_" + document.documentElement.lang)
        }));
        if (itemData.sub) {
            var subList = $("<ul>");
            $.each(itemData.sub, function () {
                subList.append(getMenuItem(this));
            });
            item.append(subList);
        }
        return item;
    };

    var $menu = $("#indexmenu");
    $.each(json.menu, function () {
        $menu.append(
            getMenuItem(this)
        );
    });
    $menu.menu();
});