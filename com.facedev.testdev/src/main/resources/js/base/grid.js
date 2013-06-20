FD.ns('FD.Grid', FD.extend(FD.Base, function(cfg, data) {
}, {
	render: function(to) {
		to.html('<table>'+
			'<tr><td>1</td><td>2</td><td>3</td></tr>'+
			'<tr><td>1</td><td>2</td><td>3</td></tr>'+
			'<tr><td>1</td><td>2</td><td>3</td></tr>'+
			'<tr><td>1</td><td>2</td><td>3</td></tr>'+
			'</table>');
	}
}));