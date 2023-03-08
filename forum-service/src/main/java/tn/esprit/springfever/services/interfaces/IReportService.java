package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.Report;

import javax.servlet.http.HttpServletRequest;

public interface IReportService {
    public Report reportPost(Post post, Long id, String desc);
    public Report reportComment(Comment comment, Long id, String desc);
    public void deleteReport(Long id);
    public Report getPostReport(Long user, Post post);
    public Report getCommentReport(Long user, Comment comment);
}
