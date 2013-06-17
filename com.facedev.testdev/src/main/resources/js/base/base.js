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