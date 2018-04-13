 $(document).ready(function () {
     $("#submit").click(function () {

         $.ajax({
             url:"/login",
             type:"POST"


         })
     })
 })