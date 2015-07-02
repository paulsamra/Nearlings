package swipe.android.DatabaseHelpers;

import com.edbert.library.database.DatabaseHelperInterface;

public class BalanceDatabaseHelper implements DatabaseHelperInterface {

    public static final String AUTHORITY = "";

    public static final String TABLE_NAME = "balance";

    public static final String LIST_TYPE = "LIST_TYPE";

    public static final String PAYMENT_ID = "_id";
    public static final String BUYER_ID = "BUYER_ID";
    public static final String SELLER_ID = "SELLER_ID";
    public static final String TITLE = "TILE";
    public static final String CREATED_AT = "CREATED_AT";
    public static final String STATUS = "STATUS";
    public static final String TXN_TYPE = "TXN_TYPE";
    public static final String TXN_DETAIL_RECIPIENT_USERNAME = "TXN_DETAIL_RECIPIENT_USERNAME";
    public static final String TXN_DETAIL_RECIPIENT_NAME = "TXN_DETAIL_RECIPIENT_NAME";
    public static final String TXN_DETAIL_task_id = "task_id";
    public static final String TXN_DETAIL_task_title= "task_title";
    public static final String TXN_DETAIL_task_cost= "task_cost";
    public static final String TXN_DETAIL_fee= "fee";

    public static final String TXN_DETAIL_total_charge= "total_charge";
    public static final String TXN_DETAIL_paypal_email= "paypal_email";
    public static final String TXN_DETAIL_amount_deposited= "amount_deposited";
    public static final String TXN_DETAIL_total_amount= "total_amount";


    public static final String TXN_DETAIL_earnings= "earnings";


    public static final String[] COLUMNS = {
            PAYMENT_ID, BUYER_ID,
            SELLER_ID, TITLE, CREATED_AT,
            STATUS, TXN_TYPE, TXN_DETAIL_RECIPIENT_USERNAME,
            TXN_DETAIL_RECIPIENT_NAME, TXN_DETAIL_task_id, TXN_DETAIL_task_title,
            TXN_DETAIL_task_cost, TXN_DETAIL_fee, TXN_DETAIL_total_charge, TXN_DETAIL_paypal_email,LIST_TYPE,
            TXN_DETAIL_amount_deposited,TXN_DETAIL_total_amount,TXN_DETAIL_earnings};

    public static final String TABLE_CREATE_ROUTES = "CREATE TABLE "
            + TABLE_NAME + "(" + PAYMENT_ID + " INTEGER NOT NULL primary key, "

            +BUYER_ID + " INTEGER, "
            +LIST_TYPE+" TEXT, "
            +SELLER_ID + " INTEGER, "
            +TITLE+" TEXT, "
            +CREATED_AT + " BIGINT, "
            +STATUS + " TEXT, "
            +TXN_TYPE + " TEXT, "
            +TXN_DETAIL_RECIPIENT_USERNAME + " TEXT, "
            +TXN_DETAIL_RECIPIENT_NAME + " TEXT, "
            +TXN_DETAIL_task_id + " INTEGER, "
            +TXN_DETAIL_task_title + " TEXT, "
            +TXN_DETAIL_task_cost + " FLOAT, "
            +TXN_DETAIL_fee + " FLOAT, "
            +TXN_DETAIL_total_charge + " FLOAT, "
            +TXN_DETAIL_paypal_email + " TEXT, "
            +TXN_DETAIL_amount_deposited + " FLOAT, "
            +TXN_DETAIL_total_amount + " FLOAT, "
            +TXN_DETAIL_earnings + " FLOAT);";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getColumnID() {
        return PAYMENT_ID;
    }

    @Override
    public String getCreateTableCommand() {
        return TABLE_CREATE_ROUTES;
    }

    @Override
    public String[] getColumns() {
        return COLUMNS;
    }
}
