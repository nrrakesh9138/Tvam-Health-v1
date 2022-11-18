package utils

import android.content.SharedPreferences

class Constants {
    companion object {
        const val SMS_SENDER_INFO = "SmsSenderInfo";
        const val CANCELLED_BY_BANK = "CANCELLED BY BANK"
        const val CANCELLED_BY_CUSTOMER = "CANCELLED BY CUSTOMER"
        const val COMPLETED = "COMPLETED"
        const val PENDING_FOR_RETURN = "PENDING FOR RETURN"
        const val RETURNED = "RETURNED"
        const val AGENT_ASSIGNED_FOR_PICKUP = "AGENT ASSIGNED FOR PICKUP"
        const val REQUEST_INITIATED = "REQUEST INITIATED"
        const val BRANCH_READY = "BRANCH READY"
        const val AGENT_ASSIGNED = "AGENT ASSIGNED"
        const val REJECTED = "REJECTED"
        const val CANCELLED = "CANCELLED"
        const val DOCUMENT_COLLECTED = "DOCUMENT COLLECTED"
        const val DOCUMENT_PICKUP = "DOCUMENT PICKUP"
        const val CASH_TRANSACTION_COMPLETED = "CASH TRANSACTION COMPLETED"
        const val ADDITIONAL_SERVICE_COMPLETED = "ADDITIONAL SERVICE COMPLETED"
        const val ADDITIONAL_SERVICE_DOCUMENT_COLLECTED = "ADDITIONAL SERVICE DOCUMENT COLLECTED"
        const val PICKUP = "PICKUP"
        const val PICK = "PICK"
        const val DROP = "DROP"
        const val CASH_TRANSACTION = "CASHTRANSACTIONS"
        const val ADDITIONAL_SERVICES = "ADDITIONALSERVICES"
        const val BANK = "BANK"
        const val CUSTOMER = "CUSTOMER"
        var TVAM_SHARED_PREFRENCE = "tvam_shared_pref"
        var PRIVATE_MODE = 0
        const val agent = "AGENT"
        const val Available = "AVAILABLE"
        const val NOTAVAILABLE = "NOTAVAILABLE"
        var sharedPreferences: SharedPreferences? = null
        const val EMAIL_PATTERN = "^\\d+(\\.\\d+)?$"
        const val agentID = "AGENTID"
        const val agentName = "agentName"
        const val status = "status"
        const val photoBlobID = "photoBlobID"
        const val photoBlobURL = "photoBlobURL"
        const val agentMobileNo = "agentMobileNo"
        const val loginKey = "LoginKey"
        const val login = "1"
        const val logout = "0"

        const val NOTIFICATION_UPDATE = "NOTIFICATION_UPDATE"
        const val NOTIFICATION_UPDATED = "1"
        const val NOTIFICATION_NOT_UPDATED = "0"

        const val otp_Req_ID = "otpReqID"
        const val E_NotificationKey = "ENotificationKey"
        const val mobile_No = "mobileNo"
        const val error_Code = "Error code"
        const val please_ensure_OTP_typed = "Please ensure that OTP is typed"
        const val server_error = "Server Error"
        const val Please_click_BACK_again_to_exit = "Please click BACK again to exit"

        const val login_text = "Login"
        const val please_digit_mobile_number = "Please enter your 10 digit mobile number"

        const val NOTIFICATION_LAT = "NOTIFICATION_LAT"
        const val NOTIFICATION_LONG = "NOTIFICATION_LONG"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val EXPIRY_TIME = "EXPIRY_TIME"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val PREF = "PREF"
        const val isByPassRequired = "isByPassRequired"
        const val isTrue = "true"
        const val isFalse = "false"
        const val en_us = "en-US"
        const val SRid = "SRid"
        const val SRRefid = "SRRefid"
        const val bangalore = "bangalore"

        val FORCE_UPDATE = "FORCE UPDATE"
        val RECOMMENDED_UPDATE = "RECOMMENDED UPDATE"

        val please_wait = "Please Wait..."

        const val REQUESTER_TYPE = "Agent";
        const val PREF_FILE_NAME = "agent_pref";

        const val Success_status = "Success"
        const val Business_Failure_status = "Business Failure"
        const val Technical_Failure_status = "Technical Failure"
        const val Handover_documents_to_branch_status = "Handover documents to branch"
        const val SELECT_DOCUMENT = "Select Document"
        const val CAPTURE_IMAGE = "Capture Image"
        const val SELECT_GALLERY = "Select From Gallery"
        const val CANCEL = "Cancel"

        const val TYPE_FILE: Int = 0
        const val TYPE_IMAGE: Int = 1
        const val TYPE_AUDIO: Int = 2
        const val TYPE_VIDEO: Int = 3
        const val TYPE_MAX: Int = TYPE_VIDEO
        const val IMAGE_DOC_DIR_NAME = ".DsbAgent/Images"

        const val AGLocationUpdateStartsAt = "AGLocationUpdateStartsAt"
        const val AGLocationUpdateEndsAt = "AGLocationUpdateEndsAt"
        const val UpdateAGLocationReq = "UpdateAGLocationReq"
        const val AGLocationTolerance = "AGLocationTolerance"
        const val LOCATION_WORK_TAG = "LOCATION_WORK_TAG"

        const val COVID_DECLATION_STATUS = "COVID_DECLATION_STATUS"
        const val LAST_TEMP_UPDATED = "LAST_TEMP_UPDATED"
        const val TEMP_THRESHOLD = "TEMP_THRESHOLD"
        const val LAST_TEMP_UPDATED_DATE = "LAST_TEMP_UPDATED_DATE"
        const val LAST_TEMP_THRESHOLD_STATUS = "LAST_TEMP_THRESHOLD_STATUS"
        const val LAST_TEMP_THRESHOLD_MESSAGE = "LAST_TEMP_THRESHOLD_MESSAGE"
    }
}
