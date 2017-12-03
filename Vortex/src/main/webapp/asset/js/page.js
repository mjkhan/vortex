$.fn.onEnterPress = function(handler) {
	return this.each(function(){
		$(this).keypress(function(evt){
			if (!handler || evt.which != 13) return;
			handler.apply();
		});
	});
}

function requiredEmpty(whenEmpty) {
	var empty = false;
	$("*[required]").each(function(){
		if (empty) return;
		var input = $(this),
			value = input.val();
		if (empty = isEmpty(value)) {
			if (whenEmpty)
				whenEmpty(input);
			else {
				var label = $("label[for='" + input.attr("id") + "']").html();
				alert(label + "을(를) 입력하십시오.");
			}
			input.focus();
		}
	});
	return empty;
}
/**config = {
 * 	data:[],
 * 	ifEmpty:"...",
 * 	tr:function(row){return ...;},
 * 	append:true || false
 * }
 * $("select tbody").populate(list, options);
 */
$.fn.populate = function(config) {
	return this.each(function(){
		var list = config.data,
			length = !list ? 0 : list.length,
			table = $(this),
			trs = [];
		if (length) {
			for (var i = 0; i < length; ++i) {
				trs.push(config.tr(list[i]));
			}
		} else {
			if (!config.append) {
				trs.push(config.ifEmpty);
			}
		}
		if (!config.append)
			table.html(trs.join());
		else
			table.append(trs.join());
	});
}

function checkbox(selector) {
	var objs = {
		target: $(selector),
		values: function(checked) {
			checked = checked != false;
			var result = [];
			objs.target.each(function(){
				var obj = $(this),
					check = obj.is(":checked");
				if ((checked && check) || (!checked && !check))
					result.push(obj.val());
			});
			return result;
		},
		value: function(checked) {
			var values = objs.values(checked != false);
			return values.length > 0 ? values : null;
		},
		isChecked: function() {
			var checked = false;
			objs.target.each(function(){
				var obj = $(this);
				if (obj.is(":checked"))
					checked = true;
			});
			return checked;
		},
		check:function(checked) {
			objs.target.each(function(){$(this).prop("checked", checked).change();});
			return objs;
		},
		onChange: function(handler) {
			var watch = function() {
				var checked = 0;
				objs.target.each(function(){
					if ($(this).is(":checked"))
						++checked;
				});
				handler(checked);
			};
			objs.target.each(function(){
				$(this).change(function(){watch();});
			});
			return objs;
		}
	};
	return objs;
};

$.fn.message = function(msg) {
	return this.each(function(){
		var elem = $(this);
		if ("text" != elem.attr("type")) {
			elem.html(msg).show();
			setTimeout(function(){
				elem.hide({effect:"fade"}).html(null);
			}, 2000);
		} else {
			elem.val(s).focus().select();
		}
	});
};
/**options = {
 * 	title:"...",
 *  content:"... ",
 *  onclose:function
 * }
 */

var dialog = {
	container:null,
	onclose:null,
	render:function(options) {
		dialog.container.addClass("dialogModal");
		if (options.override)
			dialog.container.html(options.content);
		else {
			$("#_dlgTitle").html(options.title || "Vortex");
			$("#_dlgContent").html(options.content);
		}
		if (options.onOK)
			$("#_ok").off("click").on("click", options.onOK);
		dialog.container.show();
	},
	show:function(options) {
		dialog.onclose = options.onclose;
		if (!dialog.container) {
			dialog.container = $("<div>").appendTo("body");
			ajax({
				url:wctx.path + "/asset/html/dialog.html",
				success:function(resp) {
					dialog.container.html(resp);
					dialog.render(options);
				}
			});
		} else
			dialog.render(options);
	},
	close:function() {
		dialog.container.fadeOut();
		if (dialog.onclose)
			dialog.onclose();
	}
};
/**
 * @param config
 * {start:0,
 *  fetchSize:10,
 *  totalSize:135,
 *  links:3,
 *  first:function(index, label){return "..."},
 *  previous:function(index, label){return "..."},
 *  link:function(index, label){return "..."},
 *  current:function(index, label){return "..."},
 *  next:function(index, label){return "..."},
 *  last:function(index, label){return "..."}
 * }
 * @returns {String}
 */
function paginate(config) {
	var rc = config.totalSize;
	if (!rc) return "";
	
	var	fetchCount = config.fetchSize;
	if (!fetchCount) return "";
	
	var fetch = {
		all:0,
		none:-1,
		count:function(elementCount, size) {
			if (!elementCount || size == fetch.all) return 1;
			return parseInt((elementCount / size) + ((elementCount % size) == 0 ? 0 : 1));
		},
		end:function(elementCount, size, start) {
			if (size < fetch.all) throw "Invalid size: " + size;
			if (elementCount < 0) throw "Invalid elementCount: " + elementCount;
			var last = elementCount - 1;
			if (size == fetch.all) return last;
			return Math.min(last, start + size -1);
		},
		page:function(current, count) {
			return parseInt(count < 1 ? 0 : current / count);
		},
		band:function(page, visibleLinks) {
			return parseInt(visibleLinks < 1 ? 0 : page / visibleLinks);
		}
	};
	var lc = fetch.count(rc, fetchCount);
	if (lc < 2) return "";
	
	var links = ifEmpty(config.links, fetch.all),
		page = fetch.page(ifEmpty(config.start, 0), fetchCount),
		band = fetch.band(page, links),
		tags = {
			link:function(tag, index, label) {
				return !tag ? "" : tag(index, label);
			},
			first:function() {
				return band < 2 ? "" : tags.link(config.first, 0, 1);
			},
			previous:function() {
				if (band < 1) return "";
			    var prevBand = band - 1,
					prevPage = (prevBand * links) + (links - 1),
			        fromRec = prevPage * fetchCount;
			    return tags.link(config.previous, fromRec, prevPage + 1);
			},
			visibleLinks:function() {
				var s = "",
					fromPage = links == fetch.all ? 0 : band * links,
					toPage = links == fetch.all ? lc : Math.min(lc, fromPage + links);
				for (var i = fromPage; i < toPage; ++i) {
					var fromRec = i * fetchCount,
						label = i + 1;
					s += tags.link(i == page ? config.current : config.link, fromRec, label);
				}
				return s;
			},
			next:function(bandCount) {
				bandCount = parseInt(bandCount);
				if (bandCount - band < 2) return "";
		
				var nextBand = band + 1,
					page = nextBand * links,
					fromRec = page * fetchCount;
				return tags.link(config.next, fromRec, page + 1);
			},
			last:function(bandCount) {
				bandCount = parseInt(bandCount);
			    var lastBand = bandCount - 1;
			    if (lastBand - band < 2) return "";
		
			    var pages = lastBand * links,
			        fromRec = pages * fetchCount;
				return tags.link(config.last, fromRec, pages + 1);
			}
		},
		tag = "";
	if (links != fetch.all) {
		tag += tags.first();
		tag += tags.previous();
	}
	tag += tags.visibleLinks();
	if (links != fetch.all) {
        var bandCount = parseInt(lc / links);
        bandCount += lc % links == 0 ? 0 : 1;
		tag += tags.next(bandCount);
		tag += tags.last(bandCount);
	}
	return tag;
}

$.fn.paginate = function(config) {
	return this.each(function(){
		var tag = paginate(config),
			container = $(this);
		container.html(tag);
		if (!tag && config.hideIfEmpty != false)
			container.hide();
	});
}