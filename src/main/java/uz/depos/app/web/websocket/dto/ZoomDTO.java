package uz.depos.app.web.websocket.dto;

public class ZoomDTO {

    private String password;
    private boolean startCall = false;
    private boolean endCall = true;

    public ZoomDTO() {}

    public ZoomDTO(String password, boolean startCall, boolean endCall) {
        this.password = password;
        this.startCall = startCall;
        this.endCall = endCall;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStartCall() {
        return startCall;
    }

    public void setStartCall(boolean startCall) {
        this.startCall = startCall;
    }

    public boolean isEndCall() {
        return endCall;
    }

    public void setEndCall(boolean endCall) {
        this.endCall = endCall;
    }
}
