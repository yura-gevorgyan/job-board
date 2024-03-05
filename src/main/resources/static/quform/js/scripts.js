'use strict';
jQuery(function ($) {
    $('form.quform').Quform();
    if (window.tippy) {
        $('.quform-tooltip').each(function () {
            tippy(this, {theme: 'quform'});
        });
    }
    $('#subject').replaceSelectWithTextInput({onValue: 'Other'});
});
(function ($) {
    $(window).on('load', function () {
        var images = ['/static/quform/images/close.png', '/static/quform/images/success.png', '/static/quform/images/error.png', '/static/quform/images/default-loading.gif'];
        if ($('.quform-theme-light-light, .quform-theme-light-rounded').length) {
            images = images.concat(['/static/quform/themes/light/images/button-active-bg-rep.png', '/static/quform/themes/light/images/close.png', '/static/quform/themes/light/images/input-active-bg-rep.png']);
        }
        if ($('.quform-theme-dark-dark, .quform-theme-dark-rounded').length) {
            images = images.concat(['/static/quform/themes/dark/images/button-active-bg-rep.png', '/static/quform/themes/dark/images/close.png', '/static/quform/themes/dark/images/input-active-bg-rep.png', '/static/quform/themes/dark/images/loading.gif']);
        }
        if ($('.quform-theme-minimal-light').length) {
            images = images.concat(['/static/quform/themes/minimal/images/close-light.png']);
        }
        if ($('.quform-theme-minimal-dark').length) {
            images = images.concat(['/static/quform/themes/minimal/images/close-dark.png', '/static/quform/themes/minimal/images/loading-dark.gif']);
        }
        $.preloadImages(images);
    });
})(jQuery);