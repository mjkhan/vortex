function enterPressed(selector, handler) {
	$(selector).keypress(function(evt) {
		if (evt.which != 13) return;
		handler.apply();
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
			var values = objs.values(checked);
			return values.length > 0 ? values[0] : null;
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
		echo: function(children) {
			objs.target.click(function(){
				var checked = $(this).is(":checked");
				$(children).each(function() {$(this).prop("checked", checked).change()});
			});
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

/*message("element").set(msg);
 */
function message(id) {
	var msg = {
		elem: $("#" + id),
		set: function(s) {
			if ("text" != msg.elem.attr("type")) {
				msg.elem.html(s).show();
				setTimeout(function() {
					msg.elem.hide({effect:"fade"}).html(null);
				}, 2000);
			} else {
				msg.elem.val(s).focus().select();
			}
		}
	};
	return msg;
}
