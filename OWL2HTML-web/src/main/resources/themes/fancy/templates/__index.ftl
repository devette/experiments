<#macro selectOntologyFiles file path>
  	  <li>
		  <#if (file.directory)>
			  <#list file.listFiles() as subDirectory>
			  		<ul>
						<@selectOntologyFiles file=subDirectory path=path +"/" +file.name />
					</ul>
			  </#list>
		  <#else>
		     <a href="select?ontology=${path + "/" + file.name}">${file.name}</a>
		  </#if>
	   </li>
</#macro>

<!DOCTYPE html>
<html lang="en">
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
    <style>
    <!-- little style tuning -->
    .category-products .badge {
    	font-size:12px;
    }
   	.category-products .panel-default .panel-heading .panel-title a {
		  font-size: 12px;
	}
	.panel-group {
	    margin-bottom: 7px
	}
	.panel-heading {
		padding: 7px 10px;
	}
    </style>
</head><!--/head-->

<body>
     <@selectOntologyFiles file=session.ontologyDirectory path=""/>
</body>

</html>