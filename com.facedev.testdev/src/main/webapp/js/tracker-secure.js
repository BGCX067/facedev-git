
        		(function(window ,undefined) {
        			'use strict';
        			var document = window.document,
        				location = window.location;
        		
    			
        	(function() {

var TrackerIdeas = FD.extend(FD.Track.View, function() {
	TrackerIdeas.supr.constructor.call(this, 'tracker.ideas', '#ideasItem');
});

FD.View.register(new TrackerIdeas());

})();(function() {

var TrackerReqs = FD.extend(FD.Track.View, function() {
	TrackerReqs.supr.constructor.call(this, 'tracker.requirements', '#rqsItem');
});

FD.View.register(new TrackerReqs());

})();(function() {

var TrackerTests = FD.extend(FD.Track.View, function() {
	TrackerTests.supr.constructor.call(this, 'tracker.tests', '#testsItem');
});

FD.View.register(new TrackerTests());

})();
        		})(this);
        	