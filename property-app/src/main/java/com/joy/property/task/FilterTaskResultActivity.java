package com.joy.property.task;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jinyi.ihome.infrastructure.MessageTo;
import com.jinyi.ihome.module.home.ServiceCategoryParam;
import com.jinyi.ihome.module.home.ServiceMainExpandTo;
import com.joy.common.api.ApiClient;
import com.joy.common.api.HomeApi;
import com.joy.common.api.HttpCallback;
import com.joy.library.fragment.CustomDialogFragment;
import com.joy.property.R;
import com.joy.property.base.BaseActivity;
import com.joy.property.inspection.CallDetailActivity;
import com.joy.property.inspection.adapter.MySubmissionAdapter;
import com.joy.property.task.adapter.TaskHallAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by usb on 2017/6/20.
 * //任务大厅--任务大厅筛选结果
 */

public class FilterTaskResultActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView mPullToRefreshListView;
    private int pageIndex = 0;
    private ListView mList;
    private TaskHallAdapter mAdapter =null;
    private List<ServiceMainExpandTo> mainExpandToList = new ArrayList<>();
    private ServiceCategoryParam param;
    private CustomDialogFragment dialogFragment = null;
    private ImageView noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_result);
        findById();
        mList = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mList.setItemsCanFocus(true);
        registerForContextMenu(mList);
        mAdapter = new TaskHallAdapter(getThisContext());
        mAdapter.setList(mainExpandToList);
        mList.setAdapter(mAdapter);
        setList(0);
        initDatas();
    }
    private void findById() {
        mPullToRefreshListView = (PullToRefreshListView)findViewById(R.id.list);
        findViewById(R.id.back).setOnClickListener(this);
        noData = (ImageView) findViewById(R.id.noData);
    }
    private void initDatas() {
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
                String label = DateUtils.formatDateTime(
                        getThisContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                listViewPullToRefreshBase.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);
                pageIndex = 0 ;
                setList(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
                String label = DateUtils.formatDateTime(
                        getThisContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                listViewPullToRefreshBase.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);

                pageIndex ++ ;
                setList(pageIndex);
            }
        });
    }
    private void setList(final int index) {
        param= (ServiceCategoryParam) getIntent().getSerializableExtra("ServiceParam");
        param.setType("1");

        param.setPageIndex(index);
        param.setGroupUserSid(mUserHelper.getSid());
        if (TextUtils.isEmpty(param.getResponseUser()))
            param.setResponseUser("");
        Log.i("param", "param: "+param.toString());
        HomeApi api = ApiClient.create(HomeApi.class);
        api.getServiceFilterResult(param, new HttpCallback<MessageTo<List<ServiceMainExpandTo>>>(getThisContext()) {
            @Override
            public void success(MessageTo<List<ServiceMainExpandTo>> msg, Response response) {

                if (msg == null)
                    return;
                if (msg.getSuccess() == 0) {
                    if (index == 0) {
                        mainExpandToList.clear();
                    }
                    if (msg.getData() != null) {
                        mainExpandToList.addAll(msg.getData());
                        if (mainExpandToList.size()>0)
                            noData.setVisibility(View.GONE);
                        else
                            noData.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();

                    }


                    mPullToRefreshListView.onRefreshComplete();
                    mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ServiceMainExpandTo main = mainExpandToList.get(position - 1);
                            Intent intent = new Intent(getThisContext(), ReceiveTaskDetailActivity.class);
                            intent.putExtra("sid", main.getServiceSid());
                            intent.putExtra("right", "right");
                            intent.putExtra("apartmentSid",main.getApartmentSid());
                            startActivity(intent);
                            goToAnimation(1);
                        }
                    });
                } else {
                    Toast.makeText(getThisContext(),
                            msg.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {

                System.out.println(error+"error");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToAnimation(2);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK)
            onBackPressed();
        return super.onKeyDown(keyCode, event);
    }
}
