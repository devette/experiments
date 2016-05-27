<#macro create_menu ontology>
	<div class="panel-group category-products" id="accordian">
		<#if (ontology.root.subClasses?size > 0)>
			<#list ontology.root.subClasses as clazz>	
				<@menu_level1 clazz=clazz/>		
			</#list>
		<#else>
			<#list ontology.classes as clazz>					
				<#if (clazz.rootClass)>
					<@menu_level1 clazz=clazz/>
				</#if>
			</#list>
		</#if>
	</div>
</#macro>

<#macro menu_level1 clazz>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">
		  	  <li>
			   	  <a href="select?class=${clazz.iri?url}">${clazz.getLocalLabel(context.locale)} </a> <a data-toggle="collapse" data-parent="#accordian" href="#${clazz.iriId?url}"><span class="badge pull-right">(${clazz.individualsCount}) <#if (clazz.subClasses?size > 0)><i class="fa fa-plus"><#else>&nbsp;&nbsp;&nbsp;</#if></i></span></a>
			  </li>
		   </h3>
	   </div>
	  <#if (clazz.subClasses?size > 0)>
		  <div id="${clazz.iriId?url}" class="panel-collapse collapse">
		  	  <div class="panel-body">
				  <ul>
					  <#list clazz.subClasses as subClass>
							<@recurse_menu clazz=subClass depth=2 />
					  </#list>
				  </ul>
			  </div>
		  </div>
	  </#if>	
   </div>
</#macro>

<#macro recurse_menu clazz depth>
  	  <li>
  	  	  <span class="badge pull-right">(${clazz.individualsCount})</span>
	   	  <a href="select?class=${clazz.iri?url}">${clazz.getLocalLabel(context.locale)}  </a> 
		  <#if (clazz.subClasses?size > 0)>
			  <#list clazz.subClasses as subClass>
			  		<ul>
						<@recurse_menu clazz=subClass depth=depth+1 />
					</ul>
			  </#list>
		  </#if>	
	   </li>
</#macro>

<#macro page title ontology>
<#setting url_escaping_charset="UTF-8">

<!DOCTYPE html>
<html lang="${session.context.locale.language}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>${title?html}</title>
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

<body onload="bodyLoad();">

    <#include "header.ftl">

	<section>
		<div class="container">
			<div class="row">
				<div class="col-sm-3">
					<div class="left-sidebar">
						<h2>${messages("__common.menu.title")}</h2>
						<@create_menu ontology=ontology/>
					</div>
				</div>
				<div class="col-sm-9">
					<div class="blog-post-area">
						<div class="single-blog-post">
							
							<#nested/>
							
						</div>
					</div>
                </div>
			</div>
		</div>
	</section>
	
	<#include "footer.ftl">

    <script src="themes/${context.theme}/static/bower_components/jquery/dist/jquery.js"></script>
	<script src="themes/${context.theme}/static/bower_components/scrollup/dist/jquery.scrollUp.min.js"></script>
	<script src="themes/${context.theme}/static/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
    <script src="themes/${context.theme}/static/bower_components/prettyphoto/js/jquery.prettyPhoto.js"></script>
    <script src="themes/${context.theme}/static/bower_components/vis/dist/vis.js"></script>
    <script src="themes/${context.theme}/static/js/main.js"></script>
    <script src="themes/${context.theme}/static/js/menu.js"></script>
</body>
</html>
</#macro>
