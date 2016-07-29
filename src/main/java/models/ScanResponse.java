package models;

public final class ScanResponse {
    private String status;
    private String message;
    private String jobId;

    public ScanResponse(
            final String status,
            final String message,
            final String jobId
    ) {
        this.status = status;
        this.message = message;
        this.jobId = jobId;
    }

    public final String getStatus() {
        return this.status;
    }

    public final String getMessage() {
        return this.message;
    }

    public final String getJobId() {
        return this.jobId;
    }
}
