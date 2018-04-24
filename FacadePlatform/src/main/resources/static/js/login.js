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
            var data = $("#loginform").serializeJson();
            if (!isValid(data["username"])) {
                toastr.error("用户名不能为空！");
                return;
            }
            data["rememberMe"] = $("#rememberMe").is(":checked");
            $.ajax({
                url: "/login",
                type:"POST",
                data: JSON.stringify(data),
                contentType: "application/json;charset=UTF-8",
                dataType:"json",
                success: function (res) {
                    console.log(res);
                    if (res.success === true){
                        window.location.href = baseurl + "/";
                    }else{
                        toastr.error(res.errorMessage);
                    }
                }
            })
        })
    }
})();