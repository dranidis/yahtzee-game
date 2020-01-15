$(document).ready(function () {
    $("#gameControl").hide();
    $("#playerList").hide();
    $('#startBtn').hide();
    $('#restartBtn').hide();

    var MS_DELAY = 200; // milliseconds delay for dice animation
    var playerList = [];
    var playerName;
    var disabled;
    var stopAnimation = false;
    var end = false;
    var entered = false;

    function connect() {
        var socket = new SockJS('/gamews');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);

            var sessionId = /\/([^\/]+)\/websocket/.exec(socket._transport.url)[1];
            console.log("connected, session id: " + sessionId);

            stompClient.subscribe('/topic/players', function (player) {
                playerList = JSON.parse(player.body).players;
                showPlayers();
                console.log("My name :" + playerName)
            });
            stompClient.subscribe('/topic/game', function (game) {
                if (!end)
                    handleGame(JSON.parse(game.body));
            });
            stompClient.subscribe('/topic/end', function (endresponse) {
                end = true;
                endGame(JSON.parse(endresponse.body).message);
            });
            stompClient.subscribe('/topic/rolling', function (msg) {
                rollAnimation(JSON.parse(msg.body).keep);
            });
            stompClient.subscribe('/user/queue/errors', function (msg) {
                console.log("Error: " + msg)
                alert(msg.body);
            });
            stompClient.subscribe('/topic/name' + sessionId, function (msg) {
                console.log("Actual name: " + msg)
                playerName = msg.body;
                console.log("Player name :" + playerName);
            });
        });
    }


    connect();
    // TODO: After connecting get players already in the game

    function showPlayers() {
        $('#players').html(playerList.map(function (p) {
            pid = p.split(' ').join('_');
            return '<div class="player" id="name_' + pid + '">'
            + '<i class="material-icons prefix">account_circle</i><br>'
            + p 
            + '<br/>'
            // + '<span  id="lastcat_' + pid + '">&nbsp;</span> : '
            + '<span  id="lastscore_' + pid + '">&nbsp;</span>'
            + '<br/>'
            + '<span class="score" id="score_' + pid + '">0</span>'
            + '</div>';
        }))
    }

    /*
    Handle for all players the game broadcast
    */
    function handleGame(game) {
        $("#startBtn").prop("disabled", true);
        $('#startContainer').hide();
        $("#gameControl").show();

        showscores(game);

        if (game.currentPlayer == playerName) {
            disableControl(false);
            updateScoreSheet(game);
            $('#keepBtn').prop("disabled", game.roll >= 3);

            if (game.categoryName != "") {
                $('#' + game.categoryName).html(game.score);
            }
        } else {
            disableControl(true);
            
            $('#categoryScored').html(game.categoryName);
            
                
            $('#score').html(game.score);
        }
        console.log("stop animation");
        stopAnimation = true;
        sleep(100).then(() => {
            displayDice(game.dice);
        });
    }

    $("#addPlayerBtn").click(function () {
        var name = $("#playerName").val();

        if(name.match(/^[0-9a-zA-Z]{1,16}$/)){
            playerName = name;

            if (name != "") {
                stompClient.send("/app/player", {}, JSON.stringify({ 'name': name }));
                $("#addPlayerBtn").prop("disabled", true);
                $('#nameenter').hide();
                $("#playerList").show();
                $('#startBtn').show();
                entered = true;
            }        }
        else{
            alert("Invalid name");
        }
    });

    $("#startBtn").click(function () {
        startGame();
    });

    $("#restartBtn").click(function () {
        startGame();
    });

    function startGame() {
        showPlayers();
        end = false;
        stompClient.send("/app/start", {});
        $('#startContainer').hide();
        $('#restartBtn').hide();
        $('.catscore').html(""); // clear scores
    }

    function showscores(game) {
        playerList.forEach(p => {
            p = p.split(' ').join('_');
            $("#name_" + p).removeClass("current");
        });
        var cPlayer = game.currentPlayer.split(' ').join('_');

        $("#name_" + cPlayer).addClass("current");

        $("#score_" + cPlayer).html(game.sheet.content.TS);

        if (game.categoryName != "") {
            // $("#lastcat_" + cPlayer).html(game.categoryName);
            $("#lastscore_" + cPlayer).html(game.categoryName +
                " : " + game.score);
        }
    };


    function succesFun(result) {
        $("#div1").html(result);
    }

    $('.die').click(function () {
        if (disabled) return;
        console.log(this.id);
        if ($('#' + this.id).hasClass('dieKeep')) {
            $('#' + this.id).removeClass('dieKeep');
        } else {
            $('#' + this.id).addClass('dieKeep');
        }
    })

    $("#keepBtn").click(function () {
        var keepArray = [];
        for (i = 1; i < 6; i++) {
            if ($('#die' + i).hasClass('dieKeep')) {
                // if ($("#keep" + i).is(':checked')) {
                keepArray.push(i);
            }
            // var keepValue = keepArray.join();
        }
        stompClient.send("/app/roll", {}, JSON.stringify({ 'name': playerName, 'keep': keepArray, msDelay: MS_DELAY }));
    });

    $(".catscore").click(function () {
        if (disabled) return; // not your turn!

        var tid = this.id;

        if ($('#' + tid).html() != '') {
            return;
        }
        stompClient.send("/app/score", {}, JSON.stringify({ 'name': playerName, 'category': this.id }));

        clearKeep();
    });

    function disableControl(boolean) {
        console.log("Disable controls :" + boolean)

        $('#keepBtn').prop("disabled", boolean);
        $('.keep').prop("disabled", boolean);
        disabled = boolean;
    }

    function clearKeep() {
        // $('.keep').prop('checked', false);
        for (i = 1; i <= 5; i++) {
            $('#die' + i).removeClass('dieKeep');
        }
    }

    function updateScoreSheet(game) {
        console.log("UpdateScoreSheet: " + game.sheet.content.UB + ", " + game.sheet.content.US + ", " + game.sheet.content.YB + ", " + game.sheet.content.TS);
        $('#UB').html(game.sheet.content.UB);
        $('#US').html(game.sheet.content.US);
        $('#YB').html(game.sheet.content.YB);
        $('#TS').html(game.sheet.content.TS);
    }

    function displayDice(dice) {
        for (i = 0; i < 5; i++) {
            $('#die' + (i + 1)).html(dice[i]);
        }
    }

    function endGame(message) {
        disableControl(true);
        $('#restartBtn').show();
    }

    // sleep time expects milliseconds
    function sleep(time) {
        return new Promise((resolve) => setTimeout(resolve, time));
    }

    function recursiveRollAnimation(keepArray, step) {
        if (stopAnimation) {
            return;
        }
        sleep(50).then(() => {
            if (stopAnimation)
                return;
            for (i = 1; i <= 5 && !stopAnimation; i++) {
                if (!keepArray.includes(i)) {
                    $('#die' + i).html(Math.floor(Math.random() * 5) + 1);
                }
            }
            recursiveRollAnimation(keepArray, step - 1);
        });
    }

    function rollAnimation(keepArray) {
        console.log('disable keep');
        // $('#keepBtn').prop("disabled", true);
        disableControl(true);
        console.log("start animation");
        stopAnimation = false;
        recursiveRollAnimation(keepArray, 19)
    }

});