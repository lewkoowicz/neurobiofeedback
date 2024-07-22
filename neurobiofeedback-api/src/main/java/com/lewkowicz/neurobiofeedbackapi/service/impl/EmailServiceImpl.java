package com.lewkowicz.neurobiofeedbackapi.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private String extractTitle(String htmlContent) {
        Pattern pattern = Pattern.compile("<title>(.*?)</title>");
        Matcher matcher = pattern.matcher(htmlContent);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "No Subject";
    }

    private void sendEmail(String to, String templateName, Context context, Locale locale) throws MessagingException {
        String htmlContent = templateEngine.process(templateName + "_" + locale.getLanguage(), context);
        String subject = extractTitle(htmlContent);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendBookingConfirmation(String to, String formattedDate, String formattedTime, Locale locale) throws MessagingException {
        Context context = new Context();
        context.setVariable("date", formattedDate);
        context.setVariable("time", formattedTime);
        sendEmail(to, "booking-confirmation", context, locale);
    }

    public void sendBookingUpdate(String to, String formattedDate, String formattedTime, String fullName, String mobileNumber, Locale locale) throws MessagingException {
        Context context = new Context();
        context.setVariable("date", formattedDate);
        context.setVariable("time", formattedTime);
        context.setVariable("fullName", fullName);
        context.setVariable("mobileNumber", mobileNumber);
        sendEmail(to, "booking-update", context, locale);
    }

    public void sendBookingCancel(String to, String formattedDate, String formattedTime, Locale locale) throws MessagingException {
        Context context = new Context();
        context.setVariable("date", formattedDate);
        context.setVariable("time", formattedTime);
        sendEmail(to, "booking-cancel", context, locale);
    }
}