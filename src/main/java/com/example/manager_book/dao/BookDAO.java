package com.example.manager_book.dao;

import com.example.manager_book.model.Book;
import com.example.manager_book.util.DBConnectSQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author FROM books";
        try (Connection con = DBConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query: " + sql, e);
        }
            return books;

    }

    public void add(Book book) {
        String sql = "INSERT INTO books(title, author) VALUES (?, ?)";
        try (Connection con = DBConnectSQL.getConnection();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, book.getTitle());
            pstm.setString(2, book.getAuthor());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection con = DBConnectSQL.getConnection();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Book findById(int id) {
        String sql = "SELECT id, title, author FROM books WHERE id = ?";
        try (Connection con = DBConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query: " + sql, e);
        }
        return null;
    }


    public List<Book> search(String keyword) {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT id, title, author FROM books " +
                "WHERE title LIKE ? OR author LIKE ? ORDER BY id DESC";
        try (Connection con = DBConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            try (ResultSet result = ps.executeQuery()) {
                    while (result.next()) {
                        int id = result.getInt("id");
                        String title = result.getString("title");
                        String author = result.getString("author");
                        list.add(new Book(id, title, author));
                    }
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ? WHERE id = ?";
        try (Connection con = DBConnectSQL.getConnection();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, book.getTitle());
            pstm.setString(2, book.getAuthor());
            pstm.setInt(3, book.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
