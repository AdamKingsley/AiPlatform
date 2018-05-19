"use strict";

(function() {
    init();

    function init() {
        $("#samples_upload_facade").on("click", function() {
            $("#samples_upload").click();
        });
        $("#samples_upload").on("change", function() {
            $("#samples_upload_facade").html("");
            var obj = document.getElementById("samples_upload");
            var length = obj.files.length;
            for (var i = 0; i < length; i++) {
                $("#samples_upload_facade").append(obj.files[i].name + ";");
            }
        });

        $("#samples_submit").on("click", function() {
            if (!isValid($("#samples_upload").val())) {
                toastr.error("请选择文件！");
                return;
            }

            var option = {
                success: function(res) {
                    if (res.success == true) {
                        toastr.options.onHidden = function() {
                            window.location.reload();
                        };
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

            $("#samples_form").ajaxSubmit(option);
        });
    }
})();