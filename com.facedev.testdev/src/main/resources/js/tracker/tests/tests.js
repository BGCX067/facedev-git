(function() {

var TrackerTests = FD.extend(FD.Track.View, function() {
	TrackerTests.supr.constructor.call(this, 'tracker.tests', '#testsItem');
});

FD.View.register(new TrackerTests());

})();