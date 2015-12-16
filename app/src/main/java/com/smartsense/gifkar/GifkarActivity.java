package com.smartsense.gifkar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.Constants;

import org.json.JSONArray;

public class GifkarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    TextView actionBarTitle;
    ImageView btSearch, btFilter;
    private static FragmentManager fm;
    ListView lvNavList;
    private LinearLayout llHeadProfile;
    private LinearLayout llHeadAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifkar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gifkar);
        actionBarTitle = (TextView) toolbar.findViewById(R.id.actionBarHomeTitle);
        actionBarTitle.setText("Prahlad Nagar,380015");
        btFilter = (ImageView) toolbar.findViewById(R.id.btActionBarfilter);
        btFilter.setOnClickListener(this);
        btSearch = (ImageView) toolbar.findViewById(R.id.btActionBarSearch);
        btSearch.setOnClickListener(this);
        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null)
//            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_home));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = LayoutInflater.from(this).inflate(R.layout.acitivity_drawer, null);
        navigationView.addHeaderView(header);
        fm = getSupportFragmentManager();
        llHeadProfile = (LinearLayout) header.findViewById(R.id.llHeadProfile);
        llHeadProfile.setOnClickListener(this);
        llHeadAddress = (LinearLayout) header.findViewById(R.id.llHeadAddress);
        llHeadAddress.setOnClickListener(this);
        lvNavList = (ListView) header.findViewById(R.id.lvHeadList);
        setHeader(GifkarActivity.this);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ShopListFragment()).commit();

    }


    public void setHeader(Activity a) {
        NavigationAdapter adp = new NavigationAdapter(a, fm);
        lvNavList.setAdapter(adp);
        setListViewHeightBasedOnChildren(lvNavList);
//        iv_profile_img_header.setDefaultImageResId(R.drawable.ic_men_user2);
//        iv_profile_img_header.setImageUrl(Constants.BASE_IMAGE_URL + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_PROIMG, ""), imgLoader);
//        tv_header_name.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_FULLNAME, "WelCome"));
//        tv_header_email.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_EMAIL, ""));

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btActionBarfilter:
                startActivity(new Intent(this, ShopFilterActivity.class));
                break;
            case R.id.btActionBarSearch:
                startActivity(new Intent(this, OrderDetailActivity.class));
                break;
            case R.id.llHeadProfile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.llHeadAddress:
                startActivity(new Intent(this, CitySelectActivity.class));
                break;

            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.gifkar, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class NavigationAdapter extends BaseAdapter implements View.OnClickListener {

        private LayoutInflater inflater;
        private String mNavTitles[];
        private int mIcons[];

        FragmentManager fm;
        Activity mContext;

        public NavigationAdapter(Activity context, FragmentManager fm) {
            inflater = LayoutInflater.from(context);
            mContext = context;
            this.fm = fm;
            getList();
        }


        public void getList() {

            if (0 == 1) {
                //user is login and display sign out btn
                mNavTitles = new String[]{"Home", "empty", "My Cart", "My Orders", "My Addresses", "My Reminders", "empty", "Notifications", "Refer Friends", "About Us", "Feed Us", "Setting", "Sign Out"};
                mIcons = new int[]{R.drawable.ic_home, R.drawable.ic_home, R.drawable.ic_cart,
                        R.drawable.ic_orders, R.drawable.ic_address, R.drawable.ic_reminder, R.drawable.ic_home, R.drawable.ic_notification, R.drawable.ic_refer, R.drawable.ic_about, R.drawable.ic_feedus, R.drawable.ic_setting, R.drawable.ic_logout};

            } else {
                //user not logged in display sign in btn
                mNavTitles = new String[]{"Sign In", "Home", "empty", "My Cart", "My Orders", "My Addresses", "My Reminders", "empty", "Notifications", "Refer Friends", "About Us", "Feed Us", "Setting"};
                mIcons = new int[]{R.drawable.ic_login, R.drawable.ic_home, R.drawable.ic_home, R.drawable.ic_cart,
                        R.drawable.ic_orders, R.drawable.ic_address, R.drawable.ic_reminder, R.drawable.ic_home, R.drawable.ic_notification, R.drawable.ic_refer, R.drawable.ic_about, R.drawable.ic_feedus, R.drawable.ic_setting};

            }


        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mNavTitles.length;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mNavTitles[position];
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        View convertView;

        @Override
        public View getView(final int position, View convertView1, ViewGroup parent) {

//            convertView = inflater.inflate(R.layout.element_nav_list, parent, false);
            convertView = inflater.inflate(R.layout.element_nav_list, parent, false);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_menuname_drawer);
            TextView tvCount = (TextView) convertView.findViewById(R.id.tv_menucount_drawer);
            ImageView iv_image = (ImageView) convertView.findViewById(R.id.iv_menuicon_drawer);
            View viewLine = (View) convertView.findViewById(R.id.viewLine);

            tvTitle.setText(mNavTitles[position]);
            iv_image.setImageResource(mIcons[position]);

            //set icon color
//            iv_image.setColorFilter(getResources().getColor(R.color.iconcolor));

            if (position == Constants.NavigationItems.NAV_NOTIFICATIONS) {
                tvCount.setVisibility(View.VISIBLE);
                tvCount.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_DJ_WALLET, "0"));
            } else if (position == Constants.NavigationItems.NAV_MY_CART) {
                try {
                    if (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PROD_LIST, "") != "") {
                        JSONArray productArray = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PROD_LIST, ""));
                        if (productArray.length() < 1) {
                            tvCount.setVisibility(View.INVISIBLE);
                        } else {
                            tvCount.setVisibility(View.VISIBLE);
                            tvCount.setText("" + productArray.length());
                        }
                    } else {
                        tvCount.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                if (mNavTitles[position].equalsIgnoreCase("empty")) {
                    tvTitle.setVisibility(View.GONE);
                    iv_image.setVisibility(View.GONE);
                    viewLine.setVisibility(View.VISIBLE);
                } else {
                    viewLine.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.VISIBLE);
                    iv_image.setVisibility(View.VISIBLE);
                }
                tvCount.setVisibility(View.INVISIBLE);
            }

            convertView.setOnClickListener(this);
            convertView.setTag(position);

            return convertView;
        }

        @Override
        public void onClick(View view) {
            GifkarActivity gifkarActivity = new GifkarActivity();
            gifkarActivity.navigationButtonclick(mContext, view, fm);
        }
    }

    public void navigationButtonclick(final Activity c, View v, FragmentManager fm) {
        int position = (int) v.getTag();
//        boolean flag = checkuserid();
        switch (position) {
            case Constants.NavigationItems.NAV_LOGIN:
//                setbackpress(0);
//                fragmentcall(c, new CategoryFragment(), fm);
                break;
            case Constants.NavigationItems.NAV_HOME:
//                setbackpress(0);
//                fragmentcall(c, new CategoryFragment(), fm);
                break;
            case Constants.NavigationItems.NAV_MY_CART:
                c.startActivity(new Intent(c, MyCartActivity.class));
//                if (flag) {
//                    setbackpress(3);
//                    fragmentcall(c, new DJWalletFragment(), fm);
//
//                } else {
//                    gotosignin(c);
//                }
                break;

            case Constants.NavigationItems.NAV_MY_ORDERS:
//                setbackpress(2);
                c.startActivity(new Intent(c, MyOrdersActivity.class));
                break;

            case Constants.NavigationItems.NAV_MY_ADDRESSES:
                c.startActivity(new Intent(c, MyAddressActivity.class));
//                if (flag) {
////                    managebackpress();
//                    setbackpress(3);
//                    fragmentcall(c, new OrderFragment(), fm);
//
//                } else {
//                    gotosignin(c);
//                }
                break;
            case Constants.NavigationItems.NAV_MY_REMINDERS:
                c.startActivity(new Intent(c, MyRemindersActivity.class));
////                managebackpress();
//                setbackpress(2);
//                fragmentcall(c, new OfferFragment(), fm);
                break;
            case Constants.NavigationItems.NAV_NOTIFICATIONS:
                c.startActivity(new Intent(c, NotificationActivity.class));
//                if (flag) {
//                    fragmentcall(c, new ReferEarnFragment(), fm);
////                    managebackpress();
//                    setbackpress(3);
//                } else {
//                    gotosignin(c);
//                }
                break;
            case Constants.NavigationItems.NAV_REFER_FRIEND:
                c.startActivity(new Intent(c, ReferFriendsActivity.class));

//                if (flag) {
//                    setbackpress(2);
//                    c.startActivity(new Intent(c, AvailAddressActivity.class).putExtra("intent", false));
//                } else {
//                    gotosignin(c);
//                }
                break;
            case Constants.NavigationItems.NAV_ABOUT_US:
                c.startActivity(new Intent(c, AboutUsActivity.class));
//                if (flag) {
//                    setbackpress(2);
//                    c.startActivity(new Intent(c, AvailAddressActivity.class).putExtra("intent", false));
//                } else {
//                    gotosignin(c);
//                }
                break;
            case Constants.NavigationItems.NAV_FEED_US:
                c.startActivity(new Intent(c, FeedUsActivity.class));
//                if (flag) {
//                    setbackpress(2);
//                    c.startActivity(new Intent(c, AvailAddressActivity.class).putExtra("intent", false));
//                } else {
//                    gotosignin(c);
//                }
                break;
            case Constants.NavigationItems.NAV_SETTING:
//                if (flag) {
//                    setbackpress(2);
//                    c.startActivity(new Intent(c, AvailAddressActivity.class).putExtra("intent", false));
//                } else {
//                    gotosignin(c);
//                }
                break;
            case Constants.NavigationItems.NAV_LOGOUT:
//                if (flag) {
                AlertDialog.Builder alert = new AlertDialog.Builder(c);
                alert.setTitle("Confirm!");
                alert.setMessage("are you sure you want to sign out?");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Do something here where "ok" clicked

//                            gotosignout(c);
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Do something here where "Cancel" clicked
                        dialog.cancel();
                    }
                });
                alert.show();


//                } else {
////                    gotosignin(c);
//                }
                break;
            default:
                Log.d("Navigation Drawer", String.valueOf(position));
        }
        DrawerLayout drawer = (DrawerLayout) c.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
