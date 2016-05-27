<!DOCTYPE html>
<html lang="${session.context.locale.language}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Overview</title>
    <link href="themes/${context.theme}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="themes/${context.theme}/static/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <link href="themes/${context.theme}/static/bower_components/prettyphoto/css/prettyPhoto.css" rel="stylesheet">
    <link href="themes/${context.theme}/static/bower_components/animate.css/animate.css" rel="stylesheet">
	<link href="themes/${context.theme}/static/css/main.css" rel="stylesheet">
	<link href="themes/${context.theme}/static/css/responsive.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="themes/${context.theme}/static/bower_components/html5shiv/dist/html5shiv.js"></script>
    <script src="themes/${context.theme}/static/bower_components/respond/dest/respond.min.js"></script>
    <![endif]-->
    <link rel="shortcut icon" href="themes/${context.theme}/static/images/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="themes/${context.theme}/static/images/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="themes/${context.theme}/static/images/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="themes/${context.theme}/static/images/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="themes/${context.theme}/static/images/ico/apple-touch-icon-57-precomposed.png">

</head><!--/head-->

<body>
	<#include "header.ftl">

    <div class="footer-widget">
        <div class="container">
            <div class="row">
                <div class="col-sm-2">
                    <div class="single-widget">
                         <ul id="indexmenu" class="nav nav-pills nav-stacked"></ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

	<#include "footer.ftl">

    <script src="themes/${context.theme}/static/bower_components/jquery/dist/jquery.js"></script>
    <script src="themes/${context.theme}/static/js/main.js"></script>
    <script src="themes/${context.theme}/static/js/indexmenu.js"></script>
</body>

</html>