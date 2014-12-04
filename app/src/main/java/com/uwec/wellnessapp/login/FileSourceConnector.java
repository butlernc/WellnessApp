package com.uwec.wellnessapp.login;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

/**
 * Created by butlernc on 12/3/2014.
 */
public class FileSourceConnector extends AsyncTask<String, String, String>{

    /** link to FTP site variables */
    private FTPClient ftpClient;

    private static final String FTP_HOSTNAME = "wellnessappftp.eu.pn";
    private static final String FTP_USERNAME = "1769350_uwecwellnessapp";
    private static final String FTP_PASSWORD = "android4us";
    private static final String FTP_WORKING_DIR = "/userdata/";

    private String currentDirectory;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("FILESOURCE", "creating file source connector object");
        ftpClient = new FTPClient();
    }

    @Override
    protected String doInBackground(String... strings) {
        //establish connection to the FTP server

        if(strings[2] == "get") {
            try {
                ftpClient.connect(FTP_HOSTNAME);
                Log.d("CONNECT_FTP", "Successfully connected to the server");
                ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
                ftpClient.changeDirectory(FTP_WORKING_DIR);

                ftpClient.disconnect(true);

            } catch (FTPException e) {
                e.printStackTrace();
                Log.d("CONNECT_FTP", "CONNECTION ERROR: Could not connect to server");
            } catch (FTPIllegalReplyException e) {
                e.printStackTrace();
                Log.d("CONNECT_FTP", "Server not following FTP protocol");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(strings[2] == "create") {
            try {
                ftpClient.connect(FTP_HOSTNAME);
                Log.d("CONNECT_FTP", "Successfully connected to the server");
                ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
                ftpClient.changeDirectory(FTP_WORKING_DIR);
                Log.d("CONNECT_FTP", "Our current FTP directory: " + ftpClient.currentDirectory());

                Log.d("CONNECT_FTP", "First time, create directory");
                Log.d("CONNECT_FTP", "Our current FTP directory: " + ftpClient.currentDirectory());
                ftpClient.createDirectory(strings[0]);

                ftpClient.disconnect(true);

            } catch (FTPException e) {
                e.printStackTrace();
                Log.d("CONNECT_FTP", "CONNECTION ERROR: Could not connect to server");
            } catch (FTPIllegalReplyException e) {
                e.printStackTrace();
                Log.d("CONNECT_FTP", "Server not following FTP protocol");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
