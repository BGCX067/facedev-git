FD.ns('FD.Track.BugsLayout', FD.extend(FD.Layout, function() {
	FD.Track.HomeLayout.supr.constructor.call(this, 'border');
	
	var toolbar = $('<div>').css({
		height: '50px', 
		background: '#faa'
	}).html('Toolbar');
	this.add(toolbar, 'north');
	
	var list = $('<div>').css({
		background: '#afa'
	}).html('List of bugs');
	
	var bugsGrid = new FD.Grid();
	bugsGrid.render(list);
	
	this.add(list, 'center');
}));