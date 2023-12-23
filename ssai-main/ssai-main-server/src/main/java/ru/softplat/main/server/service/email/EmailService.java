package ru.softplat.main.server.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.email.EmailMessage;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.buyer.Order;
import ru.softplat.main.server.model.buyer.OrderPosition;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String shopEmail;
    private final JavaMailSender emailSender;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void sendOrderConfirmationEmails(Order order) {
        long id = order.getId();
        Buyer buyer = order.getBuyer();
        LocalDateTime time = order.getProductionTime();
        Map<String, List<String>> sellerEmailsAndText = composeConfirmationEmailsToSellers(order);
        sendAll(id, buyer, time, sellerEmailsAndText);
    }

    private void sendAll(long id, Buyer buyer, LocalDateTime time, Map<String, List<String>> sellerEmailsAndText) {
        for (var entry : sellerEmailsAndText.entrySet()) {
            String sellerEmail = entry.getKey();
            String orderDescription = String.join("\n", entry.getValue());
            String text = getOrderConfirmEmailText(id, orderDescription, time, buyer);

            SimpleMailMessage message = createEmailMessage(sellerEmail, text);
            String emailSubject = String.format(EmailMessage.ORDER_CONFIRM_SUBJECT.body, id);
            message.setSubject(emailSubject);

            emailSender.send(message);
        }
    }

    private static Map<String, List<String>> composeConfirmationEmailsToSellers(Order order) {
        Map<String, List<String>> sellerEmailsAndText = new HashMap<>();

        for (var orderPosition : order.getProductsOrdered()) {
            String details = orderPositionToString(orderPosition);
            String email = orderPosition.getProduct().getSeller().getEmail();

            List<String> textParts = sellerEmailsAndText.getOrDefault(email, new ArrayList<>());
            textParts.add(details);
            sellerEmailsAndText.put(email, textParts);
        }
        return sellerEmailsAndText;
    }

    private SimpleMailMessage createEmailMessage(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(shopEmail);
        message.setTo(to);
        message.setText(text);
        message.setSentDate(Date.from(Instant.now()));
        return message;
    }

    private String getOrderConfirmEmailText(long orderId,
                                            String description,
                                            LocalDateTime time,
                                            Buyer buyer
    ) {
        return String.format(
                EmailMessage.ORDER_CONFIRM_EMAIL.body,
                orderId,
                time.format(formatter),
                description,
                buyer.getName(),
                buyer.getPhone(),
                buyer.getEmail());
    }

    private static String orderPositionToString(OrderPosition orderPosition) {
        String installString = orderPosition.getInstallation() ? "Да" : "Нет";
        return  "\n" +
                "Название товара: " + orderPosition.getProduct().getName() + "\n" +
                "Количество товара: " + orderPosition.getQuantity() + " шт.\n" +
                "Стоимость 1 единицы товара: " + orderPosition.getProductCost() + " руб.\n" +
                "Установка: " + installString + "\n";
    }
}