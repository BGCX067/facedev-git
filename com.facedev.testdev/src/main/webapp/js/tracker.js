
        		(function(window ,undefined) {
        			'use strict';
        			var document = window.document,
        				location = window.location;
        		
        		 
        	var noFn = function(){},
	console = window.console || {
	log: noFn
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
			if (typeof(construct) !== 'function') {
				props = construct||props;
				construct = undefined;
			}
			
			var res = function() {
				this.sup = superClass;
				if (construct) construct.apply(this, arguments);
				if (props) FD.copy(this, props);
			};
			
			var fn = function(){};
			fn.prototype = superClass.prototype;
			res.prototype = new fn();
			res.prototype.constructor = res;
			res.sup = superClass.prototype;
			
			return res;
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


FD.View.register(new (FD.extend(FD.View, function() {
	this.sup.call(this, 'tracker.home');
}, {
	render: function() {
		this.sup.render.call(this);
		console.log('yep!: ' + this._nm);
	}
}))(), true);

$(document).ready(FD.View.synch);
        		})(window);
        	