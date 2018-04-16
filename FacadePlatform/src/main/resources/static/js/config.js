'use strict';

var baseurl = "";

(function ($){
    /**
     * 表单数据转json格式
     * */
    $.fn.serializeJson = function (){
        var serializeObj={};
        $( this .serializeArray()).each( function (){
            serializeObj[ this .name]= this .value;
        });
        return serializeObj;
    };

    /**
     * 设置启用和禁用
     * */
    $.fn.disable = function() {
        $(this).attr('disabled', true);
    };
    $.fn.enable = function() {
        $(this).removeAttr('disabled');
    };

    toastr.options = {
        "closeButton": false,
        "debug": false,
        "newestOnTop": false,
        "progressBar": true,
        "positionClass": "toast-top-right",
        "preventDuplicates": false,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "1200",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
})(jQuery);

function isValid(obj) {
    return obj !== undefined && obj !== null && obj !== '';
}

function isNumber(val){
    var regPos = /^\d+(\.\d+)?$/; //非负浮点数
    var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
    if(regPos.test(val) || regNeg.test(val)){
        return true;
    }else{
        return false;
    }
}