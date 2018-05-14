"use strict";

(function() {
    init();

    function init() {
        $(".select2-basic").select2();

        $("#maxItems, #maxIters, #modelNums").inputmask({ regex: "[0-9]*" });

        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();

        $("#timeRange").daterangepicker({
            timePicker: true,
            timePicker24Hour: true,
            startDate: isValid(startTime) ? new Date(startTime) : moment().startOf('hour'),
            endDate: isValid(endTime) ? new Date(endTime) : moment().startOf('hour').add(32, 'hour'),
            locale: {
                format: "YYYY-MM-DD HH:mm:ss"
            }
        });

        $("#exam_submit").on("click", function() {
            var data = $("#exam_form").serializeJson();
            var isCreate;
            var url = baseurl;
            if (!isValid(data["id"])) {
                url += "/exam/create";
                isCreate = true;
            } else {
                url += "/exam/update";
                isCreate = false;
            }
            var timeRange = data["timeRange"];
            data["startTime"] = timeRange.substr(0, 19);
            data["endTime"] = timeRange.substr(22, 19);

            $.ajax({
                type: "POST",
                url: url,
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify(data),
                dataType: "json",
                success: function(res) {
                    if (res.success === true) {
                        if (isCreate) {
                            window.location.href = baseurl + "/mutation/exam";
                        } else {
                            window.location.reload();
                        }
                    }
                },
                error: function() {
                    toastr.error("网络错误！");
                }
            });

        });
    }
})();