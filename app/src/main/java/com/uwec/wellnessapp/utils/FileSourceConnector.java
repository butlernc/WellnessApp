package com.uwec.wellnessapp.utils;

import android.content.Context;
import android.util.Log;

import com.uwec.wellnessapp.R;
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

/**
 * Created by Noah Butler on 12/3/2014.
 *
 * Used to read and write to our Server
 */
public class FileSourceConnector {

    /** link to FTP site variables */
    private FTPClient ftpClient;

    private static final String FTP_HOSTNAME = "headphones4us.square7.ch";
    private static final String OLD_HOSTNAME = "f13-preview.freehostingeu.com";
    private static final String FTP_USERNAME = "headphones4us";
    private static final String OLD_USERNAME = "1769350_uwecwellnessapp";
    private static final String FTP_PASSWORD = "headphones4us";
    private static final String OLD_PASSWORD = "android4us";
    private static final String FTP_PATH = "/";
    private static final String OLD_PATH = "wellnessappftp.eu.pn";

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

        if (strings[0].contentEquals("readUser")) {
            readFullUserFromServer(strings);
        } else if (strings[0].contentEquals("writeUser")) {
            writeFullUserToServer(strings);


        } else if(strings[0].contentEquals("writePaPointsCache")) {
            writePaPointsCache();
        }else if(strings[0].contentEquals("writeNgPointsCache")) {
            writeNgPointsCache();
        }else if(strings[0].contentEquals("writeBonusPointsCache")) {
            writeBonusPointsCache();
        }else if(strings[0].contentEquals("writeUserInfoCache")) {
            writeUserInfoCache();


        }else if(strings[0].contentEquals("writePaPointsToServer")) {
            writePaPointsToServer();
        }else if(strings[0].contentEquals("writeNgPointsToServer")) {
            writeNgPointsToServer();
        }else if(strings[0].contentEquals("writeBonusPointsToServer")) {
            writeBonusPointsToServer();
        }else if(strings[0].contentEquals("writeUserInfoToServer")) {
            writeUserInfoToServer();


        }else if(strings[0].contentEquals("writeCachedUserToServer")) {
            writeCachedUserToServer(strings);


        }else if(strings[0].contentEquals("readWeekStartData")) {
            return readWeekStartData();
        } else if(strings[0].contentEquals("readWeekData")) {
            return readWeekData(strings);
        } else if(strings[0].contentEquals("readBonusData")) {
            return readBonusData();
        } else {
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
        ftpClient.setConnectTimeout(10 * 1000);
        ftpClient.connect(FTP_HOSTNAME);
        return ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
    }

