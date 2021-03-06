package com.ultramega.shop.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.util.CustomTypefaceSpan;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonFunctions {
    public static boolean hasNewNotification = false;
    public static boolean hasNewOrderUpdate = false;
    public static boolean hasNewWalletUpdate = false;
    public static boolean isProfPicUpdated = false;

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int NO_ACTIVE_NETWORK = 0;

    public static String placesAPIKEY = "AIzaSyBiwOag8ni-KJqxg9_JPFIYVStqTRFl-Ig";

    public static String capitalizeWord(String word) {

        if (word != null) {

            if (word.length() > 0) {

                StringBuilder builder = new StringBuilder();

                try {

                    String[] cap_word_arr = word.toLowerCase().split(" ");

                    for (String s : cap_word_arr) {
                        String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                        builder.append(cap + " ");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return builder.toString();

            } else {

                return null;

            }

        } else {

            return null;

        }

    }

    public static BitmapImageViewTarget getRoundedImageTarget(@NonNull final Context context, @NonNull final ImageView imageView,
                                                              final float radius) {
        return new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(final Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCornerRadius(radius);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        };
    }

    public static int getConnectivityStatus(Context context) {

        int NETWORK = 0;

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.d("activeNetwork", "wifi");
                return TYPE_WIFI;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                Log.d("activeNetwork", "mobile");
                return TYPE_MOBILE;
            } else {
                Log.d("activeNetwork", "no active");
                return NO_ACTIVE_NETWORK;
            }
        } else {
            Log.d("activeNetwork", "bull");
            return NO_ACTIVE_NETWORK;
        }
    }

    public static SpannableString setTypeFace(Context context, String fontName, String title) {
        if (title != null) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), fontName);
            SpannableString mNewTitle = new SpannableString(title);
            mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return mNewTitle;
        } else {
            return null;
        }
    }

    public static String currencyFormatter(String number) {
        if (number != null) {
            String pattern = "#,###,##0.00";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);

            return decimalFormat.format(Double.parseDouble(number));
        } else {
            return "";
        }
    }

    public static String commaFormatter(String number) {
        if (number != null) {
            String pattern = "#,###,##0";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);

            return decimalFormat.format(Double.parseDouble(number));
        } else {
            return "";
        }
    }

    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context) {
        String deviceId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    deviceId = mTelephony.getImei();
                }else {
                    deviceId = mTelephony.getDeviceId();
                }
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        Log.d("deviceId", deviceId);
        return deviceId;
    }

    public static String getSha1Hex(String clearString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(clearString.getBytes());
            byte[] bytes = messageDigest.digest();
            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes) {
                buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return buffer.toString();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

    public static String parseXML(String data, String nametag) {
        String result = "";
        int startpoint;
        int endpoint;

        //getting the starting point
        startpoint = data.indexOf("<" + nametag + ">");
        //getting the endpoint
        endpoint = data.indexOf("</" + nametag + ">");
        if (startpoint == -1 || endpoint == -1) {
            //return empty
            result = "NONE";
        } else {
            int starttaglen = nametag.length() + 2;
            //returning the extracted data
            result = data.substring(startpoint + starttaglen, endpoint);
        }

        return result;
    }

    public static String convertDate(String strDate) {
        String dateString = "";

        if (strDate != null) {

            try {
                SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                formatInput.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat formatOutput = new SimpleDateFormat("dd/MM/yyyy");

                Date date = formatInput.parse(strDate);
                dateString = formatOutput.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return dateString;
    }

    public static String parseDate(String strDate) {
        String dateString = "";
        try {
            SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            formatInput.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat formatOutput = new SimpleDateFormat("MM-dd-yyyy");

            Date date = formatInput.parse(strDate);
            dateString = formatOutput.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String parseTime(String str)
    {
        String time = "No Time Available.";
        StringBuilder strTime = new StringBuilder("");
        if(str != null){
            // Get Hours
            int h1 = (int)str.charAt(0) - '0';
            int h2 = (int)str.charAt(1)- '0';

            int hh = h1 * 10 + h2;

            // Finding out the Meridien of time
            // ie. AM or PM
            String Meridien;
            if (hh < 12) {
                Meridien = "AM";
            }
            else
                Meridien = "PM";

            hh %= 12;

            // Handle 00 and 12 case separately
            if (hh == 0) {
                // Printing minutes and seconds
                for (int i = 2; i < 5; ++i) {
                    strTime.append(str.charAt(i));
                }
                time = "12" + ""+strTime+" "+Meridien;
            }
            else {
                // Printing minutes and seconds
                for (int i = 2; i < 5; ++i) {
                    strTime.append(str.charAt(i));
                }
                time = hh + "" +strTime+" "+Meridien;
            }

        }
        return time;


    }

    public static String parseOrderDate(String strDate) {
        String dateString = "";
        try {
            SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            formatInput.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat formatOutput = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");

            Date date = formatInput.parse(strDate);
            dateString = formatOutput.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String getMonthNumber(String strDate) {
        String mon = "";
        try {
            Calendar calender = Calendar.getInstance();
            SimpleDateFormat formatInput = new SimpleDateFormat("MMMM");
            formatInput.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = formatInput.parse(strDate);
            calender.setTimeInMillis(date.getTime());
            if ((calender.get(Calendar.MONTH) + 1) < 10)
                mon = "0" + String.valueOf(calender.get(Calendar.MONTH) + 1);
            else
                mon = String.valueOf(calender.get(Calendar.MONTH) + 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mon;
    }

    public static String getYear(String strDate) {
        String dateString = "";
        try {
            SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            formatInput.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat formatOutput = new SimpleDateFormat("yyyy");

            Date date = formatInput.parse(strDate);
            dateString = formatOutput.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String replaceEscapeData(String xstring) {
        String result = xstring;

        if (xstring != null && !xstring.equals("")) {
            String tempstr = result;
            tempstr = tempstr.replaceAll("&AMP;", "&");
            tempstr = tempstr.replaceAll("&amp;", "&");
            tempstr = tempstr.replaceAll("&#39;", "'");
            tempstr = tempstr.replaceAll("#39;", "'");
            tempstr = tempstr.replaceAll("&#47;", "/");
            tempstr = tempstr.replaceAll("#47;", "/");
            tempstr = tempstr.replaceAll("&#GT;", ">");
            tempstr = tempstr.replaceAll("&#gt;", ">");
            tempstr = tempstr.replaceAll("&#LT;", "<");
            tempstr = tempstr.replaceAll("&#lt;", "<");
            tempstr = tempstr.replaceAll("&#40", "(");
            tempstr = tempstr.replaceAll("&#41;", ")");
            tempstr = tempstr.replaceAll("&#44;", ",");


            result = tempstr;
        }

        return result;
    }

    public static String convertDateWalletReload(String strDate) {
        String dateString = "";
        try {
            SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            formatInput.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat formatOutput = new SimpleDateFormat("dd MMM\nhh:mm a");

            Date date = formatInput.parse(strDate);
            dateString = formatOutput.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String convertDateWalletReloadPayment(String strDate) {
        String dateString = "";
        try {
            SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            formatInput.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat formatOutput = new SimpleDateFormat("dd MMMM yyyy hh:mm a");

            Date date = formatInput.parse(strDate);
            dateString = formatOutput.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String getYearFromDate(String dateval) {
        String year = "";
        try {
            Calendar calender = Calendar.getInstance();
            SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            formatInput.setTimeZone(TimeZone.getTimeZone("UTC"));
            // SimpleDateFormat formatOutput = new SimpleDateFormat("MMM. dd, yyyy hh:mm aaa");

            Date date = formatInput.parse(dateval);
            calender.setTimeInMillis(date.getTime());
            year = String.valueOf(calender.get(Calendar.YEAR));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return year;
    }

    public static String getMonthFromDate(String dateval) {
        String mon = "";
        try {
            Calendar calender = Calendar.getInstance();
            SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            formatInput.setTimeZone(TimeZone.getTimeZone("UTC"));
            // SimpleDateFormat formatOutput = new SimpleDateFormat("MMM. dd, yyyy hh:mm aaa");

            Date date = formatInput.parse(dateval);
            calender.setTimeInMillis(date.getTime());
            if ((calender.get(Calendar.MONTH) + 1) < 10)
                mon = "0" + String.valueOf(calender.get(Calendar.MONTH) + 1);
            else
                mon = String.valueOf(calender.get(Calendar.MONTH) + 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mon;
    }

    public static String convertDateNotification(String strDate) {
        String dateString = "";
        try {
            SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            formatInput.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat formatOutput = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

            Date date = formatInput.parse(strDate);
            dateString = formatOutput.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String convertDateNotificationDetails(String strDate) {
        String dateString = "";
        try {
            SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            formatInput.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat formatOutput = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

            Date date = formatInput.parse(strDate);
            dateString = formatOutput.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String getCurrentDateTimeString() {
        Calendar cal = Calendar.getInstance();
        Date curDate = cal.getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDateString = formatter.format(curDate);

        return formattedDateString;
    }

    public static String getDefaultDate() {
        Calendar cal = Calendar.getInstance();
        Date curDate = cal.getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDateString = formatter.format(curDate);

        return formattedDateString;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static String getFileNameFromURL(final String url) {
        if (url == null) {
            return "";
        }

        String cleanFile = "";

        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                cleanFile = "";
            }
        } catch (MalformedURLException e) {
            cleanFile = "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);

        final String fileName = Normalizer.normalize(url.substring(startIndex, endIndex), Normalizer.Form.NFD).replaceAll("[^.a-zA-Z0-9_-]", "");

        final String filePath = url.substring(0, startIndex);

        cleanFile = filePath.concat(fileName);

        return cleanFile;
    }

//    public static class CleanFileAsyncTask extends AsyncTask<String, String, String> {
//
//        private String cleanFile = "";
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String url = "";
//
//            if (url == null) {
//                return "";
//            }
//
//            url = strings[0];
//
//            try {
//                URL resource = new URL(url);
//                String host = resource.getHost();
//                if (host.length() > 0 && url.endsWith(host)) {
//                    // handle ...example.com
//                    cleanFile = "";
//                }
//            } catch (MalformedURLException e) {
//                cleanFile = "";
//            }
//
//            int startIndex = url.lastIndexOf('/') + 1;
//            int length = url.length();
//
//            // find end index for ?
//            int lastQMPos = url.lastIndexOf('?');
//            if (lastQMPos == -1) {
//                lastQMPos = length;
//            }
//
//            // find end index for #
//            int lastHashPos = url.lastIndexOf('#');
//            if (lastHashPos == -1) {
//                lastHashPos = length;
//            }
//
//            // calculate the end index
//            int endIndex = Math.min(lastQMPos, lastHashPos);
//
//            final String fileName = Normalizer.normalize(url.substring(startIndex, endIndex), Normalizer.Form.NFD).replaceAll("[^.a-zA-Z0-9_-]", "");
//
//            final String filePath = url.substring(0, startIndex);
//
//            cleanFile = filePath.concat(fileName);
//
//            return cleanFile;
//        }
//
//        @Override
//        protected void onPostExecute(String cleanFile) {
//            setValue(cleanFile);
//            Log.d("antonhttp", "SET CLEAN FILE: " + cleanFile);
//        }
//
//        private void setValue(String cleanFile) {
//            this.cleanFile = cleanFile;
//        }
//
//        public String getValue() {
//
//            Log.d("antonhttp", "CLEAN FILE: " + cleanFile);
//
//            return this.cleanFile;
//        }
//    }
//
//    public static String getFileNameFromURL(String url) {
//
//        CleanFileAsyncTask cleanTask = new CleanFileAsyncTask();
//        cleanTask.execute(url);
//
//        return cleanTask.getValue();
//    }

    public static SpannableStringBuilder makeSpannable(String text, String regex, String startTag, String endTag) {

        StringBuffer sb = new StringBuffer();
        SpannableStringBuilder spannable = new SpannableStringBuilder();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            sb.setLength(0);
            String group = matcher.group();
            String spanText = group.substring(startTag.length(), group.length() - endTag.length());
            matcher.appendReplacement(sb, spanText);

            spannable.append(sb.toString());
            int start = spannable.length() - spanText.length();

            spannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        sb.setLength(0);
        matcher.appendTail(sb);
        spannable.append(sb.toString());
        return spannable;
    }

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

//    public static String encodeToBase64(String input) {
//        return Base64.encodeToString(input.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT).trim();
//    }


    public static String parseJSON(String json, String key) {
        String result = "";
        try {
            JSONObject obj = new JSONObject(json);
            result = obj.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
