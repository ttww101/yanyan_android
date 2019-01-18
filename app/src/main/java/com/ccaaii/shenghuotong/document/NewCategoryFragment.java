package com.ccaaii.shenghuotong.document;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.ccaaii.base.BaseFragment;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.SwpipeListViewOnScrollListener;
import com.ccaaii.base.utils.widgets.LoadMoreListView;
import com.ccaaii.shenghuotong.CcaaiiConstants;
import com.ccaaii.shenghuotong.R;
import com.ccaaii.shenghuotong.bean.DocumentBean;
import com.ccaaii.shenghuotong.provider.CcaaiiDataStore;
import com.ccaaii.shenghuotong.provider.CcaaiiProvider;
import com.ccaaii.shenghuotong.provider.UriHelper;

/**
 */
public class NewCategoryFragment extends BaseFragment {

    public static final String TAG = "[CCAAII]NewCategoryFragment";

    private static final String CATEGORY_TYPE = "CCAAIICategoryType";

    private static final int DEFAULT_LIMIT_COUNT = 10;

    private static final int QUERY_COMPLETE = 0;
    private static final int REFRESH_COMPLETE = 1;
    private static final int LOAD_COMPLETE = 2;
    private static final int LOAD_COMPLETE_ALL = 3;

    private static final int QUERY_TOKEN = 1002;

    private int mCategoryId;

    private SwipeRefreshLayout mRefreshLayout;
    private ListView mDocumentListView;

    private QueryDocumentHandler mQueryDocumentHandler;
    private DocumentAdapter mDocumentAdapter;
    private int mCurrentIndex = 0;

    private boolean mIsVisible;
    private boolean mIsPrepared;
    private boolean mHasLoaded;

    private String getTAG(){
        return TAG + mCategoryId;
    }

