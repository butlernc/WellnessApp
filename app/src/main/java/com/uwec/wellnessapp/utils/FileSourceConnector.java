package com.uwec.wellnessapp.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.uwec.wellnessapp.data.UserData;
import com.uwec.wellnessapp.login.LoginHelper;

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

/**
 * Created by butlernc on 12/3/2014.
 */
public class FileSourceConnector extends AsyncTask<String, String, String> {

    /** link to FTP site variables */
    private FTPClient ftpClient;

    private static final String FTP_HOSTNAME = "f13-preview.freehostingeu.com";
    private static final String FTP_USERNAME = "1769350_uwecwellnessapp";
    private static final String FTP_ACCOUNT = "1769350";
    private static final String FTP_PASSWORD = "android4us";
    private static final String FTP_PATH = "wellnessappftp.eu.pn";

    private static final String PASSWORD_FILE_NAME = "password.txt";
    private static final String USER_DATA_FILE_NAME = "userfile.txt";

    private String userDirectory;

    private JsonFileConverter jsonFileConverter;
    public static UserData userData;
    private static Context currentContext;

    private boolean isDone;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("FILESOURCE", "creating file source connector object");
        ftpClient = new FTPClient();
        jsonFileConverter = new JsonFileConverter();
        isDone = false;
    }

    @Override
    protected String doInBackground(String... strings) {
        //establish connection to the FTP server

        if(strings[2] == "read") {
                boolean status = false;
                ftpClient.setConnectTimeout(10 * 1000);
            try {
                ftpClient.connect(InetAddress.getByName(FTP_HOSTNAME));
                status = ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
                Log.e("isFTPConnected", String.valueOf(status));

                if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    ftpClient.enterLocalPassiveMode();
                    ftpClient.changeWorkingDirectory(FTP_PATH);

                    FTPFile[] mFileArray = ftpClient.listFiles();
                    //check for user directory
                    for(FTPFile file : mFileArray) {
                        Log.e("FILES", "" + file.getName());
                        if(file.getName().contentEquals(strings[0])) {
                            userDirectory = file.toString();
                            Log.d("FTPFILE", "" + userDirectory);
                            ftpClient.changeWorkingDirectory(userDirectory);
                            BufferedReader passwordReader = new BufferedReader(new InputStreamReader(ftpClient.retrieveFileStream(PASSWORD_FILE_NAME)));
                            String expected = passwordReader.readLine();
                            Log.d("EXPECTED", expected);

                            if(expected.contentEquals(strings[1])) {
                                LoginHelper.setLogged(true);
                                BufferedReader userDataReader = new BufferedReader(new InputStreamReader(ftpClient.retrieveFileStream(USER_DATA_FILE_NAME)));

                                StringBuilder sb = new StringBuilder();
                                String results;
                                String line;
                                while((line = userDataReader.readLine()) != null) {
                                    sb.append(line);
                                }

                                results = sb.toString();
                                JSONObject jsonObject = new JSONObject(results);
                                FileSourceConnector.userData = jsonFileConverter.convert(jsonObject);

                                Toast.makeText(currentContext, "Welcome " + FileSourceConnector.userData.getFirst_name(), Toast.LENGTH_SHORT).show();

                                //got our data, close everything
                                userDataReader.close();
                                passwordReader.close();
                                ftpClient.disconnect();
                                isDone(true);
                            }else{
                                LoginHelper.setLogged(false);
                            }
                        }
                    }
                    Log.d("DIR", "Directory: " + ftpClient.printWorkingDirectory());
                    Log.e("Size", String.valueOf(mFileArray.length));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if(strings[2] == "write") {
            boolean status = false;
            userDirectory = strings[0];
            ftpClient.setConnectTimeout(10 * 1000);
            try {
                ftpClient.connect(InetAddress.getByName(FTP_HOSTNAME));
                status = ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
                Log.e("isFTPConnected", String.valueOf(status));

                if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    ftpClient.enterLocalPassiveMode();
                    test(ftpClient);

                   // if (strings[3] == "new") {

                    test(ftpClient);
                    boolean worked = ftpClient.changeWorkingDirectory(FTP_PATH);
                    ftpClient.makeDirectory(userDirectory);
                    Log.e("DIR", "Directory changed: " + String.valueOf(worked));
                    ftpClient.changeWorkingDirectory(userDirectory);
                    Log.d("DIR", "Directory: " + userDirectory);
                    test(ftpClient);
                    //make password file
                    JSONObject jsonPassword = new JSONObject();
                    jsonPassword.put("password", strings[1]);
                    FileOutputStream passwordFile = currentContext.openFileOutput(PASSWORD_FILE_NAME, Context.MODE_PRIVATE);
                    passwordFile.write(jsonPassword.toString().getBytes());
                    passwordFile.close();

                    //store on server
                    File passwordTransferFile = new File(currentContext.getFilesDir(), PASSWORD_FILE_NAME);
                    Log.d("FILE", "" + passwordFile.toString());
                    Log.d("DIR", ftpClient.printWorkingDirectory());
                    boolean saved = ftpClient.storeFile(PASSWORD_FILE_NAME, new FileInputStream(passwordTransferFile));

                    Log.d("SAVED", String.valueOf(saved));
                    //make userData file
                    JSONObject jsonUserData = jsonFileConverter.convert(FileSourceConnector.userData);

                    FileOutputStream userDataFile = currentContext.openFileOutput(USER_DATA_FILE_NAME, Context.MODE_PRIVATE);
                    userDataFile.write(jsonUserData.toString().getBytes());
                    userDataFile.close();
                    //store on server
                    File userDataTransferFile = new File(currentContext.getFilesDir(), USER_DATA_FILE_NAME);
                    FileInputStream fileInputStream = new FileInputStream(userDataTransferFile);
                    ftpClient.storeFile(USER_DATA_FILE_NAME, fileInputStream);

                    //close connection
                    fileInputStream.close();
                    isDone(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void setContext(Context context) {
        currentContext = context;
    }

    public boolean isDone() {
        return this.isDone;
    }

    private void isDone(boolean isDone) {
        this.isDone = isDone;
    }

    public void test(FTPClient ftpClient) throws IOException {
        FTPFile[] files = ftpClient.listFiles();

        for(FTPFile file : files) {
            Log.e("FILES", file.getName());
        }
    }
}
