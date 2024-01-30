package NotificationService.meesho.models;

import jakarta.persistence.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "sms_requests")
public class SmsRequests {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "message")
    private String message;

    @Column(name = "status")
    private String status;
    @Column(name="failure_code")
    private String failure_code;

    @Column(name = "failure_comments")
    private String failure_comments;
    @Column(name = "created_at",columnDefinition = "TIMESTAMP")
    private LocalDateTime created_at;

    @Column(name="updated_at",columnDefinition = "TIMESTAMP")
    private LocalDateTime updated_at;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailure_code() {
        return failure_code;
    }

    public void setFailure_code(String failure_code) {
        this.failure_code = failure_code;
    }

    public String getFailure_comments() {
        return failure_comments;
    }

    public void setFailure_comments(String failure_comments) {
        this.failure_comments = failure_comments;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