    private Handler mStopLoadHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg == null || getActivity() == null || getActivity().isFinishing()){
                return;
            }
            if (msg.what == QUERY_COMPLETE){
                MarketLog.w(getTAG(), "QUERY_COMPLETE");
            } else if (msg.what == REFRESH_COMPLETE){
                MarketLog.w(getTAG(), "REFRESH_COMPLETE");
                mRefreshLayout.setRefreshing(false);
                updateQuery();
            } else if (msg.what == LOAD_COMPLETE){
                MarketLog.w(getTAG(), "LOAD_COMPLETE");
                updateQuery();
            } else if (msg.what == LOAD_COMPLETE_ALL){
                MarketLog.w(getTAG(), "LOAD_COMPLETE_ALL");
                updateQuery();
            }
        }

    };

    public NewCategoryFragment(){
        MarketLog.i(getTAG(), "NewCategoryFragment instance");

    }

    public static NewCategoryFragment newInstance(int category) {
        NewCategoryFragment fragment = new NewCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CATEGORY_TYPE, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LogLevel.DEV) {
            DevLog.i(getTAG(), "onCreate...");
        }
        Bundle args = getArguments();
        mCategoryId = args != null ? args.getInt(CATEGORY_TYPE) : -1;
        if (mCategoryId < 0){
            mCategoryId = DocumentBean.CATEGORY_COMMON;
        }
        MarketLog.w(getTAG(), "mCategoryId = " + mCategoryId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (LogLevel.DEV) {
            DevLog.i(getTAG(), "onCreateView...");
        }
        View v = inflater.inflate(R.layout.category_fragment_layout, container, false);
        buildLayout(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (LogLevel.DEV) {
            DevLog.i(getTAG(), "onViewCreated...");
        }
        mIsPrepared = true;
        startQuery();
        loadData();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (LogLevel.DEV) {
            DevLog.i(getTAG(), "setUserVisibleHint...isVisibleToUser = " + isVisibleToUser);
        }
        if(getUserVisibleHint()) {
            mIsVisible = true;
            loadData();
        } else {
            mIsVisible = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LogLevel.DEV) {
            DevLog.i(getTAG(), "onResume...");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (LogLevel.DEV) {
            DevLog.i(getTAG(), "onStart...");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (LogLevel.DEV) {
            DevLog.i(getTAG(), "onPause...");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LogLevel.DEV) {
            DevLog.i(getTAG(), "onDestroy...");
        }

        if (mStopLoadHandler != null){
            mStopLoadHandler = null;
        }

        if (mDocumentAdapter != null){
            try {
                Cursor cursor = mDocumentAdapter.getCursor();
                if (cursor != null && !cursor.isClosed()){
                    cursor.close();
                    cursor = null;
                }
            } catch (Exception ex){

            }
            mDocumentAdapter = null;

        }

    }

    private void loadData(){
        if(!mIsVisible || !mIsPrepared || mHasLoaded) {
            return;
        }
        mHasLoaded = true;
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (mDocumentAdapter == null || mDocumentAdapter.getCount() <= 0){
                    mRefreshLayout.setRefreshing(true);
                }
                refreshQuery();
            }
        });

    }

    private void buildLayout(View v) {
        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.category_refresh_layout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshQuery();
            }
        });
        mRefreshLayout.setColorSchemeResources(R.color.style_color, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mRefreshLayout.setDistanceToTriggerSync(200);
        mRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white_color);
        mRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);

        mDocumentListView = (ListView) v.findViewById(R.id.category_listview);
        mDocumentListView.setOnItemClickListener(mOnItemClickListener);
        mDocumentListView.setOnScrollListener(new SwpipeListViewOnScrollListener(mRefreshLayout));
    }

    private void refreshQuery(){
        MarketLog.d(getTAG(), "refershQuery...");
        mStopLoadHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            if (mDocumentAdapter == null){
                return;
            }
            try {
                Cursor cursor = (Cursor) mDocumentAdapter.getItem(position);
                DocumentBean speech = DocumentDAO.readDocumentCursor(cursor);
                Intent intent = new Intent(getActivity(), DocumentDetailActivity.class);
                intent.putExtra(CcaaiiConstants.EXTRA_DOCUMENT_ID, speech.getId());
                getActivity().startActivity(intent);
            } catch (Exception ex){

            }
        }
    };

    private void startQuery(){
        if(LogLevel.DEV){
            DevLog.d(getTAG(), "startQuery...");
        }
        if (mDocumentAdapter != null) {
            mDocumentAdapter = null;
        }

        if (getActivity().isFinishing()) {
            return;
        }
        mDocumentAdapter = new DocumentAdapter(getActivity());
        if (mDocumentListView == null || mDocumentAdapter == null){
            return;
        }

        mDocumentListView.setAdapter(mDocumentAdapter);
        startEmptyQuery();
    }

    private void updateQuery(){
        if(LogLevel.DEV){
            DevLog.d(getTAG(), "updateQuery...");
        }
        if(mDocumentAdapter == null){
            mDocumentAdapter = new DocumentAdapter(getActivity());
            mDocumentListView.setAdapter(mDocumentAdapter);
        }
        startEmptyQuery();
    }

    public void startEmptyQuery() {

        String sortkey = DocumentProjection.SORT_ORDER;
        Uri uri = UriHelper.getUri(CcaaiiProvider.DOCUMENT_TABLE);
        String[] projection = DocumentProjection.SUMMARY_PROJECTION;
        String where = CcaaiiDataStore.DocumentTable.CATEGORY + " = '" + mCategoryId + "'";

        if (uri == null || projection == null) {
            mStopLoadHandler.sendEmptyMessage(QUERY_COMPLETE);
            return;
        }

        if (mQueryDocumentHandler == null) {
            if (LogLevel.DEV) {
                DevLog.e(getTAG(), "startEmptyQuery(), mQueryDocumentHandler is null.");
            }
            mQueryDocumentHandler = new QueryDocumentHandler(getActivity());
        }
        mQueryDocumentHandler.cancelOperation(QUERY_TOKEN);
        mQueryDocumentHandler.startQuery(QUERY_TOKEN, null, uri, projection, where, null, sortkey);
    }



    private final class QueryDocumentHandler extends AsyncQueryHandler {

        public QueryDocumentHandler(Context context) {
            super(context.getContentResolver());
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor == null || cursor.isClosed() || !cursor.moveToFirst()) {
                mStopLoadHandler.sendEmptyMessage(QUERY_COMPLETE);
                return;
            }

            if (!getActivity().isFinishing()) {
                mStopLoadHandler.sendEmptyMessage(QUERY_COMPLETE);
                mDocumentAdapter.changeCursor(cursor);
            } else {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }
    }

    private class DocumentItemView {
        public TextView countView;
        public TextView nameView;
        public TextView contentView;
    }

    protected class DocumentAdapter extends ResourceCursorAdapter {

        private Context mContext;

        public DocumentAdapter(Context context) {
            super(context, R.layout.document_item_layout_2, null);
            this.mContext = context;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = super.newView(context, cursor, parent);
            final DocumentItemView cache = new DocumentItemView();
            cache.countView = (TextView) view.findViewById(R.id.count_textview);
            cache.nameView = (TextView) view.findViewById(R.id.name_textview);
            cache.contentView = (TextView) view.findViewById(R.id.content_textview);
            view.setTag(cache);

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            if (cursor == null || cursor.isClosed()) {
                return;
            }
            final DocumentItemView cache = (DocumentItemView) view.getTag();
            final DocumentBean document = DocumentDAO.readDocumentCursor(cursor);
            if (document == null) {
                return;
            }
            cache.countView.setText(DocumentUtils.getPlayCountStr(mContext, document.getPlaycount()));
            cache.nameView.setText(document.getName());
            cache.contentView.setText(document.getDescription());

        }

    }
}
