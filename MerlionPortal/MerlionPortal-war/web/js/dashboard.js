$(document).ready(function() {
    $('.dashboard-box').mouseenter(function() {
        $(this).addClass('highlight');
    });
    $('.dashboard-box').mouseleave(function() {
    $(this).removeClass('highlight');
    });
});

