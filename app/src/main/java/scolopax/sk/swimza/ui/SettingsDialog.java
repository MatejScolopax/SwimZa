package scolopax.sk.swimza.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import scolopax.sk.swimza.R;
import scolopax.sk.swimza.ui.base.BaseDialog;


/**
 * Created by scolopax on 13/08/2017.
 */

public class SettingsDialog extends BaseDialog {

    private Context context;

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
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings);

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
    }

    public static boolean isDownloadAutomatic(Context context) {
        return context.getSharedPreferences(SHARED_DOWNLOAD, Context.MODE_PRIVATE).getBoolean(SHARED_IS_AUTO, true);
    }

    @OnClick(R.id.btn_ok)
    public void close() {
        SettingsDialog.this.dismiss();
    }

    @OnCheckedChanged(R.id.radio_auto)
    public void setAuto() {
        if (rbtnAuto.isChecked()) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(SHARED_IS_AUTO, true);
            editor.apply();
        }
    }

    @OnCheckedChanged(R.id.radio_manual)
    public void setManual() {
        if (rbtnManual.isChecked()) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(SHARED_IS_AUTO, false);
            editor.apply();
        }
    }
}
