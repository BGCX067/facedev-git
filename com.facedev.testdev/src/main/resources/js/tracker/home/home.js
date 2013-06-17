(function() {

var trackerHome = FD.extend(FD.View, function() {
	this.sup.call(this, 'tracker.home');
	this._mEl = $('#homeItem'),
	this._htm = this._mEl.html();
}, {
	render: function() {
		var me = this,
			el = me._mEl;
		
		me.sup.render.call(me);
		el.html(el.children(":first").html());
	},
	clean: function() {
		var me = this;
		me.sup.clean.call(me);
		
		me._mEl.html(me._htm);
	}
});

$(function() {
	FD.View.register(new trackerHome(), true);
	FD.View.synch();
});

})();