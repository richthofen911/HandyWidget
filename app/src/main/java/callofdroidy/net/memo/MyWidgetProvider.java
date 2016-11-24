package callofdroidy.net.memo;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import com.show.api.ShowApiRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of App Widget functionality.
 */
public class MyWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget_provider);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        // timer to show time on the widget
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1, 1000);
        // timer to show weather, update hourly
        Timer wTimer = new Timer();
        wTimer.schedule(new MyWeather(context, appWidgetManager), 1, 3600000);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    //显示时间信息处理
    public class MyTime extends TimerTask {
        RemoteViews remoteViews;
        AppWidgetManager appWidgetManager;
        ComponentName thisWidget;
        DateFormat format = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM, Locale.getDefault());
        public MyTime(Context context, AppWidgetManager appWidgetManager) {
            this.appWidgetManager = appWidgetManager;
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            thisWidget = new ComponentName(context, MyWidgetProvider.class);
        }
        @Override
        public void run() {
            remoteViews.setTextViewText(R.id.title, format.format(new Date()));
            appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        }
    }
    //显示天气信息处理
    public class MyWeather extends TimerTask {
        RemoteViews remoteViews;
        AppWidgetManager appWidgetManager;
        ComponentName thisWidget;
        public MyWeather(Context context, AppWidgetManager appWidgetManager) {
            this.appWidgetManager = appWidgetManager;
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            thisWidget = new ComponentName(context, MyWidgetProvider.class);
        }
        @Override
        public void run() {
            String temp = "";
            String city = "大兴";
            String image_url="";
            try {
                //此处笔者用的是“易源接口”提供的API，下面的***分别代表你应用的appid和secretid
                String info = new ShowApiRequest("http://route.showapi.com/9-2","27810","188ee778d5e24ad2bc4a63837c049e71")
                        .addTextPara("areaid","101050701")
                        .addTextPara("area",city)
                        .addTextPara("needMoreDay","0")
                        .addTextPara("needIndex","0")
                        .addTextPara("needHourData","0")
                        .post();
                //获取接口返回的信息
                JSONObject wholeInfo = new JSONObject(info);
                JSONObject showapi_res_body  = wholeInfo.getJSONObject("showapi_res_body");
                JSONObject now  = showapi_res_body.getJSONObject("now");
                temp = now.getString("temperature")+"℃";
                image_url = now.getString("weather_pic");
                Bitmap weather_pic = getBitmap(image_url);
                System.out.println("temp:"+temp);
                remoteViews.setTextViewText(R.id.temperature, city +":"+ temp);
                if(weather_pic!=null){
                    remoteViews.setImageViewBitmap(R.id.image,weather_pic);
                }
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);
            } catch (JSONException e) {
                e.printStackTrace();
                remoteViews.setTextViewText(R.id.temperature, city+"：-5℃");
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        //根据天气图标url从网络获取图片
        public Bitmap getBitmap(String path) throws IOException {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if(conn.getResponseCode() == 200){
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
            return null;
        }
    }
}

