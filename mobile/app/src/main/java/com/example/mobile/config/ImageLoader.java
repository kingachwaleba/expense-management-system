package com.example.mobile.config;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.mobile.R;
import com.example.mobile.model.User;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class ImageLoader {
    OkHttpClient okHttpClient;
    String accessToken;
    String BASE_URL = "http://192.168.0.31:8080/";
    String url;
    Context context;

    public interface OnPicassoCallback{
        void onPicasso(Picasso picasso);
    }

    public ImageLoader(Context context, String accessToken) {
        this.context = context;
        this.accessToken = accessToken;
        this.okHttpClient = new OkHttpClient.Builder()
                .authenticator((route, response) -> response.request().newBuilder()
                        .header("Authorization", "Bearer " + accessToken)
                        .build()).build();
    }

    public void downloadImage(String path){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + url).newBuilder();
        urlBuilder.addQueryParameter("imageName", path);

        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
        picasso.load(String.valueOf(urlBuilder)).rotate(-90).into(getTarget(getFileName("ImageName")));
    }


    public static String getFileName(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + fileName);
        return uriSting;}



    //target to save
    private static Target getTarget(final String fileName) {
        Target target = new Target() {
            //This method in target is called by picasso when image downloaded
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            File file = new File(getFileName(fileName));
                            if (file.exists()) {
                                file.delete();
                            }
                            file.createNewFile();
                            FileOutputStream fileoutputstream = new FileOutputStream(file);
                            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 60, bytearrayoutputstream);
                            fileoutputstream.write(bytearrayoutputstream.toByteArray());
                            fileoutputstream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    public static String getFileFullPath(String fileName) {
        try {

            if (fileName != null && !fileName.isEmpty()) {
                String base = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
                return "file://" + base + "/Images/" + fileName;
            } else return "";
        } catch (Exception e) {
            return "";
        }
    }
}
