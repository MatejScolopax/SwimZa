package scolopax.sk.swimza.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import scolopax.sk.swimza.R;


/**
 * Created by scolopax on 13/08/2017.
 */

public class SettingsDialog extends Dialog {

    private Context context;

    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.txt_settings_version)
    TextView txtVersion;
    @BindView(R.id.radio_auto)
    RadioButton rbtnAuto;
    @BindView(R.id.radio_manual)
    RadioButton rbtnManual;

    private static final String SHARED_DOWNLOAD = "SHARED_PREF_DOWNLOAD";
    private static final String SHARED_IS_AUTO = "SHARED_PREF_KEY_AUTO";

    private SharedPreferences sharedPref;

    public SettingsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        this.setTitle(R.string.about_caption);
        this.show();
    }

    public static boolean isDownloadAutomatic(Context context) {
        return context.getSharedPreferences(SHARED_DOWNLOAD, Context.MODE_PRIVATE).getBoolean(SHARED_IS_AUTO, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings);

        ButterKnife.bind(this);

        try {
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            txtVersion.setText(String.valueOf(pInfo.versionName));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        this.sharedPref = context.getSharedPreferences(SHARED_DOWNLOAD, Context.MODE_PRIVATE);
        boolean isAuto = sharedPref.getBoolean(SHARED_IS_AUTO, true);

        rbtnAuto.setChecked(isAuto);
        rbtnManual.setChecked(!isAuto);


        rbtnAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (rbtnAuto.isChecked()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(SHARED_IS_AUTO, true);
                    editor.apply();
                }
            }
        });

        rbtnManual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (rbtnManual.isChecked()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(SHARED_IS_AUTO, false);
                    editor.apply();
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SettingsDialog.this.dismiss();
            }
        });

    }
}
