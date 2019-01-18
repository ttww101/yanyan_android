package com.ccaaii.shenghuotong.discover;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccaaii.base.BaseFragment;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.widgets.ListViewForScrollView;
import com.ccaaii.shenghuotong.CcaaiiApp;
import com.ccaaii.shenghuotong.CcaaiiConstants;
import com.ccaaii.shenghuotong.R;
import com.ccaaii.shenghuotong.bean.DocumentBean;
import com.ccaaii.shenghuotong.discover.tools.ChangeMoneyActivity;
import com.ccaaii.shenghuotong.discover.tools.SearchIdsActivity;
import com.ccaaii.shenghuotong.discover.tools.SearchMobileActivity;
import com.ccaaii.shenghuotong.document.DocumentDAO;
import com.ccaaii.shenghuotong.document.DocumentDetailActivity;
import com.ccaaii.shenghuotong.document.DocumentUtils;
import com.ccaaii.shenghuotong.document.NewCategoryActivity;
import com.ccaaii.shenghuotong.web.WebShowActivity;

import java.util.List;

/**
 */
public class DiscoverFragment extends BaseFragment{
    public static final String TAG = "[CCAAII]DiscoverFragment";

    private View mCategoryView0;
    private View mCategoryView1;
    private View mCategoryView2;
    private View mCategoryView3;
    private View mCategoryView4;
    private View mCategoryView5;
    private View mCategoryView6;
    private View mCategoryView7;

    private ListViewForScrollView mNewDocumentListView;

    private DocumentAdapter mNewDocumentAdapter;

    public DiscoverFragment(){
        MarketLog.i(TAG, "DiscoverFragment instance");
    }

    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onAttach...");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onCreate...");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onCreateView...");
        }
        View v = inflater.inflate(R.layout.discover_fragment_layout, container, false);
        buildLayout(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onViewCreated...");
        }

        refreshNewDocument();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onResume...");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onStart...");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onPause...");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onDestroy...");
        }

        if (mNewDocumentAdapter != null)
        {
            mNewDocumentAdapter = null;
        }


    }

    private void buildLayout(View v) {



        mCategoryView0 = v.findViewById(R.id.category_view_0);
        mCategoryView1 = v.findViewById(R.id.category_view_1);
        mCategoryView2 = v.findViewById(R.id.category_view_2);
        mCategoryView3 = v.findViewById(R.id.category_view_3);
        mCategoryView4 = v.findViewById(R.id.category_view_4);
        mCategoryView5 = v.findViewById(R.id.category_view_5);
        mCategoryView6 = v.findViewById(R.id.category_view_6);
        mCategoryView7 = v.findViewById(R.id.category_view_7);


        mCategoryView0.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switchCategoryScreen(DocumentBean.CATEGORY_COMMON);
            }
        });
        mCategoryView1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switchCategoryScreen(DocumentBean.CATEGORY_TIPS);
            }
        });
        mCategoryView2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switchCategoryScreen(DocumentBean.CATEGORY_CHILD);
            }
        });
        mCategoryView3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switchCategoryScreen(DocumentBean.CATEGORY_HEALTH);
            }
        });
        mCategoryView4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switchCategoryScreen(DocumentBean.CATEGORY_SPORT);
            }
        });
        mCategoryView5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switchCategoryScreen(DocumentBean.CATEGORY_FACIAL);
            }
        });
        mCategoryView6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switchCategoryScreen(DocumentBean.CATEGORY_AID);
            }
        });
        mCategoryView7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switchCategoryScreen(DocumentBean.CATEGORY_BUSINESS);
            }
        });

        mNewDocumentListView = (ListViewForScrollView) v.findViewById(R.id.new_document_listview);
        mNewDocumentListView.setFocusable(false);
        mNewDocumentListView.setOnItemClickListener(mHotItemClickListener);

    }

    private void switchCategoryScreen(int categoryId){
        Intent intent = new Intent(getActivity(), NewCategoryActivity.class);
        intent.putExtra(CcaaiiConstants.EXTRA_CATEGORY_ID, categoryId);
        getActivity().startActivity(intent);
    }

    private AdapterView.OnItemClickListener mHotItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                if (mNewDocumentAdapter != null){
                    DocumentBean speech = mNewDocumentAdapter.getItem(position);
                    switchToDetail(speech.getId());
                }
            } catch (Exception ex){
                MarketLog.e(TAG, "mHotItemClickListener failed, ex : " + ex.getLocalizedMessage());
            }
        }
    };

    private void refreshNewDocument(){
        if (mNewDocumentListView.getChildCount() <= 0){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View footerView = inflater.inflate(R.layout.discover_footer_view, null);
            TextView seeMoreView = (TextView) footerView.findViewById(R.id.discover_see_more_view);
            seeMoreView.setText(R.string.see_more_document);
            seeMoreView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NewCategoryActivity.class);
                    getActivity().startActivity(intent);
                }
            });
            mNewDocumentListView.addFooterView(footerView);
        }
        List<DocumentBean> hotSpeechList = DocumentDAO.getNewDocumentList(CcaaiiApp.getCcaaiiContext());
        if (mNewDocumentAdapter != null){
            mNewDocumentAdapter.notifyDocumentList(hotSpeechList);
        } else {
            mNewDocumentAdapter = new DocumentAdapter(getActivity(), hotSpeechList);
            mNewDocumentListView.setAdapter(mNewDocumentAdapter);
        }

    }

    private void switchToDetail(String objectId){
        MarketLog.i(TAG, "switch to detail screen objectId = " + objectId);
        Intent intent = new Intent(getActivity(), DocumentDetailActivity.class);
        intent.putExtra(CcaaiiConstants.EXTRA_DOCUMENT_ID, objectId);
        getActivity().startActivity(intent);
    }

    private class ViewHolder{
        public ImageView colorView;
        public TextView categoryView;
        public TextView countView;
        public TextView nameView;
        public TextView contentView;

    }

    private class DocumentAdapter extends BaseAdapter {

        private Context mContext;
        private List<DocumentBean> mDocumentList;
        private LayoutInflater mInflater;

        public DocumentAdapter(Context context, List<DocumentBean> list){
            this.mContext = context;
            this.mDocumentList = list;
            mInflater = LayoutInflater.from(context);
        }

        public void notifyDocumentList(List<DocumentBean> list){
            this.mDocumentList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDocumentList != null ? mDocumentList.size() : 0;
        }

        @Override
        public DocumentBean getItem(int position) {
            return mDocumentList != null && position < mDocumentList.size() ? mDocumentList.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null){
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.document_item_layout, null);
                viewHolder.colorView = (ImageView) convertView.findViewById(R.id.color_imageview);
                viewHolder.categoryView = (TextView) convertView.findViewById(R.id.category_textview);
                viewHolder.countView = (TextView) convertView.findViewById(R.id.count_textview);
                viewHolder.nameView = (TextView) convertView.findViewById(R.id.name_textview);
                viewHolder.contentView = (TextView) convertView.findViewById(R.id.content_textview);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            DocumentBean document = mDocumentList.get(position);
            if (document == null){
                return null;
            }

            viewHolder.colorView.setImageResource(DocumentUtils.getCategoryColorImg(document.getCategory()));
            viewHolder.categoryView.setText(DocumentUtils.getCategoryStr(mContext, document.getCategory()));
            viewHolder.countView.setText(DocumentUtils.getPlayCountStr(mContext, document.getPlaycount()));
            viewHolder.nameView.setText(document.getName());
            viewHolder.contentView.setText(document.getDescription());

            return convertView;
        }
    }

}
