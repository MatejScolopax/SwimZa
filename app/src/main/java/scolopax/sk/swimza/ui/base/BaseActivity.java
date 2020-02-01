package scolopax.sk.swimza.ui.base;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Every activity in this project must be descendant of this class
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID){
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }
}
