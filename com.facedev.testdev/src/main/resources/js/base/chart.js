(function() {
	
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

})();