FD.ns('FD.Track.HomeLayout', FD.extend(FD.Layout, function() {
	FD.Track.HomeLayout.supr.constructor.call(this);
	
	var chart = new FD.Chart('line', [
	       {x: 100, y: [3000, 4000]},
	       {x: 200, y: [6000, 2000]},
	       {x: 300, y: [18000, 3000]},
	       {x: 400, y: [20000, 8000]},
	       {x: 500, y: [10000, 12000]},
	       {x: 600, y: [5000, 15000]}
	    ]),
		container = $('<div>').css({
			padding: '10px',
			background: '#fff'
		});
	chart.render(container);
	this.add(container);
	
//	var d2 = $('<div>').css({
//		width: '700px',
//		height: '400px', 
//		background: '#faa',
//	}).html('Ehehda jfas ;fjas; dja;slkjd f;asjdf;ajs d;fjas;dfjas; dfjas');
//	this.add(d2);
//	
//	var d3 = $('<div>').css({
//		width: '600px',
//		height: '300px',
//		background: '#afa',
//	}).html('eiowtroisdfgasl asdlkj flkas asdflk fjalsdj f;asj df;asj ;fla');
//	this.add(d3);
}));