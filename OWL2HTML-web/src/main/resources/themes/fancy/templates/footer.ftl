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
                        <img src="themes/${context.theme}/static/images/home/map.png" alt="" width="60%" height="60%" />
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