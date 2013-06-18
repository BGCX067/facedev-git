(function() {
	
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

})();