(function() {
	
var managers = {
	
	flow: function(cn, el, cfg) {
		if (!cn) cn = $('<div>');
		if (el === undefined) return cn;
		el.css('float', cfg||'left');
		cn.append(el);
		return cn;
	},
	borderSpec: {
		south: function(cn, el) {
			el.css({
				
			});
			cn.before(el);
		},
		center: function(cn, el) {
			el.css('height', '100%');
			cn.append(el);
		},
		north: function(cn, el) {
			cn.prepend(el);
		},
		east: function(cn, el) {
			el.css('float', 'right');
			var ch = cn.children();
			if (ch.length)ch.first().after(el);
			else cn.append(el);
		},
		west: function(cn, el) {
			el.css('float', 'left');
			var ch = cn.children();
			if (ch.length)ch.first().after(el);
			else cn.append(el);
		}
	},
	border: function(cn, el, cfg) {
		if (!cn) {
			cn = $('<div>');
			cn.css('height', '100%');
		}
		if (el === undefined) return cn;
		this.borderSpec[cfg](cn, el);
		return cn;
	}
};

FD.ns('FD.Layout', FD.extend(FD.Base, function(cfg) {
	if (!cfg || typeof(cfg) === 'string') {
		cfg = {mgr: cfg};
	} 
	if (typeof(cfg) === 'function') {
		this._mgr = cfg;
		cfg = {};
	} else {
		this._mgr = managers[cfg.mgr||'flow'];
	}
}, {
	show: function() {
		this._cn.show();
	},
	
	hide: function() {
		this._cn.hide();
	},
	
	render: function(to) {
		var me = this;
		if (!me._cn) me._cn = me._mgr.call(managers);
		to.append(me._cn);
	},
	
	add: function(el, cfg) {
		var me = this;
		me._cn = me._mgr.call(managers, me._cn, el, cfg);
	}
}));

})();