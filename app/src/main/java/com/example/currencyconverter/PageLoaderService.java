package com.example.currencyconverter;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.LocalBroadcastManager;
import com.example.currencyconverter.db.DataBase;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 03.05.17.
 */

public class PageLoaderService extends Service {
    public final static String SOURCE_URL = "http://www.cbr.ru/scripts/XML_daily.asp";

    public final static String ACTION_DATA_READY = "ACTION_DATA_READY";
    public final static String ACTION_ERROR = "ACTION_ERROR";
    public final static String ERROR_STRING = "EXTRA_ERROR_STRING";

    @RestrictTo (RestrictTo.Scope.TESTS)
    static boolean sStarted = false;

    @Override
    public void onCreate() {
        super.onCreate();
        sStarted = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sStarted = false;
    }

    private void loadPage(URL url) {
        StringBuilder buf = new StringBuilder();
        try {
            URLConnection con = url.openConnection();
            // try to get encoding
            Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
            if (con.getContentType() != null) {
                Matcher m = p.matcher(con.getContentType());

                // if encoding present use it, else use windows-1251
                String charset = m.matches() ? m.group(1) : "windows-1251";
                Reader r = new InputStreamReader(con.getInputStream(), charset);

                while (true) {
                    int ch = r.read();
                    if (ch < 0)
                        break;
                    buf.append((char) ch);
                }

                XmlParser parser = new XmlParser();

                if(!parser.parse(buf.toString())) {
                    sendError(getString(R.string.parse_error_text));
                } else {
                    DataBase db = DataBase.getInstance(this);
                    db.writeData(parser);

                    Intent intent = new Intent();
                    intent.setAction(ACTION_DATA_READY);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }

            } else {
                sendError("getContentType() is null");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            sendError(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            sendError(e.getMessage());
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            sendError(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            sendError(e.getMessage());
        } finally {
            stopSelf();
        }
    }

    private void sendError(String error) {
        Intent intent = new Intent(ACTION_ERROR);
        intent.putExtra(ERROR_STRING,error);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent workIntent, int flags, int startId) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadPage(new URL(SOURCE_URL));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        return START_NOT_STICKY;
    }

}
