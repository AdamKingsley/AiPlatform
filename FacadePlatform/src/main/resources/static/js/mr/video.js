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
        jwplayer("player1").setup({
            flashplayer: "/static/js/player.swf",
            file: "BladeRunner2049.flv",
            streamer: "rtmp://localhost/oflaDemo/",
            width: "500"
        });
        jwplayer("player2").setup({
            flashplayer: "/static/js/player.swf",
            file: "BladeRunner2049.flv",
            streamer: "rtmp://localhost/oflaDemo/",
            width: "500"
        });
    }
})();