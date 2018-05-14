"use strict";

(function() {
    init();

    function init() {
        var table = $("#bank_table").DataTable({
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
                { data: "id" },
                { data: "name" },
                { data: "nums" },
                { data: "createTime" },
                // { data: "state" },
                { data: "opt" }
            ],
            ajax: {
                url: baseurl + "/bank/list",
                type: "POST",
                contentType: "application/json;charset=utf-8",
                data: function(d) {
                    var extra = {

                    };
                    var data = $.extend({}, d, extra);
                    return JSON.stringify(data);
                },
                dataSrc: function(json) {
                    var data = json.data;
                    //给新数据添加操作

                    console.log(data);

                    for (var i = 0; i < data.length; i++) {
                        data[i]["opt"] = "<a href='/mutation/bank/edit/" + data[i]["id"] + "'>" +
                            "   <i class='fa fa-edit'></i>" +
                            "</a>" +
                            "<a href='#' class='delete-btn'>" +
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
                    url: baseurl + "/bank/delete?id=" + id,
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
    }
})();