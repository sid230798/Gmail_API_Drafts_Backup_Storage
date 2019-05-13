import csl333.GmailFileReader;
import csl333.GmailFileWriter;

import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("CSL333 Assignment #6."+args.length);
        // TODO: Do proper CLI handling.
        String gmailFilePath = args[0];
        String localFilePath = args[1];
	//for(String name : args)
	//	System.out.println(name);

	//String gmailFilePath = "Sample.txt";
	//String localFilePath = "Sample.txt";
        Main app = new Main();
        app.testReadWrite(gmailFilePath, localFilePath);
    }

    private void testReadWrite(String gmailFilePath, String localFilePath) {

        // Read text from a local file
        String localFileData = readLocalFileContents(localFilePath);

        // Write the text to Gmail inbox based "file"
        try(GmailFileWriter gfw = new GmailFileWriter(gmailFilePath)) {

            // TODO: Perform steps to write using gfw

		char buff[] = localFileData.toCharArray();
		gfw.write(buff, 0, buff.length);
		gfw.flush();

        } catch (IOException e) {
            // TODO: Handle exception properly
            e.printStackTrace();
        }

        // Read back the data from Gmail inbox based "file"
        String gmailFileData = null;
        try(GmailFileReader gfr = new GmailFileReader(gmailFilePath)) {

            // TODO: Perform steps to read the file using gfr
            // gmailFileData = ....
		char buff[] = new char[localFileData.length()];
		gfr.read(buff,0,localFileData.length());

		gmailFileData = new String(buff);
		//System.out.println(gmailFileData.length());
		//System.out.println(localFileData.length());
		

        } catch (IOException e) {
            // TODO: Handle exception properly
            e.printStackTrace();
        }

        /**
         * Compare the data read via gfr with the original. You can perform
         * a suitable comparison to check correctness of read/write to Gmail
         */
        if (gmailFileData.equals(localFileData) == true) {
            System.out.println("Success");
        } else {
            System.out.println("Failure");
        }

    }

    private String readLocalFileContents(String localFilePath) {
        // TODO: Implement it

	try{
		File fp = new File(localFilePath);
		Scanner sc = new Scanner(fp);

		String dp = "";

		while(sc.hasNextLine())
			dp += sc.nextLine();
		
        return dp;

	}catch(Exception e){

		System.out.println(e.getMessage());
		return null;

	}
    }
}
