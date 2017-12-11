package com.myzhihu.service;

import com.myzhihu.dao.CommentDAO;
import com.myzhihu.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    public List<Comment> getCommentsByEntity(int entityId, int entityType){
        return commentDAO.selectCommentByEntity(entityId,entityType);
    }

    public int addComment(Comment comment ){
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        return commentDAO.addComment(comment)>0 ?comment.getId(): 0;
    }

    public int getCommentCount(int entityId,int entityTpye){
        return commentDAO.getCommentCount(entityId,entityTpye);
    }

    public boolean deleteComment(int commentId){
        return commentDAO.updateCommentStatus(commentId,1)>0;
    }

    public Comment getCommentById(int commentId){
        return commentDAO.SelectCommentById(commentId);
    }

    public int getUserCommentCount(int userId){
        return commentDAO.getUserCommentCount(userId);
    }

}
