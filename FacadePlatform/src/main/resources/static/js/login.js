(function() {
    init();

    function init() {
        $("#submit").click(function () {
            console.log("mmp");
            var data = {
                username:$('#userid').val(),
                password:$('#userpassword').val()
            };
            $.ajax({
                url: "/login",
                type:"POST",
                data: JSON.stringify(data),
                contentType: 'application/json;charset=UTF-8',
                dataType:"json",
                success: function (data) {
                    console.log(data);
                    if (data.success=="true"){
                        // window.location.href("./index2.html")

                    }else{

                        console.log("log error")

                    }
                }


            })
        })
    }
})();