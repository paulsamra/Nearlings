package swipe.android.nearlings;

import swipe.android.DatabaseHelpers.BalanceDatabaseHelper;
import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.nearlings.MessagesSync.BalanceRequest;
import swipe.android.nearlings.MessagesSync.MessagesRequest;
import swipe.android.nearlings.json.UserHistory.PaymentHistory;
import swipe.android.nearlings.viewAdapters.BalanceViewAdapter;
import swipe.android.nearlings.viewAdapters.MessagesViewAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import android.view.View.OnClickListener;
import android.widget.ProgressBar;

public class FragmentProcessedPaymentList extends NearlingsSwipeToRefreshFragment {

    String MESSAGES_START_FLAG = FragmentProcessedPaymentList.class.getCanonicalName()
            + "_MESSAGES_START_FLAG";
    String MESSAGES_FINISH_FLAG = FragmentProcessedPaymentList.class.getCanonicalName()
            + "_MESSAGES_FINISH_FLAG";

    @Override
    public CursorLoader generateCursorLoader() {
        String selectionClause = BalanceDatabaseHelper.LIST_TYPE + " = ?";
        String[] mSelectionArgs = { type };
        CursorLoader cursorLoader = new CursorLoader(
                this.getActivity(),
                NearlingsContentProvider
                        .contentURIbyTableName(BalanceDatabaseHelper.TABLE_NAME),
                BalanceDatabaseHelper.COLUMNS, selectionClause, mSelectionArgs,
                BalanceDatabaseHelper.CREATED_AT + " DESC");

        return cursorLoader;

    }

    @Override
    public void reloadData() {
        // onRefresh();
        reloadAdapter();
    }

    View footerView;
String type;
    Cursor c;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.pull_to_refresh_single_list,
                container, false);

        type = getArguments().getString("type");
        Log.d("TYPE", type);
        ((ActionBarActivity)this.getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((ActionBarActivity)this.getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)this.getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((ActionBarActivity)this.getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((ActionBarActivity)this.getActivity()).getSupportActionBar().setTitle("Messages");
        swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        swipeView.setColorScheme(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_green_light);
        lView = (ListView) rootView.findViewById(R.id.list);

        swipeView.setOnRefreshListener(this);
        lView.setOnItemClickListener(this);
        lView.setOnScrollListener(this);
        setUpFooter(inflater);
        reloadAdapter();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public String syncStartedFlag() {
        return MESSAGES_START_FLAG;
    }

    @Override
    public String syncFinishedFlag() {
        return MESSAGES_FINISH_FLAG;
    }

    @Override
    public void setSourceRequestHelper() {
        helpers.add(new BalanceRequest(this.getActivity()));
    }

    @Override
    protected int setNumElements() {
        return mAdapter.getCount();
    }

    @Override
    public void onResume() {
        super.onResume();

        onRefresh();
    }
    int index = 0;
    @Override
    public void reloadAdapter() {

        index = lView.getFirstVisiblePosition();
        getLoaderManager().initLoader(0, null, this);
        c = generateCursor();

        this.mAdapter = new BalanceViewAdapter(this.getActivity(), c);
        lView.setAdapter(mAdapter);
        mAdapter.changeCursor(c);

        mAdapter.notifyDataSetChanged();

        c.close();
        Runnable run = new Runnable(){
            public void run(){
                //reload content
                   /*arraylist.clear();
                   arraylist.addAll(db.readAll());*/
                mAdapter.notifyDataSetChanged();
                lView.invalidateViews();
                // lView.refreshDrawableState();
            }
        };
        this.getActivity().runOnUiThread(run);
        //lView.setAdapter(mAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        this.getActivity().onBackPressed();

        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Cursor c = generateCursor();
        c.moveToPosition(position);
        String txnType = c.getString(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_TYPE));
        String message;
        if(txnType.equals("withdrawal")){
            message = withdrawalClick(c);
        }else{
            message = needClick(c);
        }
        String title = c.getString(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TITLE));
        new AlertDialog.Builder(this.getActivity())
                .setTitle( title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.cancel();
                    }
                })
                .show();
    }



    private String withdrawalClick(Cursor c){

        long createdAt = c.getLong(c.getColumnIndexOrThrow(BalanceDatabaseHelper.CREATED_AT));

        String paypal_email = c.getString(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_paypal_email));
        float total_amount  = c.getLong(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_total_amount));
        float fee  = c.getLong(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_fee));
        float amount_deposited  = c.getLong(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_total_amount));
        String s = "Created At: " + FieldsParsingUtils.getTime(this.getActivity(), createdAt) + "\n" +
                "Email: " + paypal_email + "\n" +
                "Withdraw Amount: " + total_amount + "\n" +
                "Fee: " + fee + "\n" +
                "Total: " + amount_deposited+"\n";

        int id = c.getInt(c.getColumnIndexOrThrow(BalanceDatabaseHelper.PAYMENT_ID));

        return s;



    }

    private String needClick(Cursor c){
        long createdAt = c.getLong(c.getColumnIndexOrThrow(BalanceDatabaseHelper.CREATED_AT));

        String username = c.getString(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_RECIPIENT_USERNAME));

        float earning  = c.getLong(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_earnings));
        String title  = c.getString(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TITLE));
        String recipient_name  = c.getString(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_RECIPIENT_NAME));

        float total_charge  = c.getLong(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_total_charge));
        float fee  = c.getLong(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_fee));
        float task_cost  = c.getLong(c.getColumnIndexOrThrow(BalanceDatabaseHelper.TXN_DETAIL_task_cost));

        String s =
                "Created At: " + FieldsParsingUtils.getTime(this.getActivity(), createdAt) + "\n" +
                        "Recipient Username: " + username + "\n" +
                        "Recipient Name: " + recipient_name + "\n" +
                        "Task Title: " + title + "\n" ;
        if(earning <= 0) {
            s += "Task Cost: " + task_cost+ "\n" +

                    "Task Fee: " + fee + "\n" +

                    "Total Charge: " + total_charge + "\n";
        }else{
            s += "Total Earnings: " + earning + "\n";
        }
        int id = c.getInt(c.getColumnIndexOrThrow(BalanceDatabaseHelper.PAYMENT_ID));

        return s;
    }


}