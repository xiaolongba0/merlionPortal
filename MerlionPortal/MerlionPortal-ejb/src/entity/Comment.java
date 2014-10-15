/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author manliqi
 */
@Entity
@Table(name = "Comment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comment.findAll", query = "SELECT c FROM Comment c"),
    @NamedQuery(name = "Comment.findByCommentId", query = "SELECT c FROM Comment c WHERE c.commentId = :commentId"),
    @NamedQuery(name = "Comment.findByCreateDate", query = "SELECT c FROM Comment c WHERE c.createDate = :createDate"),
    @NamedQuery(name = "Comment.findByPoster", query = "SELECT c FROM Comment c WHERE c.poster = :poster"),
    @NamedQuery(name = "Comment.findByLikes", query = "SELECT c FROM Comment c WHERE c.likes = :likes"),
    @NamedQuery(name = "Comment.findByCommentBody", query = "SELECT c FROM Comment c WHERE c.commentBody = :commentBody")})
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "commentId")
    private Integer commentId;
    @Column(name = "createDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "poster")
    private Integer poster;
    @Column(name = "likes")
    private Integer likes;
    @Size(max = 5000)
    @Column(name = "commentBody")
    private String commentBody;
    @JoinColumn(name = "feedback", referencedColumnName = "feedbackId")
    @ManyToOne(optional = false)
    private FeedBack feedback;

    public Comment() {
    }

    public Comment(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getPoster() {
        return poster;
    }

    public void setPoster(Integer poster) {
        this.poster = poster;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public FeedBack getFeedback() {
        return feedback;
    }

    public void setFeedback(FeedBack feedback) {
        this.feedback = feedback;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (commentId != null ? commentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comment)) {
            return false;
        }
        Comment other = (Comment) object;
        if ((this.commentId == null && other.commentId != null) || (this.commentId != null && !this.commentId.equals(other.commentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Comment[ commentId=" + commentId + " ]";
    }
    
}
