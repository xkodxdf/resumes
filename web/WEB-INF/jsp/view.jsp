<%@ page import="com.xkodxdf.webapp.model.SectionType" %>
<%@ page import="com.xkodxdf.webapp.model.TextSection" %>
<%@ page import="com.xkodxdf.webapp.model.ListSection" %>
<%@ page import="com.xkodxdf.webapp.model.CompanySection" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.xkodxdf.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"
                                                                                      title="Редактировать"
                                                                                      alt="Редактировать"></a></h2>
    <div id="view_contacts" class="resume-section">
        <c:if test="${not empty resume.contacts}">
            <h4 style="color: dimgrey">Контакты</h4>
        </c:if>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.xkodxdf.webapp.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </div>
    <div id="view_sections" class="resume-section">
        <c:if test="${not empty resume.sections}">
            <h4 style="color: dimgrey">О себе</h4>
        </c:if>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry" type="java.util.Map.Entry<com.xkodxdf.webapp.model.SectionType,
            com.xkodxdf.webapp.model.Section>"/>
            <c:set var="sectionType" value="<%=sectionEntry.getKey()%>"/>
            <c:set var="sectionTypeTitle" value="<%=sectionEntry.getKey().getTitle()%>"/>
            <c:choose>
                <c:when test="${(sectionType == SectionType.OBJECTIVE) or (sectionType == SectionType.PERSONAL)}">
                    <dl>
                        <dt><h3>${sectionTypeTitle}</h3></dt>
                        <br>
                        <dd>
                            <%=((TextSection) sectionEntry.getValue()).getContent()%>
                        </dd>
                    </dl>
                </c:when>
                <c:when test="${(sectionType == SectionType.ACHIEVEMENT) or (sectionType == SectionType.QUALIFICATIONS)}">
                    <dl>
                        <dt><h3>${sectionTypeTitle}</h3></dt>
                        <br>
                        <dd>
                            <ul>
                                <c:forEach var="listElement"
                                           items="<%=((ListSection) sectionEntry.getValue()).getContent()%>">
                                    <li>${listElement}</li>
                                </c:forEach>
                            </ul>
                        </dd>
                    </dl>
                </c:when>
                <c:when test="${(sectionType == SectionType.EXPERIENCE) or (sectionType == SectionType.EDUCATION)}">
                    <h3>${sectionType.title}</h3>
                    <c:forEach var="company" items="<%=((CompanySection) sectionEntry.getValue()).getContent()%>">
                        <c:if test="${empty company.homePage.url}">
                            <h4>${company.homePage.name}</h4>
                        </c:if>
                        <c:if test="${not empty company.homePage.url}">
                            <h4><a href="${company.homePage.url}">${company.homePage.name}</a></h4>
                        </c:if>
                        <c:forEach var="period" items="${company.periods}">
                            <c:if test="${sectionType == SectionType.EXPERIENCE}">
                                <h4>Должность:</h4>
                                <p>${period.title}</p>
                            </c:if>
                            <c:if test="${sectionType == SectionType.EDUCATION}">
                                <h4>Направление:</h4>
                                <p>${period.title}</p>
                            </c:if>
                            <c:if test="${sectionType == SectionType.EXPERIENCE}">
                                <c:if test="${not empty period.description}">
                                    <h4>Обязанности:</h4>
                                </c:if>
                            </c:if>
                            <c:if test="${sectionType == SectionType.EDUCATION}">
                                <c:if test="${not empty period.description}">
                                    <h4>Описание:</h4>
                                </c:if>
                            </c:if>
                            <p>${period.description}</p>
                            <c:if test="${not empty period.startDate}">
                                <h4>Период:</h4>
                                <input type="date" value="${period.startDate}" readonly>
                            </c:if>
                            <c:if test="${not empty period.endDate}">
                                - <input type="date" value="${period.endDate}" readonly>
                            </c:if>
                            <hr>
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
            <br/>
        </c:forEach>
    </div>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>