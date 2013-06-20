(function() {

var TrackerBugs = FD.extend(FD.Track.View, function() {
	TrackerBugs.supr.constructor.call(this, 'tracker.bugs', '#bugsItem');
}, {

	render: function() {
		var me = this,
			vw = me._vw;
		TrackerBugs.supr.render.call(me);
		if (me._lay) {
			me._lay.show();
			return;
		}
		
		me._lay = new FD.Track.BugsLayout();
		
		me._lay.render(vw);
	},
	clean: function() {
		var me = this;
		TrackerBugs.supr.clean.call(me);
		if (me._lay) me._lay.hide();
	}
	
});

FD.View.register(new TrackerBugs());

})();