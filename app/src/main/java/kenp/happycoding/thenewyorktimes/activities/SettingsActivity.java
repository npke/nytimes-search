package kenp.happycoding.thenewyorktimes.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import kenp.happycoding.thenewyorktimes.R;
import kenp.happycoding.thenewyorktimes.models.SearchQuery;

public class SettingsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

  @BindView(R.id.button_save)
  Button btnSave;

  @BindView(R.id.radio_group)
  RadioGroup rdSortOrder;

  @BindView(R.id.radio_newest)
  RadioButton rdNewest;

  @BindView(R.id.radio_oldest)
  RadioButton rdOldest;

  @BindView(R.id.check_box_arts)
  CheckBox chkArts;

  @BindView(R.id.check_box_fashion_styles)
  CheckBox chkFashionStyle;

  @BindView(R.id.check_box_sports)
  CheckBox chkSports;

  @BindView(R.id.edit_text_begin_date)
  EditText etBeginDate;

  private SearchQuery searchQuery;

  private DatePickerDialog datePickerDialog;

  private Calendar calendar;

  private SimpleDateFormat dateFormat;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    ButterKnife.bind(this);

    Intent intent = getIntent();
    searchQuery = intent.getParcelableExtra("searchquery");

    if (searchQuery.getSortOrderInt() == SearchQuery.NEWEST)
      rdNewest.setChecked(true);
    else rdOldest.setChecked(true);

    chkArts.setChecked(searchQuery.isArts());
    chkFashionStyle.setChecked(searchQuery.isFashionStyle());
    chkSports.setChecked(searchQuery.isSports());

    calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    dateFormat = new SimpleDateFormat("MMM dd, yyyy");

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    if (searchQuery.getBeginDate() == 0) {
      etBeginDate.setText(dateFormat.format(calendar.getTime()));
    } else {
      etBeginDate.setText(dateFormat.format(searchQuery.getBeginDate()));
      calendar.setTimeInMillis(searchQuery.getBeginDate());
    }

    datePickerDialog = new DatePickerDialog(this, this,
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
  }

  @OnClick(R.id.edit_text_begin_date)
  public void onClick(EditText editText) {
    datePickerDialog.show();
  }

  @OnClick(R.id.button_save)
  public void onClick(Button button) {
    searchQuery.setSortOrder(rdNewest.isChecked() ? SearchQuery.NEWEST : SearchQuery.OLDEST);
    searchQuery.setArts(chkArts.isChecked());
    searchQuery.setSports(chkSports.isChecked());
    searchQuery.setFashionStyle(chkFashionStyle.isChecked());
    searchQuery.setBeginDate(calendar.getTime().getTime());

    Intent data = new Intent();
    data.putExtra("searchquery", searchQuery);

    setResult(RESULT_OK, data);
    finish();
  }

  @Override public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
    calendar.set(i, i1, i2);
    etBeginDate.setText(dateFormat.format(calendar.getTime()));
  }
}
