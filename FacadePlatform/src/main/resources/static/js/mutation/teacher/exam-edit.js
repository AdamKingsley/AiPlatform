"use strict";

(function() {
    init();

    function init() {
        $(".select2-basic").select2();

        $("#maxItems, #maxIters, #modelNums").inputmask({ regex: "[0-9]*" });

        $("#timeRange").daterangepicker({
            timePicker: true,
            timePicker24Hour: true,
            startDate: moment().startOf('hour'),
            endDate: moment().startOf('hour').add(32, 'hour'),
            locale: {
                format: "YYYY-MM-DD HH:mm:ss"
            }
        });

        $("#exam_submit").on("click", function() {
            var data = $("#exam_form").serializeJson();
            var url = baseurl;
            if (!isValid(data["id"])) {
                url += "/exam/create";
            } else {
                url += "/exam/update";
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

                },
                error: function() {
                    toastr.error("网络错误！");
                }
            });

        });
    }
})();