(function() {
	
var renderers = {
	line: function(data, ctx, width, height) {
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
		
		var pad = 5,
			xPad = 35,
			yPad = 30,
			w = width - yPad - pad,
			h = height - xPad - pad,
			axisX = function() {
				var y = h + pad,
					maxX = yPad + w;
				ctx.line(yPad, y, maxX, y, '#111');
				for (var x = yPad; x <= maxX; x += w/5) {
					ctx.line(x, y, x, y + 3, '#111');
				}
			},
			axisY = function() {
				var maxY = h + pad;
				ctx.line(yPad, pad, yPad, maxY, '#111');
				for (var y = pad; y < maxY; y+= h / 5) {
					ctx.line(yPad, y, yPad - 3, y, '#111');
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

var canvasMessage = function(txt, el) {
	var div = el.find('div.chartMessage');
	if (!div.length) {
		div = $('<div>').addClass('chartMessage').css('top',isIE ? '45%' : '-55%');
		el.append(div);
	}
	div.html(txt);
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
				background: (color||'red'),
				position: 'absolute',
				top: y+'px', left: x+'px', 
				width: '1px', height: '1px'
			});
			pseudoCanvas.append(pt);
		},
		line: function(x0, y0, x1, y1, color) {

			if (x0 > x1) {
				var tmp = x0;
				x0 = x1;
				x1 = tmp;
				tmp = y0;
				y0 = y1;
				y1 = tmp;
			}
			color = color||'red';
			var prevX = x0, prevY = y0;
			for (var x = x0, y = y0;
				x <= x1; x++) {
				if (x == x1) {
					y = y1;
				} else {
					y = Math.floor(((x - x0) * (y1 - y0) / (x1 - x0)) + y0);
					if (y == prevY) {
						continue;
					}
				}
				var w = x - prevX,
					h = y - prevY,
					top = prevY;
				if (h < 0) {
					h = -h;
					top = y;
				}
				if (!w) w = 1;
				if (!h) h = 1;
				
				pseudoCanvas.append($('<div>').css({
					background: color,
					position: 'absolute',
					top: top+'px', left: prevX+'px', 
					width: w + 'px', height: h + 'px'
				}));
				prevX = x, prevY = y;
			}
		},
		rect: function() {},
		clear: function() {
			pseudoCanvas.empty();
		},
		message:  function(txt) {
			canvasMessage(txt, el);
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
		line: function(x0, y0, x1, y1, color) {
			context.strokeStyle = color||'red';
			context.beginPath();
			context.moveTo(x0, y0);
			context.lineTo(x1, y1);
			context.closePath();
			context.stroke();
		},
		rect: function() {},
		clear: function() {
			context.clearRect(0, 0, el.width(), el.height());
		},
		message: function(txt) {
			canvasMessage(txt, el);
		}
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
		});
		canvasMessage('Initializing...', me._el);
		to.append(me._el);
		if(me._data) redraw(me._el, me._data, me._type);
	},
	
	update: function(data) {
		var me = this;
		if (data) me._data = data;
		redraw(me._el, me._data, me._type);
	}
}));

})();