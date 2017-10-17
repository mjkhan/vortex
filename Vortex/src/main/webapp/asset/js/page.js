$.fn.onEnterPress = function(handler) {
	return this.each(function(){
		$(this).keypress(function(evt){
			if (!handler || evt.which != 13) return;
			handler.apply();
		});
	});
}

function emptyRequired(whenEmpty) {
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
			$("#_ok").click(options.onOK);
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