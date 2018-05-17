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
                        isMine: false
                    };
                    var type = $("#exam_status").val();
                    if (isValid(type)) {
                        extra["type"] = parseInt(type);
                    }
                    var data = $.extend({}, d, extra);
                    return JSON.stringify(data);
                },
                dataSrc: function(json) {
                    // console.log(json);
                    var data = json.data;
                    //给新数据添加操作

                    var now = new Date().getTime();
                    for (var i = 0; i < data.length; i++) {
                        var start = new Date(data[i]["startTime"]).getTime();
                        var end = new Date(data[i]["endTime"]).getTime();
                        data[i]["opt"] = "";

                        if (start > now) {
                            data[i]["state"] = "<span class='label label-info'>未开始</span>";
                            data[i]["opt"] += "<a href='/mutation/exam/edit/" + data[i]["id"] + "'>" +
                                "   <i class='fa fa-edit'></i>" +
                                "</a>";
                        } else if (end < now) {
                            data[i]["state"] = "<span class='label label-danger'>已结束</span>"
                        } else {
                            data[i]["state"] = "<span class='label label-success'>进行中</span>"
                        }
                        data[i]["opt"] += "<a href='#' class='delete-btn'>" +
                            "   <input type='hidden' value='" + data[i]["id"] + "'>" +
                            "   <i class='fa fa-trash'></i>" +
                            "</a>";
                    }
                    return data;
                },
                error: function() {
                    console.log('Network error');
                }
            }
        }).on('draw', function() {
            $('.delete-btn').on('click', function() {
                var id = $(this).find('input').val();
                $.ajax({
                    url: baseurl + "/exam/delete?id=" + id,
                    type: 'DELETE',
                    dataType: 'json',
                    success: function(json) {
                        toastr.success('删除成功！');
                        table.ajax.reload();
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        var json = JSON.parse(XMLHttpRequest.responseText);
                        toastr.error(json.message);
                    }
                });
            })
        });

        $("#search_button").on("click", function() {
            table.ajax.reload();
        });
    }
})();