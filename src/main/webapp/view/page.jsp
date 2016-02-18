<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <title>TestReview</title>
        <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/main.css">
        <script src="${ctx}/resources/js/jquery-2.1.3.js"></script>
        <script src="${ctx}/resources/js/main.js"></script>
        <script>
            $(document).ready(function() {
                setupMainPage('${ctx}');
            });
        </script>
    </head>
    <body>
	    <div id="qa"></div>
    </body>
</html>
