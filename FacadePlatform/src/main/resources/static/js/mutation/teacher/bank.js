"use strict";

(function() {
    init();

    function init() {
        $("#model_facade").on("click", function() {
            $("#model").click();
        });
        $("#model").on("change", function() {
            $("#model_facade").html($(this).val());
        });
    }
})();