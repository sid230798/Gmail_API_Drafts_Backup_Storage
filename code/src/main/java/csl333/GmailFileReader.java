package csl333;

import java.io.IOException;
import java.io.Reader;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListDraftsResponse;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;

import java.util.List;

/**
 * This class implements the {@link Reader} functionality for reading files
 * stored as Gmail messages. The Gmail allows messages to be organized using
 * folders and labels etc. in the inbox. A file thus can be stored at a
 * suitable "path" in the Gmail inbox.
 *
 * TODO: Implement/override other relevant methods of {@link Reader} class as
 * required to work with Gmail inbox based storage.
 */
public class GmailFileReader extends Reader {

	private String filePath;
	private Gmail gmailService;

	/**
	* Creates a new character-stream reader whose critical sections will
	* synchronize on the reader itself.
	* @param filePath Gmail inbox path of the file to read
	*/

	/*Constructor :-

		1. Get Gmail Service permission for Reading Drafts.
		2. Get DraftID for Draft with filePAth as Subject.

	*/
	public GmailFileReader(String filePath) throws IOException{
		
		this.filePath = filePath;
		//TODO: Implement proper logic to initialize, e.g. the gmail service
		try{

			/*Get gmailService Object for reading drafts*/
			this.gmailService = Authentication.serviceObject();
		}catch(Exception e){

			System.out.println(e.getMessage());
		}
	}

	/**
	* Creates a new character-stream reader whose critical sections will
	* synchronize on the given object.
	*
	* @param lock The Object to synchronize on.
	* @param filePath Gmail inbox path of the file to read
	*/
	public GmailFileReader(Object lock, String filePath) throws IOException{

		super(lock);
		this.filePath = filePath;
		//TODO: Implement proper logic to initialize, e.g. the gmail service
		/*Authentication class return required service object*/
		try{
			this.gmailService = Authentication.serviceObject();
		}catch(Exception e){

			System.out.println(e.getMessage());
		}
	}

	/*Read :-

		1. Get message from DraftID stored
		2. Read the message and store in char array
	*/
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		//TODO: Implement proper logic
		String userId = "me";		

		/*Generate list of Drafts preset*/
		ListDraftsResponse listResponse = gmailService.users().drafts().list(userId).execute();
		List<Draft> drafts = listResponse.getDrafts();

		if(drafts == null){

			System.out.println("No Drafts Are Present");

		}else{

			String bodyMessage = "";
			/*Check for each Draft it's Subject*/
			for(Draft sample : drafts){

				/*Subject is stored in message part of Draft get Message ID*/
				String messageId = sample.getMessage().getId();

				/*Request full Message content text/plain type or mime message type*/
				Message content = gmailService.users().messages().get(userId, messageId).setFormat("full").execute();
				MessagePart part = content.getPayload();
				//System.out.println(part);
	
				/*Get the Header List which consistes subject*/
				List<MessagePartHeader> header = part.getHeaders();


				/*Iterate through each header and check for Subject name*/
				for(MessagePartHeader head : header){

					if("Subject".equals(head.getName()) == true){

						//System.out.println("Subject of Mail = "+head.getValue());
						
						/*If Subject is Same what are we looking then decode message*/
						if(head.getValue().equals(this.filePath) == true){

							String type = part.getMimeType();
							
							/*If message only contains plainText then decode bytes*/
							if(type.equals("text/plain") == true){

								MessagePartBody body = part.getBody();
								byte[] m = body.decodeData();
								//System.out.println("Body Content : "+new String(m));
								bodyMessage = new String(m) + bodyMessage;

							
							}else{
								/*Else it contains encoded message extract it*/
								List<MessagePart> allParts = part.getParts();
								for(MessagePart p : allParts){
								
									if(p.getMimeType().equals("text/plain") == true){

										MessagePartBody body = p.getBody();
										byte[] m = body.decodeData();
										//System.out.println("Body Content : "+new String(m));
										bodyMessage = new String(m) + bodyMessage;
										

									}
								}
							}
						}
					}

				}
				//System.out.println("------------------------------");
				
				
			}
			System.out.println("Body Part of Message : "+bodyMessage);
			char arr[] = bodyMessage.toCharArray();
			for(int i = off;i<len;i++)
				cbuf[i] = arr[i];
		}

		return 0;
	}

	/*Close :-

		1. Nothing to be done

	*/
	@Override
	public void close() throws IOException {
	//TODO: Implement proper logic
	}
}
