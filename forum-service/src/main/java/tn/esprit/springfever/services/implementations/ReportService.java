package tn.esprit.springfever.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.Report;
import tn.esprit.springfever.repositories.ReportRepository;
import tn.esprit.springfever.services.interfaces.IReportService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class ReportService implements IReportService {
    @Autowired
    private ReportRepository reportRepository;

    @Override
    public Report reportPost(Post post, Long id, String desc) {
        if (getPostReport(id, post) == null) {
            Report report = new Report();
            report.setPost(post);
            report.setUser(id);
            report.setDesc(desc);
            report.setTimestamps(LocalDateTime.now());
            return reportRepository.save(report);
        } else {
            return null;
        }
    }

    @Override
    public Report reportComment(Comment comment, Long id, String desc) {
        if (getCommentReport(id, comment) == null) {
            Report report = new Report();
            report.setComment(comment);
            report.setUser(id);
            report.setDesc(desc);
            report.setTimestamps(LocalDateTime.now());
            return reportRepository.save(report);
        } else {
            return null;
        }
    }

    @Override
    public void deleteReport(Long id) {
        Report report = reportRepository.findById(id).orElse(null);
        if (report != null) {
            reportRepository.delete(report);
        }
    }

    @Override
    public Report getPostReport(Long user, Post post) {
        return reportRepository.findByUserAndPost(user, post);
    }

    @Override
    public Report getCommentReport(Long user, Comment comment) {
        return reportRepository.findByUserAndComment(user, comment);
    }
}
