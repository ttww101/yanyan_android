package com.ccaaii.shenghuotong.setting;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccaaii.base.BaseFragment;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.shenghuotong.CcaaiiConstants;
import com.ccaaii.shenghuotong.R;
import com.ccaaii.shenghuotong.discover.tools.ChangeMoneyActivity;
import com.ccaaii.shenghuotong.discover.tools.SearchIdsActivity;
import com.ccaaii.shenghuotong.discover.tools.SearchIpsActivity;
import com.ccaaii.shenghuotong.discover.tools.SearchMobileActivity;
import com.ccaaii.shenghuotong.web.WebShowActivity;


/**
 */
public class SettingFragment extends BaseFragment{
    public static final String TAG = "[CCAAII]SettingFragment";

    private ImageView mHeadImageView;
    private TextView mNameView;

    public SettingFragment(){
        MarketLog.i(TAG, "SettingFragment instance");
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
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
        View v = inflater.inflate(R.layout.setting_fragment_layout, container, false);
        buildLayout(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onViewCreated...");
        }

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
    }

    private void buildLayout(View v) {

        v.findViewById(R.id.setting_suggestion_layout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SuggestionsActivity.class);
                getActivity().startActivity(intent);
            }
        });

        v.findViewById(R.id.setting_like_layout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String str = "market://details?id=" + getActivity().getPackageName();
                Intent localIntent = new Intent("android.intent.action.VIEW");
                localIntent.setData(Uri.parse(str));
                startActivity(localIntent);
            }
        });

        v.findViewById(R.id.setting_about_layout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                getActivity().startActivity(intent);
            }
        });

        mHeadImageView = (ImageView) v.findViewById(R.id.user_head_imageview);
        mNameView = (TextView) v.findViewById(R.id.user_name_textview);

        // tools
        v.findViewById(R.id.tools_ip_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchIpsActivity.class);
                getActivity().startActivity(intent);
            }
        });

        v.findViewById(R.id.tools_phone_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchMobileActivity.class);
                getActivity().startActivity(intent);
            }
        });

        v.findViewById(R.id.tools_ids_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchIdsActivity.class);
                getActivity().startActivity(intent);
            }
        });

        v.findViewById(R.id.tools_money_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeMoneyActivity.class);
                getActivity().startActivity(intent);
            }
        });

        v.findViewById(R.id.tools_calendar_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebShowActivity.class);
                String url = "http://www.51wnl.com/calendar_lightapp/index.html";

                intent.putExtra(CcaaiiConstants.EXTRA_WEB_URL, url);
                intent.putExtra(CcaaiiConstants.EXTRA_JS_ENABLED, 1);
                intent.putExtra(CcaaiiConstants.EXTRA_WEB_TITLE, getActivity().getString(R.string.tools_title_calendar));
                getActivity().startActivity(intent);
            }
        });

        v.findViewById(R.id.tools_express_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebShowActivity.class);
                String url = "http://wap.guoguo-app.com/";
                intent.putExtra(CcaaiiConstants.EXTRA_WEB_URL, url);
                intent.putExtra(CcaaiiConstants.EXTRA_JS_ENABLED, 1);
//                intent.putExtra(CcaaiiConstants.EXTRA_NO_TOP_BAR, true);
                intent.putExtra(CcaaiiConstants.EXTRA_WEB_TITLE, getActivity().getString(R.string.tools_title_express));
                getActivity().startActivity(intent);
            }
        });

        v.findViewById(R.id.tools_caipiao_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebShowActivity.class);
                String url = "http://m.500.com/info/kaijiang/";
                intent.putExtra(CcaaiiConstants.EXTRA_WEB_URL, url);
                intent.putExtra(CcaaiiConstants.EXTRA_JS_ENABLED, 1);
                intent.putExtra(CcaaiiConstants.EXTRA_NO_TOP_BAR, true);
                intent.putExtra(CcaaiiConstants.EXTRA_WEB_TITLE, getActivity().getString(R.string.tools_title_caipiao));
                getActivity().startActivity(intent);
            }
        });

        v.findViewById(R.id.tools_car_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebShowActivity.class);
                String url = "http://m.qiche100.cn";
                intent.putExtra(CcaaiiConstants.EXTRA_WEB_URL, url);
                intent.putExtra(CcaaiiConstants.EXTRA_JS_ENABLED, 1);
                intent.putExtra(CcaaiiConstants.EXTRA_NO_TOP_BAR, true);
                intent.putExtra(CcaaiiConstants.EXTRA_WEB_TITLE, getActivity().getString(R.string.tools_title_car));
                getActivity().startActivity(intent);
            }
        });
    }

}
