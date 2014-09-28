$(document).ready(function(){
  $(".ui-panelmenu-content").css("display","block"); //shows the menuitems
  $(".ui-panelmenu-header").addClass("ui-state-active"); //sets the submenu header to active state
  $(".ui-icon-triangle-1-e").removeClass("ui-icon-triangle-1-e").addClass("ui-icon-triangle-1-s"); //sets the triangle icon to "expaned" version
});