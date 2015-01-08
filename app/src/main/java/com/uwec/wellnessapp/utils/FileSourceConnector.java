package com.uwec.wellnessapp.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.uwec.wellnessapp.login.LoginActivity;
import com.uwec.wellnessapp.scripts.PushStaticData;
import com.uwec.wellnessapp.statics.Statics;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
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
import java.util.concurrent.Executor;

/**
 * Created by butlernc on 12/3/2014.
 */
public class FileSourceConnector {

    /** link to FTP site variables */
    private FTPClient ftpClient;

    private static final String FTP_HOSTNAME = "f13-preview.freehostingeu.com";
    private static final String FTP_USERNAME = "1769350_uwecwellnessapp";
    private static final String FTP_ACCOUNT = "1769350";
    private static final String FTP_PASSWORD = "android4us";
    private static final String FTP_PATH = "wellnessappftp.eu.pn";

    private static final String PASSWORD_FILE_NAME = "password.txt";
    private static final String USER_DATA_FILE_NAME = "userfile.txt";

    private static final String WEEK_FILE_NAME_TRUNC = "week_data_";
    private static final String WEEK_DATA_DIRECTORY = "weekData";
    private static final String WEEK_DATA_FILE_NAME = "new_week_info.txt";

    private static Context currentContext;

    private String userDirectory;
    private JsonFileConverter jsonFileConverter;
    private String RETURN_STR = "";
    private boolean isDone;

