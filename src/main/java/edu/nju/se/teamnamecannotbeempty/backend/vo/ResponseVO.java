package edu.nju.se.teamnamecannotbeempty.backend.vo;

import javax.validation.constraints.NotNull;

public class ResponseVO {
    private boolean success;
    private String message;
    private Object content;

    public ResponseVO(@NotNull boolean success, String message, Object content) {
        this.success = success;
        this.message = message;
        this.content = content;
    }

    public ResponseVO() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public static ResponseVO success(){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setSuccess(true);
        return responseVO;
    }

    public static ResponseVO success(String message){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setSuccess(true);
        responseVO.setMessage(message);
        return responseVO;
    }

    public static ResponseVO fail(){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setSuccess(false);
        return responseVO;
    }

    public static ResponseVO fail(String message){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setSuccess(false);
        responseVO.setMessage(message);
        return responseVO;
    }
}
