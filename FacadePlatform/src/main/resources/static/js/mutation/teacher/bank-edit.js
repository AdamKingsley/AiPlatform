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
            $("#script").click();
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
            var data = $("#bank_form").serializeJson();
            $.ajax({
                url: baseurl + "/bank/update/simple",
                type: "POST",
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify(data),
                dataType: "json",
                success: function(res) {
                    if (res.success === true) {
                        window.location.reload();
                    }
                },
                error: function() {
                    toastr.error("网络错误！");
                }
            });
        });
        var option = {
            success: function(res) {
                if (res.success == true) {
                    toastr.success(res.message);
                } else {
                    toastr.error(res.errorMessage);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                var json = JSON.parse(XMLHttpRequest.responseText);
                toastr.error(json.errorMessage);
            }
        };
        $("#script_submit").on("click", function() {
            $("#script_form").ajaxSubmit(option);
        });
        $("#models_submit").on("click", function() {
            $("#models_form").ajaxSubmit(option);
        });
        $("#samples_submit").on("click", function() {
            $("#samples_form").ajaxSubmit(option);
        });
    }
})();