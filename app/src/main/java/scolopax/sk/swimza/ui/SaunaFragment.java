package scolopax.sk.swimza.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import scolopax.sk.swimza.R;
import scolopax.sk.swimza.data.Cons;


/**
 * Created by scolopax on 26/08/2017.
 */

public class SaunaFragment extends ScrollingFragment {

    private static final String SHARED_PREF_GENDER = "SHARED_PREF_GENDER";
    private static final String SHARED_GENDER = "SHARED_GENDER";

    private static final int GENDER_MAN = 0;
    private static final int GENDER_WOMAN = 1;
    private static final int GENDER_MIX = 2;
    private int defaultGender = 2;

    @BindView(R.id.v_sauna_monday)
    TextSwitcher txtMonday;

    @BindView(R.id.v_sauna_tuesday)
    TextSwitcher txtTuesday;

    @BindView(R.id.v_sauna_wednesday)
    TextSwitcher txtWednesday;

    @BindView(R.id.v_sauna_thursday)
    TextSwitcher txtThursday;

    @BindView(R.id.v_sauna_friday)
    TextSwitcher txtFriday;

    @BindView(R.id.v_sauna_saturday)
    TextSwitcher txtSaturday;

    @BindView(R.id.v_sauna_sunday)
    TextSwitcher txtSunday;

    @BindView(R.id.scrollView_sauna)
    ScrollingView scrollView;

    @BindView(R.id.radio_men)
    RadioButton rbtnM;

    @BindView(R.id.radio_women)
    RadioButton rbtnW;

    @BindView(R.id.radio_together)
    RadioButton rbtnT;

    @Override
    public void scrollToTop() {
        if (scrollView != null) {
            scrollView.scrollTo(0, 0);
        }
    }

    private ScrollingView.OnScrollChangedListener mOnScrollChangedListener = new ScrollingView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            Log.v("scroll", "sauna " + l + " " + t + " " + oldl + " " + oldt);
            triggerScroll(t - oldt);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sauna, container, false);
    }


    @OnClick(R.id.btn_page)
    public void openSaunaProgram() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Cons.URL_SAUNA)));
    }


    @OnCheckedChanged(R.id.radio_men)
    public void setMen() {
        if (rbtnM.isChecked()) {
            defaultGender = GENDER_MAN;
            txtTuesday.setText("");
            txtWednesday.setText(getContext().getString(R.string.sauna_week2));
            txtThursday.setText(getContext().getString(R.string.sauna_week1));
            txtFriday.setText(getContext().getString(R.string.sauna_week2));
            txtSaturday.setText("");
            txtSunday.setText("");
        }
    }

    @OnCheckedChanged(R.id.radio_women)
    public void setWomen() {
        if (rbtnW.isChecked()) {
            defaultGender = GENDER_WOMAN;
            txtTuesday.setText(getContext().getString(R.string.sauna_week2));
            txtWednesday.setText(getContext().getString(R.string.sauna_week1));
            txtThursday.setText(getContext().getString(R.string.sauna_week2));
            txtFriday.setText("");
            txtSaturday.setText("");
            txtSunday.setText("");
        }
    }

    @OnCheckedChanged(R.id.radio_together)
    public void setTogether() {
        if (rbtnT.isChecked()) {
            defaultGender = GENDER_MIX;
            txtTuesday.setText(getContext().getString(R.string.sauna_week1));
            txtWednesday.setText("");
            txtThursday.setText("");
            txtFriday.setText(getContext().getString(R.string.sauna_week1));
            txtSaturday.setText(getContext().getString(R.string.sauna_weekend1) + "\n" + getContext().getString(R.string.sauna_weekend2));
            txtSunday.setText(getContext().getString(R.string.sauna_weekend3));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollView.setOnScrollChangedListener(mOnScrollChangedListener);

        SharedPreferences settings = getContext().getSharedPreferences(SHARED_PREF_GENDER, Context.MODE_PRIVATE);
        defaultGender = settings.getInt(SHARED_GENDER, defaultGender);

        initTextSwitcher(txtMonday);
        initTextSwitcher(txtTuesday);
        initTextSwitcher(txtWednesday);
        initTextSwitcher(txtThursday);
        initTextSwitcher(txtFriday);
        initTextSwitcher(txtSaturday);
        initTextSwitcher(txtSunday);


        if (defaultGender == GENDER_MAN) {
            rbtnM.setChecked(true);
            setMen();
        } else if (defaultGender == GENDER_WOMAN) {
            rbtnW.setChecked(true);
            setWomen();
        } else {
            setTogether();
            rbtnT.setChecked(true);
        }
    }

    private void initTextSwitcher(TextSwitcher ts) {
        ts.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new TextView(getContext());
            }
        });
    }


    @Override
    public void onPause() {
        SharedPreferences settings = getContext().getSharedPreferences(SHARED_PREF_GENDER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SHARED_GENDER, defaultGender);
        editor.apply();
        super.onPause();
    }
}
