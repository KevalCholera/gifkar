package com.smartsense.gifkar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.CircleImageView;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GifkarActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    private TextView actionBarTitle;
    private ImageView btSearch, btFilter;
    private static FragmentManager fm;
    private ListView lvNavList;
    private LinearLayout llHeadProfile;
    private LinearLayout llHeadAddress;
    private CircleImageView ivHeadImage;
    private ImageLoader imageLoader;
    private TextView tVHeadName;
    private TextView tVHeadMobileNo;
    private TextView tvHeadAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifkar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gifkar);
        imageLoader = GifkarApp.getInstance().getDiskImageLoader();
        actionBarTitle = (TextView) toolbar.findViewById(R.id.actionBarHomeTitle);
        actionBarTitle.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_AREA_NAME, "") + ", " + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_AREA_PIN_CODE, ""));
        setSupportActionBar(toolbar);
        btFilter = (ImageView) toolbar.findViewById(R.id.btActionBarfilter);
        btFilter.setOnClickListener(this);
        btSearch = (ImageView) toolbar.findViewById(R.id.btActionBarSearch);
        btSearch.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        View header = LayoutInflater.from(this).inflate(R.layout.acitivity_drawer, null);
        navigationView.addHeaderView(header);
        fm = getSupportFragmentManager();
        llHeadProfile = (LinearLayout) header.findViewById(R.id.llHeadProfile);
        llHeadProfile.setOnClickListener(this);
        llHeadAddress = (LinearLayout) header.findViewById(R.id.llHeadAddress);
        llHeadAddress.setOnClickListener(this);
        ivHeadImage = (CircleImageView) header.findViewById(R.id.ivHeadImage);
        ivHeadImage.setDefaultImageResId(R.drawable.ic_user);
        tvHeadAddress = (TextView) header.findViewById(R.id.tvHeadAddress);
        tVHeadName = (TextView) header.findViewById(R.id.tVHeadName);
        tVHeadMobileNo = (TextView) header.findViewById(R.id.tVHeadMobileNo);
        lvNavList = (ListView) header.findViewById(R.id.lvHeadList);
        setHeader(GifkarActivity.this);
        if (SharedPreferenceUtil.contains(Constants.PrefKeys.PREF_USER_FULLNAME))
            getUserDetail();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ShopListFragment()).commit();

    }


    public void setHeader(Activity a) {
        NavigationAdapter adp = new NavigationAdapter(a, fm);
        lvNavList.setAdapter(adp);
        setListViewHeightBasedOnChildren(lvNavList);
        tvHeadAddress.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_AREA_NAME, "") + ", " + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_AREA_PIN_CODE, ""));
        tVHeadMobileNo.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_MNO, ""));
        tVHeadName.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_FULLNAME, ""));
        ivHeadImage.setImageUrl(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_PROIMG, ""), imageLoader);
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
            drawer.openDrawer(GravityCompat.START);
//            super.onBackPressed();
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
            if (SharedPreferenceUtil.contains(Constants.PrefKeys.PREF_ACCESS_TOKEN)) {
                //user is login and display sign out btn
                mNavTitles = new String[]{"Home", "empty", "My Cart", "My Orders", "My Addresses", "My Reminders", "empty", "Notifications", "Refer Friends", "About Us", "Feed Us", "Sign Out", "Setting"};
                mIcons = new int[]{R.drawable.ic_home, R.drawable.ic_home, R.drawable.ic_cart,
                        R.drawable.ic_orders, R.drawable.ic_address, R.drawable.ic_reminder, R.drawable.ic_home, R.drawable.ic_notification, R.drawable.ic_refer, R.drawable.ic_about, R.drawable.ic_feedus, R.drawable.ic_logout, R.drawable.ic_setting};

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
                convertView.setOnClickListener(this);
                tvCount.setVisibility(View.VISIBLE);
                tvCount.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_DJ_WALLET, "0"));
            } else if (position == Constants.NavigationItems.NAV_MY_CART) {
                convertView.setOnClickListener(this);
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

            } else if (position == Constants.NavigationItems.NAV_SETTING) {
                convertView.setVisibility(View.INVISIBLE);
            } else {
                if (mNavTitles[position].equalsIgnoreCase("empty")) {
                    tvTitle.setVisibility(View.GONE);
                    iv_image.setVisibility(View.GONE);
                    viewLine.setVisibility(View.VISIBLE);

                } else {
                    convertView.setOnClickListener(this);
                    viewLine.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.VISIBLE);
                    iv_image.setVisibility(View.VISIBLE);
                }
                tvCount.setVisibility(View.INVISIBLE);
            }


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
                c.startActivity(new Intent(c, StartActivity.class));
                break;
            case Constants.NavigationItems.NAV_HOME:
