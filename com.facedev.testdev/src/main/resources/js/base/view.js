
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


