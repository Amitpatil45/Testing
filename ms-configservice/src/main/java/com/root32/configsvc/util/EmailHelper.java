package  com.root32.configsvc.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;

import com.root32.configsvc.repository.CompanyConfigRepository;
import com.root32.entity.MessageTemplate;

public class EmailHelper {
	private static final Properties prop = new Properties();
	private static final Session session;

	@Autowired
	private CompanyConfigRepository companyConfigRepository;

	static {
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.from", "Ameliorate-Admin");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		session = Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("contact@amelioratesolutions.com", "kamcbxexgsrcouii");

			}
		});

	}

	public static void sendAuthenticationOTPEmail(MessageTemplate msgTemplate, String toEmailId, String otp,
			String subject) throws AddressException, MessagingException {

		String msg = String.format(msgTemplate.getTemplateValue(), otp);
		int otpExpiryInMinutes = CompanyConfigRepository.OTP_EXPIRY_MINUTES;
		msg = msg + ". The OTP will expire within " + otpExpiryInMinutes + " minutes.";

		Message message = constructJavaxMailMessage(toEmailId, subject, msg);

		Transport.send(message);

	}

	private static Message constructJavaxMailMessage(String toEmailId, String subject, String msg)
			throws MessagingException, AddressException {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("Root32"));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailId));
		message.setSubject(subject);

		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(msg, "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);

		message.setContent(multipart);
		return message;
	}

	public static void sendUserEmail(MessageTemplate messageTemplate, String toEmailId, String subject,
			String adminName, String adminContactNumber, String adminEmailId, String userPassword, String userEmail,String UserMobileNUmber)
			throws AddressException, MessagingException {
		String messageString = String.format(messageTemplate.getTemplateValue(), userEmail,UserMobileNUmber, userPassword, adminName,
				adminContactNumber, adminEmailId);
		sendEmail(toEmailId, subject, messageString);
	}

	private static void sendEmail(String emailId, String subject, String msg)
			throws MessagingException, AddressException {
		Message message = constructJavaxMailMessage(emailId, subject, msg);
		Transport.send(message);
	}

	public static void sendForgotPasswordOTPEmail(MessageTemplate msgTemplate, String toEmailId, String otp,
			String subject) throws AddressException, MessagingException {

		String msg = String.format(msgTemplate.getTemplateValue(), otp);

		sendEmail(toEmailId, subject, msg);

	}

}
