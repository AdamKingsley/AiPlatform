"use strict";

(function() {
    init();

    var presentation_list = document.querySelectorAll(".presentation");
    var content_list = document.querySelectorAll(".presentation-content");
    function init() {
        $(".presentation").on("click", function() {
            var index = 0;
            for (var i = 0; i < presentation_list.length; i++) {
                if (this === presentation_list[i]) {
                    index = i;
                    break;
                }
            }

            $(presentation_list).removeClass("active");
            $(this).addClass("active");

            $(content_list).hide();
            $(content_list[index]).show();
        });
    }
})();