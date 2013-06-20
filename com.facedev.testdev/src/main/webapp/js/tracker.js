
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
		Base: {},
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

FD.ns('FD.View', FD.extend(FD.Base,

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

})();(function() {
	
var renderers = {
	line: function(data, ctx, width, height) {
		if (!data) {
			ctx.message("No data to display");
			return;
		}
		var cfg = {};
		if (!data.length) {
			cfg = data;
			data = cfg.data;
		}
		if (!data || !data.length) {
			ctx.message("No data to display");
			return;
		}
		var max = Math.max,
			min = Math.min,
			maxX = data[data.length-1].x||(data.length-1),
			minX = data[0].x||0,
			maxY = 1/-0, minY = 1/0;

		data = $(data);
		data.each(function(i, el) {
			$(el.y||el).each(function(i, el) {
				maxY = max(maxY, el);
				minY = min(minY, el);
			});
		});
		
		if (maxX <= minX) maxX = minX + 1;
		if (maxY <= minY) maxX = minY + 1;
		
		var pad = cfg.padding||5,
			xPad = 35,
			yPad = 30,
			w = width - yPad - pad,
			h = height - xPad - pad,
			numX = cfg.labelX && cfg.labelX.length ? cfg.labelX.length : min(6, max(2, maxX - minX)),
			numY = cfg.labelY && cfg.labelY.length ? cfg.labelY.length : min(6, max(2, maxY - minY)),
			axisText = function(i, len, maxAxis, minAxis) {
				return Math.ceil(((maxAxis - minAxis) * i / len) + minAxis);
			},
			xText = function(i, len) {
				if (typeof(cfg.labelX) === 'function') {
					return cfg.labelX(i, len, function(i, len) {
						return axisText(i, len, maxX, minX);
					});
				}
				if (cfg.labelX && i < cfg.labelX.length) {
					return cfg.labelX[i];
				}
				return axisText(i, len, maxX, minX); 
			},
			yText = function(i, len) {
				i = len - i - 1;
				if (typeof(cfg.labelY) === 'function') {
					return cfg.labelY(i, len, function(i, len) {
						return axisText(i, len, maxY, minY);
					});
				}
				if (cfg.labelY && i < cfg.labelY.length) {
					return cfg.labelY[i];
				}
				return axisText(i, len, maxY, minY);
			},
			textColor = '#111',
			axisX = function() {
				var y = h + pad,
					maxX = yPad + w;
				ctx.line(yPad, y, maxX, y, textColor);
				for (var x = yPad, v = 0; x <= maxX; x += w/(numX - 1), v++) {
					ctx.line(x, y, x, y + 3, textColor);
					var txt = ''+xText(v, numX);
					ctx.text(x - txt.length * 4, y + 15, xText(v, numX), textColor);
				}
			},
			axisY = function() {
				var maxY = h + pad;
				ctx.line(yPad, pad, yPad, maxY, textColor);
				for (var y = pad, v = 0; y <= maxY; y+= h / (numY - 1), v++) {
					ctx.line(yPad, y, yPad - 3, y, textColor);
					ctx.text(2, y + 5, yText(v, numY), textColor);
				}
			},
			scaleX = function(v) {
				return yPad + w * (v - minX) / (maxX - minX);
			},
			scaleY = function(v) {
				return pad + h * (1 - (v - minY) / (maxY - minY));
			},
			prev;
			
		ctx.clear();
		
		axisX();axisY();
		data.each(function(i, el) {
			if (i == 0) {
				prev = el;
				return;
			}
			var x = el.x;
			$(el.y).each(function(i, el) {
				ctx.line(scaleX(prev.x), scaleY(prev.y[i]), 
						scaleX(x), scaleY(el));
			});
			prev = el;
		});
	}
};

var getCanvas = (!window.CanvasRenderingContext2D ? function(el) {
	var div = $('<div>').addClass('chartMessage').css('top', '45%');
	div.html("Your browser doesn't support charts");
	el.empty().append(div);
	return {
		point: noFn,
		line: noFn,
		text: noFn,
		rect: noFn,
		clear: noFn,
		message: noFn
	};
} : function(el) {
	var canvas = $('<canvas>')
			.attr('width', el.width())
			.attr('height', el.height()),
		context = canvas.get(0).getContext('2d'),
		defaultColor = '#000';
	el.empty().append(canvas);
	return {
		point: function(x, y, color) {
			context.fillStyle = color||defaultColor;
			context.fillRect(x, y, 1, 1);
		},
		line: function(x0, y0, x1, y1, color) {
			context.strokeStyle = color||defaultColor;
			context.beginPath();
			context.moveTo(x0, y0);
			context.lineTo(x1, y1);
			context.closePath();
			context.stroke();
		},
		rect: function() {},
		text: function(x, y, text, color, fnt, maxW) {
			context.font = fnt||"12px serif";
			context.fillStyle = color||defaultColor;
			context.fillText(text, x, y, maxW||(el.width()-x));
		},
		clear: function() {
			context.clearRect(0, 0, el.width(), el.height());
		},
		message: function(txt) {
			var div = el.find('div.chartMessage');
			if (!div.length) {
				div = $('<div>').addClass('chartMessage').css('top',isIE ? '45%' : '-55%');
				el.append(div);
			}
			div.html(txt);
		}
	};
});
	
function redraw(el, data, type) {
	var canvas = getCanvas(el),
		renderer = renderers[type];
	renderer(data, canvas, el.width(), el.height());
};
	
FD.ns('FD.Chart', FD.extend(FD.Base, function(cfg, data) {
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
		});
		to.append(me._el);
		redraw(me._el, me._data, me._type);
	},
	
	update: function(data) {
		var me = this;
		if (data !== undefined) me._data = data;
		redraw(me._el, me._data, me._type);
	}
}));

})();FD.ns('FD.Grid', FD.extend(FD.Base, function(cfg, data) {
}, {
	render: function(to) {
		to.html('<table>'+
			'<tr><td>1</td><td>2</td><td>3</td></tr>'+
			'<tr><td>1</td><td>2</td><td>3</td></tr>'+
			'<tr><td>1</td><td>2</td><td>3</td></tr>'+
			'<tr><td>1</td><td>2</td><td>3</td></tr>'+
			'</table>');
	}
}));FD.ns('FD.Track.View', FD.extend(FD.View, function(name, selector) {
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
});FD.ns('FD.Track.BugsLayout', FD.extend(FD.Layout, function() {
	FD.Track.HomeLayout.supr.constructor.call(this, 'border');
	
	var toolbar = $('<div>').css({
		height: '50px', 
		background: '#faa'
	}).html('Toolbar');
	this.add(toolbar, 'north');
	
	var list = $('<div>').css({
		background: '#afa'
	}).html('List of bugs');
	
	var bugsGrid = new FD.Grid();
	bugsGrid.render(list);
	
	this.add(list, 'center');
}));(function() {

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

})();FD.ns('FD.Track.HomeLayout', FD.extend(FD.Layout, function() {
	FD.Track.HomeLayout.supr.constructor.call(this);
	
	var chart = new FD.Chart({
			type:'line',
			width: 400,
			height: 300
		}, {
			padding: 10,
			labelY:['0', '5k', '10k', '15k', '20k'],
			data: [
		       {x: 100, y: [3000, 4000]},
		       {x: 200, y: [6000, 2000]},
		       {x: 300, y: [18000, 3000]},
		       {x: 400, y: [20000, 8000]},
		       {x: 500, y: [10000, 12000]},
		       {x: 600, y: [5000, 15000]}
	       ]
		}),
		container = $('<div>').css({
			padding: '10px',
			background: '#fff'
		});
	chart.render(container);
	this.add(container);
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
        	