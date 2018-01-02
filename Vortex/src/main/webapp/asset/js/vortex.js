$.fn.setPaging = function(config) {
	config.links = 3;
	config.first = function(index, label) {return "<a onclick='{func}(0);'>|◀</a>";};
	config.previous = function(index, label) {return "<a onclick='{func}({index});'>◀</a>".replace(/{index}/, index);};
	config.link = function(index, label) {return "<a onclick='{func}({index});'>{label}</a>".replace(/{index}/, index).replace(/{label}/, label);};
	config.current = function(index, label) {return "<span class='current'>{label}</span>".replace(/{label}/, label);};
	config.next = function(index, label) {return "<a onclick='{func}({index});'>▶</a>".replace(/{index}/, index);};
	config.last = function(index, label) {return "<a onclick='{func}({index});'>▶|</a>".replace(/{index}/, index);};

	return this.each(function(){
		var tag = paginate(config),
			container = $(this);
		if (tag)
			container.html(tag.replace(/{func}/g, config.func)).show();
		else {
			if (config.hideIfEmpty != false)
				container.hide();
		}
	});
}