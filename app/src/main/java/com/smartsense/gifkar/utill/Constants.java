package com.smartsense.gifkar.utill;

public class Constants {

    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_FAIL = 500;
    public static final int STATUS_CHECK = 201;
    public static final String DB_PATH = "/data/data/com.smartsense.gifkar/databases/";
    public static final String BASE_URL = "https://gifkar.cloudapp.net";
    //    public static final String BASE_URL = "https://192.168.1.113";
    public static final String BASE_IMAGE_URL = "http://104.43.15.1:8025";
    public static final String PRO_TITLE = "Please Wait";
    public static final String PRO_LIST = "Syncing Process Running...";
    public static final String HAPPY = "1";
    public static final String SAD = "2";
    public static final String GOOGLE = "2  ";
    public static final String FB = "1";
    public static final String ONLINE_PAYMENT = "1";
    public static final String COD = "2";
    public static final String SCREEN = "screen";
    public static final String DEFAULT_TOKEN = "Uxl5lRYZHwx2jDpVsUoANJtRK5fNffzRewwbQjjd";
    public static final String OTP = "otp";
    public static final String MOBILENO = "mobile_no";
    // public static final String BASE_URL =
    // "http://ywtest.cloudapp.net:8080/SupremeCab";

    public class PrefKeys {


        public static final String PREF_CITY_ID = "city_id";
        public static final String PREF_CITY_NAME = "city_name";
        public static final String PREF_AREA_ID = "area_id";
        public static final String PREF_AREA_NAME = "area_name";
        public static final String PREF_CATEGORY_ID = "category_id";
        public static final String PREF_DJ_WALLET = "djwallet";
        public static final String PREF_ACCESS_TOKEN = "accessToken";
        public static final String PREF_REFER_CODE = "refer_code";
        public static final String PREF_USER_ID = "user_id";
        public static final String PREF_REFER_MSG = "refer_msg";
        public static final String PREF_USER_FULLNAME = "user_fullname";
        public static final String PREF_USER_MNO = "user_mno";
        public static final String PREF_USER_EMAIL = "user_email";
        public static final String PREF_USER_PROIMG = "user_proimg";
        public static final String PREF_USER_REFER = "user_refer";
        public static final String PREF_USER_PASS = "user_pass";
        public static final String PREF_PROD_LIST = "prod_list";
        public static final String PREF_ADDRESS = "address";
        public static final String PREF_ADDRESS_ID = "address_id";
        public static final String PREF_ISWAITING_SMS = "iswaiting_sms";
        public static final String PREF_ISOTP_VERIFIED = "isotp_verified";
        public static final String PREF_COUNTRY_LIST = "country_list";
        public static final String PREF_AREA_PIN_CODE = "area_pin_code";
        public static final String PREF_USER_INFO = "user_info";
        public static final String SHOP_NAME = "shop_name";
        public static final String SHOP_ID = "shop_id";
        public static final String OCCASIONS = "occasions";
        public static final String SHOP_FILTER = "shop_filter";
        public static final String FILTER_SHOP_NAME = "filter_shop_name";
        public static final String FILTER_SHOP_MIN = "filter_shop_min";
        public static final String FILTER_SHOP_RATTING = "filter_shop_rat";
        public static final String FILTER_PROD_NAME = "filter_prod_name";
        public static final String FILTER_PROD_PRICE = "filter_prod_price";
        public static final String PROD_FILTER = "prod_filter";

        public static final String SHOP_IMAGE = "shop_image";
        public static final String TIMESLOT = "time_slot";
    }

    public class NavigationItems {


        public static final int NAV_LOGIN = 0;
        public static final int NAV_HOME = 1;

        public static final int NAV_MY_CART = 3;
        public static final int NAV_MY_ORDERS = 4;
        public static final int NAV_MY_ADDRESSES = 5;
        public static final int NAV_MY_REMINDERS = 6;

