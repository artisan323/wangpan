package com.ssm.pojo;

public class Share {

    private int shareId;
    private String shareUrl;
    private String path;
    private String shareUser;
    private int status;
    private String command;

    private String shareFileName;

    public String getShareFileName() {
        return shareFileName;
    }

    public void setShareFileName(String shareFileName) {
        this.shareFileName = shareFileName;
    }

    public int getShareId() {
        return shareId;
    }

    public void setShareId(int shareId) {
        this.shareId = shareId;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getShareUser() {
        return shareUser;
    }

    public void setShareUser(String shareUser) {
        this.shareUser = shareUser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "Share{" +
                "shareId=" + shareId +
                ", shareUrl='" + shareUrl + '\'' +
                ", path='" + path + '\'' +
                ", shareUser='" + shareUser + '\'' +
                ", status=" + status +
                ", command='" + command + '\'' +
                ", shareFileName='" + shareFileName + '\'' +
                '}';
    }
}
