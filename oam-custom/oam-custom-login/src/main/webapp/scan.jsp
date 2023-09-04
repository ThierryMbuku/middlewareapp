<html>
    <head>
        <title>Scan Fingerprint</title>
    </head>
    <body onload="connect()">
    	<%
        final String reqId = request.getParameter("request_id");
        %>
        <h1>Scan Fingerprint</h1>
		<script>
			var socket;

			function connect() {
				if (window.WebSocket) {
					socket = new WebSocket("ws://127.0.0.1:49080/silvercipher/api/socket");
					socket.onopen = function () {
						console.log('open');
					};
					socket.onclose = function () {
						console.log('close');
					};
					socket.onmessage = function (event) {
						var result = JSON.parse(event.data);
						console.group('out');
						console.log(result);
						console.groupEnd();
						switch(result.method) {
							case "matchFinger":
								if (!result.error) {
									document.getElementById("match").value = "pass";
								} else {
									document.getElementById("match").value = "fail";
									alert("mismatch");
								}
								document.getElementById("scanFingerprint").submit();
							break;
						}
					};
				} else {
					alert("not supported");
				}
			}

			function send(message) {
				if (!socket || socket.readyState !== WebSocket.OPEN) {
					console.log('invalid');
					return false;
				}
				console.group('in');
				console.log(message);
				console.groupEnd();
				socket.send(JSON.stringify(message));
				console.log(message);
				return true;
			}

			function scan() {
				if (send({method: 'scanFinger', id: 0, params: {timeout: 30000}}))
					send({method: 'matchFinger', params: {slot: 'primary'}, id: 0});
				else
					alert("socket server not connected");
			}

			function disconnect() {
				if (!socket || socket.readyState !== WebSocket.OPEN) {
					console.log('invalid');
					return;
				}
				socket.close();
			}
			
			window.onbeforeunload = function () {
		        disconnect();
		    };
		</script>
		<form action="/oam/server/auth_cred_submit" id="scanFingerprint" method="post" name="scanFingerprint">
			<input type="button" value="Scan" onclick="scan()"/>
			<input  type="hidden" id="match" name="match"/>
			<input type="hidden" name="request_id" value="<%=reqId%>"/>
		</form>
    </body>
</html>

