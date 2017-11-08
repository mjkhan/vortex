<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div id="searchUsers" style="width:100%;">
	<div class="inputArea">
		<select id="field">
			<option value="">검색조건</option>
			<option value="USER_ID">아이디</option>
			<option value="USER_NAME">이름</option>
			<option value="ALIAS">별명</option>
		 </select>
		 <input id="value" type="search" placeholder="검색어" style="width:40%;"/>
		 <button onclick="getUsers(0);" type="button">찾기</button>
		 <button onclick="test();" type="button">테스트</button>
		 <button onclick="newUser();" type="button" class="add">추가</button>
		 <button id="btnRemove" onclick="removeUsers();" type="button" class="hidden">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
				<th width="20%">아이디</th>
				<th width="30%">이름</th>
				<th width="20%">별명</th>
				<th width="20%">등록</th>
			</tr>
		</thead>
		<tbody id="userList">
			<c:set var="notFound"><tr><td colspan="5" class="notFound">사용자를 찾지 못했습니다.</td></c:set>
			<c:set var="userRow"><tr>
				<td><input name="userID" value="{userID}" type="checkbox" /></td>
				<td><a onclick="getUser('{userID}')">{userID}</a></td>
				<td>{userName}</td>
				<td>{alias}</td>
				<td>{insTime}</td>
			</tr></c:set>
		</tbody>
	</table>
	<div class="paging">
		<button type="button">더 보기</button>
	</div>
</div>
<div id="userDetail" style="padding:1em 0;"></div>
<vtx:script type="decl">
var checkedUsers,
	currentUsers,
	afterSave;
	
function test() {
	ajax({
		url:"<c:url value='/user/lists.do'/>",
		success:function(resp) {
			log(JSON.stringify(resp));
		}
	});
}

function getUsers(start) {
	var field = $("#field").val(),
		value = $("#value").val();
	if (value && !field)
		return alert("검색조건을 선택하십시오.");
	ajax({
		url:"<c:url value='/user/list.do'/>",
		data:{
			field:field,
			value:value,
			start:start || 0
		},
		success:function(resp) {
			setUserList(resp, start);
		}
	});
	currentUsers = function(){getUsers(start);};
}

function removeUsers() {
	if (!confirm("선택한 사용자를 삭제하시겠습니까?")) return;

	ajax({
		url:"<c:url value='/user/remove.do'/>",
		data:{userID:checkedUsers.values().join(",")},
		success:function(resp) {
			if (resp.saved) {
				currentUsers();
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}

function showList(show) {
	if (show == false)
		$("#searchUsers").hide();
	else
		$("#searchUsers").fadeIn();
}

function closeUser() {
	if (afterSave) {
		afterSave();
		afterSave = null;
	}
	$("#userDetail").hide();
	showList();
}

function newUser() {
	ajax({
		url:"<c:url value='/user/info.do'/>",
		success:function(resp) {
			showList(false);
			$("#userDetail").html(resp).fadeIn();
		}
	});
}

function getUser(userID) {
	ajax({
		url:"<c:url value='/user/info.do'/>?userID=" + userID,
		success:function(resp) {
			showList(false);
			$("#userDetail").html(resp).fadeIn();
		}
	});
}

function setUserList(resp, start) {
	var append = start > 0;
	$("#userList").populate({
		data:resp.users,
		tr:function(row){
			return "${vtx:jstring(userRow)}"
				.replace(/{userID}/g, row.USER_ID)
				.replace(/{userName}/g, row.USER_NAME)
				.replace(/{alias}/g, row.ALIAS)
				.replace(/{insTime}/g, row.INS_TIME);
		},
		ifEmpty:"${vtx:jstring(notFound)}",
		append:append
	});
	
	if (!append)
		$("#btnRemove").fadeOut();
	if (resp.more) {
		$(".paging button")
			.removeAttr("onclick")
			.attr("onclick", "getUsers(" + resp.next + ")");
		$(".paging").show();
	} else {
		$(".paging").hide();
	}
	
	checkedUsers = checkbox("input[type='checkbox'][name='userID']")
		.onChange(function(checked){
			if (checked)
				$("#btnRemove").fadeIn();
			else
				$("#btnRemove").fadeOut();
		});
	checkbox("#toggleChecks").onChange(function(checked){checkedUsers.check(checked);});
}

/**
 config = {
 	start:0,
 	fetchSize:10,
 	totalSize:20
 };
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
			return (elementCount / size) + ((elementCount % size) == 0 ? 0 : 1);
		},
		end:function(elementCount, size, start) {
			if (size < fetch.all) throw "Invalid size: " + size;
			if (elementCount < 0) throw "Invalid elementCount: " + elementCount;
			var last = elementCount - 1;
			if (size == fetch.all) return last;
			return Math.min(last, start + size -1);
		},
		page:function(current, count) {
			return count < 1 ? 0 : current / count;
		},
		band:function(page, visibleLinks) {
			return visibleLinks < 1 ? 0 : page / visibleLinks;
		}
	};
	var lc = fetch.count(rc, fetchCount);
	if (lc < 2) return "";
	
	var links = ifEmpty(config.links, fetch.all),
		page = fetch.page(ifEmpty(config.start, 0), fetchCount),
		band = fetch.band(page, links),
		tags = {
			link:function(tag, current) {
				return tag.replace(/{start}/gi, current)
						  .replace(/{end}/gi, fetch.end(rc, fetchCount, current));
			},
			first:function() {
				return band < 2 ? "" : tags.link(config.first, 0);
			},
			previous:function() {
				if (band < 1) return "";
			    var prevBand = band - 1,
					prevPage = (prevBand * links) + (links - 1),
			        fromRec = prevPage * fetchCount;
			    return linkTag(config.previous, fromRec);
			},
			next:function() {},
			last:function() {}
		},
		tag = "";
	if (links != fetch.all) {
		tag += tags.first();
		tag += tags.previous();
	}
	if (links != fetch.all) {
		tag += tags.next();
		tag += tags.last();
	}
	return tag;
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("사용자 정보");
	subTitle("사용자 정보");
	$("#value").onEnterPress(getUsers);
	setUserList({
		users:<vtx:json data="${users}" mapper="${objectMapper}"/>,
		more:${more},
		next:${next}
	}, 0);
	currentUsers = getUsers;
	
	log("paging info: " + paginate({
		start:0,
		fetchSize:10,
		totalSize:23
	}));
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>