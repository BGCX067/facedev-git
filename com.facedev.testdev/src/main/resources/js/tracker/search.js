$(function() {
	var el = $('#search'),
		tip = $('#searchTip');
	
	if (el.val()) {
		tip.hide();
	}
	
	tip.click(function() {
		el.focus();
	});
	
	el.focus(function() {
		tip.hide();
	});
	
	el.blur(function() {
		if (!el.val()) tip.show();
	});
});