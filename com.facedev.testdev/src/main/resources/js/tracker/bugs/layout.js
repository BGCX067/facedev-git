FD.ns('FD.Track.BugsLayout', FD.extend(FD.Layout, function() {
	FD.Track.HomeLayout.supr.constructor.call(this, 'border');
	
	var toolbar = $('<div>').css({
		height: '50px', 
		background: '#faa'
	}).html('Toolbar');
	this.add(toolbar, 'north');
	
	var list = $('<div>').html('List of bugs');
	
	var bugsGrid = new FD.Grid({
		headers:[{
			name:'id',
			title:'ID'
		}, {
			name: 'title',
			title:'Title'
		}, {
			name:'version',
			title: 'Version'
		}, {
			name: 'assign',
			title: 'Assignee'
		}],
		data:[
		      { id: 1, title: 'Bug #1', version: '0.0.1', assign: 'Alex'},
		      { id: 2, title: 'Bug #2', version: '0.0.1', assign: 'Alex'}, 
		      { id: 3, title: 'Bug #3', version: '0.0.2', assign: 'Alex'},
		      { id: 4, title: 'Bug #4', version: '0.0.2', assign: 'Alex'},
		      { id: 5, title: 'Bug #5', version: '0.0.2', assign: 'Alex'},
		      { id: 6, title: 'Bug #6', version: '0.0.2', assign: 'Alex'}
		]
	});
	bugsGrid.render(list);
	
	this.add(list, 'center');
}));