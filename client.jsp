<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Client</title>
<script type="text/javascript">
var ws = null;
function startWebSocket() {
	if ('WebSocket' in window){
		ws = new WebSocket("ws://192.168.11.24:8080/PubSub_Web/index.do?ID="+document.getElementById('ID').value+"&character=subscriber");
		
	}
	else if ('MozWebSocket' in window)
		ws = new MozWebSocket("ws://192.168.11.24:8080/PubSub_Web/index.do?ID="+document.getElementById('ID').value+"&character=subscriber");
	else
		alert("not support");
	
	
	ws.onmessage = function(evt) {
		alert(evt.data);
	};
	
	ws.onopen = function(evt) {
		alert("open");
		ws.send("SUBSCRIBE:"+document.getElementById('ID').value);
		//連上時，就送出要訂閱“自己”這個頻道的訊息 
	};
	
	ws.onclose = function(evt) {
		alert(evt);
	};
	
	
	
}

function sendMsg() {
	ws.send("SUBSCRIBE:"+document.getElementById('channel').value);
}

</script>
</head>
<body>
<input type="button" value="Connect" onclick="startWebSocket()"></input>
<h3>Who are you:</h3><input type="text" id="ID"></input>
<h3>Which channel you want to subscribe:</h3><input type="text" id="channel"></input>
<input type="button" value="send" onclick="sendMsg()"></input>
</body>
</html>