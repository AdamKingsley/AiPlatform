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
            // serverSide: true,
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
            // columns: [
            //     // { data: "id" },
            //     { data: "userid" },
            //     { data: "name" },
            //     { data: "position" },
            //     { data: "departmentName" },
            //     { data: "mobile" },
            //     // { data: "email" },
            //     { data: "gender" },
            //     // { data: "telephone" },
            //     { data: "attendanceRule" },
            //     { data: "opt" }
            // ],
            //ajax
            // ajax: {
            //     url: '/api/admin/enterprise/employee/list',
            //     type: 'POST',
            //     data: function(d) {
            //         return $.extend({}, d, {
            //             userid: $('#userid').val(),
            //             name: $('#name').val(),
            //             departmentId: $('#departmentId').val(),
            //             positionId: $('#positionId').val()
            //         })
            //     },
            //     dataSrc: function(json) {
            //         var data = json.data;
            //         //给新数据添加操作
            //         for (var i = 0; i < data.length; i++) {
            //             data[i]['gender'] = genderStr(data[i]['gender']);
            //             data[i]['opt'] = '';
            //             data[i]['opt'] = '<a href="/admin/enterprise/employee/' + data[i]['id'] + '">' +
            //                 '   <i class="fa fa-edit"></i>' +
            //                 '</a>';
            //         }
            //         return data;
            //     },
            //     error: function() {
            //         console.log('Network error');
            //     }
            // }
        });
    }
})();