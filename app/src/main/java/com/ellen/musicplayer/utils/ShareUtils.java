package com.ellen.musicplayer.utils;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShareUtils {

    private static final String SHARE_TYPE_IMAGE = "image/*";
    private static final String SHARE_TYPE_VIDEO = "video/*";

    /**
     * 7.0及其以上使用此方法进行分享
     * @param context
     * @param shareType
     * @param files
     */
    private static void originalShare(Context context, String shareType, List<File> files) {
        Intent share_intent = new Intent();
        ArrayList<Uri> imageUris = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (File f : files) {
                if(shareType.equals(SHARE_TYPE_IMAGE)) {
                    Uri imageContentUri = getImageContentUri(context, f);
                    imageUris.add(imageContentUri);
                }else {
                    Uri videoContentUri = getVideoContentUri(context, f);
                    imageUris.add(videoContentUri);
                }
            }
        } else {
            for (File f : files) {
                imageUris.add(Uri.fromFile(f));
            }
        }
        share_intent.setAction(Intent.ACTION_SEND_MULTIPLE);//设置分享行为
        share_intent.setType(shareType);//设置分享内容的类型
        share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        share_intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        if(shareType.equals(SHARE_TYPE_IMAGE)) {
            context.startActivity(Intent.createChooser(share_intent, "Share Image"));
        }else {
            context.startActivity(Intent.createChooser(share_intent, "Share Video"));
        }
    }

    /**
     * 6.0及其以下使用此方法分享
     * @param context
     * @param files
     */
    private static void share(Context context, String shareType, List<File> files){
        Intent share_intent = new Intent();
        ArrayList<Uri> imageUris = new ArrayList<>();
        for (File f : files) {
            imageUris.add(Uri.fromFile(f));
        }
        share_intent.setAction(Intent.ACTION_SEND_MULTIPLE);//设置分享行为
        share_intent.setType(shareType);//设置分享内容的类型
        share_intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        if(shareType.equals(SHARE_TYPE_IMAGE)) {
            context.startActivity(Intent.createChooser(share_intent, "Share Image"));
        }else {
            context.startActivity(Intent.createChooser(share_intent, "Share Video"));
        }
    }

    /**
     * 分享单张图片
     */
    public static void shareSingleImage(Context context, String imagePath){
        File file = new File(imagePath);
        List<File> fileList = new ArrayList<>();
        fileList.add(file);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            originalShare(context, SHARE_TYPE_IMAGE, fileList);
        }else {
            share(context,SHARE_TYPE_IMAGE,fileList);
        }
    }

    /**
     * 分享多张图片
     */
    public static void shareMultipleImage(Context context, List<String> imagePathList){
        List<File> fileList = new ArrayList<>();
        for(String imagePath:imagePathList){
            File file = new File(imagePath);
            fileList.add(file);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            originalShare(context, SHARE_TYPE_IMAGE, fileList);
        }else {
            //6.0以下调用此方法进行分享
            share(context,SHARE_TYPE_IMAGE,fileList);
        }
    }

    /**
     * 分享视频
     * @param context
     * @param videoPath
     */
    public static void shareVideo(Context context, String videoPath){
        File file = new File(videoPath);
        List<File> fileList = new ArrayList<>();
        fileList.add(file);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            originalShare(context, SHARE_TYPE_VIDEO, fileList);
        }else {
            share(context,SHARE_TYPE_VIDEO,fileList);
        }
    }

    /**
     * 分享文本
     * @param context
     * @param msgTitle
     * @param msgText
     */
    public static void shareText(Context context, String msgTitle, String msgText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void shareMusic(Context context,String musicPath){

    }

    /**
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */
    private static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * Gets the content:// URI from the given corresponding path to a file
     *
     * @param context
     * @param videoFile
     * @return content Uri
     */
    public static Uri getVideoContentUri(Context context, File videoFile) {
        String filePath = videoFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Video.Media._ID }, MediaStore.Video.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/video/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (videoFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Video.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}
