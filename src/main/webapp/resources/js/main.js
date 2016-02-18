function setupMainPage(context) {

	var sendVoteAndLoadNext = function(id, vote, onSuccess) {
		sendVote(id, vote, function() {
			loadNextQa(id, function() {
				if (!!onSuccess) {
					onSuccess();
				}
			});
		});
	}

	var busy = false;
	$('body').keydown(function(ev) {
		if (busy) return;
		var id = getId();
		busy = true;
		if (ev.keyCode == 192) {
			sendVoteAndLoadNext(id, 0, function() {
			 	busy = false;
			});
		} else if (ev.keyCode == 49) {
			sendVoteAndLoadNext(id, 1, function() {
				busy = false;
			});
		} else if (ev.keyCode == 50) {
			sendVoteAndLoadNext(id, 2, function() {
				busy = false;
			});
		} else if (ev.keyCode == 37) {
			loadPrevQa(id, function() {
				busy = false;
			});
		} else if (ev.keyCode == 39) {
			loadNextQa(id, function() {
				busy = false;
			});
		} else {
			busy = false;
		}
	});

	var loadQa = function(id, onSuccess) {
		$.get(context + '/qa/' + id,
    		function(qa) {
    			$('#qa').html('');
    			addQA(qa);
    			if (!!onSuccess) {
    				onSuccess();
    			}
    		}
    	);
	}

	var loadNextQa = function(id, onSuccess) {
		loadQa(id + 1, onSuccess);
	}

	var loadPrevQa = function(id, onSuccess) {
		if (id != 0) {
			loadQa(id - 1, onSuccess);
		} else {
			if (!!onSuccess) {
				onSuccess();
			}
		}
	}

	var skipLoad = false;
	
	var setHash = function(hash) {
		skipLoad = true;
		window.location.hash = hash;
	}
	
	var getHash = function() {
		return window.location.hash;
	}

	var getId = function() {
		var hash = getHash();
		var regexp = /^#qa\/([0-9]+)$/;
		if (regexp.test(hash)) {
			var result = regexp.exec(hash);
			return parseInt(result[1]);
		}
		return 0;
	}

	var loadHash = function() {
		loadQa(getId());
	}

	var sendVote = function(id, vote, onSuccess) {
	    $.post(context + '/qa/' + id,
	    	{"vote" : vote},
	    	function() {
	    		if (!!onSuccess) {
	    			onSuccess();
				}
	    	}
		);
    }

	var addQA = function(qa) {
		var containerId = 'qa_' + qa.id;
		$('#qa').append('<div id="' + containerId + '">' +
				'<input type="button" id="vote0" value="0"/>' +
				'<input type="button" id="vote1" value="1"/>' +
				'<input type="button" id="vote2" value="2"/><br>' +
				'<input type="button" id="prev" value="prev"/>' +
				'<input type="button" id="next" value="next"/><br><br>' +
				// '<span>' + qa.author.name + ' (вопрос: ' + qa.vote + " всего: " + qa.author.total + ')</span><br><br>' +
				'<span> выставлено баллов за вопрос: ' + qa.vote + '</span><br><br>' +
				'<span>' + qa.question + '</span><br><br>' +
				'<span>' + qa.answer + '</span><br>' +
			'</div>');

        var initVoteButton = function(vote) {
            $('#vote' + vote).click(function() {
                sendVote(qa.id, vote, function() {
                    loadQa(qa.id);
                });
            });
        }

        $('#next').click(function() {
            loadNextQa(qa.id);
        });
        $('#prev').click(function() {
            loadPrevQa(qa.id);
        });

        initVoteButton(0);
        initVoteButton(1);
        initVoteButton(2);

		setHash('#qa/' + qa.id);
	}
	
	loadHash();
	
	window.onhashchange = function () {
		if (!skipLoad) {
			loadHash();
		}
		skipLoad = false;
	};
};