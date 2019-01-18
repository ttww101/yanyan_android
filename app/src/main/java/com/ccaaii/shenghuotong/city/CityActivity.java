package com.ccaaii.shenghuotong.city;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.ccaaii.shenghuotong.CcaaiiApp;
import com.ccaaii.base.BaseActivity;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.widgets.GridViewForScrollView;
import com.ccaaii.shenghuotong.R;
import com.ccaaii.shenghuotong.bean.City;
import com.ccaaii.shenghuotong.provider.CcaaiiProvider;
import com.ccaaii.shenghuotong.provider.UriHelper;
import com.ccaaii.shenghuotong.weather.WeatherDAO;

import java.util.List;

/**
 */
public class CityActivity extends BaseActivity{

    private static final String TAG = "[CCAAII]CityActivity";

    private EditText mSearchEditText;
    private ImageButton mClearButton;
    private Button mSearchButton;
    private TextView mLocationCityView;
    private View mLocationLayout;
    private GridViewForScrollView mHotCityGridView;
    private ListView mSearchCityListView;


    private SearchCityAdapter mSearchCityAdapter;
    private HotCityAdapter mHotCityAdapter;
    private List<City> mHotCityList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.city_layout, true);

        buildLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            Cursor cursor = mSearchCityAdapter.getCursor();
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
                cursor = null;
            }
        } catch (Exception ex){

        }

        if (mHotCityAdapter != null){
            mHotCityAdapter = null;
        }

        if (mSearchCityAdapter != null){
            mSearchCityAdapter = null;
        }
    }

    private void buildLayout(){
        this.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSearchEditText = (EditText) this.findViewById(R.id.search_edit_text);
        mClearButton = (ImageButton) this.findViewById(R.id.button_clear);
        mSearchButton = (Button) this.findViewById(R.id.button_search);
        mLocationLayout = this.findViewById(R.id.location_city_layout);
        mLocationCityView = (TextView) this.findViewById(R.id.location_city_name_view);
        mHotCityGridView = (GridViewForScrollView) this.findViewById(R.id.hot_city_gridview);
        mSearchCityListView = (ListView) this.findViewById(R.id.search_listview);

        mSearchCityListView.setVisibility(View.GONE);
        mClearButton.setVisibility(View.GONE);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchEditText.getEditableText().clear();
                hideKeyboard();
            }
        });
        mLocationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    City city = CityDAO.getCityByCityAndProv(CityActivity.this, CcaaiiApp.getProvince(), CcaaiiApp.getCity());
                    if (city != null){
                        WeatherDAO.saveWeather(CityActivity.this, city, "", true);
                        finish();
                    }

                } catch (Exception ex){

                }
            }
        });
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String edit = editable.toString();
                if (!TextUtils.isEmpty(edit)){
                    mClearButton.setVisibility(View.VISIBLE);
                    mSearchCityListView.setVisibility(View.VISIBLE);
                    startSearchQuery(edit);
                } else {
                    mClearButton.setVisibility(View.GONE);
                    mSearchCityListView.setVisibility(View.GONE);
                    if (mSearchCityAdapter != null){
                        mSearchCityAdapter.changeCursor(null);
                    }
                }
            }
        });

        mHotCityGridView.setOnItemClickListener(mOnHotItemClickListener);
        mHotCityGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hideKeyboard();
                }
                return false;
            }
        });

        LayoutInflater inflater = LayoutInflater.from(this);
        View emptyView = inflater.inflate(R.layout.empty_layout, null);
        TextView emptyTextView = (TextView) emptyView.findViewById(R.id.empty_textview);
        emptyTextView.setText(R.string.no_search_result);
        mSearchCityListView.setEmptyView(emptyView);
        mSearchCityListView.setOnItemClickListener(mOnSearchItemClickListener);
        mSearchCityListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hideKeyboard();
                }
                return false;
            }
        });
    }

    private void initView(){

        City city = CityDAO.getCityByCityAndProv(this, CcaaiiApp.getProvince(), CcaaiiApp.getCity());
        if (city != null){
            mLocationCityView.setText(city.city + " - " + city.prov);
        } else {
            mLocationCityView.setText(R.string.location_failed);
        }

        if (mHotCityAdapter == null){
            mHotCityList = CityUtils.getHotCityList();
            mHotCityAdapter = new HotCityAdapter(this, mHotCityList);
            mHotCityGridView.setAdapter(mHotCityAdapter);
        }


        if (mSearchCityAdapter == null){
            mSearchCityAdapter = new SearchCityAdapter(this);
            mSearchCityListView.setAdapter(mSearchCityAdapter);
        }

       if (TextUtils.isEmpty(mSearchEditText.getEditableText().toString())){
           mSearchCityListView.setVisibility(View.GONE);
       } else {
           mSearchCityListView.setVisibility(View.VISIBLE);
           startSearchQuery(mSearchEditText.getEditableText().toString());
       }

    }

    private class ViewHolder {
        public TextView cityView;
    }

    private class HotCityAdapter extends BaseAdapter{

        private Context mContext;
        private List<City> mCityList;
        private LayoutInflater mInflater;

        public HotCityAdapter(Context context, List<City> list){
            this.mContext = context;
            this.mCityList = list;
            this.mInflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return mCityList != null ? mCityList.size() : 0;
        }

        @Override
        public Object getItem(int i) {
            return mCityList != null && i < mCityList.size() ? mCityList.get(i) : null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null){
                view = mInflater.inflate(R.layout.hot_city_item_layout, null);
                holder = new ViewHolder();
                holder.cityView = (TextView) view.findViewById(R.id.city_name_view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            City city = mCityList.get(i);
            if (city == null){
                return null;
            }
            holder.cityView.setText(city.city);
            return view;
        }
    }

    private void startSearchQuery(String search){
        if(LogLevel.DEV){
            DevLog.d(TAG, "startSearchQuery()...search str = " + search);
        }
        if(mSearchCityAdapter == null){
            if(LogLevel.DEV){
                DevLog.e(TAG, "startSearchQuery() failed, mSearchCityAdapter is null.");
            }
            return;
        }

        mSearchCityAdapter.getFilter().filter(search);
    }

    private class SearchCityAdapter extends ResourceCursorAdapter{

        private Context mContext;
        public SearchCityAdapter(Context context) {
            super(context, R.layout.search_city_item_layout, null, false);
            mContext = context;
        }

        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
            String search = constraint.toString();
            Uri uri = UriHelper.getUri(CcaaiiProvider.CITY_TABLE);
            return mContext.getContentResolver().query(uri, CityProjection.SUMMARY_PROJECTION, CityProjection.QUERY_CITY_PROJECTION, new String[]{search, search, search, search, search, search}, null);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = super.newView(context, cursor, parent);
            final ViewHolder cache = new ViewHolder();
            cache.cityView = (TextView) view.findViewById(R.id.city_name_view);
            view.setTag(cache);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            if (cursor == null || cursor.isClosed()) {
                return;
            }
            final ViewHolder cache = (ViewHolder) view.getTag();
            final City city = CityDAO.readCityByCursor(cursor);
            if (city == null) {
                return;
            }
            cache.cityView.setText(city.city + " - " + city.prov);
        }

        @Override
        public CharSequence convertToString(Cursor cursor) {
            try {
                return cursor.getString(CityProjection.CITY_INDEX);
            } catch (Exception ex){
                return "";
            }

        }
    }

    private AdapterView.OnItemClickListener mOnHotItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            try {
                City city = mHotCityList.get(position);
                MarketLog.w(TAG, "mOnHotItemClickListener city = " + city.city);
                WeatherDAO.saveWeather(CityActivity.this, city, "", true);
                finish();
            } catch (Exception ex){

            }
        }
    };

    private AdapterView.OnItemClickListener mOnSearchItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            try {
                Cursor cursor = (Cursor) mSearchCityAdapter.getItem(position - 1);
                City city = CityDAO.readCityByCursor(cursor);
                MarketLog.w(TAG, "mOnHotItemClickListener city = " + city.city);
                WeatherDAO.saveWeather(CityActivity.this, city, "", true);
                finish();
            } catch (Exception ex){

            }
        }
    };

    private void hideKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
