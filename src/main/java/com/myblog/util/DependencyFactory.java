package com.myblog.util;

import com.myblog.config.DatabaseConfig;
import com.myblog.repository.*;
import com.myblog.service.*;
import com.zaxxer.hikari.HikariDataSource;

public class DependencyFactory {
    private static final HikariDataSource dataSource = DatabaseConfig.getDataSource();
    private static final UserRepository userRepository = new UserRepositoryImpl(dataSource);
    private static final PostRepository postRepository = new PostRepositoryImpl(dataSource, userRepository);
    private static final CommentRepository commentRepository = new CommentRepositoryImpl(dataSource, postRepository, userRepository);

    public static CommentService createCommentService() {
        return new CommentServiceImpl(commentRepository);
    }

    public static PostService createPostService() {
        return new PostServiceImpl(postRepository);
    }

    public static UserService createUserService() {
        return new UserServiceImpl(userRepository);
    }
}