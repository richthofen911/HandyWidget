package callofdroidy.net.memo;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityWidgetConfigure extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ActivityConfigure";

    int mAppWidgetId;
    String colorPicked;
    ImageView lastSelectedIcon;
    ImageView lastSelectedColor;

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

    @BindView(R.id.iv_color_white)
    ImageView ivColorWhite;
    @BindView(R.id.iv_color_blue)
    ImageView ivColorBlue;
    @BindView(R.id.iv_color_green)
    ImageView ivColorGreen;
    @BindView(R.id.iv_color_orange)
    ImageView ivColorOrange;
    @BindView(R.id.iv_color_red)
    ImageView ivColorRed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configure);
        ButterKnife.bind(this);

        ivIconContact.setOnClickListener(this);
        ivIconDate.setOnClickListener(this);
        ivIconTodo.setOnClickListener(this);
        ivIconTravel.setOnClickListener(this);
        ivIconContact.setTag(R.drawable.icon_event_contact);
        ivIconDate.setTag(R.drawable.icon_event_date);
        ivIconTodo.setTag(R.drawable.icon_event_todo);
        ivIconTravel.setTag(R.drawable.icon_event_travel);

        ivColorWhite.setOnClickListener(this);
        ivColorBlue.setOnClickListener(this);
        ivColorGreen.setOnClickListener(this);
        ivColorOrange.setOnClickListener(this);
        ivColorRed.setOnClickListener(this);

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
            if(lastSelectedIcon != null) lastSelectedIcon.setBackgroundColor(Color.WHITE);
            lastSelectedIcon = (ImageView) view;
            lastSelectedIcon.setTag(view.getTag());
            view.setBackgroundColor(Color.parseColor("#a4a8ad"));
        }else
        if(viewId == R.id.iv_color_white || viewId == R.id.iv_color_blue ||
                viewId == R.id.iv_color_green || viewId == R.id.iv_color_orange || viewId == R.id.iv_color_red){
            if(lastSelectedColor != null) lastSelectedColor.setImageResource(android.R.color.transparent);
            lastSelectedColor = (ImageView) view;
            ((ImageView) view).setImageResource(R.drawable.icon_check_mark);
            colorPicked = (String)view.getTag();
        }

    }

    public void done(View v) {
        if(lastSelectedIcon == null || lastSelectedColor == null)
            Toast.makeText(this, "Please select a icon and a color", Toast.LENGTH_SHORT).show();
        else {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.my_widget);

            // set the selected icon as the widget icon
            views.setImageViewResource(R.id.iv_widget_image, (int)lastSelectedIcon.getTag());
            // set widget icon
            views.setInt(R.id.iv_widget_image, "setBackgroundColor", Color.parseColor(colorPicked));
            // set widget title
            views.setTextViewText(R.id.tv_widget_title, etTitle.getText().toString());
            views.setInt(R.id.tv_widget_title, "setTextColor", Color.parseColor(colorPicked));
            // set widget text content
            views.setTextViewText(R.id.tv_widget_content, etContent.getText().toString());
            views.setInt(R.id.tv_widget_content, "setTextColor", Color.parseColor(colorPicked));

            // update the widget
            appWidgetManager.updateAppWidget(mAppWidgetId, views);
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    }
}
