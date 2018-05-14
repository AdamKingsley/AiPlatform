"use strict";

(function() {
    init();

    function init() {
        $(".select2-basic").select2();

        $("input[type='checkbox']").iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' // optional
        });

        var table = $("#exam_table").DataTable({
            //是否显示"处理中"
            processing: true,
            //是否使用服务器端数据
            serverSide: true,
            //展示完全的页码
            pagingType: "full_numbers",
            //第一列默认升序排列
            // order: [[0, 'asc']],
            //关闭本地搜索
            searching: false,
            //自定义中文显示
            language: {
                url: datatables_i18n_zhCN
            },
            //表格映射
            columns: [
                // { data: "id" },
                { data: "name" },
                { data: "startTime" },
                { data: "endTime" },
                { data: "state" },
                { data: "opt" }
            ],
            ajax: {
                url: baseurl + "/exam/list",
                type: "POST",
                contentType: "application/json;charset=utf-8",
                data: function(d) {
                    var extra = {
                        isMine: $("#join_in").is("checked")
                    };
                    var type = $("#exam_status").val();
                    if (isValid(type)) {
                        extra["type"] = parseInt(type);
                    }
                    var data = $.extend({}, d, extra);
                    return JSON.stringify(data);
                },
                dataSrc: function(json) {
                    var data = json.data;
                    //给新数据添加操作

                    var now = new Date().getTime();
                    for (var i = 0; i < data.length; i++) {
                        var start = new Date(data[i]["startTime"]).getTime();
                        var end = new Date(data[i]["endTime"]).getTime();
                        data[i]["opt"] = "";
                        if (start > now) {
                            data[i]["state"] = "<span class='label label-info'>未开始</span>"
                        } else if (end < now) {
                            data[i]["state"] = "<span class='label label-danger'>已结束</span>"
                        } else {
                            data[i]["state"] = "<span class='label label-success'>进行中</span>"
                            data[i]["opt"] = "<a href='/mutation/exam/join/" + data[i]["id"] + "'>" +
                                "   进入" +
                                "</a>";
                        }
                    }
                    return data;
                },
                error: function() {
                    console.log('Network error');
                }
            }
        });

        $("#search_button").on('click', function() {
            // console.log($("#join_in").is(":checked"));
            table.ajax.reload();
        });
    }
})();