        public static final int NAV_NOTIFICATIONS = 8;
        public static final int NAV_REFER_FRIEND = 9;
        public static final int NAV_ABOUT_US = 11;
        public static final int NAV_FEED_US = 10;
        public static final int NAV_SETTING = 13;
        public static final int NAV_LOGOUT = 12;


    }


    public class Events {
        public static final int EVENT_CITY = 1;
        public static final int EVENT_CATEGORY = 2;
        public static final int EVENT_FEATURE = 3;
        public static final int EVENT_LOGIN = 4;
        public static final int EVENT_PRODUCT_CATEGORY = 5;
        public static final int EVENT_EMAIL_CHECK = 6;
        public static final int EVENT_FORGOT_PASS = 7;
        public static final int EVENT_SIGNUP = 8;
        public static final int EVENT_UPDATE = 9;
        public static final int EVENT_SIGN_OUT = 10;
        public static final int EVENT_OFFERS = 11;
        public static final int EVENT_CHECK_OUT = 12;
        public static final int EVENT_APPLY_COUPAN = 13;
        public static final int EVENT_MYORDER = 14;
        public static final int EVENT_GET_ADDRESS = 15;
        public static final int EVENT_ADD_ADDRESS = 16;
        public static final int EVENT_EDIT_ADDRESS = 17;
        public static final int EVENT_DEL_ADDRESS = 18;
        public static final int EVENT_SEARCH = 19;
        public static final int EVENT_DJBAL = 20;
        public static final int EVENT_OTPVERIFICATION = 21;
        public static final int EVENT_RESEND_OTP = 22;
        public static final int EVENT_CHECK_RATING = 24;
        public static final int EVENT_DO_RATING = 23;
        public static final int EVENT_REFER = 25;
        public static final int EVENT_EMAIL_DETAILS = 26;
        public static final int EVENT_ADD_REMINDER = 27;
        public static final int EVENT_COUNTRY_LIST = 28;
        public static final int EVENT_SEND_OTP = 29;
        public static final int EVENT_MOBILE_CHECK = 30;
        public static final int EVENT_USER_DETAIL = 31;
        public static final int EVENT_BOTTOM = 32;
        public static final int EVENT_SHOPLIST = 33;
        public static final int EVENT_PINCODE = 34;
        public static final int EVENT_PRODLIST = 35;
        public static final int EVENT_GET_REMINDER = 36;
        public static final int EVENT_UPDATE_REMINDER = 37;
        public static final int EVENT_DEL_REMINDER = 38;
        public static final int EVENT_GET_NOTIFICATION = 39;
        public static final int PROFILE_UPDATE = 40;
        public static final int EVENT_DEL_NOTIFICATION = 41;
        public static final int EVENT_SEEN_NOTIFICATION = 42;
        public static final int EVENT_ORDER_HISTORY = 43;
        public static final int EVENT_ORDER_DETAIL = 44;
        public static final int EVENT_GET_OCCASION = 45;
        public static final int EVENT_GET_TIMESLOT = 46;
        public static final int EVENT_GET_REVIEW = 47;
        public static final int EVENT_REPORT_ERROR = 48;
    }


    public class ErrorCode {
        public static final int INVALID_CREDENTIALS = 1003;
        public static final int PARAM_MISSING = 1001;
        public static final int UNAUTHORIZED = 1002;
        public static final int SERVER_ERROR = 1004;

    }

    public class ScreenCode {
        public static final int SCREEN_OTP = 1;
        public static final int SCREEN_FORGOT = 2;
        public static final int SCREEN_MYADDRESS = 3;
        public static final int SCREEN_MYREMINDER = 4;
        public static final int SCREEN_LOGIN = 5;
    }


    public class ScreenReminderCode {
        public static final int ONE_HOUR = 3;
        public static final int ONE_DAY = 1;
        public static final int TWO_DAY = 2;
    }

}
