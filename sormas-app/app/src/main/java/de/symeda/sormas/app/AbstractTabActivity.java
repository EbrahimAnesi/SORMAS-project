package de.symeda.sormas.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import de.symeda.sormas.app.R;
import de.symeda.sormas.app.util.SlidingTabLayout;

public abstract class AbstractTabActivity extends AppCompatActivity {

    public static final String KEY_PAGE = "page";

    protected ViewPager pager;
    protected SlidingTabLayout tabs;
    protected int currentTab = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void createTabViews(FragmentStatePagerAdapter adapter) {
        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assigning the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(false);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
            @Override
            public void onPageSelected(int position) {
                setCurrentTab(position);
                invalidateOptionsMenu();
            }
        });
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
    }

    public void reloadTabs() {
        createTabViews((FragmentStatePagerAdapter) pager.getAdapter());
    }

}
