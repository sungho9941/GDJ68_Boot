<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<c:import url="/WEB-INF/views/layout/headCSS.jsp"></c:import>
</head>
<body id="page-top">

	<!-- Page Wrapper -->
    <div id="wrapper">
		<!-- sidebar -->
		<c:import url="/WEB-INF/views/layout/sidebar.jsp"></c:import>
		
			<div id="content-wrapper" class="d-flex flex-column">
				<div id="content">
					<c:import url="/WEB-INF/views/layout/topbar.jsp"></c:import>
				
					<div class="container-fluid">
						<!-- page 실제 내용 -->
						<sec:authentication property="principal" var="vo"></sec:authentication>
						
						${vo.username}<br>
						${vo.email}<br>
						${vo.birth}
					</div>
				</div>
		
			<c:import url="/WEB-INF/views/layout/footer.jsp"></c:import>
		
				</div>
	
			</div>
	

	
	<c:import url="/WEB-INF/views/layout/footjs.jsp"></c:import>
</body>
</html>