(function ($) {    
    jQuery.fn.extend({
        closestChild: function (selector) {
            return $(this).find(selector).first();
        },    
        disable: function() {
            return $(this).each(function() {
                $(this).prop('disabled' , 'disabled');
            });
        },
        enable: function() {
            return $(this).each(function() {
                $(this).prop('disabled' , '');
            });
        }
    });
})(jQuery);
