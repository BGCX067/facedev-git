FD.ns('FD.Track.HomeLayout', FD.extend(FD.Layout, function() {
	FD.Track.HomeLayout.supr.constructor.call(this);
	
	var chart = new FD.Chart('line', []),
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