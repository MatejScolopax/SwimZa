package scolopax.sk.swimza.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import scolopax.sk.swimza.R;
import scolopax.sk.swimza.data.DataShare;


/**
 * Created by scolopax on 26/08/2017.
 */

public class SaunaFragment extends ScrollingFragment {

    private TextView txtTuesday, txtWednesday, txtThursday, txtFriday, txtSaturday, txtSunday;
    private RadioButton rbtnM, rbtnW, rbtnT;
    private ScrollingView scrollView;
    private Button btnOnline;

    private static final String SHARED_PREF_GENDER = "SHARED_PREF_GENDER";
    private static final String SHARED_GENDER = "SHARED_GENDER";

    private static final int GENDER_MAN = 0;
    private static final int GENDER_WOMAN = 1;
    private static final int GENDER_MIX = 2;
    private int defaultGender = 2;

    @Override
    public void scrollToTop() {
        if (scrollView != null){
            scrollView.scrollTo(0,0);
        }
    }

    private ScrollingView.OnScrollChangedListener mOnScrollChangedListener = new ScrollingView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            Log.v("scroll", "sauna " + l + " " + t + " " + oldl + " " +oldt);
            triggerScroll(t-oldt);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sauna, container, false);

        txtTuesday = (TextView) view.findViewById(R.id.v_sauna_tuesday);
        txtWednesday = (TextView) view.findViewById(R.id.v_sauna_wednesday);
        txtThursday = (TextView) view.findViewById(R.id.v_sauna_thursday);
        txtFriday = (TextView) view.findViewById(R.id.v_sauna_friday);

        scrollView = (ScrollingView)  view.findViewById(R.id.scrollView_sauna);

        rbtnM = (RadioButton) view.findViewById(R.id.radio_men);
        rbtnW = (RadioButton) view.findViewById(R.id.radio_women);
        rbtnT = (RadioButton) view.findViewById(R.id.radio_together);

        txtSaturday = (TextView) view.findViewById(R.id.v_sauna_saturday);
        txtSunday = (TextView) view.findViewById(R.id.v_sauna_sunday);

        btnOnline = (Button) view.findViewById(R.id.btn_page);

        scrollView.setOnScrollChangedListener(mOnScrollChangedListener);

        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(DataShare.URL_SAUNA)));
            }
        });


        SharedPreferences settings = getContext().getSharedPreferences(SHARED_PREF_GENDER, Context.MODE_PRIVATE);
        defaultGender = settings.getInt(SHARED_GENDER, defaultGender);

        rbtnM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    setMen();
            }
        });

        rbtnW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    setWomen();
            }
        });

        rbtnT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    setTogether();
            }
        });

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
        return view;
    }

    @Override
    public void onPause() {

        SharedPreferences settings = getContext().getSharedPreferences(SHARED_PREF_GENDER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SHARED_GENDER, defaultGender);
        editor.commit();
        super.onPause();
    }

    private void setWomen() {
        defaultGender = GENDER_WOMAN;
        txtTuesday.setText(getContext().getString(R.string.sauna_week2));
        txtWednesday.setText(getContext().getString(R.string.sauna_week1));
        txtThursday.setText(getContext().getString(R.string.sauna_week2));
        txtFriday.setText("");
        txtSaturday.setText("");
        txtSunday.setText("");
    }

    private void setMen() {
        defaultGender = GENDER_MAN;
        txtTuesday.setText("");
        txtWednesday.setText(getContext().getString(R.string.sauna_week2));
        txtThursday.setText(getContext().getString(R.string.sauna_week1));
        txtFriday.setText(getContext().getString(R.string.sauna_week2));
        txtSaturday.setText("");
        txtSunday.setText("");
    }

    private void setTogether() {
        defaultGender = GENDER_MIX;
        txtTuesday.setText(getContext().getString(R.string.sauna_week1));
        txtWednesday.setText("");
        txtThursday.setText("");
        txtFriday.setText(getContext().getString(R.string.sauna_week1));
        txtSaturday.setText(getContext().getString(R.string.sauna_weekend1) + "\n" + getContext().getString(R.string.sauna_weekend2));
        txtSunday.setText(getContext().getString(R.string.sauna_weekend3));
    }
}
