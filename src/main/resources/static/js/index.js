var $window = $(window);

function fullScreenHeight() {
    var e = $(".full-screen"), t = $window.height();
    e.css("min-height", t)
}

function ScreenFixedHeight() {
    var e = $("header").height(), t = $(".screen-height"), e = $window.height() - e;
    t.css("height", e)
}

function SetResizeContent() {
    fullScreenHeight(), ScreenFixedHeight()
}

$window.resize(function (e) {
    setTimeout(function () {
        SetResizeContent()
    }, 500), e.preventDefault()
}), SetResizeContent(), $(document).ready(function () {
    $(".inner-page-slides").owlCarousel({
        items: 1,
        loop: !0,
        autoplay: !0,
        smartSpeed: 800,
        margin: 0,
        center: !1,
        dots: !0,
        responsive: {0: {items: 1}, 576: {items: 2}, 992: {items: 3}, 1200: {items: 4}}
    }), $(".animate-redirect a[href^='#']").click(function (e) {
        e.preventDefault();
        e = $($(this).attr("href")).offset().top;
        $("body, html").animate({scrollTop: e}, 1e3)
    }), $window.on("scroll", function () {
        500 < $(this).scrollTop() ? $(".scroll-to-top").fadeIn(400) : $(".scroll-to-top").fadeOut(400)
    }), $(".scroll-to-top").on("click", function (e) {
        e.preventDefault(), $("html, body").animate({scrollTop: 0}, 600)
    }), $(".countup").counterUp({delay: 50, time: 2e3}), $(".current-year").text((new Date).getFullYear())
});
