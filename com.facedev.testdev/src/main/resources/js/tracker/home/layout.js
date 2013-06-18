FD.ns('FD.Track.HomeLayout', FD.extend(FD.Layout, function() {
	FD.Track.HomeLayout.supr.constructor.call(this);
	
	var d1 = $('<div>').css({
		width: '800px',
		height: '200px',
		background: '#eee',
	}).html('Bla bla bla bla asdf asjd fjasd lfjasld fja;sld fj;lasdjf');
	this.add(d1);
	
	var d2 = $('<div>').css({
		width: '700px',
		height: '400px', 
		background: '#faa',
	}).html('Ehehda jfas ;fjas; dja;slkjd f;asjdf;ajs d;fjas;dfjas; dfjas');
	this.add(d2);
	
	var d3 = $('<div>').css({
		width: '600px',
		height: '300px',
		background: '#afa',
	}).html('eiowtroisdfgasl asdlkj flkas asdflk fjalsdj f;asj df;asj ;fla');
	this.add(d3);
}));