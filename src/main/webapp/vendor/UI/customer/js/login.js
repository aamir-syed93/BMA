//$('#login-trigger').click(function(){
//    $(this).next('#login-content').slideToggle();
//    $(this).toggleClass('active');          
//    
//    if ($(this).hasClass('active')) $(this).find('span').html('&#x25B2;')
//      else $(this).find('span').html('&#x25BC;')
//    })


$('#login-trigger, #login-content').on({
    mouseenter: function(e) {
        if (e.target.id == 'login-trigger') $('#login-content').slideDown('slow');
        clearTimeout( $('#login-content').data('timer') );
    },
    mouseleave: function() {
        $('#login-content').data('timer', 
            setTimeout(function() {
                $('#login-content').slideUp('slow')
            }, 300)
        );
    }
});