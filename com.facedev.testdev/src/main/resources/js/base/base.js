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