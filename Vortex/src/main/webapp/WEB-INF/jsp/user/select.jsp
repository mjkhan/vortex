<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<%	String type = request.getParameter("type");
	if (type == null)
		type = "checkbox";
	pageContext.setAttribute("type", type);
%>
<div class="inputArea">
	<select id="_field">
		<option value="">검색조건</option>
		<option value="USER_ID">아이디</option>
		<option value="USER_NAME">이름</option>
		<option value="ALIAS">별명</option>
	</select>
	<input id="_value" type="search" placeholder="검색어" style="width:40%;"/>
	<button onclick="userInfo.get(0);" type="button">찾기</button>
</div>
<table class="infoList">
	<thead>
		<tr><th width="10%"><c:if test="${'checkbox' == type}"><input id="_toggleUsers" type="${type}" /></c:if></th>
			<th width="20%">아이디</th>
			<th width="30%">이름</th>
			<th width="20%">별명</th>
		</tr>
	</thead>
	<tbody id="_userList">
		<c:set var="notFound"><tr><td colspan="4" class="notFound">사용자를 찾지 못했습니다.</td></c:set>
		<c:set var="userRow"><tr>
			<td><input name="_userID" value="{userID}" type="${type}" /></td>
			<td>{userID}</td>
			<td>{userName}</td>
			<td>{alias}</td>
		</tr></c:set>
	</tbody>
</table>
<div class="more">
	<button onclick="" type="button">더 보기</button>
</div>
<script type="text/javascript">
var userInfo = {
	get:function(start){
		var field = $("#_field").val(),
			value = $("#_value").val();
		if (value && !field)
			return alert("검색조건을 선택하십시오.");
		ajax({
			url:"<c:url value='/user/select.do'/>",
			data:{
				field:field,
				value:value,
				start:start || 0
			},
			success:function(resp) {
				userInfo.set(resp, start);
			}
		});
	},
	set:function(resp, start){
		var users = resp.users,
			append = start > 0;
		$("#_userList").populate({
			data:users,
			tr:function(row){
				return "${vtx:jstring(userRow)}"
					.replace(/{userID}/g, row.USER_ID)
					.replace(/{userName}/g, row.USER_NAME)
					.replace(/{alias}/g, row.ALIAS);
			},
			ifEmpty:"${vtx:jstring(notFound)}",
			append:append
		});

		if (resp.more) {
			$(".more button")
				.removeAttr("onclick")
				.attr("onclick", "userInfo.get(" + resp.next + ")");
			$(".more").show();
		} else {
			$(".more").hide();
		}
		<c:if test="${'checkbox' == type}">
		userInfo.value = function() {
			var userIDs = checkbox("input[name='_userID']").value();
			log("userIDs: " + userIDs);
			return elementsOf(users, "USER_ID", userIDs);
		};
		checkbox("#_toggleActions").onChange(function(checked){
			userInfo.checked.check(checked);
		});
		</c:if>
		<c:if test="${'radio' == type}">
		userInfo.value = function() {
			var userID = $("input[name='_userID']:checked").val();
			return elementsOf(user, "USER_ID", userID)[0];
		};
		</c:if>
		showOK(users && users.length);
	}
};

$(function(){
	userInfo.set({
		users:<vtx:json data="${users}" mapper="${objectMapper}"/>,
		more:${more},
		next:${next}
	}, 0);
});
</script>