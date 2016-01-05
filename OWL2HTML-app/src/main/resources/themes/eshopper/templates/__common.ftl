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
			   	  <a href="${clazz.filename}.html">${clazz.getLocalLabel(context.locale)} </a> <a data-toggle="collapse" data-parent="#accordian" href="#${clazz.iriId?url}"><span class="badge pull-right">(${clazz.individualsCount}) <#if (clazz.subClasses?size > 0)><i class="fa fa-plus"><#else>&nbsp;&nbsp;&nbsp;</#if></i></span></a>
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
	   	  <a href="${clazz.filename}.html">${clazz.getLocalLabel(context.locale)} </a> 
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
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>${title?html}</title>
    <link href="bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <link href="bower_components/prettyphoto/css/prettyPhoto.css" rel="stylesheet">
    <link href="bower_components/animate.css/animate.css" rel="stylesheet">
	<link href="css/main.css" rel="stylesheet">
	<link href="css/responsive.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="bower_components/html5shiv/dist/html5shiv.js"></script>
    <script src="bower_components/respond/dest/respond.min.js"></script>
    <![endif]-->       
    <link rel="shortcut icon" href="images/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="images/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="images/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="images/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="images/ico/apple-touch-icon-57-precomposed.png">
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
	<header id="header"><!--header-->
		<div class="header_top"><!--header_top-->
			<div class="container">
				<div class="row">
					<div class="col-sm-6">
						<div class="contactinfo">
							<ul class="nav nav-pills">
								<li><a href=""><i class="fa fa-envelope"></i> ${messages("__common.header.contact")}</a></li>
							</ul>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="social-icons pull-right">
							<ul class="nav navbar-nav">
								<li><a href=""><i class="fa fa-facebook"></i></a></li>
								<li><a href=""><i class="fa fa-twitter"></i></a></li>
								<li><a href=""><i class="fa fa-linkedin"></i></a></li>
								<li><a href=""><i class="fa fa-dribbble"></i></a></li>
								<li><a href=""><i class="fa fa-google-plus"></i></a></li>
								<li><a href=""><i class="fa"><img src="images/famfamfam_flag_icons/png/${context.locale.language?lower_case}.png"/> ${context.locale}</i></a></li>
								
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div><!--/header_top-->
		
        <div class="header-bottom"><!--header-bottom-->
			<div class="container">
		
				<div class="row">
					<div class="col-sm-10">
						<div class="navbar-header">
							<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
								<span class="sr-only">Toggle navigation</span>
								<span class="icon-bar"></span>
								<span class="icon-bar"></span>
								<span class="icon-bar"></span>
							</button>
						</div>
						<div class="mainmenu pull-left">
							<ul class="nav navbar-nav collapse navbar-collapse">
								<li><a href="index.html">${messages("__common.nav_home")}</a></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div><!--/header-bottom-->
	</header><!--/header-->
	
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
	
	<footer id="footer"><!--Footer-->
		<div class="footer-top">
        </div>
		
		<div class="footer-widget">
			<div class="container">
				<div class="row">
					<div class="col-sm-2">
						<div class="single-widget">
							<h2>${messages("__common.footer.generation")}</h2>
							<ul class="nav nav-pills nav-stacked">
								<li><i class="fa fa-user"></i> ${messages("__common.footer.author")}</li>
                                <li><i class="fa fa-calendar"></i> ${.now?string["yyyy-MMMM-dd"]}</li>
								<li><i class="fa fa-clock-o"></i> ${.now?time}</li>
							</ul>
						</div>
					</div>
				
					<div class="col-sm-3 col-sm-offset-1 pull-right">
						<div class="single-widget">
							<h2>${messages("__common.footer.about")}</h2>
                            <img src="images/home/map.png" alt="" width="60%" height="60%" />
						</div>
					</div>
					
				</div>
			</div>
		</div>
		
		<div class="footer-bottom">
			<div class="container">
				<div class="row">
					<p class="pull-left">${messages("__common.footer.copyright")} ${.now?string["yyyy"]}</p>
					<p class="pull-right">${messages("__common.footer.designedby")}<span><a target="_blank" href="http://www.themeum.com">Themeum</a></span></p>
				</div>
			</div>
		</div>
		
	</footer><!--/Footer-->
	

    <script src="bower_components/jquery/dist/jquery.js"></script>
	<script src="bower_components/scrollup/dist/jquery.scrollUp.min.js"></script>
	<script src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
    <script src="bower_components/prettyphoto/js/jquery.prettyPhoto.js"></script>
    <script src="bower_components/vis/dist/vis.js"></script>
    <script src="js/main.js"></script>
</body>
</html>
</#macro>
