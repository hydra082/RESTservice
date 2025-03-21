package com.myblog.repository;

import com.myblog.entity.Comment;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentRepositoryImpl implements CommentRepository {
    private final HikariDataSource dataSource;

    public CommentRepositoryImpl(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Comment> findById(int id) {
        String sql = "SELECT id, content, post_id, user_id FROM comments WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Comment comment = new Comment();
                comment.setId(resultSet.getInt("id"));
                comment.setContent(resultSet.getString("content"));
                return Optional.of(comment);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске комментария по ID: " + e.getMessage());
        }
    }

    @Override
    public List<Comment> findAll() {
        String sql = "SELECT id, content, post_id, user_id FROM comments";
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setId(resultSet.getInt("id"));
                comment.setContent(resultSet.getString("content"));
                comments.add(comment);
            }
            return comments;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех комментариев: " + e.getMessage());
        }
    }

    @Override
    public List<Comment> findByPostId(int postId) {
        String sql = "SELECT id, content, post_id, user_id FROM comments WHERE post_id = ?";
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, postId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setId(resultSet.getInt("id"));
                comment.setContent(resultSet.getString("content"));
                comments.add(comment);
            }
            return comments;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске комментариев по post_id: " + e.getMessage());
        }
    }

    @Override
    public void save(Comment comment) {
        String sql = "INSERT INTO comments (content, post_id, user_id) VALUES (?, ?, ?) RETURNING id";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, comment.getContent());
            statement.setInt(2, comment.getPost().getId());
            statement.setInt(3, comment.getUser().getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                comment.setId(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении комментария: " + e.getMessage());
        }
    }

    @Override
    public void update(Comment comment) {
        String sql = "UPDATE comments SET content = ?, post_id = ?, user_id = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, comment.getContent());
            statement.setInt(2, comment.getPost().getId());
            statement.setInt(3, comment.getUser().getId());
            statement.setInt(4, comment.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении комментария: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM comments WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении комментария: " + e.getMessage());
        }
    }
}