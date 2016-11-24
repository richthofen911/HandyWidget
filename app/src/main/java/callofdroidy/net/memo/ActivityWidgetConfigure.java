package callofdroidy.net.memo;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityWidgetConfigure extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ActivityConfigure";

    int mAppWidgetId;
    int selectedIconId;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.iv_icon_todo)
    ImageView ivIconTodo;
    @BindView(R.id.iv_icon_date)
    ImageView ivIconDate;
    @BindView(R.id.iv_icon_travel)
    ImageView ivIconTravel;
    @BindView(R.id.iv_icon_contact)
    ImageView ivIconContact;
    @BindView(R.id.GridLayout1)
    GridLayout GridLayout1;
    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.activity_widget_configure)
    LinearLayout activityWidgetConfigure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configure);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_icon_contact || viewId == R.id.iv_icon_date || viewId == R.id.iv_icon_todo || viewId == R.id.iv_icon_travel) {

        }

    }

    public void done(View v) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.weather_widget);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
