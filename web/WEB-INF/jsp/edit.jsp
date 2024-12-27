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
        <h3>О себе:</h3>
        <div id="sections">
            <c:forEach var="type" items="${SectionType.values()}">
                <jsp:useBean id="type" type="com.xkodxdf.webapp.model.SectionType"/>
                <c:set var="section" value="${resume.getSection(type)}"/>
                <jsp:useBean id="section" type="com.xkodxdf.webapp.model.Section"/>
                <dl>
                    <dt>${type.title}</dt>
                    <br>
                    <dd>
                        <c:choose>
                            <c:when test="${(type == SectionType.OBJECTIVE) or (type == SectionType.PERSONAL)}">
                                    <textarea name="${type.name()}" rows="4" cols="64"
                                              onkeyup="this.value = this.value.replace(/[!#&*<>\/]/g, '')"><%=((TextSection) section).getContent()%></textarea>
                            </c:when>
                            <c:when test="${(type == SectionType.ACHIEVEMENT) or (type == SectionType.QUALIFICATIONS)}">
                                    <textarea name="${type.name()}" rows="8" cols="64"
                                              onkeyup="this.value = this.value.replace(/[!#&*<>\/]/g, '')"><%=String.join("\n", ((ListSection) section).getContent())%></textarea>
                            </c:when>
                            <c:when test="${(type == SectionType.EXPERIENCE) or (type == SectionType.EDUCATION)}">
                                <c:set var="inputErrMsg"
                                       value="Допустимы буквы русского, английского алфавита, пробелы, цифры, точки, слеши, знак равенства, знак @, тире, нижнее подчёркивание и скобки"/>
                                <c:forEach var="company" varStatus="counter"
                                           items="<%=((CompanySection) section).getContent()%>">
                                    <p>Название:</p>
                                    <input type="text" name="${type}" size="32"
                                           pattern="^[a-zA-Zа-яА-ЯёЁ0-9\s.:=@_\(\)\/\-]+$"
                                           title="${inputErrMsg}"
                                           value="${company.homePage.name}">
                                    <p>Ссылка:</p>
                                    <input type="text" name="${type}url" size="32"
                                           pattern="^[a-zA-Zа-яА-ЯёЁ0\s.:=@_\(\)\/\-]+$"
                                           title="${inputErrMsg}"
                                           value="${company.homePage.url}">
                                    <c:forEach var="period" items="${company.periods}">
                                        <p>Начало:</p>
                                        <input type="date" min="1900-01-01" max="2100-01-01"
                                               name="${type}${counter.index}start_date" value="${period.startDate}">
                                        <p>Окончание:</p>
                                        <input type="date" min="1900-01-01" max="2100-01-01"
                                               name="${type}${counter.index}end_date" value="${period.endDate}">
                                        <p>Заголовок:</p>
                                        <input type="text"
                                               pattern="^[a-zA-Zа-яА-ЯёЁ0-9\s.:=@_\(\)\/\-]+$"
                                               title="${inputErrMsg}"
                                               name="${type}${counter.index}title" size="64" value="${period.title}">
                                        <p>Описание:</p>
                                        <textarea name="${type}${counter.index}description" rows="4" cols="64"
                                                  onkeyup="this.value = this.value.replace(/[!#&*<>\/]/g, '')">${period.description}</textarea>
                                    </c:forEach>
                                    <hr>
                                    <br>
                                </c:forEach>
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
