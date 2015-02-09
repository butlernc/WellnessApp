package com.uwec.wellnessapp.utils;

import android.content.Context;
import android.util.Log;

import com.uwec.wellnessapp.statics.Statics;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * Created by Noah Butler on 12/3/2014.
 *
 * Used to read and write to our Server
 */
public class FileSourceConnector {

    /** link to FTP site variables */
    private FTPClient ftpClient;

    private static final String FTP_HOSTNAME = "f13-preview.freehostingeu.com";
    private static final String FTP_USERNAME = "1769350_uwecwellnessapp";
    private static final String FTP_PASSWORD = "android4us";
    private static final String FTP_PATH = "wellnessappftp.eu.pn";

    private static final String USER_DATA_FILE_NAME = "userfile.txt";

    private static final String WEEK_FILE_NAME_TRUNC = "week_data_";
    private static final String WEEK_DATA_DIRECTORY = "weekData";
    private static final String WEEK_DATA_FILE_NAME = "new_week_data.txt";

    private Context currentContext;

    private JsonFileConverter jsonFileConverter;
    private String RETURN_STR = "";

    public FileSourceConnector(Context context) {
        this.currentContext = context;
    }

    public Object queue(final String... strings) {

        Log.d("line~50", "creating file source connector object");
        ftpClient = new FTPClient();
        jsonFileConverter = new JsonFileConverter();

        if(strings[0].contentEquals("readUser")) {
            readUser(strings);
        }else if(strings[0].contentEquals("writeUser")) {
            writeUser(strings);
        }else if(strings[0].contentEquals("readWeekStartData")) {
            return readWeekStartData();
        } else if(strings[0].contentEquals("readWeekData")) {
            return readWeekData(strings);
        }else {
            Log.d("line~65", "Not a recognized task");
        }
        return null;
    }

    /**
     * Used to connect to the FTP sever.
     *
     * @return boolean
     * @throws IOException
     */
    private boolean connectToFTP() throws IOException {
        ftpClient.connect(InetAddress.getByName(FTP_HOSTNAME));
        return ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
    }

    /**
     * Used when initializing FTPClient
     * @return boolean to see if this worked
     * @throws IOException
     */
    private boolean setTransferSettings() throws IOException {
        //set transfer settings
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();

        //change our current path to the path that all of the user information is held
        return ftpClient.changeWorkingDirectory(FTP_PATH);
    }

    /**
     * Returns a json object of the given
     * file name on the server
     *
     * @param absoluteFilePath: where the file is located (ending with the file name included)
     * @return json object made from the file on the FTP Server.
     */
    private JSONObject readFromServer(String absoluteFilePath) {
        ftpClient.setConnectTimeout(10 * 1000);
        Log.d("F", "Filename: " + absoluteFilePath);
        //create our return object
        JSONObject jsonObject = null;

        try {

            /* attempt to connect to the FTP server. */
            boolean notConnectedToFTP = true;
            do {
                notConnectedToFTP = !connectToFTP();
            } while (notConnectedToFTP);

        /* check if logged in correctly */
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {

            /* We should be connected to the FTP server now, set the transfer settings. */
                if (setTransferSettings()) {

                    BufferedReader dataReader = new BufferedReader(new InputStreamReader(ftpClient.retrieveFileStream(absoluteFilePath)));

                    //helper objects
                    StringBuilder sb = new StringBuilder();
                    String line;

                    //loop over the data file
                    while ((line = dataReader.readLine()) != null) {
                        sb.append(line);
                    }

                    //close the buffered reader
                    dataReader.close();

                    //get file data from string builder and put it into a json object
                    jsonObject = new JSONObject(sb.toString());
                }
            }

            ftpClient.disconnect();
        }catch(IOException | JSONException e) {
            e.printStackTrace();
            //String error = "{\"error\":\"Failed to connect, please restart the application.\"}";
            //jsonObject = new JSONObject(error);
        }

        return jsonObject;
    }

