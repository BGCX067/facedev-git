FD.ns('FD.Track.HomeLayout', FD.extend(FD.Layout, function() {
	FD.Track.HomeLayout.supr.constructor.call(this);
	
	var chart = new FD.Chart({
			type:'line',
			width: 400,
			height: 300
		}, {
			padding: 10,
			labelY:['0', '5k', '10k', '15k', '20k'],
			data: [
		       {x: 100, y: [3000, 4000]},
		       {x: 200, y: [6000, 2000]},
		       {x: 300, y: [18000, 3000]},
		       {x: 400, y: [20000, 8000]},
		       {x: 500, y: [10000, 12000]},
		       {x: 600, y: [5000, 15000]}
	       ]
		}),
		container = $('<div>').css({
			padding: '10px',
			background: '#fff'
		});
	chart.render(container);
	this.add(container);
}));