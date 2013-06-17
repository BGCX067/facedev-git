FD.View.register(new (FD.extend(FD.View, function() {
	this.sup.call(this, 'tracker.home');
}, {
	render: function() {
		this.sup.render.call(this);
		console.log('yep!: ' + this._nm);
	}
}))(), true);

$(document).ready(FD.View.synch);