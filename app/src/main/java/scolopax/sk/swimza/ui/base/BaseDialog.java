package scolopax.sk.swimza.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import butterknife.ButterKnife;

public abstract class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void setContentView(int layoutResID){
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }
}
