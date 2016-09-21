package appsshoppy.com.whosnext.util;

/**
 * Created by akshayacharya on 04/09/16.
 */
public class Constants {

    public static String AUTH_KEY = "auth_key";

    public static String kHost_URL = "http://whoisnext.betaclubs.com";
    public static String kHost_API_URL = "http://whoisnext.betaclubs.com/api/";
    public static String kLogin_API = kHost_API_URL+"login";
    public static String kRegister_API = kHost_API_URL+"register";
    public static String kRegister_Business_API = kHost_API_URL+"register/business";
    public static String kCity_API = kHost_API_URL+"list/cities";
    public static String kCountry_API = kHost_API_URL+"list/countries";
    public static String kServiceListing_API = kHost_API_URL+"business/services?access_token=";
    public static String kCategoryListing_API = kHost_API_URL+"business/searchcat?access_token=";
    public static String kAddService_API = kHost_API_URL+"business/addservice?access_token=";
    public static String kBusinessHours_API = kHost_API_URL+"business/businesstime?access_token=";
    public static String kFeePolicy_API = kHost_API_URL+"business/feepolicies?access_token=";
    public static String kChangePassword_API = kHost_API_URL+"business/changepassword?access_token=";
    public static String kChangePaypalEmail_API = kHost_API_URL+"business/paypal_account?access_token=";
    public static String kBusinessCategories_API = kHost_API_URL+"default/get-categories";
    public static String kBusinessAddCategory_API = kHost_API_URL+"business/addcat?access_token=";
    public static String kBusinessRemoveCategory_API = kHost_API_URL+"business/removecat?access_token=";
    public static String kStaffMember_API = kHost_API_URL+"business/staff?access_token=";
    public static String kNewStaffMember_API = kHost_API_URL+"business/newstaff?access_token=";
}
