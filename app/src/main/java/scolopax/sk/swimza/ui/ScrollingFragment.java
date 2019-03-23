package scolopax.sk.swimza.ui;

import java.util.LinkedList;

import scolopax.sk.swimza.ui.base.BaseFragment;

/**
 * Created by scolopax on 01/09/2017.
 */

public abstract class ScrollingFragment extends BaseFragment {

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