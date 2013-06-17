
var fd_view_regex = /([\#\&])vw\=([^\&]*)/gi;

FD.ns('FD.View', FD.extend({
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
	}
},

function(name){
	this._nm = name;
}));

(function () {
	var registry = {},
		defaultView = null,
		register = function(vw, dflt) {
			if (registry[vw._nm]) {
				console.log(vw._nm);
			}
			registry[vw._nm] = vw;
			if (dflt) {
				defaultView = vw;
			}
		},
		render = function(name) {
			(name ? registry[name]||defaultView : defaultView).render();
		},
		synch = function() {
			var parts = fd_view_regex.exec(location.hash);
			render(parts && parts[2]);
		};
	
	FD.ns('FD.View.render', render);
	FD.ns('FD.View.register', register);
	FD.ns('FD.View.synch', synch);
})();


