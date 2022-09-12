package com.example.tcpsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.PrecomputedText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    EditText ip,msg;
    Button send;
    ProgressBar pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = findViewById(R.id.ip);
        msg = findViewById(R.id.msg);

        send = findViewById(R.id.button);
        pr = findViewById(R.id.progressBar);

        Thread thrd = new Thread(new MyServer());
        thrd.start();


    }

    public void click(View view) {
        Background b = new Background();
        b.execute(ip.getText().toString(),msg.getText().toString());
    }

    class MyServer implements Runnable{
        Socket ss;
        DataInputStream ios;
        ServerSocket ssr;
        String message;
        Handler handler;

        @Override
        public void run() {
            try {
                ssr = new ServerSocket(9700);

                handler.post(() -> Toast.makeText(getApplicationContext(),"waiting", Toast.LENGTH_SHORT).show());
                while(true){
                    pr.setVisibility(View.GONE);
                    ss = ssr.accept();
                    ios = new DataInputStream(ss.getInputStream());
                    message = ios.readUTF();
                    handler.post(() -> Toast.makeText(getApplicationContext(), "success",Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    class Background extends AsyncTask<String,Void,String> {
        Socket s;
        DataOutputStream dos;
        String ip,message;

        @Override
        protected String doInBackground(String... params) {
            ip= params[0];
            message = params[1];
            try{
                s = new Socket(ip,9700);
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(message);
                dos.close();
                s.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return  null;

        }
    }
}