package com.example.manager_book.dao;

import com.example.manager_book.model.Book;
import com.example.manager_book.util.DBConnectSQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try(Connection con = DBConnectSQL.getConnection();
            Statement stm = con.createStatement();
            ResultSet result = stm.executeQuery(sql)) {
            while (result.next()) {
                books.add(new Book(
                        result.getInt("id"),
                        result.getString("title"),
                        result.getString("author")
                ));
            }
        } catch(SQLException e) {
                e.printStackTrace();
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

    public Book findBook(String title, String author) {
        String sql = "SELECT * FROM books WHERE tilte = ?, author = ?";
        try (Connection con = DBConnectSQL.getConnection();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, title);
            pstm.setString(2, author);
            try (ResultSet result = pstm.executeQuery()) {
                if (result.next()) {
                    return new Book(
                            result.getInt("id"),
                            result.getString("title"),
                            result.getString("author")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
