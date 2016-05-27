$.getJSON("owl/menu.json", function(json) {
    console.log(json); // this will show the info it in firebug console
    var getMenuItem = function (itemData) {
        var item = $("<li class=\"dropdown\">")
            .append(
        $("<a>", {
            href: itemData.link,
            html: eval("itemData.name_" + document.documentElement.lang)
        }));
        if (itemData.sub) {
            var subList = $("<ul role=\"menu\" class=\"sub-menu\">");
            $.each(itemData.sub, function () {
                subList.append(getMenuItem(this));
            });
            item.append(subList);
        }
        return item;
    };

    var $menu = $("#menu");
    $.each(json.menu, function () {
        $menu.append(
            getMenuItem(this)
        );
    });
    $menu.menu();
});