    /**
     * Used to create a json temp file that
     * will be transferred to the FTP server
     *
     * @param filename
     * @return local file to push to server
     * @throws JSONException
     * @throws IOException
     */
    public File createTempFile(String filename) throws JSONException, IOException{
        //make json object
        JSONObject jsonObject;
        String rawData;

        jsonObject = jsonFileConverter.convertUserToJSON(Statics.globalUserData);
        rawData = jsonObject.toString();

        FileOutputStream tempFile = currentContext.openFileOutput(filename, Context.MODE_PRIVATE);
        tempFile.write(rawData.getBytes());
        tempFile.close();

        return new File(currentContext.getFilesDir(), filename);
    }

    /**
     * Used to write the user data and user password
     * @param strings
     *
     * strings[0]: "writeUser"
     * strings[1]: user email
     * strings[2]: "new" if registering, "old" if not
     */
    private void writeUser(String[] strings) {

        ftpClient.setConnectTimeout(10 * 1000);
        try {
            //try to connect to the server
            connectToFTP();
            //check if logged in correctly
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                //set transfer settings
                boolean worked = setTransferSettings();
                //check if we're making a new user or not
                if(strings[2] == "new") {
                    ftpClient.makeDirectory(strings[1]);
                    ftpClient.changeWorkingDirectory(strings[1]);
                }else {
                    ftpClient.changeWorkingDirectory(strings[1]);
                }

                //create the user data temp file
                File userDataSourceFile = createTempFile(USER_DATA_FILE_NAME);
                //save our temp file to the server
                ftpClient.storeFile(USER_DATA_FILE_NAME, new FileInputStream(userDataSourceFile));

                //close connection
                ftpClient.disconnect();

                Statics.messenger.sendMessage("Created new user...");
                RETURN_STR = "GOOD";
            }
        } catch (IOException e) {
            RETURN_STR = "NOGOOD";
            e.printStackTrace();
        } catch (JSONException e) {
            RETURN_STR = "NOGOOD";
            e.printStackTrace();
        }
    }

    /**
     * Used when logging in
     *
     * Assigns the static user data object
     * with the data object created from the file
     * read in from the server.
     * @param strings
     *
     * strings[0]: user directory/user email
     * strings[1]: user password
     * strings[2]: "readUser"
     */
    private void readUser(String[] strings) {

        //get password that is saved on the server
        String absolutePath = strings[1] + "/" + USER_DATA_FILE_NAME;
        JSONObject jsonObject = readFromServer(absolutePath);

        try {
            /* grab their password */
            String expected = jsonObject.getString("password");
            /* check password with entered password */
            if (expected.compareTo(strings[2]) == 0) {
                Log.e("here", "password is correct");
                RETURN_STR = "GOOD";
                /* password is correct, get user data set the application's userData object. */
                Statics.globalUserData = jsonFileConverter.convertJSONToUser(jsonObject);
                Log.d("STR", "RETURN STRING: " + RETURN_STR);
            } else {
                /* return that the user entered in the wrong password */
                RETURN_STR = "NCP";
                Log.d("STR", "should not be here");
            }
        }catch (JSONException e) {
            e.printStackTrace();
            RETURN_STR = "NCP";
        }
    }

    /**
     * Used to read files from the goal directory
     * @param strings
     * strings[0]: current week #
     *
     */
    private Object readWeekData(String[] strings) {

        String absoluteFilePath = WEEK_DATA_DIRECTORY + "/" + WEEK_FILE_NAME_TRUNC + strings[1] + ".txt";
        Log.e("DATA", "" + absoluteFilePath);
        JSONObject jsonObject = readFromServer(absoluteFilePath);
        try {
            Statics.globalWeekDataList.add(jsonFileConverter.convertWeekDataJSON(jsonObject));
        }catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("DATA", "get it");
        return true;
    }

    /**
     * Reads the file that saves the weeks' start dates.
     */
    private Object readWeekStartData() {
        String absoluteFilePath = WEEK_DATA_DIRECTORY + "/" + WEEK_DATA_FILE_NAME;
        JSONObject jsonObject = readFromServer(absoluteFilePath);
        return jsonObject;
    }

    /**
     * used as a callback method
     * @return return_string
     */
    public String getRETURN_STR() {
        return RETURN_STR;
    }
}
