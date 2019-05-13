/*

Name :- Siddharth Nahar
Entry No :- 2016csb1043
Date :- 22/11/18
Purpose :-

	1. From given Gmail-Service Create Draft.
	2. Set Subject to file Path
	3. Set body to null
*/

package csl333;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.MessagingException;
import javax.mail.Session;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;

import java.util.Properties;
import java.io.*;

public class Drafts{

	/*Create an Empty Message with filePath as subject*/
	public static MimeMessage createEmail(String subject, String body)throws MessagingException {

		/*Create Objects of class for MimeMessgae*/
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);

		/*Set From Address as your gmail id and set subject*/
		email.setFrom(new InternetAddress("sid23nahar@gmail.com"));
		/*
		email.addRecipient(javax.mail.Message.RecipientType.TO,
			new InternetAddress("sid23nahar@gmail.com"));*/
		email.setSubject(subject);
		email.setText(body);

		return email;

	}

	/**
	* Create a message from an email.
	*
	* @param emailContent Email to be set to raw of message
	* @return a message containing a base64url encoded email
	* @throws IOException
	* @throws MessagingException
	*/
	public static Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		emailContent.writeTo(buffer);

		/*Encode message to raw wncoding and set to message*/
		byte[] bytes = buffer.toByteArray();
		String encodedEmail = Base64.encodeBase64URLSafeString(bytes);

		Message message = new Message();
		message.setRaw(encodedEmail);
		return message;

	}


	/**
	* Create draft email.
	*
	* @param service an authorized Gmail API instance
	* @param userId user's email address. The special value "me"
	* can be used to indicate the authenticated user
	* @param emailContent the MimeMessage used as email within the draft
	* @return the created draft
	* @throws MessagingException
	* @throws IOException
	*/
	public static Draft createDraft(Gmail service,String filePath) throws MessagingException, IOException {

		/*Get mimemessage email with subject set as file path*/
		MimeMessage emailContent = createEmail(filePath, "");
		String userId = "me";

		/*Get raw encoded message from emailcontent and create Draft*/
		Message message = createMessageWithEmail(emailContent);
		Draft draft = new Draft();
		draft.setMessage(message);
		draft = service.users().drafts().create(userId, draft).execute();

		//System.out.println("Draft id: " + draft.getId());
		//System.out.println(draft.toPrettyString());

		/*Return draft object for its manipulation*/ 
		return draft;
	}





}
