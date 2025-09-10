// Global variables
let currentBookId = null;
let isEditMode = false;

// DOM Elements
const bookForm = document.getElementById('book-form');
const formTitle = document.getElementById('form-title');
const submitBtn = document.getElementById('submit-btn');
const cancelBtn = document.getElementById('cancel-btn');
const bookIdInput = document.getElementById('book-id');

const titleInput = document.getElementById('title');
const authorInput = document.getElementById('author');
const deleteModal = document.getElementById('delete-modal');

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    // Form validation and effects
    const inputs = document.querySelectorAll('.form-control');

    inputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.parentElement.style.transform = 'scale(1.02)';
        });

        input.addEventListener('blur', function() {
            this.parentElement.style.transform = 'scale(1)';
        });
    });

    // Form submission
    bookForm.addEventListener('submit', handleFormSubmit);

    // Add hover effects to book items
    const bookItems = document.querySelectorAll('.book-item');
    bookItems.forEach((item, index) => {
        item.addEventListener('mouseenter', function() {
            this.style.transform = 'translateX(10px) scale(1.02)';
        });

        item.addEventListener('mouseleave', function() {
            this.style.transform = 'translateX(0) scale(1)';
        });
    });

    // Modal click outside to close
    window.addEventListener('click', function(event) {
        if (event.target === deleteModal) {
            closeDeleteModal();
        }
    });

    // Search input focus effect
    const searchInput = document.querySelector('.search-input');
    if (searchInput) {
        searchInput.addEventListener('focus', function() {
            this.parentElement.style.transform = 'scale(1.02)';
            this.parentElement.style.boxShadow = '0 10px 30px rgba(102, 126, 234, 0.3)';
        });

        searchInput.addEventListener('blur', function() {
            this.parentElement.style.transform = 'scale(1)';
            this.parentElement.style.boxShadow = '0 5px 15px rgba(0,0,0,0.1)';
        });
    }
}

function handleFormSubmit(e) {
    const submitButton = e.target.querySelector('.btn-submit');

    // Add loading state
    submitButton.classList.add('loading');
    submitButton.disabled = true;

    if (isEditMode) {
        submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang cập nhật...';
    } else {
        submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang thêm...';
    }

    // Form will submit normally, this is just for UI feedback
}

function editBook(id, title, author) {
    isEditMode = true;
    currentBookId = id;

    bookIdInput.value = id;   // hidden input để Servlet nhận biết update
    titleInput.value = title;
    authorInput.value = author;

    formTitle.innerHTML = '<i class="fas fa-edit"></i> Chỉnh Sửa Sách';
    submitBtn.innerHTML = '<i class="fas fa-save"></i> Cập Nhật Sách';
    cancelBtn.style.display = 'block';
}

function resetForm() {
    // Reset edit mode
    isEditMode = false;
    currentBookId = null;

    // Clear form
    bookForm.reset();
    bookIdInput.value = '';

    // Reset UI
    formTitle.innerHTML = '<i class="fas fa-plus-circle"></i> Thêm Sách Mới';
    submitBtn.innerHTML = '<i class="fas fa-plus"></i> Thêm Sách Vào Thư Viện';
    submitBtn.className = 'btn-submit';
    submitBtn.disabled = false;
    cancelBtn.style.display = 'none';

    // Remove loading state if present
    submitBtn.classList.remove('loading');
}

function deleteBook(id, title) {
    currentBookId = id;
    document.getElementById('delete-book-title').textContent = title;
    showDeleteModal();
}

function showDeleteModal() {
    deleteModal.style.display = 'block';
    document.body.style.overflow = 'hidden';

    // Focus on cancel button for accessibility
    setTimeout(() => {
        document.querySelector('.btn-modal-cancel').focus();
    }, 100);
}

function closeDeleteModal() {
    deleteModal.style.display = 'none';
    document.body.style.overflow = 'auto';
    currentBookId = null;
}

function confirmDelete() {
    if (!currentBookId) return;

    const confirmButton = document.querySelector('.btn-modal-confirm');
    confirmButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xóa...';
    confirmButton.disabled = true;

    // Tạo form GET với action=delete&id=...
    const deleteForm = document.createElement('form');
    deleteForm.method = 'GET';
    deleteForm.action = `${getContextPath()}/books`;

    const actionInput = document.createElement('input');
    actionInput.type = 'hidden';
    actionInput.name = 'action';
    actionInput.value = 'delete';

    const idInput = document.createElement('input');
    idInput.type = 'hidden';
    idInput.name = 'id';
    idInput.value = currentBookId;

    deleteForm.appendChild(actionInput);
    deleteForm.appendChild(idInput);

    document.body.appendChild(deleteForm);
    deleteForm.submit();
}

function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)) || '';
}

