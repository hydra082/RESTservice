package com.myblog.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
        private int id;
        private String username;
        private String email;
        private List<Post> posts = new ArrayList<>();
        private List<Comment> comments = new ArrayList<>();

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public List<Post> getPosts() {
                return posts;
        }

        public void setPosts(List<Post> posts) {
                this.posts = posts;
        }

        public List<Comment> getComments() {
                return comments;
        }

        public void setComments(List<Comment> comments) {
                this.comments = comments;
        }

        public void addPost(Post post) {
                posts.add(post);
                post.setUser(this);
        }

        public void addComment(Comment comment) {
                comments.add(comment);
                comment.setUser(this);
        }

}
