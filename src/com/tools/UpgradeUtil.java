package com.tools;

import com.lease.R;
import com.net.Config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//http://fengzhizi715.iteye.com/blog/797782
//http://www.jb51.net/article/35813.htm

public class UpgradeUtil{
    // 应用程序Context
    private static Context mContext;
    // 提示消息
    private static String updateMsg = "有最新版本的App，请下载更新！";
    // 下载安装包的网络路径
    //private String apkUrl = Public.homeUrl + "apk/sil.apk";
    private static String apkUrl = Config.apkUrl + "unislease.apk";
    private static Dialog noticeDialog;// 提示有软件更新的对话框
    private static Dialog downloadDialog;// 下载对话框
    private static final String savePath = "/Unis";// 保存apk的文件夹
    private static final String saveFileName =  "/unislease.apk";
    // 进度条与通知UI刷新的handler和msg常量
    private static ProgressBar mProgress;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static int progress;// 当前进度
    private static Thread downLoadThread; // 下载线程
    private static boolean interceptFlag = false;// 用户取消下载
    
    private static TextView tvUpdPercent;
    
    // 通知处理刷新界面的handler
    private static Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    // 显示更新程序对话框，供主程序调用
    public static int checkVersion(Context contex) {
        //0-打开下载对话框； 1-已是最新版本；2-无法得到是否有最新的版本
    	mContext = contex; 
    	showNoticeDialog(contex);
    	    	
        return 0;
    }
    
    private static void showNoticeDialog(Context context) {
        android.app.AlertDialog dlgUpgrade = new android.app.AlertDialog.Builder(context).create();
        dlgUpgrade.setTitle("软件版本更新");
        dlgUpgrade.setMessage(updateMsg);
        //dlgUpgrade.setIcon(R.drawable.refresh);

        dlgUpgrade.setButton(DialogInterface.BUTTON_POSITIVE, "下载更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDownloadDialog();
            }
        });
        dlgUpgrade.show();

        Button btnPositive =
                dlgUpgrade.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        //btnPositive.setBackgroundColor(ContextCompat.getColor(Public.curMainHandler, R.color.lightblue));
        //btnPositive.setTextColor(ContextCompat.getColor(Public.curMainHandler, R.color.lightblue));
        btnPositive.setTextSize(16);

/*
        //两个button, 下载和以后再说
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                mContext);// Builder，可以通过此builder设置改变AleartDialog的默认的主题样式及属性相关信息
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setIcon(R.drawable.refresh);
        builder.setPositiveButton("下 载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();// 当取消对话框后进行操作一定的代码？取消对话框
                showDownloadDialog();
            }
        });

        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        noticeDialog = builder.create();
        noticeDialog.show();
*/

    }
    
    protected static void showDownloadDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        //builder.setTitle("版本更新中...");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.upgrade_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.updprogress);
        
        tvUpdPercent = (TextView)v.findViewById(R.id.txtUpdPercent);

        builder.setView(v);// 设置对话框的内容为一个View
        
        
        /*
        //增加强制更新功能，不能取消   2016-08-28
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        */
        downloadDialog = builder.create();
       
        downloadDialog.show();
        WindowManager manager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        Display d = manager.getDefaultDisplay();  //为获取屏幕宽、高     
        android.view.WindowManager.LayoutParams p = downloadDialog.getWindow().getAttributes();  //获取对话框当前的参数值     
        //p.height = (int) (d.getHeight() * 0.3);   //高度设置为屏幕的0.3   
        p.width = (int) (d.getWidth() * 0.9);    //宽度设置为屏幕的0.5       
        downloadDialog.getWindow().setAttributes(p);     //设置生效 
        
        downloadApk();
    }
    private static void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }
    protected static void installApk() {
        File apkfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + savePath + saveFileName);
        if (!apkfile.exists()) {
            return;
        }

        /*
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");// File.toString()会返回路径信息
        mContext.startActivity(intent);
        */

        String cmd = "chmod 777" +
                Environment.getExternalStorageDirectory().getAbsolutePath() + savePath + saveFileName;
        try{
            Runtime.getRuntime().exec(cmd);
        }catch(Exception e){
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");// File.toString()会返回路径信息
        mContext.startActivity(intent);

        uptVersion();
    }

    private static void uptVersion()
    {
    	SaveUserInfo.setFirstOpenFlag(true);
    }

    private static Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            URL url = null;
            try {
                url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                int length = conn.getContentLength();

                String local_file = Environment.getExternalStorageDirectory().getAbsolutePath()+savePath;
                File filePath = new File(local_file);
                if(!filePath.exists()){
                    filePath.mkdirs();
                }

                File fileOut = new File(filePath.getAbsolutePath()+saveFileName);

                if (!fileOut.exists()) {
                         if(!fileOut.createNewFile()) {

                         }
                    }

                FileOutputStream outStream = new FileOutputStream(fileOut);
                /*
                byte[] bytes = new byte[1024];
                int c;
                while ((c = in.read(bytes)) != -1) {
                    out.write(bytes, 0, c);
                }
                in.close();
                out.close();
                */
                int count = 0;
                int updPercent = 0;

                byte buf[] = new byte[1024];
                do {
                    int numread = in.read(buf);
                    count += numread;

                    updPercent = (int) (((float) count / length) * 100);
                    progress = updPercent;


                    // 下载进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    outStream.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消停止下载
                outStream.close();
                in.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}