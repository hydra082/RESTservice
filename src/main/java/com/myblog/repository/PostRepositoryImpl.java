package com.myblog.repository;

import com.myblog.config.DatabaseConfig;
import com.myblog.entity.Post;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostRepositoryImpl implements PostRepository {
    private final HikariDataSource dataSource;
    private final UserRepository userRepository;

    public PostRepositoryImpl(HikariDataSource dataSource, UserRepository userRepository) {
        this.dataSource = dataSource;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Post> findById(int id) {
        String sql = "SELECT * FROM posts WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Post post = new Post();
                post.setId(resultSet.getInt("id"));
                post.setTitle(resultSet.getString("title"));
                post.setContent(resultSet.getString("content"));
                int userId = resultSet.getInt("user_id");
                userRepository.findById(userId).ifPresent(post::setUser);
                return Optional.of(post);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске поста по ID: " + e.getMessage());
        }
    }

    @Override
    public List<Post> findAll() {
        String sql = "SELECT * FROM posts";
        List<Post> posts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Post post = new Post();
                post.setId(resultSet.getInt("id"));
                post.setTitle(resultSet.getString("title"));
                post.setContent(resultSet.getString("content"));
                int userId = resultSet.getInt("user_id");
                userRepository.findById(userId).ifPresent(post::setUser);
                posts.add(post);
            }
            return posts;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех постов: " + e.getMessage());
        }
    }

    @Override
    public List<Post> findByUserId(int userId) {
        String sql = "SELECT * FROM posts WHERE user_id = ?";
        List<Post> posts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Post post = new Post();
                post.setId(resultSet.getInt("id"));
                post.setTitle(resultSet.getString("title"));
                post.setContent(resultSet.getString("content"));
                userRepository.findById(userId).ifPresent(post::setUser);
                posts.add(post);
            }
            return posts;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске постов по user_id: " + e.getMessage());
        }
    }

    @Override
    public void save(Post post) {
        String sql = "INSERT INTO posts (title, content, user_id) VALUES (?, ?, ?) RETURNING id";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getContent());
            statement.setInt(3, post.getUser().getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                post.setId(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении поста: " + e.getMessage());
        }
    }

    @Override
    public void update(Post post) {
        String sql = "UPDATE posts SET title = ?, content = ?, user_id = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getContent());
            statement.setInt(3, post.getUser().getId());
            statement.setInt(4, post.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении поста: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении поста: " + e.getMessage());
        }
    }
}