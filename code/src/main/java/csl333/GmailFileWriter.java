package csl333;

/*Import Gmail API packages Required for managing Drafts*/
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;

import java.io.IOException;
import java.io.Writer;
import java.lang.Math;


/**
 * This class implements the {@link Writer} functionality for writing files
 * as Gmail messages. The Gmail allows messages to be organized using
 * folders and labels etc. in the inbox. A file thus can be stored at a
 * suitable "path" in the Gmail inbox.
 *
 * TODO: Implement/override other relevant methods of {@link Writer} class as
 * required to work with Gmail inbox based storage.
 */
public class GmailFileWriter extends Writer {

	/*filePath as Subject, DataWritten will act as buffer Storage ,gmailService as Service Object*/
	private String filePath;
	private String DataWritten;
	private Gmail gmailService;
	//private Draft draft;

	/**
	* Creates a new character-stream writer whose critical sections will
	* synchronize on the writer itself.
	* @param filePath Gmail inbox path of the file to write
	*/

	/*Constructor :-

		1. Get Gmail servic with Writing Permission in Draft.
		2. Create an empty Draft with filePath as Subject.
		3. Store the DraftID for further use.

	*/
	public GmailFileWriter(String filePath) throws IOException{

		this.filePath = filePath;
		//TODO: Implement proper logic to initialize, e.g. the gmail service

		try{
			/*Authentication class return required service object*/
			this.gmailService = Authentication.serviceObject();
			
		}catch(Exception e){

			System.out.println(e.getMessage());
		}

		this.DataWritten = "";
	}

	/**
	* Creates a new character-stream writer whose critical sections will
	* synchronize on the given object.
	*
	* @param lock Object to synchronize on
	* @param filePath Gmail inbox path of the file to write
	*/
	public GmailFileWriter(Object lock, String filePath) throws IOException {

		super(lock);
		this.filePath = filePath;
		//TODO: Implement proper logic to initialize, e.g. the gmail service
		/*Authentication class return required service object*/
		try{
			this.gmailService = Authentication.serviceObject();
			
		}catch(Exception e){

			System.out.println(e.getMessage());
		}		
		this.DataWritten = "";

	}

	/*Write :-

		1. Get the Draft.
		2. Append the string to current content.
		3. Update the Draft.

	*/
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
	//TODO: Implement proper logic

		/*Append the new character array to buffer and when flush is called it will save the content*/
		try{
			this.DataWritten += new String(cbuf, off, len);
			//System.out.println("This Data is being Written : "+this.DataWritten);
			
		}catch(IndexOutOfBoundsException e){

			System.out.println(e.getMessage());
		}
		
	}

	/*Flush :- 

		1. Get message Stored in Draft till Now
		2. Update Draft message to NULL
		3. Output the message Content		

	*/
	@Override
	public void flush() throws IOException {
	//TODO: Implement proper logic

		try{
			/*Create new email,Messgae and Update the Previous Draft*/
			//System.out.println("This Data Will be Written : "+this.DataWritten);
			if(this.DataWritten.equals("") == false){ 

				String userId = "me";
				/*Get email and message part of that email to store as Draft*/
	    			MimeMessage email = Drafts.createEmail(filePath, this.DataWritten);
				Message updated = Drafts.createMessageWithEmail(email);

				/*Create new Draft and set message*/
				Draft draft = new Draft();
				draft.setMessage(updated);

				/*Create the draft*/
				draft = gmailService.users().drafts().create(userId, draft).execute();

			}else{

				//System.out.println("No Message Content to Write");
				return;
			}			

		}catch(MessagingException e){

			System.out.println(e.getMessage());

		}

		/*Empty the Buffer*/
		this.DataWritten = "";

	
	}


	/*Close :-

		1. Call flush() to first flush the content
		2. Delete the Draft Written by this writer

	*/
	@Override
	public void close() throws IOException {
	//TODO: Implement proper logic

		/*Flush the remaining content to mail and Refer it to null for avoiding further access*/
		flush();
		//this = null;
	}
}
