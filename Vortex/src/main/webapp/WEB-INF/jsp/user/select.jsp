<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
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
		<tr><th width="10%"><input id="_toggleUsers" type="checkbox" /></th>
			<th width="20%">아이디</th>
			<th width="30%">이름</th>
			<th width="20%">별명</th>
		</tr>
	</thead>
	<tbody id="_userList">
		<c:set var="notFound"><tr><td colspan="4" class="notFound">사용자를 찾지 못했습니다.</td></c:set>
		<c:set var="userRow"><tr>
			<td><input name="_userID" value="{userID}" type="checkbox" /></td>
			<td>{userID}</td>
			<td>{userName}</td>
			<td>{alias}</td>
		</tr></c:set>
	</tbody>
</table>
<div class="paging">
	<button type="button">더 보기</button>
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
			$(".paging").show();
		} else {
			$(".paging").hide();
		}
		
		userInfo.checked = checkbox("input[type='checkbox'][name='_userID']");
		checkbox("#_toggleUsers").onChange(function(checked){
			userInfo.checked.check(checked);
		});
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