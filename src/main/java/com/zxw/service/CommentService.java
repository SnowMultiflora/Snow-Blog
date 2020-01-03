package com.zxw.service;

import com.zxw.pojo.Comment;

import java.util.List;

public interface CommentService {
    //根据博客id获取评论信息
    List<Comment> listCommentByBlogId(Long blogId);
    //保存评论
    Comment saveComment(Comment comment);
}
