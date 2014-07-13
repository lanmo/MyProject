<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<html>
	<script type="text/javascript" src="/js/jquery-1.11.0.min.js"></script>
		<script type="text/javascript">
			$(function() {
				function longPolling() {
					var username = $("#user").val();
					
					$.ajax({
						url:"/servlet/chat",
						type:"post",
						dataType:"json",
						data:{"username":username,"action":"longPolling"},
						success:function(data, textStatus){
							
							var label="<br><label>"+data.from+":"+data.chat+"</label>";
							$("#messages").append(label);
						},
						complete:function(XMLHttpRequest, textStatus){
							console.log("textStatus:"+textStatus);
							longPolling();
						},
						error:function(XMLHttpRequest, textStatus, errorThrown){
							
						}
					});
				}
				
				$("#send").click(function() {
					var username = $("#user").val();
					if(username == '') {
						alert("不能为空!!!");
						return;
					}
					
					var message = $("#message").val();
					if(message == null) {
						alert("不能为空!!!");
						return;

					}
					
					$.ajax({
						url:"/servlet/chat",
						type:"post",
						dataType:"json",
						data:{"action":"chat","username":username,"message":message},
						success:function(data, textStatus){
						},
						complete:function(XMLHttpRequest, textStatus){
						},
						error:function(XMLHttpRequest, textStatus, errorThrown){
							
						}
					});
				});
				
				$("#join").click(function() {
					
					var username = $("#username").val();
					if(username == '') {
						alert("不能为空!!!");
						return;
					}
					
					$("#user").val(username);
					$.ajax({
						url:"/servlet/chat",
						type:"post",
						dataType:"json",
						data:{"action":"join","username":username},
						success:function(data, textStatus){
							var label = "<label>"+data.username+"&nbsp;：加入了聊天室！</label>";
							$("#messages").append(label);
							$("#join").attr("disabled",true);
							$("#send").attr("disabled",false);
							longPolling();
						},
						complete:function(XMLHttpRequest, textStatus){
						},
						error:function(XMLHttpRequest, textStatus, errorThrown){
							
						}
					});
				});
			});
		</script>
	<body>
		<h2>Comet Demo!</h2>
		<div id="messages" style="height: 200px;width:50%; border: 1px solid red;"></div>
		<table>
			<tr>
				<td>发送消息:</td>
				<td><input id="message" size="100"></td>
				<td><input type="button" id="send" value="发送" disabled="disabled"></td>
			</tr>
			
			<tr id="userinfo">
				<td>昵称：</td>
				<td><input id="username" size="100"></td>
				<td><input type="button" id="join" value="join"></td>
			</tr>
			
			<input type="hidden" id="user" value="asy"/>
		</table>
	</body>
</html>