    /**
     * Used to close the connection to the FTP Server
     *
     * @throws java.io.IOException
     */
    private void disconnectFromFTP() throws IOException {
        ftpClient.logout();
        ftpClient.disconnect();
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
     * The following methods are used to call the UserIO objects methods to read and write
     * the user's data.
     */

    /**
     * Used to set the userData object at startup
     * string[1]: user's email
     * string[2]: user's password
     */
    private void readFullUserFromServer(String[] strings) {
        UserIO userIO = new UserIO(strings[1], strings[2]);
        userIO.constructUserObject();

    }

    private void writeFullUserToServer(String[] strings) {
        UserIO userIO = new UserIO(strings[1], strings[2]);
        userIO.writeNewUserObjectToServer();
    }

    /**
     * Used to write all the user data that is saved on the phone to the database.
     * @param strings
     */
    private void writeCachedUserToServer(String[] strings) {
        UserIO userIO = new UserIO(strings[1], strings[2]);
        userIO.writeCachedUserObject();
    }

    /**
     * the following methods are used to quickly save data to the phone instead of the server
     */
    private void writePaPointsCache() {
        try {
            UserIO userIO = new UserIO(Statics.globalUserData.getEmail(), Statics.globalUserData.getPassword());
            userIO.writePhysicalPoints(false, true);
        }catch(IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    private void writeNgPointsCache() {
        try {
            UserIO userIO = new UserIO(Statics.globalUserData.getEmail(), Statics.globalUserData.getPassword());
            userIO.writeNutritionPoints(false, true);
        }catch(IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    private void writeBonusPointsCache() {
        try {
            UserIO userIO = new UserIO(Statics.globalUserData.getEmail(), Statics.globalUserData.getPassword());
            userIO.writeBonusPoints(false, true);
        }catch(IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    private void writeUserInfoCache() {
        try {
            UserIO userIO = new UserIO(Statics.globalUserData.getEmail(), Statics.globalUserData.getPassword());
            userIO.writeUserInfo(false, true);
        }catch(IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * The following methods are used to write the cached data to the server
     */
    private void writePaPointsToServer() {
        try {
            UserIO userIO = new UserIO(Statics.globalUserData.getEmail(), Statics.globalUserData.getPassword());
            userIO.writePhysicalPoints(true, true);
        }catch(IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    private void writeNgPointsToServer() {
        try {
            UserIO userIO = new UserIO(Statics.globalUserData.getEmail(), Statics.globalUserData.getPassword());
            userIO.writeNutritionPoints(true, true);
        }catch(IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    private void writeBonusPointsToServer() {
        try {
            UserIO userIO = new UserIO(Statics.globalUserData.getEmail(), Statics.globalUserData.getPassword());
            userIO.writeBonusPoints(true, true);
        }catch(IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    private void writeUserInfoToServer() {
        try {
            UserIO userIO = new UserIO(Statics.globalUserData.getEmail(), Statics.globalUserData.getPassword());
            userIO.writeUserInfo(true, true);
        }catch(IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads in a file contained in the project files. Mostly used for getting the week data,
     * the bonus data, and the start week data.
     */
    private JSONObject readFromFile(int id) {

        try {
            BufferedReader dataReader = new BufferedReader(new InputStreamReader(currentContext.getResources().openRawResource(id)));

            //helper objects
            StringBuilder sb = new StringBuilder();
            String line;

            //loop over the data file
            while ((line = dataReader.readLine()) != null) {
                sb.append(line);
            }

            //close the buffered reader
            dataReader.close();

            return new JSONObject(sb.toString());

        }catch(IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Used to read files from the goal directory
     * @param strings
     * strings[1]: current week #
     *
     */
    private Object readWeekData(String[] strings) {
        int id = 0;
        switch(Integer.parseInt(strings[1])) {
            case 1:
                id = R.raw.week_data_1;
                break;
            case 2:
                id = R.raw.week_data_2;
                break;
            case 3:
                id = R.raw.week_data_3;
                break;
            case 4:
                id = R.raw.week_data_4;
                break;
            case 5:
                id = R.raw.week_data_5;
                break;
            case 6:
                id = R.raw.week_data_6;
                break;
        }
        JSONObject jsonObject = readFromFile(id);
        try {
            if(jsonObject.has("error")) {
                return jsonObject.getString("error");
            }else {
                Statics.globalWeekDataList.add(jsonFileConverter.convertWeekDataJSON(jsonObject));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Reads the file that saves the weeks' start dates.
     */
    private Object readWeekStartData() {
        JSONObject jsonObject = readFromFile(R.raw.new_week_data);
        return jsonObject;
    }

    /**
     * Reads in all of the data for the bonus
     * activities the user can complete.
     */
    private Object readBonusData() {
        JSONObject jsonObject = readFromFile(R.raw.bonus_data);
        try {
            if(jsonObject.has("error")) {
                return jsonObject.getString("error");
            }else {
                Statics.globalBonusData = jsonFileConverter.convertBonusDataJSON(jsonObject);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return true;


    }
    /**
     * used as a callback method
     * @return return_string
     */
    public String getRETURN_STR() {
        return RETURN_STR;
    }

    public class UserIO{

        private static final String PA_POINTS_FILE = "user_pa_points.txt";
        private static final String BONUS_POINTS_FILE = "user_bonus_points.txt";
        private static final String NG_POINTS_FILE = "user_ng_points.txt";
        private static final String USER_INFO_FILE = "user_data.txt";

        private UserJsonFileConverter userJsonFileConverter;
        private String userName;
        private String userPassword;

        public UserIO(String userName, String userPassword) {
            this.userName = userName;
            this.userPassword = userPassword;
            userJsonFileConverter = new UserJsonFileConverter();
        }

        public void writeNewUserObjectToServer() {
            try {
                Statics.messenger.registering("Creating User File...");
                writePhysicalPoints(true, true);
                writeNutritionPoints(true, true);
                writeBonusPoints(true, true);
                Statics.messenger.registering("Completing Registration...");
                writeUserInfo(true, true);
                RETURN_STR = "GOOD";
            }catch (IOException | JSONException e) {
                e.printStackTrace();
                RETURN_STR = "NOGOOD";
            }
        }

        public void writeCachedUserObject() {
            try {
                writePhysicalPoints(true, false);
                writeNutritionPoints(true, false);
                writeBonusPoints(true, false);
                writeUserInfo(true, false);
                RETURN_STR = "GOOD";
            }catch (IOException | JSONException e) {
                e.printStackTrace();
                RETURN_STR = "NOGOOD";
            }
        }

        /**
         * Used to write the user's physical points to the server
         * @throws JSONException
         * @throws IOException
         */
        public void writePhysicalPoints(boolean toServer, boolean toTemp) throws JSONException, IOException{
            String rawData;

            rawData = userJsonFileConverter.convertUserPAPointsToJSON(Statics.globalUserData).toString();
            if(toTemp) {
                writeTempFile(rawData, PA_POINTS_FILE);
            }
            if(toServer) {
                writeToServer(PA_POINTS_FILE, new File(currentContext.getFilesDir(), PA_POINTS_FILE));
            }
        }

        /**
         * The following two methods are used to read/write the bonus points for a user.
         * @return
         * @throws JSONException
         * @throws IOException
         */
        public void writeNutritionPoints(boolean toServer, boolean toTemp) throws JSONException, IOException{
            String rawData;

            rawData = userJsonFileConverter.convertUserNGPointsToJSON(Statics.globalUserData).toString();
            if(toTemp) {
                writeTempFile(rawData, NG_POINTS_FILE);
            }
            if(toServer) {
                writeToServer(NG_POINTS_FILE, new File(currentContext.getFilesDir(), NG_POINTS_FILE));
            }
        }

        /**
         * The following method is used to write the bonus points for a user.
         * @return
         * @throws JSONException
         * @throws IOException
         */
        public void writeBonusPoints(boolean toServer, boolean toTemp) throws JSONException, IOException {
            String rawData;

            rawData = userJsonFileConverter.convertUserBonusPointsToJSON(Statics.globalUserData).toString();
            if(toTemp) {
                writeTempFile(rawData, BONUS_POINTS_FILE);
            }
            if (toServer) {
                writeToServer(BONUS_POINTS_FILE, new File(currentContext.getFilesDir(), BONUS_POINTS_FILE));
            }
        }

        /**
         * The following read/writes the user info data
         * @return
         * @throws JSONException
         * @throws IOException
         */
        public void writeUserInfo(boolean toServer, boolean toTemp) throws JSONException, IOException {
            String rawData;

            rawData = userJsonFileConverter.convertUserInfoToJSON(Statics.globalUserData).toString();
            if(toTemp) {
                writeTempFile(rawData, USER_INFO_FILE);
            }
            if (toServer) {
                writeToServer(USER_INFO_FILE, new File(currentContext.getFilesDir(), USER_INFO_FILE));
            }
        }

        /**
         * Used to construct the Statics.globalUserData object, called by readFullUserFromServer()
         */
        public void constructUserObject() {
            try {
            /* call all read methods, use the userJsonFileConverter.userData object when setting the Statics.globalUserData object */
                JSONObject userInfoJSON = readUserInfo();
                Statics.messenger.loggingIn("Checking Password...");
                if (userInfoJSON != null) {
                    if(userInfoJSON.getString("password").contentEquals(userPassword)) {
                        userJsonFileConverter.convertJSONToUserInfo(userInfoJSON);
                        Statics.messenger.loggingIn("Loading Your Data...");
                        userJsonFileConverter.convertJSONToUserPAPoints(readPhysicalPoints());
                        userJsonFileConverter.convertJSONToUserNGPoints(readNutritionPoints());
                        userJsonFileConverter.convertJSONToUserBPoints(readBonusPoints());
                        RETURN_STR = "GOOD";
                    }else{
                        RETURN_STR = "NCP";
                    }

                    Statics.globalUserData = userJsonFileConverter.userData;
                }else{
                    RETURN_STR = "INVALID";
                }

            }catch(IOException | JSONException e){
                e.printStackTrace();
            }
        }

        private JSONObject readPhysicalPoints() throws JSONException, IOException {
            return readFromServer(PA_POINTS_FILE);
        }
        private JSONObject readNutritionPoints() throws JSONException, IOException {
            return readFromServer(NG_POINTS_FILE);
        }
        private JSONObject readBonusPoints() {
            return readFromServer(BONUS_POINTS_FILE);
        }
        private JSONObject readUserInfo() throws JSONException, IOException{
            return readFromServer(USER_INFO_FILE);
        }

        /**
         * The following methods are used as helper methods for this object
         * @param rawData
         * @param filename
         * @throws JSONException
         * @throws IOException
         */

        private void writeTempFile(String rawData, String filename) throws JSONException, IOException{
            FileOutputStream tempFile = currentContext.openFileOutput(filename, Context.MODE_PRIVATE);
            tempFile.write(rawData.getBytes());
            tempFile.close();
        }

        private void writeToServer(String staticFileName, File fileToWrite) {
            ftpClient.setConnectTimeout(10 * 1000);
            try {
                //try to connect to the server
                connectToFTP();
                //check if logged in correctly
                if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                    //set transfer settings
                    boolean worked = setTransferSettings();
                    //check if we're making a new user or not

                    //save our temp file to the server
                    changeWorkDirectory();
                    ftpClient.storeFile(staticFileName, new FileInputStream(fileToWrite));

                    //close connection
                    disconnectFromFTP();

                    RETURN_STR = "GOOD";
                }
            } catch (IOException e) {
                RETURN_STR = "NOGOOD";
                e.printStackTrace();
            }
        }

        private JSONObject readFromServer(String filename) {
            //create our return object
            JSONObject jsonObject = null;

            try {

                connectToFTP();

                /* check if logged in correctly */
                if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {

                    /* We should be connected to the FTP server now, set the transfer settings. */
                    if (setTransferSettings()) {
                        boolean registered = false;
                        FTPFile[] userFiles = ftpClient.listFiles();
                        for (FTPFile userFile : userFiles) {
                            if (userName.contentEquals(userFile.getName())) {
                                registered = true;
                            }
                        }

                        if (!registered) {
                            return null;
                        }


                        ftpClient.changeWorkingDirectory(userName);
                        BufferedReader dataReader = new BufferedReader(new InputStreamReader(ftpClient.retrieveFileStream(filename)));

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

                disconnectFromFTP();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                String error = "{\"error\":\"Failed to connect, please restart the application.\"}";
                try {
                    jsonObject = new JSONObject(error);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            return jsonObject;
        }

        private void changeWorkDirectory() throws IOException{
            boolean isCreated = false;
            FTPFile[] files = ftpClient.listFiles();
            for(FTPFile file : files) {
                if(file.getName().contentEquals(userName)) {
                    isCreated = true;
                    break;
                }
            }

            if(isCreated) {
                ftpClient.changeWorkingDirectory(userName);
            }else {
                ftpClient.makeDirectory(userName);
                ftpClient.changeWorkingDirectory(userName);
            }
        }

    }
}
