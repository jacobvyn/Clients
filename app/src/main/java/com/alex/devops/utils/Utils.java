package com.alex.devops.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.alex.devops.db.Client;
import com.example.test.app.jacob.mygalleryapp.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static final String LOG_TAG = Utils.class.getSimpleName();
    private static final String IMG_FILE_PREFIX = "IMG_";
    private static final String IMG_FILE_SUFFIX = ".jpg";
    private static final int FULL_QUALITY = 100;


    public static String createTempImageFile(Context context) {
        if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            File tempFile = createTempFile(createBaseDir(context));
            return tempFile == null ? null : tempFile.getAbsolutePath();
        } else {
            Toast.makeText(context, R.string.storage_unavailible, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static String createImageFilePath(Context context) {
        if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            File tempFile = new File(createBaseDir(context), generateName());
            createFile(tempFile);
            return tempFile.getAbsolutePath();
        } else {
            Toast.makeText(context, R.string.storage_unavailible, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static File createImageFile(Context context) {
        if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            File tempFile = new File(createBaseDir(context), generateName());
            createFile(tempFile);
            return tempFile;
        } else {
            Toast.makeText(context, R.string.storage_unavailible, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private static File createTempFile(File baseDir) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile(generateName(), null, baseDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    private static String generateName() {
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(new Date());
        return IMG_FILE_PREFIX + timeStamp + IMG_FILE_SUFFIX;
    }

    private static void createFile(File tempFile) {
        if (tempFile != null && !tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    private static File createBaseDir(Context context) {
        File baseDir = context == null ? null : context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        createDir(baseDir);
        return baseDir;
    }

    private static void createDir(File dir) {
        if (dir != null && !dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (mkdirs) {
                Log.e(LOG_TAG, "base dir is created");
            } else {
                Log.e(LOG_TAG, "base dir did not create");
            }
        }
    }

    public static boolean isResolved(Intent intent, Context activity) {
        return intent.resolveActivity(activity.getPackageManager()) != null;
    }

    public static byte[] getBlobFromFile(String fileName) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(fileName, bmOptions);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        return outputStream.toByteArray();
    }

    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getWindow().getDecorView().getRootView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static Bitmap getBitMap(byte[] blobPhoto) {
        return BitmapFactory.decodeByteArray(blobPhoto, 0, blobPhoto.length);
    }

    public static void writeToFileAsync(final String file, final byte[] bytes) {
        ExecutorHelper.submit(new Runnable() {
            @Override
            public void run() {
                writeToFileSyncNewVer(file, bytes);
            }
        });
    }

    private static void writeToFileSyncNewVer(String file, byte[] bytes) {
        if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                outputStream.write(bytes);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(LOG_TAG, "Couldn't write log. Read-only file system.");
        }
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, FULL_QUALITY, outputStream);
            return outputStream.toByteArray();
        } finally {
            closeQuietly(outputStream);
        }
    }

    private static void closeQuietly(ByteArrayOutputStream outputStream) {
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setImageBitmapAsync(final Client client, final ImageView view) {
        ExecutorHelper.submit(new Runnable() {
            @Override
            public void run() {
                if (view != null) {
                    String path = client.getPhotoPath();
                    Bitmap bitMap = getBitMap(path, view.getWidth(), view.getHeight());
                    view.setImageBitmap(bitMap);
                }
            }
        });
    }

    private static Bitmap getBitMap(String photoPath, int targetW, int targetH) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }
}
