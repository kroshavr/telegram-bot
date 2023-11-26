package pro.sky.telegrambot.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long p_key;

    private Long chatId;

    private String notification;

    private LocalDateTime dateTime;

    public NotificationTask(Long chatId, String notification, LocalDateTime dateTime) {
        this.chatId = chatId;
        this.notification = notification;
        this.dateTime = dateTime;
    }

    public Long getP_key() {
        return p_key;
    }

    public void setP_key(Long p_key) {
        this.p_key = p_key;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(p_key, that.p_key) && Objects.equals(chatId, that.chatId) && Objects.equals(notification, that.notification) && Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p_key, chatId, notification, dateTime);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "p_key=" + p_key +
                ", chatId=" + chatId +
                ", notification='" + notification + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
