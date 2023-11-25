package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message().text() != null) {
                String message = update.message().text();
                long chatId = update.message().chat().id();
                String name = update.message().chat().firstName();
                switch (message) {
                    case "/start":
                        SendResponse response = telegramBot.execute(new SendMessage(chatId,"Hi, " + name + "! You're amazing!"));
                        break;
                    case "/help":
                        System.out.println("This bot is the result of a future Java developer's coursework, thanks for coming here.");
                        break;
                    default:
                        System.out.println("Invalid message");
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
