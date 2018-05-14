"use strict";

(function() {
    init();

    function init() {
        $("input").iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' // optional
        });

        $("#submit").click(function () {
            var _this = this;
            var data = $("#loginform").serializeJson();
            if (!isValid(data["username"])) {
                toastr.error("用户名不能为空！");
                return;
            }
            data["rememberMe"] = $("#rememberMe").is(":checked");
            $(this).disable();
            $(this).html('登录<div style="display: inline-block; margin-left: 6px"><i class="fa fa-circle-o-notch fa-spin"></i></div>');
            $.ajax({
                url: "/login",
                type:"POST",
                data: JSON.stringify(data),
                contentType: "application/json;charset=UTF-8",
                dataType:"json",
                success: function(res) {
                    if (res.success === true){
                        window.location.href = baseurl + "/";
                    }else{
                        toastr.error(res.errorMessage);
                    }
                },
                error: function() {
                    toastr.error("网络错误！");
                },
                complete: function() {
                    $(_this).enable();
                    $(_this).html("登录");
                }
            })
        })
    }
})();