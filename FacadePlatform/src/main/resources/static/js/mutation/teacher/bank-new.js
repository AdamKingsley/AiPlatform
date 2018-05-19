"use strict";

(function() {
    init();

    function init() {
        $("#script_facade").on("click", function() {
            $("#script").click();
        });
        $("#script").on("change", function() {
            $("#script_facade").html("");
            var obj = document.getElementById("script");
            var length = obj.files.length;
            for (var i = 0; i < length; i++) {
                $("#script_facade").append(obj.files[i].name + ";");
            }
        });
        $("#standard_model_facade").on("click", function() {
            $("#standard_model").click();
        });
        $("#standard_model").on("change", function() {
            $("#standard_model_facade").html($(this).val());
        });
        $("#models_facade").on("click", function() {
            $("#models").click();
        });
        $("#models").on("change", function() {
            $("#models_facade").html("");
            var obj = document.getElementById("models");
            var length = obj.files.length;
            for (var i = 0; i < length; i++) {
                $("#models_facade").append(obj.files[i].name + ";");
            }
        });
        $("#samples_facade").on("click", function() {
            $("#samples").click();
        });
        $("#samples").on("change", function() {
            $("#samples_facade").html("");
            var obj = document.getElementById("samples");
            var length = obj.files.length;
            for (var i = 0; i < length; i++) {
                $("#samples_facade").append(obj.files[i].name + ";");
            }
        });

        $("#bank_submit").on("click", function() {
            var option = {
                url: baseurl + "/bank/create",
                type: "POST",
                data: "json",
                success: function(res) {
                    if (res.success == true) {
                        toastr.options.onHidden = function() {
                            window.location.href = baseurl + "/mutation/bank";
                        };
                        toastr.success(res.message);
                    } else {
                        toastr.error(res.errorMessage);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    var json = JSON.parse(XMLHttpRequest.responseText);
                    toastr.error(json.message);
                }
            };
            $("#bank_form").ajaxSubmit(option);
        })
    }
})();