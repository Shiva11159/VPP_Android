package com.application.vpp.Datasets;

public class UploadFileResponse {
    private String fileName;
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    private String fileDownloadUri;
    private String fileType;
    private long size;
    private String status;
    private String message;

    public String getProof_type() {
        return proof_type;
    }

    public void setProof_type(String proof_type) {
        this.proof_type = proof_type;
    }

    private String proof_type;

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


    public UploadFileResponse(String fileName, String fileDownloadUri, String fileType, long size, String status, String message, String proof_type) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
        this.message = message;
        this.status = status;
        this.proof_type = proof_type;
    }

    // Getters and Setters (Omitted for brevity)



}