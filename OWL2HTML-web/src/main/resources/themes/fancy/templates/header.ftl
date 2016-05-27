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
                            <li><a href=""><i class="fa"><img src="themes/${context.theme}/static/images/famfamfam_flag_icons/png/${context.locale.language?lower_case}.png"/></i></a></li>

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
                        <ul id="menu" class="nav navbar-nav collapse navbar-collapse"></ul>
                    </div>
                    <div class="mainmenu pull-right">
                        <ul class="nav navbar-nav collapse navbar-collapse">
                            <li class="dropdown"><a href="#" class="active">${messages("__common.nav_reasoning")}<i class="fa fa-angle-down"></i></a>
                                <ul role="menu" class="sub-menu">
                                    <li><a href="select?inferred=false" class="active">${messages("__common.nav_reasoning.without")}</a></li>
                                    <li><a href="select?inferred=true">${messages("__common.nav_reasoning.with")}</a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="col-sm-2">
                    <!--
                    <div class="search_box pull-right">
                        <input type="text" placeholder="${messages("__common.search")}"/>
                    </div>
                    -->
                </div>
            </div>
        </div>
    </div><!--/header-bottom-->
</header><!--/header-->