"use strict";

(function() {
    init();

    function init() {
        $('#submit').on("click", function() {
            var _this = this;
            var data = $("#registerform").serializeJson();
            if (!isValid(data["username"])) {
                toastr.error("用户名不能为空！");
                return;
            }
            if (!isValid(data["mail"])) {
                toastr.error("邮箱不能为空！");
                return;
            }
            if (!isValid(data["password"])) {
                toastr.error("密码不能为空！");
                return;
            }
            if (data['password'] !== data['confirmPassword']) {
                toastr.error("两次输入的密码不同！");
                return;
            }
            // data["roleId"] = 1;
            $(this).disable();
            $(this).html('注册<div style="display: inline-block; margin-left: 6px"><i class="fa fa-circle-o-notch fa-spin"></i></div>');
            $.ajax({
                url: baseurl + "/register",
                type: "POST",
                data: JSON.stringify(data),
                contentType: "application/json;charset=UTF-8",
                dataType: "json",
                success: function(res) {
                    if (res.success === true) {
                        toastr.options.onHidden = function() {
                            window.location.href = baseurl + "/login";
                        };
                        toastr.success("注册成功！已发送注册确认邮件，请尽快确认！");
                    } else {
                        toastr.error(res.errorMessage);
                    }
                },
                error: function() {
                    toastr.error("网络错误！");
                },
                complete: function() {
                    $(_this).enable();
                    $(_this).html("注册");
                }
            });
        });
    }
})();