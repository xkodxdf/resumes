<%@ page import="com.xkodxdf.webapp.model.SectionType" %>
<%@ page import="com.xkodxdf.webapp.model.TextSection" %>
<%@ page import="com.xkodxdf.webapp.model.ListSection" %>
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
    <h4 style="color: dimgrey">Контакты</h4>
    <div>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.xkodxdf.webapp.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </div>
    <h4 style="color: dimgrey">Секции</h4>
    <div>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry" type="java.util.Map.Entry<com.xkodxdf.webapp.model.SectionType,
            com.xkodxdf.webapp.model.Section>"/>
            <c:set var="sectionType" value="<%=sectionEntry.getKey()%>"/>
            <c:set var="sectionTypeTitle" value="<%=sectionEntry.getKey().getTitle()%>"/>
            <c:choose>
                <c:when test="${(sectionType == SectionType.OBJECTIVE) or (sectionType == SectionType.PERSONAL)}">
                    <dl>
                        <dt><p>${sectionTypeTitle}</p></dt><br>
                        <dd>
                            <%=((TextSection) sectionEntry.getValue()).getContent()%>
                        </dd>
                    </dl>
                </c:when>
                <c:when test="${sectionType.equals(SectionType.ACHIEVEMENT) or sectionType.equals(SectionType.QUALIFICATIONS)}">
                    <dl>
                        <dt>${sectionTypeTitle}</dt><br>
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
            </c:choose>
            <br/>
        </c:forEach>
    </div>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>