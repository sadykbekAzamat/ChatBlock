package kz.beta.chatblock;

import java.util.List;

public class Chat {
    private String chatID;
    private List<Message> messages;

    public Chat() {
        // Default constructor required for Firebase
    }

    public Chat(String chatID, List<Message> messages) {
        this.chatID = chatID;
        this.messages = messages;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
