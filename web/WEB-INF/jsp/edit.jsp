<%@ page import="com.xkodxdf.webapp.model.*" %>
<%@ page import="com.xkodxdf.webapp.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <%--@elvariable id="resume" type="com.xkodxdf.webapp.model.Resume"--%>
    <c:if test="${not empty resume}">
        <jsp:useBean id="resume" type="com.xkodxdf.webapp.model.Resume" scope="request"/>
    </c:if>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>ФИО:</dt>
            <dd><input type="text" name="fullName" size=50 maxlength="128" pattern="^[a-zA-Zа-яА-ЯёЁ\s\-]+$"
                       title="Только буквы русского и английского алфавита, пробелы и тире" required placeholder="ФИО"
                       value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 maxlength="128"
                           pattern="^[a-zA-Zа-яА-ЯёЁ0-9.:=@_\(\)\/\-]+$"
                           title="Допустимы буквы русского, английского алфавита, цифры, точки, слеши, знак равенства, знак @, тире, нижнее подчёркивание и скобки"
                           placeholder="${type.title}"
                           value="${empty resume.contacts ? "" : resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <h3>Секции:</h3>
        <div>
            <c:forEach var="type" items="${SectionType.values()}">
                <jsp:useBean id="type" type="com.xkodxdf.webapp.model.SectionType"/>
                <dl>
                    <c:if test="${(type != SectionType.EXPERIENCE) && (type != SectionType.EDUCATION)}">
                        <dt>${type.title}</dt>
                    </c:if>
                    <dd>
                        <c:choose>
                            <c:when test="${type == SectionType.OBJECTIVE}">
                                <textarea name="${type.name()}" rows="4" cols="64"
                                          onkeyup="this.value = this.value.replace(/[!#&*<>\/]/g, '')">${OBJECTIVE}</textarea>
                            </c:when>
                            <c:when test="${type == SectionType.PERSONAL}">
                                <textarea name="${type.name()}" rows="4" cols="64"
                                          onkeyup="this.value = this.value.replace(/[!#&*<>\/]/g, '')">${PERSONAL}</textarea>
                            </c:when>
                            <c:when test="${type == SectionType.ACHIEVEMENT}">
                                <textarea name="${type.name()}" rows="8" cols="64"
                                          onkeyup="this.value = this.value.replace(/[!#&*<>\/]/g, '')">${ACHIEVEMENT}</textarea>
                            </c:when>
                            <c:when test="${type == SectionType.QUALIFICATIONS}">
                                <textarea name="${type.name()}" rows="8" cols="64"
                                          onkeyup="this.value = this.value.replace(/[!#&*<>\/]/g, '')">${QUALIFICATIONS}</textarea>
                            </c:when>
                        </c:choose>
                    </dd>
                </dl>
                <br/>
            </c:forEach>
        </div>
        <hr>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.location.href = '/resumes/resume'">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
