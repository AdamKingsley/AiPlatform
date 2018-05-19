"use strict";

(function() {
    init();

    var data;
    function init() {


        // console.log(userId);
        // console.log(examId);
        // console.log(modelId);
        // console.log(iters);
        var iters = $("input[name='iters']").val();

        initIters(iters);
        getData(1);
    }

    function initIters(iters) {
        $("#iter_select").html("");
        for (var i = 1; i <= iters; i++) {
            $("#iter_select").append("<option value='" + i + "'>第" + i + "次迭代</option>");
        }

        $("#iter_select").on("change", function() {
            var iter = $(this).val();
            getData(iter);
        });

        $(".select2-basic").select2();
    }

    function getData(iter) {
        var userId = $("input[name='userId']").val();
        var examId = $("input[name='examId']").val();
        var modelId = $("input[name='modelId']").val();
        $.ajax({
            // url: baseurl + "/static/resource/net.json",
            url: baseurl + "/model-process/detail?userId=" + userId + "&examId=" + examId + "&modelId=" + modelId + "&iter=" + iter,
            type: "GET",
            dataType: "json",
            success: function(res) {
                if (res.success == false) {
                    toastr.error("没有相关数据！");
                    return;
                }

                var t = res["data"];

                $.ajax({
                    url: baseurl + t["resultLocation"],
                    type: "GET",
                    dataType: "json",
                    success: function(res) {
                        data = res;

                        initImage();

                        asyncData(0);
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        var json = JSON.parse(XMLHttpRequest.responseText);
                        toastr.error(json.errorMessage);
                    }
                });


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
            var str = json[i]["path"];
            var start = str.lastIndexOf("/") + 1;
            var end = str.length - 1;
            $("#img_select").append("<option value='" + str.substring(1, end) + "'>" + str.substring(start, end) + "</option>");
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


        initGraph(arc["activation_list"], arc["standard_list"]);
    }

    function initGraph(activation_list, standard_list) {
        $("#nerual_graph").html("");

        var i,j;
        for (i = 0; i < activation_list.length; i++) {
            var item = activation_list[i];
            var item_standard = standard_list[i];

            var nerual_result = "";
            for (j = 0; j < item.length; j++) {
                if (item[j] > 0) {
                    nerual_result += "<i class='fa fa-circle nerual-active' title='神经元" + (i + 1) + "_" + (j + 1) + ": 激活，数值为" + item[j] + "'></i>";
                } else {
                    nerual_result += "<i class='fa fa-circle nerual-inactive' title='神经元" + (i + 1) + "_" + (j + 1) + ": 未激活'></i>";
                }
            }

            var standard_result = "";
            for (j = 0; j < item_standard.length; j++) {
                if (item_standard[j] > 0) {
                    standard_result += "<i class='fa fa-circle nerual-active' title='神经元" + (i + 1) + "_" + (j + 1) + ": 激活，数值为" + item_standard[j] + "'></i>";
                } else {
                    standard_result += "<i class='fa fa-circle nerual-inactive' title='神经元" + (i + 1) + "_" + (j + 1) + ": 未激活'></i>";
                }
            }

            $("#nerual_graph").append("<div class='col-sm-12' style='margin-bottom: 10px'>" +
                "   <div class='col-sm-2'>第" + (i + 1) + "层(" + item.length + ")</div>" +
                "   <div class='col-sm-4'>" + nerual_result + "</div>" +
                "   <div class='col-sm-4'>" + standard_result + "</div>" +
                "</div>");
        }
    }
})();