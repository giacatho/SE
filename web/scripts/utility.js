(function ($) {    
    jQuery.fn.extend({
        closestChild: function (selector) {
            return $(this).find(selector).first();
        }
    });
})(jQuery);
