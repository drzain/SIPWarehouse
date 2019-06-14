package com.sip.warehouse;

public class AppConfig {

    public static final String IMAGE_DIRECTORY_NAME = "SIP Warehouse";
    // Server user login url
    public static String URL_LOGIN = "https://devsteam.sitama.co.id/api/v1/auth/login";

    // Server user logout url
    public static String URL_LOGOUT = "https://devsteam.sitama.co.id/api/v1/auth/logout";

    public static String URL_QUESTION_RECEIVE = "https://devsteam.sitama.co.id/api/v1/master/parts";

    public static String URL_QUESTION_RECEIVE2 = "https://devsteam.sitama.co.id/api/v2/master/parts";

    public static String URL_QUESTION_CAMERA = "http://35.240.141.26/api/cameras";

    public static String URL_ASSET_RECEIVE_LIST = "https://devsteam.sitama.co.id/api/v1/asset-receives/checklists";

    public static String URL_GRADING_LIST = "https://devsteam.sitama.co.id/api/v1/self-grading/order";

    public static String URL_CANCEL_RECEIVE_LIST = "https://devsteam.sitama.co.id/api/v1/asset-receives/flag/cancel/checklists";

    public static String URL_CANCEL_GRADING = "https://devsteam.sitama.co.id/api/v1/self-grading/flag/cancel";

    public static String URL_POST_ASSET_RECEIVE = "https://devsteam.sitama.co.id/api/v1/asset-receives/submit/checklists";

    public static String URL_POST_GRADING = "https://devsteam.sitama.co.id/api/v1/self-grading/submit";

    public static String URL_VALIDATE_CEKLIS = "https://devsteam.sitama.co.id/api/v1/asset-receives/validate/checklists";

    public static String URL_VALIDATE_GRADING = "https://devsteam.sitama.co.id/api/v1/self-grading/validate";

    public static String URL_POST_ASSET_RECEIVE2 = "http://35.240.141.26/test/";

    public static String URL_FLAG_RECEIVE_ASSET = "https://devsteam.sitama.co.id/api/v1/asset-receives/flag/checklists";

    public static String URL_FLAG_GRADING = "https://devsteam.sitama.co.id/api/v1/self-grading/flag";

    public static String URL_ASSET_APPROVE_LIST = "https://devsteam.sitama.co.id/api/v1/asset-receives";

    public static String URL_DELETE_FOTO = "https://devsteam.sitama.co.id/api/v1/asset-receives/photo/remove";

    public static String URL_APPROVE_DATA = "https://devsteam.sitama.co.id/api/v1/asset-receives/checklists/approve";
}
