package NotificationService.meesho.clientmodels;

import java.util.Date;

public class SmsRequests {
    private int id;
    private String phone_number;
    private String message;
    private String status;
    private String failure_code;
    private String failure_comments;
    private Date created_at;
    private Date updated_at;

    public SmsRequests(int id, String phonenumber, String message, String status, String failure_code, String failure_comments, Date created_at, Date updated_at) {
        id = id;
        this.phone_number = phonenumber;
        this.message = message;
        this.status = status;
        this.failure_code = failure_code;
        this.failure_comments = failure_comments;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public String getPhonenumber() {
        return phone_number;
    }

    public void setPhonenumber(String phonenumber) {
        this.phone_number = phonenumber;
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public SmsRequests() {
    }

    @Override
    public String toString() {
        return "SmsRequests{" +
                "Id=" + id +
                ", phonenumber='" + phone_number + '\'' +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", failure_code='" + failure_code + '\'' +
                ", failure_comments='" + failure_comments + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
