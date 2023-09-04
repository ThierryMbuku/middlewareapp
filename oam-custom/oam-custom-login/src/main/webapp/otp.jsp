<html>
    <head>
        <title>OAM Custom Authentication</title>
    </head>
    <body>
    	<%
        final String reqId = request.getParameter("request_id");
        %>
        <h1>OAM Custom Authentication</h1>
		<form action="/oam/server/auth_cred_submit" id="loginData" method="post" name="loginData">
			<p>Enter One-Time PIN:</p>
			<input class="textinput" id="OTPin" name="OTPin" type="password" />
			<input class="formButton" onclick="form.submit();return false;" type="submit" value="Login" />
			<input name="request_id" type="hidden" value="<%=reqId%>" />
		</form>
    </body>
</html>
