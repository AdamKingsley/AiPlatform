"use strict";

(function() {
    init();

    function init() {
        $('#submit').on("click", function() {
            var data = $("#registerform").serializeJson();
            if (!isValid(data["username"])) {
                toastr.error("用户名不能为空！");
                return;
            }
            if (!isValid(data["password"])) {
                toastr.error("密码不能为空！");
                return;
            }
            if (data['password'] !== data['retypepassword']) {
                toastr.error("两次输入的密码不同！");
                return;
            }
            $.ajax({
                url: baseurl + "/register",
                type: "POST",
                data: JSON.stringify(data),
                contentType: "application/json;charset=UTF-8",
                dataType: "json",
                success: function(res) {
                    if (res.success === true) {
                        window.location.href = baseurl + "/login";
                    } else {
                        toastr.error(res.errorMessage);
                    }
                }
            });
        });
    }
})();