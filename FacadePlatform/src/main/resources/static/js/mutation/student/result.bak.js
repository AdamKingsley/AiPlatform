"use strict";

(function() {
    var chart;
    var imgIndex = 0;
    var color = ["#2f4554", "#c23531"];

    var data;

    var startXPos = 100;
    var startYPos = 100;
    var r = 20;
    var yGap = 20;

    init();

    function init() {
        

        $.get("/static/resource/net.json", function(d) {
            if (!d) return;
            data = d;
            var arc = data["architecture"];
            var detail  = arc["detail"];
            var maxYIndex = 0;
            for (var i = 0; i < detail.length; i++) {
                if (maxYIndex < detail[i]) {
                    maxYIndex = detail[i];
                }
            }
            // $("#chart").height(startYPos + maxYIndex * (2 * r + yGap) + startYPos);
            initImage();
            // initGraph();
            // asyncData(imgIndex);
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
        var json = data["activation"][index];
        var series = getSeries(json.list);
        chart.setOption({
            series: series
        });

        //获取结果值
        $("#result").html(json["result"]);
        $("#predict_result").html(json["predict_result"]);
    }

    function initGraph() {
        chart = echarts.init(document.getElementById('chart'));
        var option = {
            title: {
                text: '神经元激活图'
            },
            tooltip: {},
            // animationDurationUpdate: 1500,
            // animationEasingUpdate: 'quinticInOut',
            series : [
                {
                    type: 'graph',
                    layout: 'none',
                    roam: true
                    // animationThreshold: 200
                }
            ]
        };

        chart.setOption(option);
    }

    function getPosition(xIndex, yIndex) {
        var width = $("#chart").width();

        var xGap = width / 4;

        var x = startXPos + xIndex * (2 * r + xGap);
        var y = startYPos + yIndex * (2 * r + yGap);
        return [x, y, r];
    }

    function getSeries(json) {
        var data = [];
        var links = [];
        var i, j, k;
        var maxYIndex = 0;
        for (i = 0; i < json.length; i++) {
            var list = json[i];
            for (j = 0; j < list.length; j++) {
                var value = list[j];
                var position = getPosition(i, j);
                var item = {
                    name: getName(i + 1, j + 1),
                    x: position[0],
                    y: position[1],
                    symbolSize: 2 * position[2]
                };
                if (value === 1) {
                    item["value"] = "激活";
                    item["itemStyle"] = {
                        color: color[1],
                        borderColor: "#000",
                        borderWidth: 3
                    };
                } else {
                    item["value"] = "非激活";
                    item["itemStyle"] = {
                        color: color[0],
                        borderWidth: 0
                    };
                }
                data.push(item);
            }

            if (maxYIndex < list.length) {
                maxYIndex = list.length;
            }
        }
        for (i = 0; i < json.length - 1; i++) {
            var list1 = json[i];
            var list2 = json[i + 1];
            for (j = 0; j < list1.length; j++) {
                for (k = 0; k < list2.length; k++) {
                    var link = {
                        source: getName((i + 1), (j + 1)),
                        target: getName((i + 2), (k + 1)),
                        lineStyle: {
                            // normal: { curveness: -0.2 }
                        }
                    };
                    links.push(link);
                }
            }
        }
        var series = [{
            data: data,
            links: links,
            // silent: true
            tooltip: {
                formatter: "{b}: {c}"
            },
            top: startYPos,
            left: startXPos,
            bottom: startYPos
        }];
        return series;
    }

    function getName(i, j) {
        return "神经元" + i + "_" + j;
    }
})();