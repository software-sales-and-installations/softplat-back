package ru.softplat.main.server.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.email.EmailMessage;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.buyer.Order;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String email;
    private final JavaMailSender emailSender;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void sendOrderConfirmationEmails(Order order) {
        long id = order.getId();
        Buyer buyer = order.getBuyer();
        LocalDateTime time = order.getProductionTime();
        Map<String, List<String>> sellerEmailsAndText = new HashMap<>();

        for (var orderPosition : order.getProductsOrdered()) {
            String details = orderPosition.toString();
            String email = orderPosition.getProduct().getSeller().getEmail();

            List<String> textParts = sellerEmailsAndText.getOrDefault(email, new ArrayList<>());
            textParts.add(details);
            sellerEmailsAndText.put(email, textParts);
        }

        for (var entry : sellerEmailsAndText.entrySet()) {
            String sellerEmail = entry.getKey();
            String orderDescription = combineOrderPartsInSingleText(entry.getValue());
            String text = getOrderConfirmEmailText(id, orderDescription, time, buyer);

            SimpleMailMessage message = createEmailMessage(sellerEmail, text);
            String emailSubject = String.format(EmailMessage.ORDER_CONFIRM_SUBJECT.body, id);
            message.setSubject(emailSubject);

            emailSender.send(message);
        }
    }

    private SimpleMailMessage createEmailMessage(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
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

    private String combineOrderPartsInSingleText(List<String> orderPositions) {
        StringBuilder sb = new StringBuilder();
        for (String positionDescription : orderPositions) {
            sb.append(positionDescription).append("\n");
        }
        return sb.toString();
    }
}