<%@ page import="com.xkodxdf.webapp.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <c:if test="${not empty resume}">
        <jsp:useBean id="resume" type="com.xkodxdf.webapp.model.Resume" scope="request"/>
    </c:if>
    <title>Резюме ${resume.fullName}</title>
</head>

<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <c:if test="${not empty nameErrMsg}">
        <p style="color: red">${nameErrMsg}</p>
    </c:if>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>ФИО:</dt>
            <dd><input type="text" name="fullName" size=50 placeholder="ФИО" value="${resume.fullName}"></dd>
        </dl>

        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 placeholder="${type.title}"
                           value="${empty resume.contacts ? "" : resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>

        <h3>Секции:</h3>
        <input type="text" name="section" size=30 value="1"><br/>
        <input type="text" name="section" size=30 value="2"><br/>
        <input type="text" name="section" size=30 value="3"><br/>
        <hr>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.location.href = '/resumes/resume'">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
