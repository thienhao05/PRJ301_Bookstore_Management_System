<%-- 
    Document   : pagination
    Created on : Mar 12, 2026, 4:52:34 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<nav aria-label="Page navigation" class="mt-4">
    <ul class="pagination justify-content-center shadow-sm">
        
        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
            <a class="page-item py-2 px-3 border-0 ${currentPage == 1 ? 'text-muted' : 'text-primary'}" 
               href="MainController?action=${targetAction}&page=${currentPage - 1}" 
               tabindex="-1">
                <i class="bi bi-chevron-left"></i>
            </a>
        </li>

        <c:forEach begin="1" end="${totalPages}" var="i">
            <li class="page-item ${currentPage == i ? 'active' : ''}">
                <a class="page-link border-0 mx-1 rounded-circle text-center" 
                   style="width: 40px; height: 40px; line-height: 24px;"
                   href="MainController?action=${targetAction}&page=${i}">
                   ${i}
                </a>
            </li>
        </c:forEach>

        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
            <a class="page-link py-2 px-3 border-0 ${currentPage == totalPages ? 'text-muted' : 'text-primary'}" 
               href="MainController?action=${targetAction}&page=${currentPage + 1}">
                <i class="bi bi-chevron-right"></i>
            </a>
        </li>
        
    </ul>
</nav>

<style>
    /* Hiệu ứng cho phân trang giống các sàn TMĐT */
    .page-link {
        transition: all 0.3s;
        font-weight: 500;
    }
    .page-item.active .page-link {
        background-color: #0d6efd;
        border-color: #0d6efd;
        box-shadow: 0 4px 10px rgba(13, 110, 253, 0.3);
    }
    .page-item.disabled .page-link {
        background-color: transparent;
    }
</style>