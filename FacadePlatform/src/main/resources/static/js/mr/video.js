"use strict";

(function() {
    init();

    function init() {
        // var options = {};
        //
        // var player = videojs("example_video_1", options);
        // player.src({
        //     src: "rtmp://localhost/oflaDemo/BladeRunner2049.flv",
        //     type: "rtmp/flv"
        // });
        var video1 = jwplayer("player1").setup({
            flashplayer: "/static/js/player.swf",
            file: "other_result_1.mp4",
            streamer: "rtmp://localhost/oflaDemo/",
            width: "300",
            height: "200"
        });
        var video2 = jwplayer("player2").setup({
            flashplayer: "/static/js/player.swf",
            file: "source_result_1.mp4",
            streamer: "rtmp://localhost/oflaDemo/",
            width: "300",
            height: "200"
        });

        var isStart = false;
        $("#play").on("click", function() {
            if (!isStart) {
                video1.play();
                video2.play();
            } else {
                video1.stop();
                video2.stop();
            }
        });
    }
})();