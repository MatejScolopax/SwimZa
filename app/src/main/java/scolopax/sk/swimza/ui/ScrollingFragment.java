package scolopax.sk.swimza.ui;

import android.support.v4.app.Fragment;

import java.util.LinkedList;

/**
 * Created by scolopax on 01/09/2017.
 */

public abstract class ScrollingFragment extends Fragment {

    private final LinkedList<VerticalScrolling> listeners = new LinkedList<>();

    public abstract void scrollToTop();

    protected void triggerScroll(int dy) {
        for (final VerticalScrolling l : listeners) {
            l.scrolledBy(dy);
        }
    }

    protected ScrollingFragment addListener(VerticalScrolling l) {
        listeners.add(l);
        return this;
    }

    protected interface VerticalScrolling {
        void scrolledBy(int dy);
    }
}