"use strict";

(function() {
    init();

    var data;
    function init() {
        var userId = $("input[name='userId']").val();
        var examId = $("input[name='examId']").val();
        var modelId = $("input[name='modelId']").val();
        var iters = $("input[name='iters']").val();

        console.log(userId);
        console.log(examId);
        console.log(modelId);
        console.log(iters);

        $.ajax({
            // url: baseurl + "/static/resource/net.json",
            url: baseurl + "/model-process/detail?userId=" + userId + "&examId=" + examId + "&modelId=" + modelId + "&iter=1",
            type: "GET",
            dataType: "json",
            success: function(res) {
                if (res.success == false) {

                }

                data = res;
                console.log(data);
                // initImage();

                // asyncData(0);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                var json = JSON.parse(XMLHttpRequest.responseText);
                toastr.error(json.errorMessage);
            }
        });
    }

    function initImage() {
        var json = data["activation"];
        $("#img_select").html("");

        for (var i = 0; i < json.length; i++) {
            $("#img_select").append("<option>" + json[i]["path"] + "</option>");
        }

        $("#img_select").select2();
        $("#img_select").on("change", function() {
            $("#img_small").attr("src", baseurl + $(this).val());
            var select = document.getElementById("img_select");
            asyncData(select.selectedIndex);
        });

        $("#img_small").attr("src", baseurl + $("#img_select").val());
    }

    function asyncData(index) {
        var arc = data["activation"][index];
        $("#result").html(arc["result"]);
        $("#predict_result").html(arc["predict_result"]);

        initGraph(arc["list"]);
    }

    function initGraph(list) {
        $("#nerual_graph").html("");

        var i,j;
        for (i = 0; i < list.length; i++) {
            var item = list[i];

            var nerual_result = "";
            for (j = 0; j < item.length; j++) {
                if (item[j] === 0) {
                    nerual_result += "<i class='fa fa-circle nerual-inactive' title='神经元" + (i + 1) + "_" + (j + 1) + ": 未激活'></i>";
                } else {
                    nerual_result += "<i class='fa fa-circle nerual-active' title='神经元" + (i + 1) + "_" + (j + 1) + ": 激活'></i>";
                }
            }

            $("#nerual_graph").append("<div class='col-sm-12' style='margin-bottom: 10px'>" +
                "   <div class='col-sm-2'>第" + (i + 1) + "层(" + item.length + ")</div>" +
                "   <div class='col-sm-10'>" + nerual_result + "</div>" +
                "</div>");
        }
    }
})();