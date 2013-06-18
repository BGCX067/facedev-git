(function() {

var TrackerIdeas = FD.extend(FD.Track.View, function() {
	TrackerIdeas.supr.constructor.call(this, 'tracker.ideas', '#ideasItem');
});

FD.View.register(new TrackerIdeas());

})();