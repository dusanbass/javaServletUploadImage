<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action="Upload" method="post" 
enctype="multipart/form-data"">
Description: &nbsp <input type="text" name="alt">
<input type="file" name="stuff">
<input type="submit" name="submit" value="submitSet">
</form><br>
${msg}
<br>
<img 
	src='IMAGES/${src}'
	width="50%">
</body>
</html>