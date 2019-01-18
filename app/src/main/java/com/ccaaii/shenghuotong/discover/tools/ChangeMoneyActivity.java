package com.ccaaii.shenghuotong.discover.tools;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ccaaii.base.BaseActivity;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.utils.AnimationUtils;
import com.ccaaii.base.utils.DialogUtils;
import com.ccaaii.base.utils.OnProgressDialogCancelListener;
import com.ccaaii.base.utils.widgets.GridViewForScrollView;
import com.ccaaii.shenghuotong.CcaaiiApp;
import com.ccaaii.shenghuotong.R;
import com.ccaaii.shenghuotong.api.ApiUtils;
import com.ccaaii.shenghuotong.api.IResponseListener;
import com.ccaaii.shenghuotong.bean.Finance;
import com.ccaaii.shenghuotong.bean.Weather;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class ChangeMoneyActivity extends BaseActivity {

    private static final String TAG = ChangeMoneyActivity.class.getSimpleName();

    private static final String[] HOT_MONEY_UNIT = new String[]{
            "CNY 人民币", "USD 美元", "EUR 欧元", "GBP 英镑",
            "AUD 澳元", "JPY 日元", "KRW 韩元", "CAD 加拿大元",
            "SGD 新加坡元", "RUB 俄罗斯卢布", "HKD 港币", "TWD 新台币"
    };

    private static final String[] MONEY_UNIT = new String[]{
            "AED 阿联酋迪拉姆",
            "AFN 阿富汗尼",
            "ALL 阿尔巴尼亚列克",
            "AMD 亚美尼亚德拉姆",
            "ANG 列斯荷兰盾",
            "AOA 安哥拉宽扎",
            "ARS 阿根廷比索",
            "AUD 澳元",
            "AWG 阿鲁巴弗罗林",
            "AZN 阿塞拜疆马纳特",
            "BAM 波黑兑换马克",
            "BBD 巴巴多斯元",
            "BDT 孟加拉塔卡",
            "BGN 保加利亚列瓦",
            "BHD 巴林第纳尔",
            "BIF 布隆迪法郎",
            "BMD 百慕大元",
            "BND 文莱元",
            "BOB 玻利维亚诺",
            "BRL 巴西雷亚尔",
            "BSD 巴哈马元",
            "BTN 不丹努扎姆",
            "BWP 博茨瓦纳普拉",
            "BYR 白俄罗斯卢布",
            "BZD 伯利兹元",
            "CAD 加拿大元",
            "CDF 刚果法郎",
            "CHF 瑞士法郎",
            "CLF 智利比索",
            "CLP 智利比索",
            "CNH 离岸人民币",
            "CNY 人民币",
            "COP 哥伦比亚比索",
            "CRC 哥斯达黎加科朗",
            "CUP 古巴比索",
            "CVE 佛得角埃斯库多",
            "CZK 捷克克朗",
            "DJF 吉布提法郎",
            "DKK 丹麦克朗",
            "DOP 多米尼加比索",
            "DZD 阿尔及利亚第纳尔",
            "EGP 埃及镑",
            "ERN 厄立特里亚纳克法",
            "ETB 埃塞俄比亚比尔",
            "EUR 欧元",
            "FJD 斐济元",
            "FKP 福克兰镑",
            "GBP 英镑",
            "GEL 格鲁吉亚拉里",
            "GHS 加纳的塞地",
            "GIP 直布罗陀镑",
            "GMD 冈比亚达拉西",
            "GNF 几内亚法郎",
            "GTQ 危地马拉格查尔",
            "GYD 圭亚那元",
            "HKD 港币",
            "HNL 洪都拉斯伦皮拉",
            "HRK 克罗地亚库纳",
            "HTG 海地古德",
            "HUF 匈牙利福林",
            "IDR 印度尼西亚盾",
            "ILS 以色列新谢克尔",
            "INR 印度卢比",
            "IQD 伊拉克第纳尔",
            "IRR 伊朗里亚尔",
            "ISK 冰岛克朗",
            "JMD 牙买加元",
            "JOD 约旦第纳尔",
            "JPY 日元",
            "KES 肯尼亚先令",
            "KGS 吉尔吉斯斯坦索姆",
            "KHR 柬埔寨瑞尔",
            "KMF 科摩罗法郎",
            "KPW 朝鲜元",
            "KRW 韩元",
            "KWD 科威特第纳尔",
            "KYD 开曼群岛元",
            "KZT 哈萨克斯坦腾格",
            "LAK 老挝基普",
            "LBP 黎巴嫩镑",
            "LKR 斯里兰卡卢比",
            "LRD 利比里亚元",
            "LSL 莱索托洛蒂",
            "LTL 立陶宛立特",
            "LVL 拉脱维亚拉特",
            "LYD 利比亚第纳尔",
            "MAD 摩洛哥迪拉姆",
            "MDL 摩尔多瓦列伊",
            "MGA 马达加斯加阿里亚里",
            "MKD 马其顿代纳尔",
            "MMK 缅甸元",
            "MNT 蒙古图格里克",
            "MOP 澳门元",
            "MRO 毛里塔尼亚乌吉亚",
            "MUR 毛里求斯卢比",
            "MVR 马尔代夫拉菲亚",
            "MWK 马拉维克瓦查",
            "MXN 墨西哥比索",
            "MYR 马元",
            "MZN 梅蒂卡尔",
            "NAD 纳米比亚元",
            "NGN 尼日利亚奈拉",
            "NIO 尼加拉瓜科多巴",
            "NOK 挪威克朗",
            "NPR 尼泊尔卢比",
            "NZD 新西兰元",
            "OMR 阿曼里亚尔",
            "PAB 巴拿马巴波亚",
            "PEN 秘鲁新索尔",
            "PGK 巴布亚新几内亚基那",
            "PHP 菲律宾比索",
            "PKR 巴基斯坦卢比",
            "PLN 波兰兹罗提",
            "PYG 巴拉圭瓜拉尼",
            "QAR 卡塔尔里亚尔",
            "RON 罗马尼亚列伊",
            "RSD 塞尔维亚第纳尔",
            "RUB 俄罗斯卢布",
            "RWF 卢旺达法郎",
            "SAR 沙特里亚尔",
            "SBD 所罗门群岛元",
            "SCR 塞舌尔卢比",
            "SDG 苏丹人磅",
            "SEK 瑞典克朗",
            "SGD 新加坡元",
            "SHP 圣赫勒拿群岛磅",
            "SLL 塞拉利昂利昂",
            "SOS 索马里先令",
            "SRD 苏里南元",
            "STD 圣多美和普林西比多布拉",
            "SVC 萨尔瓦多科朗",
            "SYP 叙利亚镑",
            "SZL 斯威士兰里兰吉尼",
            "THB 泰铢",
            "TJS 塔吉克斯坦索莫尼",
            "TMT 土库曼斯坦马纳特",
            "TND 突尼斯第纳尔",
            "TOP 汤加潘加",
            "TRY 新土耳其里拉",
            "TTD 特立尼达多巴哥元",
            "TWD 新台币",
            "TZS 坦桑尼亚先令",
            "UAH 乌克兰格里夫纳",
            "UGX 乌干达先令",
            "USD 美元",
            "UYU 乌拉圭比索",
            "UZS 乌兹别克斯坦苏姆",
            "VEF 委内瑞拉玻利瓦尔",
            "VND 越南盾",
            "VUV 瓦努阿图瓦图",
            "WST 萨摩亚塔拉",
            "XAF 中非金融合作法郎",
            "XAU 金价盎司",
            "XCD 东加勒比元",
            "XDR 特别提款权（国际货币基金）",
            "XOF 非洲金融共同体法郎（西非法郎）",
            "XPF 太平洋法郎",
            "YER 也门里亚尔",
            "ZAR 南非兰特",
            "ZMW 赞比亚克瓦查",
            "ZWL 津巴布韦币"
    };

    private View mFromView;
    private TextView mFromTextView;
    private View mToView;
    private TextView mToTxtView;
    private EditText mMoneyEditText;
    private ImageButton mSwitchButton;
    private Button mChangeButton;
    private TextView mResultView;
    private View mSelectUnitView;
    private EditText mMoneyUnitEditText;
    private View mHotView;
    private GridViewForScrollView mHotUnitGridView;
    private ListView mAllUnitListView;
    private ImageButton mClearButton;

    private String mFromMoneyId;
    private String mFromMoneyName;
    private String mToMoneyId;
    private String mToMoneyName;

    private Dialog mProcessDialog;

    private List<String> mUnitList;
    private MoneyUnitAdapter mMoneyUnitAdapter;

    private boolean mIsSelectFrom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onCreate...");
        }
        this.setContentView(R.layout.tools_money_layout, true);

        mIsSelectFrom = false;
        buildLayout();
        initView();

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
    public void finish() {
        super.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onDestroy...");
        }


    }

    private void buildLayout() {
        this.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mFromView = this.findViewById(R.id.money_from_layout);
        mFromTextView = (TextView) this.findViewById(R.id.money_from_textview);
        mToView = this.findViewById(R.id.money_to_layout);
        mToTxtView = (TextView) this.findViewById(R.id.money_to_textview);
        mMoneyEditText = (EditText) this.findViewById(R.id.money_edittext);
        mSwitchButton = (ImageButton) this.findViewById(R.id.money_button_switch);
        mChangeButton = (Button) this.findViewById(R.id.button_change);
        mResultView = (TextView) this.findViewById(R.id.search_result_textview);

        mSelectUnitView = this.findViewById(R.id.money_select_view);
        mMoneyUnitEditText = (EditText) this.findViewById(R.id.money_unit_edittext);
        mHotView = this.findViewById(R.id.hot_view);
        mHotUnitGridView = (GridViewForScrollView) this.findViewById(R.id.hot_money_gridview);
        mAllUnitListView = (ListView) this.findViewById(R.id.all_money_listview);
        mClearButton = (ImageButton) this.findViewById(R.id.button_clear);

        mSelectUnitView.setVisibility(View.GONE);


        mFromView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                mIsSelectFrom = true;
                mFromView.setSelected(true);
                mToView.setSelected(false);
                if (mSelectUnitView.getVisibility() == View.GONE){
                    AnimationUtils.bottomInOrBottomOutAniamtion(mSelectUnitView, 500, true);
                }
            }
        });

        mToView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                mIsSelectFrom = false;
                mToView.setSelected(true);
                mFromView.setSelected(false);
                if (mSelectUnitView.getVisibility() == View.GONE){
                    AnimationUtils.bottomInOrBottomOutAniamtion(mSelectUnitView, 500, true);
                }
            }
        });

        mSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                String str = mFromMoneyName;
                String id = mFromMoneyId;
                mFromMoneyName = mToMoneyName;
                mFromMoneyId = mToMoneyId;
                mToMoneyName = str;
                mToMoneyId = id;
                mFromTextView.setText(mFromMoneyName);
                mToTxtView.setText(mToMoneyName);
            }
        });

        mMoneyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String edit = editable.toString();
                if (TextUtils.isEmpty(edit)){
                    mChangeButton.setEnabled(false);
                } else {
                    mChangeButton.setEnabled(true);
                }
            }
        });

        mChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectUnitView.getVisibility() == View.VISIBLE){
                    AnimationUtils.bottomInOrBottomOutAniamtion(mSelectUnitView, 500, false);
                }
                hideKeyboard();
                search();
            }
        });

        mMoneyUnitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String edit = editable.toString();
                mUnitList = new ArrayList<String>();
                if (TextUtils.isEmpty(edit)){
                    mClearButton.setVisibility(View.GONE);
                    mHotView.setVisibility(View.VISIBLE);
                    for (String s : MONEY_UNIT){
                        mUnitList.add(s);
                    }
                } else {
                    mClearButton.setVisibility(View.VISIBLE);
                    mHotView.setVisibility(View.GONE);
                    for (String s : MONEY_UNIT){
                        if (s.toLowerCase().contains(edit.toLowerCase())){
                            mUnitList.add(s);
                        }
                    }
                }

                if (mMoneyUnitAdapter == null){
                    mMoneyUnitAdapter = new MoneyUnitAdapter(ChangeMoneyActivity.this, false, mUnitList);
                    mAllUnitListView.setAdapter(mMoneyUnitAdapter);
                } else {
                    mMoneyUnitAdapter.refreshList(mUnitList);
                }

            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyUnitEditText.getEditableText().clear();
            }
        });

        mHotUnitGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String money = HOT_MONEY_UNIT[i];
                    if (mIsSelectFrom){
                        mFromMoneyId = money.substring(0,3);
                        mFromMoneyName = money.substring(4);
                        mFromTextView.setText(mFromMoneyName);
                    } else {
                        mToMoneyId = money.substring(0,3);
                        mToMoneyName = money.substring(4);
                        mToTxtView.setText(mToMoneyName);
                    }
                } catch (Exception ex){

                }

                AnimationUtils.bottomInOrBottomOutAniamtion(mSelectUnitView, 500, false);
                mMoneyUnitEditText.getEditableText().clear();

            }
        });

        mAllUnitListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    hideKeyboard();
                }
                return false;
            }
        });
        mAllUnitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String money = mUnitList.get(i);
                    if (mIsSelectFrom){
                        mFromMoneyId = money.substring(0,3);
                        mFromMoneyName = money.substring(4);
                        mFromTextView.setText(mFromMoneyName);
                    } else {
                        mToMoneyId = money.substring(0,3);
                        mToMoneyName = money.substring(4);
                        mToTxtView.setText(mToMoneyName);
                    }
                } catch (Exception ex){

                }
                AnimationUtils.bottomInOrBottomOutAniamtion(mSelectUnitView, 500, false);
                mMoneyUnitEditText.getEditableText().clear();
            }
        });

    }

    private void initView(){
        mFromMoneyId = "CNY";
        mFromMoneyName = "人民币";
        mToMoneyId = "USD";
        mToMoneyName = "美元";
        mFromTextView.setText(mFromMoneyName);
        mToTxtView.setText(mToMoneyName);

        List<String> hotList = new ArrayList<String>();
        for (String s : HOT_MONEY_UNIT){
            hotList.add(s);
        }
        MoneyUnitAdapter hotAdapter = new MoneyUnitAdapter(this, true, hotList);
        mHotUnitGridView.setAdapter(hotAdapter);

        mUnitList = new ArrayList<String>();
        for (String s : MONEY_UNIT){
            mUnitList.add(s);
        }
        mMoneyUnitAdapter = new MoneyUnitAdapter(this, false, mUnitList);
        mAllUnitListView.setAdapter(mMoneyUnitAdapter);
    }

    private void search(){

        if(mMoneyEditText!=null&&mMoneyEditText.getEditableText()!=null){
            final String money = mMoneyEditText.getEditableText().toString();
            if (TextUtils.isEmpty(money)){
                return;
            }
            try {
                final float moneyf = Float.valueOf(money);
                mProcessDialog = DialogUtils.showProgressDialog(this, R.string.search_points, true, new OnProgressDialogCancelListener() {
                    @Override
                    public void onProgressDialogCancel() {

                    }
                });

                ApiUtils.getChangeMoney(this, mFromMoneyId, mToMoneyId, money, new IResponseListener() {
                    @Override
                    public void onResponseResult(boolean success, int code, String msg) {
                        try {
                            mProcessDialog.dismiss();
                        } catch (Exception ex) {

                        }

                        Finance finance = CcaaiiApp.getGson().fromJson(msg, new TypeToken<Finance>() {
                        }.getType());
                        if (success && finance != null && finance.getSuccess().equals("1") && finance.getResult() != null) {
                            String convertedamount = String.valueOf(Float.valueOf(finance.getResult().getRate()) * moneyf);
                            mResultView.setText(ChangeMoneyActivity.this.getString(R.string.search_result_money, finance.getResult().getUpdate(), mFromMoneyName, mToMoneyName, money, finance.getResult().getRate(), convertedamount));
                        } else {
                            mResultView.setText(R.string.search_failed);
                        }

                    }
                });
            }catch (Exception e){
                if(e!=null) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }




    }

    private class ViewHolder {
        public TextView unitView;
    }

    private class MoneyUnitAdapter extends BaseAdapter {

        private Context mContext;
        private List<String> mUnitList;
        private LayoutInflater mInflater;
        private boolean mIsHot = false;

        public MoneyUnitAdapter(Context context, boolean isHot, List<String> list){
            this.mContext = context;
            this.mUnitList = list;
            this.mInflater = LayoutInflater.from(context);
            this.mIsHot = isHot;

        }

        public void refreshList(List<String> list){
            this.mUnitList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mUnitList != null ? mUnitList.size() : 0;
        }

        @Override
        public Object getItem(int i) {
            return mUnitList != null && i < mUnitList.size() ? mUnitList.get(i) : null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null){
                view = mInflater.inflate(mIsHot ? R.layout.default_listview_item_layout2 : R.layout.default_listview_item_layout, null);
                holder = new ViewHolder();
                holder.unitView = (TextView) view.findViewById(R.id.default_text_view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.unitView.setText(mIsHot ? mUnitList.get(i).substring(4) : mUnitList.get(i));

            return view;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if (mSelectUnitView.getVisibility() == View.VISIBLE){
                AnimationUtils.bottomInOrBottomOutAniamtion(mSelectUnitView, 500, false);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

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