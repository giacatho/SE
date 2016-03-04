(function ($) {    
    $(document).on('click', '.search-zone .search-item .btn-remove', function(e){
        e.preventDefault();
        e.stopPropagation();
        
        var item = $(this).closest('.search-item');
        item.remove();
    });
    
    $(document).on('click', '.search-outline .btn-add-item', function(e){
        e.preventDefault();
        e.stopPropagation();
        
        var zone = $(this).closest('.search-outline').closestChild('.search-zone');
        
        //clone new item from first item and clean data
        var item = zone.closestChild('.search-item').clone();
        item.find('select option:first-child').attr("selected", "selected");
        item.find('input[type=text]').val("");
        
        zone.append(item);        
    });
    
    $(document).on('click', '.search-outline .btn-clear', function(e){
        e.preventDefault();
        e.stopPropagation();
        
        var zone = $(this).closest('.search-outline').closestChild('.search-zone');        
        zone.find('select option:first-child').attr("selected", "selected");
        zone.find('input[type=text]').val("");
    });
})(jQuery);