//                setbackpress(0);
                fragmentcall(c, new ShopListFragment(), fm);
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
//                c.startActivity(new Intent(c, MyOrdersActivity.class));
                fragmentcall(c, new MyOrdersActivity(), fm);
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
//                c.startActivity(new Intent(c, MyRemindersActivity.class));
////                managebackpress();
//                setbackpress(2);
                fragmentcall(c, new MyRemindersActivity(), fm);
                break;
            case Constants.NavigationItems.NAV_NOTIFICATIONS:
//                c.startActivity(new Intent(c, NotificationActivity.class));
//                if (flag) {
                fragmentcall(c, new NotificationActivity(), fm);
////                    managebackpress();
//                    setbackpress(3);
//                } else {
//                    gotosignin(c);
//                }
                break;
            case Constants.NavigationItems.NAV_REFER_FRIEND:
//                c.startActivity(new Intent(c, ReferFriendsActivity.class));
                fragmentcall(c, new ReferFriendsActivity(), fm);

//                if (flag) {
//                    setbackpress(2);
//                    c.startActivity(new Intent(c, AvailAddressActivity.class).putExtra("intent", false));
//                } else {
//                    gotosignin(c);
//                }
                break;
            case Constants.NavigationItems.NAV_ABOUT_US:
//                c.startActivity(new Intent(c, AboutUsActivity.class));
                fragmentcall(c, new AboutUsActivity(), fm);
//                if (flag) {
//                    setbackpress(2);
//                    c.startActivity(new Intent(c, AvailAddressActivity.class).putExtra("intent", false));
//                } else {
//                    gotosignin(c);
//                }
                break;
            case Constants.NavigationItems.NAV_FEED_US:
                fragmentcall(c, new FeedUsActivity(), fm);
//                c.startActivity(new Intent(c, FeedUsActivity.class));
//                if (flag) {
//                    setbackpress(2);
//                    c.startActivity(new Intent(c, AvailAddressActivity.class).putExtra("intent", false));
//                } else {
//                    gotosignin(c);
//                }
                break;
//            case Constants.NavigationItems.NAV_SETTING:
//                if (flag) {
//                    setbackpress(2);
//                    c.startActivity(new Intent(c, AvailAddressActivity.class).putExtra("intent", false));
//                } else {
//                    gotosignin(c);
//                }
//                break;
            case Constants.NavigationItems.NAV_LOGOUT:
//                if (flag) {
                AlertDialog.Builder alert = new AlertDialog.Builder(c);
                alert.setTitle("Confirm!");
                alert.setMessage("are you sure you want to sign out?");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Do something here where "ok" clicked
                        SharedPreferenceUtil.clear();
                        SharedPreferenceUtil.save();
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

    public void fragmentcall(Activity c, Fragment frg, FragmentManager fm) {

        fm.beginTransaction().replace(R.id.fragment_container, frg).commit();

    }

    public void getUserDetail() {
        final String tag = "userDetail";
        String url = Constants.BASE_URL + "/mobile/user/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&eventId=" + String.valueOf(Constants.Events.EVENT_USER_DETAIL) + "&userToken=" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "");
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(this, "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_USER_DETAIL:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_FULLNAME, response.optJSONObject("data").optJSONObject("userDetails").optString("firstName") + " " + response.optJSONObject("data").optJSONObject("userDetails").optString("lastName"));
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_EMAIL, response.optJSONObject("data").optJSONObject("userDetails").optString("email"));
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_MNO, response.optJSONObject("data").optJSONObject("userDetails").optString("mobile"));
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_PROIMG, response.optJSONObject("data").optJSONObject("userDetails").optString("image"));
                            SharedPreferenceUtil.save();
                            tVHeadName.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_FULLNAME, ""));
                            ivHeadImage.setImageUrl(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_PROIMG, ""), imageLoader);
                            tVHeadMobileNo.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_MNO, ""));
                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
