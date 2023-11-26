package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@AllArgsConstructor
@NoArgsConstructor
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private NotificationTaskRepository notificationTaskRepository;

    private final Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
    private final  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message().text() != null) {
                long chatId = update.message().chat().id();
                String name = update.message().chat().firstName();
                if (update.message().text().equals("/start")) {
                    telegramBot.execute(new SendMessage(chatId,"Hi, " + name + "! To create a new notification, " +
                            "enter a text in the following format:\n 01.01.2022 20:00 Do homework"));
                }

                Matcher matcher = pattern.matcher(update.message().text());
                String date = null;
                String text = null;

                if (matcher.matches()) {
                    date = matcher.group(1);
                    text = matcher.group(3);
                    logger.info("Date: {}, item: {}", date, text);
                }

                if (date != null) {
                    LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
                    NotificationTask notificationTask = new NotificationTask(chatId, text, dateTime);
                    notificationTaskRepository.save(notificationTask);
                    telegramBot.execute(new SendMessage(chatId, "Successfully!"));
                    logger.info("A new notification has been saved " + notificationTask);
                } else {
                    telegramBot.execute(new SendMessage(chatId, "I'm sorry, I don't understand you. " +
                            "My job as a bot is to record your tasks and send you a notification. " +
                            "Please write in the format below: \n 01.01.2022 20:00 Do homework!"));
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotification () {
        notificationTaskRepository.findAllByDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .forEach(notificationTask -> {
                    telegramBot.execute(new SendMessage(notificationTask.getChatId(), notificationTask.getNotification()));
                    logger.info("Sent a notification for the chat room " + notificationTask.getChatId());
                });

    }

}
