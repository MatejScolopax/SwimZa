package scolopax.sk.swimza.ui.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;

import butterknife.ButterKnife;

public abstract class BaseDialog extends AppCompatDialog {

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void setContentView(int layoutResID){
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }
}
