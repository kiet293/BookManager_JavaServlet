package com.example.manager_book.servlet;

import com.example.manager_book.dao.BookDAO;
import com.example.manager_book.model.Book;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/books")
public class BookServlet extends HttpServlet {
    private BookDAO bookDAO = new BookDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        String action = req.getParameter("action");
        if("delete".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                bookDAO.delete(id);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            resp.sendRedirect(req.getContextPath() + "/books");
            return;
        }

        req.setAttribute("books", bookDAO.findAll());
        req.getRequestDispatcher("/index.jsp").forward(req, resp);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String method = req.getParameter("_method");
        if (method == null) {
            method = "POST";
        }
        switch (method.toUpperCase()) {
            case "PUT":
                updateBook(req, resp);
                break;
            case "DELETE":
                deleteBook(req, resp);
                break;
            default:
                addBook(req, resp);
        }


        resp.sendRedirect(req.getContextPath() + "/books");
    }

    public void addBook(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String author = req.getParameter("author");
        if (title != null && author != null && !title.isBlank() && !author.isBlank()) {
            bookDAO.add(new Book(0, title.trim(), author.trim()));
        }
    }


    private void updateBook(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String title = req.getParameter("title");
            String author = req.getParameter("author");
            if (title != null && author != null && !title.isBlank() && !author.isBlank()) {
                bookDAO.update(new Book(id, title.trim(), author.trim()));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void deleteBook(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String title = req.getParameter("title");
            bookDAO.delete(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

}
