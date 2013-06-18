(function() {

var TrackerReqs = FD.extend(FD.Track.View, function() {
	TrackerReqs.supr.constructor.call(this, 'tracker.requirements', '#rqsItem');
});

FD.View.register(new TrackerReqs());

})();