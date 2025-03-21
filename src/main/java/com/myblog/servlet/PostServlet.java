package com.myblog.servlet;

import com.google.gson.Gson;
import com.myblog.dto.PostDTO;
import com.myblog.service.PostService;
import com.myblog.util.DependencyFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/posts/*")
public class PostServlet extends HttpServlet {
    private final PostService postService;
    private final Gson gson = new Gson();

    public PostServlet(PostService postService) {
        this.postService = postService;
    }

    public PostServlet() {
        this(DependencyFactory.createPostService());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<PostDTO> posts = postService.findAll();
                out.print(gson.toJson(posts));
            } else {
                String[] parts = pathInfo.split("/");
                if (parts.length == 2) {
                    if (parts[1].equals("user")) {
                        int userId = Integer.parseInt(req.getParameter("userId"));
                        List<PostDTO> posts = postService.findByUserId(userId);
                        out.print(gson.toJson(posts));
                    } else {
                        int id = Integer.parseInt(parts[1]);
                        PostDTO post = postService.findById(id);
                        if (post == null) {
                            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.print("{\"error\": \"Post not found\"}");
                        } else {
                            out.print(gson.toJson(post));
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
            PostDTO postDTO = gson.fromJson(req.getReader(), PostDTO.class);
            if (postDTO == null || postDTO.getUserId() == 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Invalid post data or missing userId\"}");
                return;
            }
            postService.save(postDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setHeader("Location", "/posts/" + postDTO.getId());
            out.print(gson.toJson(postDTO));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Failed to create post: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            PostDTO postDTO = gson.fromJson(req.getReader(), PostDTO.class);
            if (postDTO == null || postDTO.getId() == 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Invalid post data or missing ID\"}");
                return;
            }
            postService.update(postDTO);
            resp.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(postDTO));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Failed to update post: " + e.getMessage() + "\"}");
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
                out.print("{\"error\": \"Missing post ID\"}");
            } else {
                String[] parts = pathInfo.split("/");
                if (parts.length == 2) {
                    int id = Integer.parseInt(parts[1]);
                    postService.delete(id);
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
            out.print("{\"error\": \"Failed to delete post: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}