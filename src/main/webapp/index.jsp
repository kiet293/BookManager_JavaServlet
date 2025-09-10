<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>📚 Book Manager Pro</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="header">
        <h1><i class="fas fa-book-open"></i> Book Manager Pro</h1>
        <p>Quản lý thư viện sách thông minh và hiện đại</p>
    </div>

    <div class="main-content">
        <!-- Book List Section -->
        <div class="card book-list-card">
            <div class="card-header">
                <h2><i class="fas fa-list"></i> Danh Sách Sách</h2>

                <!-- Search Section -->
                <div class="search-section">
                    <form method="get" action="${pageContext.request.contextPath}/books" class="search-form">
                        <div class="search-input-group">
                            <input type="text"
                                   name="search"
                                   class="search-input"
                                   placeholder="Tìm kiếm sách hoặc tác giả..."
                                   value="${param.search}">
                            <button type="submit" class="btn-search">
                                <i class="fas fa-search"></i>
                            </button>
                        </div>
                    </form>
                    <c:if test="${not empty param.search}">
                        <a href="${pageContext.request.contextPath}/books" class="btn-clear-search">
                            <i class="fas fa-times"></i> Xóa bộ lọc
                        </a>
                    </c:if>
                </div>
            </div>

            <div class="stats">
                <div class="stat-item">
                    <span class="stat-number">${books.size()}</span>
                    <span class="stat-label">Tổng sách</span>
                </div>
            </div>

            <div class="book-list">
                <c:choose>
                    <c:when test="${empty books}">
                        <div class="empty-state">
                            <i class="fas fa-book"></i>
                            <h3>
                                <c:choose>
                                    <c:when test="${not empty param.search}">
                                        Không tìm thấy kết quả cho "${param.search}"
                                    </c:when>
                                    <c:otherwise>
                                        Chưa có sách nào
                                    </c:otherwise>
                                </c:choose>
                            </h3>
                            <p>
                                <c:choose>
                                    <c:when test="${not empty param.search}">
                                        Hãy thử tìm kiếm với từ khóa khác
                                    </c:when>
                                    <c:otherwise>
                                        Hãy thêm cuốn sách đầu tiên của bạn!
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="b" items="${books}" varStatus="status">
                            <div class="book-item" style="animation-delay: ${status.index * 0.1}s" data-book-id="${b.id}">
                                <div class="book-info">
                                    <span class="book-id">#${b.id}</span>
                                    <div class="book-details">
                                        <div class="book-title">
                                            <i class="fas fa-book"></i> ${b.title}
                                        </div>
                                        <div class="book-author">
                                            <i class="fas fa-user"></i> ${b.author}
                                        </div>
                                    </div>
                                </div>
                                <div class="book-actions">
                                    <button class="btn-action btn-edit" onclick="editBook(${b.id}, '${b.title}', '${b.author}')">
                                        <i class="fas fa-edit"></i>
                                    </button>
                                    <button class="btn-action btn-delete" onclick="deleteBook(${b.id}, '${b.title}')">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Add/Edit Book Form Section -->
        <div class="card form-container">
            <h2 id="form-title"><i class="fas fa-plus-circle"></i> Thêm Sách Mới</h2>

            <form method="post" action="${pageContext.request.contextPath}/books" id="book-form">
                <input type="hidden" id="book-id" name="id" value="">
                <input type="hidden" id="form-action" name="_method" value="">

                <div class="form-group">
                    <label for="title">
                        <i class="fas fa-heading"></i> Tên Sách
                    </label>
                    <input type="text"
                           id="title"
                           name="title"
                           class="form-control"
                           placeholder="Nhập tên sách..."
                           required>
                </div>

                <div class="form-group">
                    <label for="author">
                        <i class="fas fa-feather-alt"></i> Tác Giả
                    </label>
                    <input type="text"
                           id="author"
                           name="author"
                           class="form-control"
                           placeholder="Nhập tên tác giả..."
                           required>
                </div>

                <div class="form-buttons">
                    <button type="submit" class="btn-submit" id="submit-btn">
                        <i class="fas fa-plus"></i> Thêm Sách Vào Thư Viện
                    </button>
                    <button type="button" class="btn-cancel" id="cancel-btn" onclick="resetForm()" style="display: none;">
                        <i class="fas fa-times"></i> Hủy
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div id="delete-modal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3><i class="fas fa-exclamation-triangle"></i> Xác nhận xóa</h3>
        </div>
        <div class="modal-body">
            <p>Bạn có chắc chắn muốn xóa sách "<span id="delete-book-title"></span>"?</p>
            <p class="warning-text">Hành động này không thể hoàn tác!</p>
        </div>
        <div class="modal-footer">
            <button class="btn-modal-cancel" onclick="closeDeleteModal()">
                <i class="fas fa-times"></i> Hủy
            </button>
            <button class="btn-modal-confirm" onclick="confirmDelete()">
                <i class="fas fa-trash"></i> Xóa
            </button>
        </div>
    </div>
</div>

<script src="js/script.js"></script>
</body>
</html>