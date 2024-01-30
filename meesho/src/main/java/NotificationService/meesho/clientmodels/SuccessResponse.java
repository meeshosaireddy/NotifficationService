package NotificationService.meesho.clientmodels;

public class SuccessResponse {
    private final int requestId;
    private final String comments;

    public SuccessResponse(int requestId, String comments) {
        this.requestId = requestId;
        this.comments = comments;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getComments() {
        return comments;
    }
}
