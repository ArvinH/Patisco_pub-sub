<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Publisher</title>
<script type="text/javascript">
var ws = null;
function startWebSocket() {
	if ('WebSocket' in window){
		ws = new WebSocket("ws://192.168.11.24:8080/PubSub_Web/index.do?ID=publisher&character=publisher");
		
	}
	else if ('MozWebSocket' in window)
		ws = new MozWebSocket("ws://192.168.11.24:8080/PubSub_Web/index.do?ID=publisher&character=publisher");
	else
		alert("not support");
	
	
	ws.onmessage = function(evt) {
		alert(evt.data);
	};
	
	ws.onopen = function(evt) {
		alert("open");
	};
	
	ws.onclose = function(evt) {
		alert(evt);
	};
}

function sendMsg() {
	ws.send("PUBLISH:"+document.getElementById('channel').value+":"+document.getElementById('writeMsg').value);
}

</script>
</head>
<body>
<h3>Which channel you want to push:</h3><input type="text" id="channel"></input>
<h3>Write down your message:</h3><input type="text" id="writeMsg"></input>
<input type="button" value="Send" onclick="sendMsg()"></input>
<input type="button" value="Connect" onclick="startWebSocket()"></input>
</body>
</html>