"use strict";

(function() {
    init();

    function init() {
        $("#samples_upload_facade").on("click", function() {
            $("#samples_upload").click();
        });
        $("#samples_upload").on("change", function() {
            $("#samples_upload_facade").html($(this).val());
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