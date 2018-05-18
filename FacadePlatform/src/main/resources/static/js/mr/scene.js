"use strict";

(function() {
    init();

    function init() {
        $("#style_path").on("click", function() {
            $("#style_file").click();
        });
        $("#style_file").on("change", function() {
            $("#style_path").html($(this).val());
        });
    }
})();