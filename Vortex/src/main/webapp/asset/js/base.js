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

function isNumber(v, strict) {
	return strict ?
		  !isNaN(v) :
		  isEmpty(v) || !isNaN(v.replace(/,/gi, ""));
}

function numberFormat(x) {
    var parts = x.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
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

function ajax(options) {
	if (window.csrf) 
	    options.beforeSend = function(xhr){xhr.setRequestHeader(csrf.header, csrf.token);};
	if (!options.error)
		options.error = function(req, status, error) {
			log(req.status + "\n\n" + error);
		};
	$.ajax(options);
}

function onException(obj) {
	if (!obj.failed) return;
	
	var msg = [];
	msg.push("요청하신 작업을 수행하지 못했습니다.");
	if (obj.exception)
		msg.push(obj.exception);
	if (obj.message)
		msg.push(obj.message);
	alert(msg.join("\n\n"));
}