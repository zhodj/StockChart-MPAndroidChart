package com.android.stockapp.ui.market.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.stockapp.R;
import com.android.stockapp.common.data.ChartData;
import com.android.stockapp.common.dbhandler.Klines;
import com.android.stockapp.ui.base.BaseFragment;
import com.android.stockapp.ui.market.activity.StockDetailLandActivity;
import com.github.mikephil.charting.stockChart.CoupleChartGestureListener;
import com.github.mikephil.charting.stockChart.data.TimeDataManage;
import com.github.mikephil.charting.stockChart.view.OneDayView;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.stockapp.common.Utility.unicodeToUTF_8;
import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

/**
 * 分时页
 */
public class ChartOneDayFragment extends BaseFragment {

    @BindView(R.id.chart)
    OneDayView chart;
    Unbinder unbinder;

    private boolean land;//是否横屏
    private TimeDataManage kTimeData = new TimeDataManage();
    private JSONObject object;

    public static ChartOneDayFragment newInstance(boolean land) {
        ChartOneDayFragment fragment = new ChartOneDayFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("landscape", land);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_one_day;
    }

    @Override
    public void initBase(View view) {

        chart.initChart(land);
        //测试数据
        try {
            object = new JSONObject(ChartData.TIMEDATA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(LOG_TAG, "tushare http request");
        EasyHttp.post("/")
                .readTimeOut(30 * 1000)
                .writeTimeOut(30 * 1000)
                .connectTimeout(30 * 1000)
                .params("api_name", "daily")
                .params("ts_code", "000001.SZ")
                .params("start_date", "20190101")
                .params("end_date", "20190614")
                .timeStamp(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.i(LOG_TAG, unicodeToUTF_8(e.getMessage()));
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String response) {
                        Log.i(LOG_TAG, "hello" + unicodeToUTF_8(response));
                        if (response != null)
                            showToast(response);
                    }
                });

        //上证指数代码000001.IDX.SH
        kTimeData.parseTimeData(object,"000001.IDX.SH",0);
        chart.setDataToChart(kTimeData);

        //非横屏页单击转横屏页
        if (!land) {
            chart.getGestureListenerLine().setCoupleClick(new CoupleChartGestureListener.CoupleClick() {
                @Override
                public void singleClickListener() {
                    Intent intent = new Intent(getActivity(), StockDetailLandActivity.class);
                    getActivity().startActivity(intent);
                }
            });
            chart.getGestureListenerBar().setCoupleClick(new CoupleChartGestureListener.CoupleClick() {
                @Override
                public void singleClickListener() {
                    Intent intent = new Intent(getActivity(), StockDetailLandActivity.class);
                    getActivity().startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        land = getArguments().getBoolean("landscape");
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}