// Search functionality
function handleSearch() {
    const searchInput = document.querySelector('.search-input');
    const searchForm = document.querySelector('.search-form');

    if (searchInput && searchForm) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                searchForm.submit();
            }
        });

        // Clear search on Escape
        searchInput.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                this.value = '';
                window.location.href = getContextPath() + '/books';
            }
        });
    }
}

// Utility functions
function showNotification(message, type = 'success') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <i class="fas ${type === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle'}"></i>
        <span>${message}</span>
    `;

    document.body.appendChild(notification);

    // Animate in
    setTimeout(() => {
        notification.classList.add('show');
    }, 100);

    // Remove after 3 seconds
    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
            document.body.removeChild(notification);
        }, 300);
    }, 3000);
}

function validateForm() {
    const title = titleInput.value.trim();
    const author = authorInput.value.trim();

    if (!title) {
        showNotification('Vui lòng nhập tên sách', 'error');
        titleInput.focus();
        return false;
    }

    if (!author) {
        showNotification('Vui lòng nhập tên tác giả', 'error');
        authorInput.focus();
        return false;
    }

    if (title.length > 200) {
        showNotification('Tên sách không được quá 200 ký tự', 'error');
        titleInput.focus();
        return false;
    }

    if (author.length > 100) {
        showNotification('Tên tác giả không được quá 100 ký tự', 'error');
        authorInput.focus();
        return false;
    }

    return true;
}

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    // Ctrl + N: New book
    if (e.ctrlKey && e.key === 'n') {
        e.preventDefault();
        resetForm();
        titleInput.focus();
    }

    // Escape: Cancel edit or close modal
    if (e.key === 'Escape') {
        if (deleteModal.style.display === 'block') {
            closeDeleteModal();
        } else if (isEditMode) {
            resetForm();
        }
    }

    // Ctrl + F: Focus search
    if (e.ctrlKey && e.key === 'f') {
        e.preventDefault();
        const searchInput = document.querySelector('.search-input');
        if (searchInput) {
            searchInput.focus();
        }
    }
});

// Auto-save draft (optional feature)
function saveDraft() {
    if (titleInput.value.trim() || authorInput.value.trim()) {
        const draft = {
            title: titleInput.value,
            author: authorInput.value,
            timestamp: new Date().getTime()
        };
        // Note: localStorage not available in Claude artifacts
        // localStorage.setItem('book-draft', JSON.stringify(draft));
    }
}

function loadDraft() {
    // Note: localStorage not available in Claude artifacts
    // const draft = localStorage.getItem('book-draft');
    // if (draft) {
    //     const draftData = JSON.parse(draft);
    //     // Load draft data if it's recent (within 24 hours)
    //     if (new Date().getTime() - draftData.timestamp < 24 * 60 * 60 * 1000) {
    //         titleInput.value = draftData.title;
    //         authorInput.value = draftData.author;
    //     }
    // }
}

function clearDraft() {
    // Note: localStorage not available in Claude artifacts
    // localStorage.removeItem('book-draft');
}

// Initialize additional features
document.addEventListener('DOMContentLoaded', function() {
    handleSearch();
    // loadDraft(); // Uncomment if using localStorage

    // Auto-save draft every 10 seconds
    // setInterval(saveDraft, 10000); // Uncomment if using localStorage

    // Form validation
    bookForm.addEventListener('submit', function(e) {
        if (!validateForm()) {
            e.preventDefault();
            return false;
        }
        // clearDraft(); // Uncomment if using localStorage
    });
});

// Enhanced animations
function animateBookItem(element, delay = 0) {
    element.style.opacity = '0';
    element.style.transform = 'translateX(30px)';

    setTimeout(() => {
        element.style.transition = 'all 0.5s ease-out';
        element.style.opacity = '1';
        element.style.transform = 'translateX(0)';
    }, delay);
}

function animateStats() {
    const statNumbers = document.querySelectorAll('.stat-number');

    statNumbers.forEach(stat => {
        const finalNumber = parseInt(stat.textContent);
        let currentNumber = 0;
        const increment = Math.ceil(finalNumber / 30);

        const counter = setInterval(() => {
            currentNumber += increment;
            if (currentNumber >= finalNumber) {
                currentNumber = finalNumber;
                clearInterval(counter);
            }
            stat.textContent = currentNumber;
        }, 50);
    });
}

// Initialize animations
document.addEventListener('DOMContentLoaded', function() {
    // Animate book items on load
    const bookItems = document.querySelectorAll('.book-item');
    bookItems.forEach((item, index) => {
        animateBookItem(item, index * 100);
    });

    // Animate stats
    setTimeout(animateStats, 500);
});

// Performance optimization
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Debounced search for live search functionality (if implemented)
const debouncedSearch = debounce(function(query) {
    // Implement live search here if needed
    console.log('Searching for:', query);
}, 300);

// Export functions for potential use in other scripts
window.BookManager = {
    editBook,
    deleteBook,
    resetForm,
    showNotification,
    closeDeleteModal
};