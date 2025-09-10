package com.example.manager_book.servlet;

import com.example.manager_book.dao.BookDAO;
import com.example.manager_book.model.Book;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/books")
public class BookServlet extends HttpServlet {
    private final BookDAO bookDAO = new BookDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                bookDAO.delete(id);
                resp.sendRedirect(req.getContextPath() + "/books");
            } else {
                String keyword = req.getParameter("search");
                List<Book> books = (keyword != null && !keyword.isBlank())
                        ? bookDAO.search(keyword)
                        : bookDAO.findAll();

                req.setAttribute("books", books);
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException("Error in BookServlet doGet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String idStr = req.getParameter("id");
        String title = req.getParameter("title");
        String author = req.getParameter("author");

        try {
            if (title != null && author != null && !title.isBlank() && !author.isBlank()) {
                if (idStr == null || idStr.isBlank()) {
                    // Add new
                    bookDAO.add(new Book(0, title.trim(), author.trim()));
                } else {
                    // Update existing
                    int id = Integer.parseInt(idStr);
                    bookDAO.update(new Book(id, title.trim(), author.trim()));
                }
            }
            // Sau khi thêm/sửa xong → redirect về danh sách
            resp.sendRedirect(req.getContextPath() + "/books");

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/books");
        }
    }
}
