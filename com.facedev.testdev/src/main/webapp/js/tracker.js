
        		(function(window, undefined) {
        			'use strict';
        			var document = window.document,
        				location = window.location;
        		
        		 
        	var noFn = function(){},
	console = window.console || {
		log: noFn
	},
	isIE = (window.navigator.appName.indexOf("Internet Explorer")!=-1);

Function.prototype.delay = function(delay, scope) {
	var me = this;
	setTimeout(function() {
		me.call(scope);
	}, delay);
};

var FD = window.FD = (function() {
	return {
		copy: function(target, src, over) {
			for (var k in src) {
				if (target[k] !== undefined && !over) {
					continue;
				}
				target[k] = src[k];
			}
		},
		extend: function(superClass, construct, props) {
			var fn = function(){};
			fn.prototype = superClass.prototype; 
			construct.prototype = new fn();
			construct.prototype.constructor = construct;
			construct.supr = superClass.prototype;
			
			if (props) FD.copy(construct.prototype, props, true);
			
			return construct;
		},
		
		ns: function(name, def) {
			var me = window,
				names = name.split('.'),
				i = 0,
				len = names.length - 1;
			while (i < len) {
				var nm = names[i++];
				if (me[nm] === undefined) {
					me[nm] = {};
				}
				me = me[nm];
			}
			me[names[i]] = def || {};
		}
	};
}) ();
var fd_view_regex = /([\#\&])vw\=([^\&]*)/gi;

FD.ns('FD.View', FD.extend({},

function(name){
	this._nm = name;
}, {
	render: function() {
		var hash = location.hash,
			val = 'vw=' + this._nm;
		if (!hash) {
			location.hash = '#' + val;
			return;
		}
		var parts = hash.split(fd_view_regex),
			result = [parts[0], parts[1]||'&', val, parts[3]||''];

		location.hash = result.join('');
	},
	clean: noFn,
	getName: function() { return this._nm; }
}));

(function () {
	var registry = {},
		defaultView = null,
		currentView = null,
		register = function(vw, dflt) {
			if (registry[vw._nm]) {
				console.log(vw._nm + ' exists!');
				return;
			}
			registry[vw._nm] = vw;
			if (dflt) {
				defaultView = vw;
			}
		},
		render = function(name) {
			var vw = name ? registry[name]||defaultView : defaultView;
			if (currentView !== vw) {
				if (currentView) currentView.clean();
				currentView = vw;
				vw.render();
			}
		},
		synch = function() {
			var parts = fd_view_regex.exec(location.hash);
			render(parts && parts[2]);
		},
		init = function() {
			for (var k in registry) {
				registry[k].init();
			}
		};
		
	
	FD.ns('FD.View.render', render);
	FD.ns('FD.View.current', function() { return currentView; });
	FD.ns('FD.View.register', register);
	FD.ns('FD.View.synch', synch);
	FD.ns('FD.View.init', init);
})();


(function() {
	
var managers = {
	flow: function(cn, el, cfg) {
		if (!cn) cn = $('<div>');
		if (el === undefined) return cn;
		el.css('float', cfg||'left');
		cn.append(el);
		return cn;
	}
};

FD.ns('FD.Layout', FD.extend({}, function(cfg) {
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
		if (!me._cn) me._cn = me._mgr();
		to.append(me._cn);
	},
	
	add: function(el, cfg) {
		var me = this;
		me._cn = me._mgr(me._cn, el, cfg);
	}
}));

})();(function() {
	
var renderers = {
	line: function(data, ctx, w, h) {
		for(var x = 0; x < 200; x++) {
			var y = (x * x/100);
			if (y>h) continue;
			ctx.point(x, Math.floor(y));
		}
	}
};

var getCanvas = (!window.CanvasRenderingContext2D ? function(el) {
	var pseudoCanvas = $('<div>').css({
		position:'absolute',
		width: el.css('width'),
		height: el.css('height')
	});
	el.empty().append(pseudoCanvas);
	return {
		point: function(x, y, color) {
			var pt = $('<div>').css({
				borderLeft: '1px solid ' + (color||'red'),
				position: 'absolute',
				top: y+'px', left: x+'px', 
				width: '1px', height: '1px'
			});
			pseudoCanvas.append(pt);
		},
		line: function(x0, y0, x1, y1, color) {
			// (x - x0) / (x1 - x0) = (y - y0) / (y1 - y0) => y = ((x - x0) * (y1 - y0) / (x1 - x0)) + y0
		},
		rect: function() {},
		clear: function() {
			pseudoCanvas.empty();
		} 
	};
} : function(el) {
	var canvas = $('<canvas>')
			.attr('width', el.width())
			.attr('height', el.height()),
		context = canvas.get(0).getContext('2d');
	el.empty().append(canvas);
	return {
		point: function(x, y, color) {
			context.fillStyle = color||'red';
			context.fillRect(x, y, 1, 1);
		},
		line: function() {},
		rect: function() {},
		clear: function() {} 
	};
});
	
function redraw(el, data, type) {
	var canvas = getCanvas(el),
		renderer = renderers[type];
	renderer(data, canvas, el.width(), el.height());
};
	
FD.ns('FD.Chart', FD.extend({}, function(cfg, data) {
	if (typeof(cfg) === 'string') {
		cfg = { type : cfg };
	}
	var me = this;
	me._data = cfg.data||data;
	me._type = cfg.type;
	me._cfg = {
		w:cfg.width||200,
		h:cfg.height||200
	}; 
}, {
	render: function(to) {
		var me = this,
			cfg = me._cfg;
		me._el = $('<div>').addClass('chart').css({
			width: cfg.w,
			height: cfg.h,
		}).html('Initializing...');
		to.append(me._el);
		if(me._data) redraw(me._el, me._data, me._type);
	},
	
	update: function(data) {
		var me = this;
		if (data) me._data = data;
		redraw(me._el, me._data, me._type);
	}
}));

})();FD.ns('FD.Track.View', FD.extend(FD.View, function(name, selector) {
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
}));$(function() {
	var el = $('#search'),
		tip = $('#searchTip');
	
	if (el.val()) {
		tip.hide();
	}
	
	tip.click(function() {
		el.focus();
	});
	
	el.focus(function() {
		tip.hide();
	});
	
	el.blur(function() {
		if (!el.val()) tip.show();
	});
});(function() {

var TrackerBugs = FD.extend(FD.Track.View, function() {
	TrackerBugs.supr.constructor.call(this, 'tracker.bugs', '#bugsItem');
});

FD.View.register(new TrackerBugs());

})();FD.ns('FD.Track.HomeLayout', FD.extend(FD.Layout, function() {
	FD.Track.HomeLayout.supr.constructor.call(this);
	
	var chart = new FD.Chart('line', []),
		container = $('<div>').css({
			padding: '10px',
			background: '#fff'
		});
	chart.render(container);
	this.add(container);
	
//	var d2 = $('<div>').css({
//		width: '700px',
//		height: '400px', 
//		background: '#faa',
//	}).html('Ehehda jfas ;fjas; dja;slkjd f;asjdf;ajs d;fjas;dfjas; dfjas');
//	this.add(d2);
//	
//	var d3 = $('<div>').css({
//		width: '600px',
//		height: '300px',
//		background: '#afa',
//	}).html('eiowtroisdfgasl asdlkj flkas asdflk fjalsdj f;asj df;asj ;fla');
//	this.add(d3);
}));(function() {

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
        		})(this);
        	