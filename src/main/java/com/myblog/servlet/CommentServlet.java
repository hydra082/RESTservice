package com.myblog.servlet;

import com.google.gson.Gson;
import com.myblog.config.DatabaseConfig;
import com.myblog.dto.CommentDTO;
import com.myblog.repository.*;
import com.myblog.service.CommentService;
import com.myblog.service.CommentServiceImpl;
import com.myblog.util.DependencyFactory;
import com.zaxxer.hikari.HikariDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/comments/*")
public class CommentServlet extends HttpServlet {
    private final CommentService commentService;
    private final Gson gson = new Gson();
    public CommentServlet(CommentService commentService) {
        this.commentService = commentService;
    }
    public CommentServlet() {
        this(DependencyFactory.createCommentService());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<CommentDTO> comments = commentService.findAll();
                out.print(gson.toJson(comments));
            } else {
                String[] parts = pathInfo.split("/");
                if (parts.length == 2) {
                    if (parts[1].equals("post")) {
                        int postId = Integer.parseInt(req.getParameter("postId"));
                        List<CommentDTO> comments = commentService.findByPostId(postId);
                        out.print(gson.toJson(comments));
                    } else {
                        int id = Integer.parseInt(parts[1]);
                        CommentDTO comment = commentService.findById(id);
                        if (comment == null) {
                            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.print("{\"error\": \"Comment not found\"}");
                        } else {
                            out.print(gson.toJson(comment));
                        }
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Invalid URL\"}");
                }
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Invalid ID format\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Server error: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            CommentDTO commentDTO = gson.fromJson(req.getReader(), CommentDTO.class);
            if (commentDTO == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Invalid comment data\"}");
                return;
            }
            commentService.save(commentDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setHeader("Location", "/comments/" + commentDTO.getId());
            out.print(gson.toJson(commentDTO));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Failed to create comment: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            CommentDTO commentDTO = gson.fromJson(req.getReader(), CommentDTO.class);
            if (commentDTO == null || commentDTO.getId() == 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Invalid comment data or missing ID\"}");
                return;
            }
            commentService.update(commentDTO);
            resp.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(commentDTO));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Failed to update comment: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Missing comment ID\"}");
            } else {
                String[] parts = pathInfo.split("/");
                if (parts.length == 2) {
                    int id = Integer.parseInt(parts[1]);
                    commentService.delete(id);
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Invalid URL\"}");
                }
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Invalid ID format\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Failed to delete comment: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}