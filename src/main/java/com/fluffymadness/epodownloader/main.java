package com.fluffymadness.epodownloader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;

import com.stericson.RootTools.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;


public class main extends Activity {

    TextView errorOrSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        this.errorOrSuccess = (TextView) findViewById(R.id.error_or_success);
        try {
            Process process = Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void downloadEpo(View view) {
        if (RootTools.isRootAvailable()) {
            if (RootTools.isBusyboxAvailable()) {
                try {
                    Process process = Runtime.getRuntime().exec(new String[] { "su", "-c", "busybox wget http://epodownload.mediatek.com/EPO.DAT -O /data/misc/EPO.DAT && busybox chmod 777 /data/misc/EPO.DAT"});
                    process.waitFor();
                    Process process2 = Runtime.getRuntime().exec(new String[] { "su", "-c", "busybox wget http://epodownload.mediatek.com/EPO.MD5 -O /data/misc/EPO.MD5 && busybox chmod 777 /data/misc/EPO.MD5"});
                    process2.waitFor();
                    File file = new File("/data/misc/EPO.DAT");
                    Date lastModDate = new Date(file.lastModified());
                    File file2 = new File("/data/misc/EPO.MD5");
                    Date lastModDate2 = new Date(file2.lastModified());
                    errorOrSuccess.setText("EPO Files Update-Time:\n\n"+"EPO.DAT: "+lastModDate.toString()+"\n"+"EPO.MD5: "+lastModDate2.toString());
                } catch (IOException e) {
                    errorOrSuccess.setText("Something went wrong: Maybe Mediatek Servers are down, or something different");
                } catch (InterruptedException e) {
                    errorOrSuccess.setText("Something went wrong: Maybe Mediatek Servers are down, or something different");
                }
            } else {
                errorOrSuccess.setText("Busybox is Missing");
                RootTools.offerBusyBox(this);
            }
        } else {
            this.errorOrSuccess.setText("Error, no root permissions");
        }

    }
}