    public Runnable queue(final String... strings) {
        Runnable runnable = null;
        Log.d("FILESOURCE", "creating file source connector object");
        ftpClient = new FTPClient();
        jsonFileConverter = new JsonFileConverter();
        isDone(false);

        if(strings[0] == "readUser") {
            runnable = new Runnable() {
                @Override
                public void run() {
                    readUser(strings);
                }
            };
        }else if(strings[0] == "writeUser") {
            runnable = new Runnable() {
                @Override
                public void run() {
                    writeUser(strings);
                }
            };
        }else if(strings[0] == "readWeekData") {
            runnable = new Runnable() {
                @Override
                public void run() {
                    readWeekData(strings);
                }
            };
        }else if(strings[0].contentEquals("readWeekStartData")) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    readWeekStartData(strings);
                }
            };
        }else if(strings[0].contentEquals("writeWeekData")) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    writeWeekData(strings);
                }
            };
        }
        return runnable;
    }

    /** needed to get the current context so that we can read and write temp files to the phone */
    public static void setContext(Context context) {
        currentContext = context;
    }

    /** check if current read or write from server is finished */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * Set if IO to the server is completed
     * @param isDone
     */
    private void isDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Used to connect to the FTP sever.
     *
     * @return boolean
     * @throws IOException
     */
    private boolean connectToFTP() throws IOException {
        ftpClient.connect(InetAddress.getByName(FTP_HOSTNAME));
        boolean status = ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
        return status;
    }

    private boolean setTransferSettings() throws IOException {
        //set transfer settings
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();

        //change our current path to the path that all of the user information is held
        boolean worked = ftpClient.changeWorkingDirectory(FTP_PATH);
        return worked;
    }

    /**
     * Returns a json object of the given
     * file name on the server
     *
     * @param filename
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private JSONObject readFromServer(String filename) throws IOException, JSONException {
        //retrieve data file
        Log.d("F", "Filename: " + filename);
        BufferedReader dataReader = new BufferedReader(new InputStreamReader(ftpClient.retrieveFileStream(filename)));

        //helper objects
        StringBuilder sb = new StringBuilder();
        String line;

        //loop over the data file
        while((line = dataReader.readLine()) != null) {
            sb.append(line);
        }

        //close the buffered reader
        dataReader.close();

        //get file data from string builder and put it into a json object
        return new JSONObject(sb.toString());
    }

    /**
     * Used to create a json temp file that
     * will be transferred to the FTP server
     *
     * @param filename
     * @param inputData
     * @param isUserData
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public File createTempFile(String filename, String inputData, boolean isUserData) throws JSONException, IOException{
        //make json object
        JSONObject jsonObject = new JSONObject();
        String rawData = "";
        if(isUserData) {
            jsonObject = jsonFileConverter.convertToJSON(Statics.globalUserData);
            rawData = jsonObject.toString();
        }else{
            rawData = inputData;
        }

        FileOutputStream tempFile = currentContext.openFileOutput(filename, Context.MODE_PRIVATE);
        tempFile.write(rawData.getBytes());
        tempFile.close();

        return new File(currentContext.getFilesDir(), filename);
    }

    /**
     * Used to write the user data and user password
     * @param strings
     *
     * strings[0]: user directory/user email
     * strings[1]: user password
     * strings[2]: "writeUser"
     * strings[3]: "new" if registering, "old" if not
     */
    private void writeUser(String[] strings) {

        //get the what the user directory name should be
        userDirectory = strings[1];
        ftpClient.setConnectTimeout(10 * 1000);

        try {
            //try to connect to the server
            connectToFTP();
            //check if logged in correctly
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                //set transfer settings
                boolean worked = setTransferSettings();
                //check if we're making a new user or not
                if(strings[3] == "new") {
                    ftpClient.makeDirectory(userDirectory);
                    ftpClient.changeWorkingDirectory(userDirectory);
                }else {
                    ftpClient.changeWorkingDirectory(userDirectory);
                }

                //create the user data temp file
                File userDataSourceFile = createTempFile(USER_DATA_FILE_NAME, null, true);
                //save our temp file to the server
                ftpClient.storeFile(USER_DATA_FILE_NAME, new FileInputStream(userDataSourceFile));

                //close connection
                ftpClient.disconnect();

                isDone(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
        ftpClient.setConnectTimeout(100 * 1000);

        try {
            //try connecting to the FTP Server
            connectToFTP();

            //check if logged in correctly
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                //set transfer config params
                boolean worked = setTransferSettings();

                //get all files and directories in the current directory
                FTPFile[] mFileArray = ftpClient.listFiles();

                //check for user directory
                for(FTPFile file : mFileArray) {
                    if(file.getName().contentEquals(strings[1])) {
                        //get the user's directory name
                        userDirectory = file.getName();

                        //change to the given user directory
                        boolean changed = ftpClient.changeWorkingDirectory(userDirectory);

                        //get password that is saved on the server
                        JSONObject jsonObject = readFromServer(USER_DATA_FILE_NAME);
                        String expected = jsonObject.getString("password");

                        //check password with entered password
                        if(expected.compareTo(strings[2]) == 0) {
                            //password is correct, get user data
                            //set the application's userData object.
                            Statics.globalUserData = jsonFileConverter.convertUserDataJSON(jsonObject);

                            RETURN_STR = "GOOD";
                            isDone(true);
                        }else{
                            //return that the user entered in the wrong password
                            RETURN_STR = "NCP";
                            isDone(true);
                        }
                    }
                }
            }
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Used to read files from the goal directory
     * @param strings
     * strings[0]: current week #
     *
     */
    private void readWeekData(String[] strings) {
        ftpClient.setConnectTimeout(10 * 1000);

        try {
            //try connecting to the FTP Server
            connectToFTP();

            //check if logged in correctly
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                //set transfer config params
                boolean worked = setTransferSettings();
                ftpClient.changeWorkingDirectory(WEEK_DATA_DIRECTORY);

                String fullFileName = WEEK_FILE_NAME_TRUNC + strings[1] + ".txt";
                JSONObject jsonObject = readFromServer(fullFileName);
                Statics.globalWeekData = jsonFileConverter.convertWeekDataJSON(jsonObject);

            }

            isDone(true);

            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the file that saves the weeks' start dates.
     *
     * @param strings
     */
    private void readWeekStartData(String[] strings) {
        ftpClient.setConnectTimeout(10 * 1000);

        try {
            //try connecting to the FTP Server
            connectToFTP();

            //check if logged in correctly
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                //set transfer config params
                boolean worked = setTransferSettings();
                ftpClient.changeWorkingDirectory(WEEK_DATA_DIRECTORY);

                JSONObject jsonObject = readFromServer(WEEK_DATA_FILE_NAME);
                RETURN_STR = jsonObject.toString();

                isDone(true);

            }
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to write all of the week data to the server.
     * @param strings
     */
    private void writeWeekData(String[] strings) {
        ftpClient.setConnectTimeout(10 * 1000);

        try {
            connectToFTP();

            if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                boolean worked  = setTransferSettings();
                if(strings[1].contentEquals("new")) {
                    ftpClient.makeDirectory(WEEK_DATA_DIRECTORY);
                }

                ftpClient.changeWorkingDirectory(WEEK_DATA_DIRECTORY);

                /* call methods that create our strings of data for our files */
                String rawData = PushStaticData.weekStartDays();
                File file = createTempFile(WEEK_DATA_FILE_NAME, rawData, false);
                //ftpClient.storeFile(WEEK_DATA_FILE_NAME, new FileInputStream(file));

                //String weekRawData = PushStaticData.weeklyData(1);
                //File file1 = createTempFile("week_data_1.txt", weekRawData, false);
                //ftpClient.storeFile("week_data_1.txt", new FileInputStream(file1));

                isDone(true);
            }
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getRETURN_STR() {
        return RETURN_STR;
    }
}
