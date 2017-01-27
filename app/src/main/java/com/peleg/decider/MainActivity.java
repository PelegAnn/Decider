package com.peleg.decider;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddNewChoiceFragment.OnDoneAddNewItemListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNewFragment();
            }
        });

    }

    public void showAddNewFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(android.R.id.content, new AddNewChoiceFragment())
                .addToBackStack(null).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDone(Choice itemToAdd) {

        mSectionsPagerAdapter.add(itemToAdd);
        //OptionsListFragment fragment = (OptionsListFragment) getFragmentManager().findFragmentByTag(SectionsPagerAdapter.makeFragmentName(R.id.container,1));

        //OptionsListFragment.newInstance().addNewItem(itemToAdd);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        private OptionsListFragment optionsListFragment;
        private PlaceholderFragment mainFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PlaceholderFragment.newInstance(position + 1);
                case 1:
                    return OptionsListFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }

        public void add(Choice item) {
            if(optionsListFragment!= null) {
                optionsListFragment.addNewItem(item);
            }
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    mainFragment = (PlaceholderFragment) createdFragment;
                    break;
                case 1:
                    optionsListFragment = (OptionsListFragment) createdFragment;
                    break;
            }
            return createdFragment;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ImageButton decideBtn;
        private ImageView gif;
        private TextView textView;


        private View rootView;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

           // gif = (ImageView) rootView.findViewById(R.id.decision_gif);
//            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gif);
//            Glide.with(getContext()).load(R.raw.test).into(imageViewTarget);

            decideBtn = (ImageButton) rootView.findViewById(R.id.decide);
//            gif.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                   textView.setText(makeADecision().getName());
//
//
//                }
//            });
            decideBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView.setText(makeADecision().getName());
                }
            });
            return rootView;
        }

        private Choice makeADecision() {
            ArrayList<Choice> items = OptionsList.getInstance().getList();
            if(items != null) {
                if(items.size() == 0) {
                    Snackbar.make(rootView, "Please add options", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return null;
                } else if(items.size()==1) {
                    return items.get(0);
                } else {
                    float sumWeights =0;
                    for(int i=0; i<items.size(); i++) {
                        sumWeights += items.get(i).getRank()/5;
                    }
                    double rand = (Math.random()*sumWeights);
                    for(int i =0; i<items.size();i++) {
                        Choice result = items.get(i);
                        if(rand < result.getRank()/5)
                            return result;
                        rand -= result.getRank()/5;
                    }
                }
            }
            return null;
        }


    }
}
