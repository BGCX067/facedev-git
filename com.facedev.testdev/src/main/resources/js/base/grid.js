FD.ns('FD.Grid', FD.extend(FD.Base, function(cfg, data) {
	this._data = cfg.data||data||function(){
		return [];
	};
	this._heads = cfg.headers||[];
}, {
	_head: function() {
		var me = this,
			el = me._el,
			headers = me._heads;
		if (!headers.length) {
			return;
		}
		var tr = $('<tr>');
		$(headers).each(function(i, h) {
			var td = $('<td>').html(h.title||h.name||h).addClass(i == 0 ? 'gridHeaderFirst' : 'gridHeader');
			tr.append(td);
		});
		el.append(tr);
	},
	render: function(to) {
		var me = this,
			el = me._el = $('<table>').addClass('grid');
		me.update();
		to.append(el);
	},
	update: function(newData) {
		var me = this,
			el = me._el,
			buffer = [];
		if (newData === undefined) {
			newData = me._data;
		}
		me.clear();
		
		if (!newData || !newData.length) {
			return;
		}
		$(newData).each(function(x, data) {
			var tr = $('<tr>'),
				headers = me._heads;
			if (headers.length && headers[0].name) {
				$(headers).each(function(i, header) {
					var td = $('<td>').addClass(i%2 ? 'gridCellDark' : 'gridCell').html(data[header.name]);
					tr.append(td);
				});
			} else {
				$(data).each(function(i, cell) {
					var td = $('<td>').addClass(i%2 ? 'gridCellDark' : 'gridCell').html(cell);
					tr.append(td);
				});
			}
			buffer.push(tr);
		});
		
		el.append(buffer);
	},
	clear: function() {
		var me = this;
		me._el.empty();
		me._head();
	}
}));