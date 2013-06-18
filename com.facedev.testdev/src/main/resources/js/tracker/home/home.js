(function() {

var TrackerHome = FD.extend(FD.Track.View, function() {
	TrackerHome.supr.constructor.call(this, 'tracker.home', '#homeItem');
}, {
	render: function() {
		var me = this,
			vw = me._vw;
		TrackerHome.supr.render.call(me);
		if (me._lay) {
			me._lay.show();
			return;
		}
		
		me._lay = new FD.Track.HomeLayout();
		
		me._lay.render(vw);
	},
	clean: function() {
		var me = this;
		TrackerHome.supr.clean.call(me);
		if (me._lay) me._lay.hide();
	}
});

FD.View.register(new TrackerHome(), true);

$(function() {
	FD.View.init();
	FD.View.synch();
});

})();