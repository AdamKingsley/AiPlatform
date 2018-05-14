"use strict";

(function() {
    init();

    function init() {
        $("#changePasswordSubmit").on("click", function() {
            var _this = this;
            var data = $("#changePassword").serializeJson();
            if (!isValid(data["password"])) {
                toastr.error("新密码为空！");
                return;
            }
            if (data["password"] !==  data["confirmPassword"]) {
                toastr.error("两次输入的密码不同！");
                return;
            }
            $(this).disable();
            $.ajax({
                url: baseurl + "/account/change-password",
                type: "POST",
                data: JSON.stringify(data),
                contentType: "application/json;charset=UTF-8",
                dataType: "json",
                success: function(res) {
                    if (res.success === true) {
                        toastr.options.onHidden = function() {
                            window.location.reload();
                        };
                        toastr.success("更新密码成功！");
                    } else {
                        toastr.error(res.errorMessage);
                    }
                },
                error: function() {
                    toastr.error("网络错误！");
                },
                complete: function() {
                    $(_this).enable();
                }
            });
        });
    }
})();