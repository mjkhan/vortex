function log(msg) {
	if (window.console && window.console.log)
		console.log(msg);
}

function trim(s) {
	if (null == s || undefined == s) return "";
	if ("string" != typeof(s)) return s;
	return s.replace(/^\s+/, "").replace(/\s+$/, "");
}

function isEmpty(v) {
	if (v == undefined || v == "undefined"
	 || v == null || v == "null") return true;
			
	switch (typeof(v)) {
	case "boolean": if (false == v) return false;
	case "number": if (0 == v) return false;
	default: return "" == trim(v);
	}
}

function ifEmpty(v, nv) {
	if (!isEmpty(v)) return v;
	if (typeof(nv) == "function")
		return nv.apply();
	else
		return nv;
}

var number = {
	isValid:function(v, strict) {
		return strict ?
			!isNaN(v) :
			isEmpty(v) || !isNaN(v.replace(/,/gi, ""));
	},
	format:function(v) {
		if (isEmpty(v)) return s;
		
	    var parts = v.toString().split(".");
	    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	    return parts.join(".");
	},
	parse:function(v) {
		if (isEmpty(v))
			return 0;
		return Number(v.replace(/,/gi, ""));
	},
	get:function(id) {
		var v = $("#" + id).val();
		return number.parse(v);
	} 
};

function elementsOf(array, name, values) {
	if (!array || array.length < 1 || !values) return [];
	
	if ("string" == typeof(values))
		values = values.split(",");
	
	var test = function(e) {
		var v = e[name];
		for (var i = 0; i < values.length; ++i) {
			var value = values[i];
			if (v == value) return true;
		}
		return false;
	};
	return array.filter(test);
}

function valuesOf(array, name) {
	if (!array || array.length < 1) return [];
	
	var result = [];
	if (array && array.length > 0)
		for (var i = 0; i < array.length; ++i) {
			result.push(array[i][name]);
		}
	return result;
}

function getParam(name) {
	return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
}

function toQuery(map, encode) {
	if (isEmpty(map)) return "";

	var query = [];
	for (var key in map) {
		var v = map[key];
		if (v != null && v == undefined) continue;
		if (v != null)
		switch (typeof(v)) {
		case "object":
		case "function": continue;
		}
		if (isEmpty(v))
			query.push(key);
		else
			query.push(key + "=" + (encode != false ? encodeURIComponent(v) : v));
	}
	return query.join("&");
}

function Eval(expr, debug) {
	if (debug == true)
		log(expr);
	try {
		return eval(expr);
	} catch (e) {
		alert(e.description + "\n\n" + expr);
		throw e;
	}
}

function wait(show) {
	if (show == false)
		$(".wait").hide();
	else
		$(".wait").show();
}

function onError(xhr, options, error) {
	if (xhr.readyState == 0)
		return alert("서버에 접근할 수 없습니다.");
	
	var resp = JSON.parse(xhr.responseText);
	if (resp.handler)
		return eval(resp.handler);
	
	var	msgs = [];
	for (key in resp)
		msgs.push(resp[key])
	msgs = msgs.join("\n\n");
	if (msgs)
		alert(msgs);
}

function ajax(options) {
	options.beforeSend = function(xhr) {
		wait();
		if (window.csrf)
			xhr.setRequestHeader(csrf.header, csrf.token);
	}
/*
	if (window.csrf) 
	    options.beforeSend = function(xhr){xhr.setRequestHeader(csrf.header, csrf.token);};
*/
	if (!options.type) {
		if (options.data)
			options.type = "POST";
	}
	if (!options.error)
		options.error = onError
	if (!options.complete)
		options.complete = function(){wait(false);};
		
	$.ajax(options);
}
