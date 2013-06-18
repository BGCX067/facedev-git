FD.ns('FD.Track.View', FD.extend(FD.View, function(name, selector) {
	FD.Track.View.supr.constructor.call(this, name);
	this._sel = selector;
}, {
	init: function() {
		var el = $(this._sel);
		this._mEl = el;
		this._vw = $('#viewPort');
		this._htm = el.html();
		el.show();
		this.clean(); 
	},
	render: function() {
		var me = this,
			el = me._mEl;
		
		FD.Track.View.supr.render.call(me);
		el.html(this._htm);
	},
	clean: function() {
		var me = this,
			el = me._mEl,
			anch = $('<a>');
		FD.Track.View.supr.clean.call(me);

		el.empty();
		anch.attr('href', '#');
		anch.html(this._htm);
		el.append(anch);
		anch.click(function() {
			FD.View.render(me.getName());
			return false;
		});
	}
}));