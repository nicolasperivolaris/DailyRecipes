package com.example.dailyrecipes.utils;
import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.dailyrecipes.MainActivity;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FTPManager extends ViewModel {
    public boolean connected;
    private String repository = "/";
    private FTPClient con;

    public FTPManager(){con = new FTPClient();}

    public void connect(String hostname, String user, String pwd){
        Thread t = new Thread(() -> {
            try {
                con.connect(hostname, 2121);
                connected = con.login(user, pwd);
                con.enterLocalPassiveMode();
                con.setFileType(FTP.BINARY_FILE_TYPE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    public void disconnect() throws IOException {
        connected = false;
        con.logout();
        con.disconnect();
    }

    public Uri getFile(String name){
        File file = new File(MainActivity.instance.getCacheDir(), name);
        try
        {
           if (con.isConnected())
            {
                OutputStream out = new FileOutputStream(file);
                boolean result = con.retrieveFile(repository+name, out);
                out.close();
                if (result) Log.i("download result", "succeeded");
                else return null;
            }
        }
        catch (Exception e)
        {
            Log.i("download result","failed");
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }

    public void setFile(Uri uri){
        Thread t = new Thread(() -> {
            try
            {
                if (connected)
                {
                    con.enterLocalPassiveMode();
                    con.setFileType(FTP.BINARY_FILE_TYPE);
                    File file = new File(uri.getPath());
                    InputStream in = MainActivity.instance.getContentResolver().openInputStream(uri);
                    boolean result = con.storeFile(file.getName(), in);
                    in.close();
                    if (result) Log.v("upload result", "succeeded");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
        t.start();
    }
}
