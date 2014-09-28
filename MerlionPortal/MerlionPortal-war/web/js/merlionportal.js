$(document).ready(function(){
  //shows the menuitems
  $(".ui-panelmenu-content").css("display","block"); 
  
  //sets the submenu header to active state
  $(".ui-panelmenu-header").addClass("ui-state-active");
  
//  //sets the triangle icon to "expaned" version
//  $(".ui-icon-triangle-1-e").removeClass("ui-icon-triangle-1-e").addClass("ui-icon-triangle-1-s"); 
});

//
////To expand the panel menu only if is collapsed
//var menuIsExpanded = jQuery('div.ui-panelmenu-content.ui-widget-content.ui-helper-hidden').is(':visible');
//    if (!menuIsExpanded) {
//        jQuery('.ui-panelmenu-panel a').first().click();
//    }