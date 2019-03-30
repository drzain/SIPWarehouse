package com.sip.warehouse;

public class AppConfig {

    public static final String IMAGE_DIRECTORY_NAME = "SIP Warehouse";
    // Server user login url
    public static String URL_LOGIN = "https://sip.uridu.id/api/v1/auth/login";

    // Server user logout url
    public static String URL_LOGOUT = "https://sip.uridu.id/api/v1/auth/logout";

    public static String URL_QUESTION_RECEIVE = "https://sip.uridu.id/api/v1/master/parts";

    public static String URL_QUESTION_CAMERA = "http://35.240.141.26/api/cameras";

    public static String URL_ASSET_RECEIVE_LIST = "https://sip.uridu.id/api/v1/asset-receives/checklists/";

    public static String URL_CANCEL_RECEIVE_LIST = "https://sip.uridu.id/api/v1/asset-receives/flag/cancel/checklists/";

    public static String URL_POST_ASSET_RECEIVE = "http://35.240.141.26/test/";

}
