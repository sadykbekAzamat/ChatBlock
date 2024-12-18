package kz.beta.chatblock;

public class Message {
    private String messageID;
    private String content;
    private int senderID;
    public Message() {
        // Default constructor required for Firebase
    }

    public Message(String messageID, String content, int senderID) {
        this.messageID = messageID;
        this.content = content;
        this.senderID = senderID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

}
