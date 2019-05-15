package com.campussay.carpool.ui.post;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.graphics.Rect;
import android.net.ParseException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.R;
import com.campussay.carpool.ShellActivity;
import com.campussay.carpool.ui.adapter.PostRecyclerViewAdapter;
import com.campussay.carpool.ui.adapter.PostSearchRecyclerViewAdapter;
import com.campussay.carpool.ui.base.BaseFragment;
import com.campussay.carpool.utils.LogUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * create by WenJinG on 2019/4/6
 */
public class PostFragment extends BaseFragment<PostPresenter> implements IPostFragmentView{

    private EditText post_start;
    private EditText post_end;
    private Button post;
    private RecyclerView postRecyclerView;
    private RecyclerView postSearchRecyclerView;
    private PostRecyclerViewAdapter adapter;
    private PostSearchRecyclerViewAdapter searchAdapter;
    private AlertDialog postDialog;
    private PostInfoPicker postInfoPicker;
    private Button post_cancel;
    private Button post_verify_post;
    private SwipeRefreshLayout refreshLayout;
    public boolean startSearch = false;
    public boolean endSearch = false;
    private boolean only = false;//这和变量为了防止监听EditView时，改变文本引起死循环。
    private boolean firstLoad = true;
    private String cityName ="";
    public PostRouteData routeData = new PostRouteData();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // 相当于onResume()方法
            LogUtils.d("看见时请求");
            if(CarpoolApplication.getAccount()!=null) {
                if(!firstLoad&& routeData.getOriginName()!=null&&routeData.getDestinationName()!=null)
               mPresenter.getSimilarRoute(routeData);
            }

        } else {
            if(post_start!=null && post_end!=null) {
                //post_start.setText("");
                //post_end.setText("");
                // 相当于onpause()方法
            }
        }
    }


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_post;
    }

    @Override
    protected PostPresenter createPresenter() {
        return new PostPresenter();
    }

    @Override
    protected void initView() {
        setParentListener();

        if(CarpoolApplication.getAccount()!=null) {
            if(firstLoad) {
                mPresenter.startPoiSearch("");
                firstLoad = false;
            }
        }

        post_start = mView.findViewById(R.id.post_start);
        post = mView.findViewById(R.id.post);
        post_end = mView.findViewById(R.id.post_end);
        post_start = mView.findViewById(R.id.post_start);
        postRecyclerView = mView.findViewById(R.id.post_recyclerView);
        refreshLayout = mView.findViewById(R.id.post_swipeRefresh);
        post.setEnabled(false);
        initPostButtonStartAndEND();
        initPostSearchRecyclerView();
        onLayoutFinished();
        initPostRecyclerView();
        initPostDialog();
        post_carpool();
        initSwipeRefresh();
    }

    private void setParentListener(){
        ShellActivity shellActivity = (ShellActivity)getActivity();
        shellActivity.setBackListener(() -> {
            if(postSearchRecyclerView.getVisibility() == View.VISIBLE){

                if(!TextUtils.isEmpty(post_start.getText())){
                    if(startSearch){
                        mPresenter.initOriginRouteData(routeData,0);
                    }
                }

                if(!TextUtils.isEmpty(post_end.getText())){
                    if(endSearch){
                        mPresenter.initDestinationRouteData(routeData,0);
                    }
                }
                if(!TextUtils.isEmpty(post_start.getText()) && !TextUtils.isEmpty(post_end.getText())) {
                    mPresenter.getSimilarRoute(routeData);
                }
                postSearchRecyclerView.setVisibility(View.GONE);
                postRecyclerView.setVisibility(View.VISIBLE);
                return false;
            }
            return true;
        });
        shellActivity.addLocationChangeListener((latitude, longitude, title, city) -> cityName  = city);
    }

    private void onLayoutFinished(){
        mView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            View decorView =  getActivity().getWindow().getDecorView();
            decorView.getWindowVisibleDisplayFrame(rect);
            int displayHeight = rect.bottom - rect.top;
            int height = decorView.getHeight();
            boolean visible = (double) displayHeight / height < 0.8;
            //LogUtils.d("键盘显示："+visible);
            if(!visible){
                post_start.setCursorVisible(false);
                post_end.setCursorVisible(false);
            }

        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initPostRecyclerView(){

        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //adapter = new PostRecyclerViewAdapter( mPresenter.search());
        adapter = new PostRecyclerViewAdapter(mPresenter.items);
        adapter.setJoinClickListener(new PostRecyclerViewAdapter.JoinClickListener() {
            @Override
            public void onJoinClick(int position) {
                mPresenter.joinTeam(mPresenter.items.get(position).getTeamId());
            }
        });
        postRecyclerView.setAdapter(adapter);

        postRecyclerView.setOnTouchListener((v, event) -> {
            InputMethodManager manager = ((InputMethodManager)getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE));
            if (manager != null)
                manager.hideSoftInputFromWindow(mView.getWindowToken()
                        ,InputMethodManager.HIDE_NOT_ALWAYS);
            return false;
        });

    }


    public void initPostDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.post_dialog_numberpicker, null);
        postDialog = new AlertDialog.Builder(mView.getContext())
                .setView(dialogView)
                .create();
    }

    public void onDataChange(){
        refreshLayout.setRefreshing(false);
        //showToast("刷新成功");
        adapter.notifyDataSetChanged();
        postRecyclerView.scrollToPosition(0);
    }

    public void initSwipeRefresh(){

        refreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);//设置刷新时的颜色
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            mPresenter.getSimilarRoute(routeData);
        });
        refreshLayout.setEnabled(false);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initPostButtonStartAndEND(){
        post_start.setCursorVisible(false);
        post_end.setCursorVisible(false);
        post_start.setOnTouchListener((v, event) -> {
            post_start.setCursorVisible(true);
            if(!post_start.getText().toString().equals("")){
                if(startSearch) {
                    mPresenter.initOriginRouteData(routeData, 0);
                }
            }
            return false;
        });
        post_end.setOnTouchListener((v, event) -> {
            post_end.setCursorVisible(true);
            if(!post_end.getText().toString().equals("")){
                if(endSearch)
                mPresenter.initDestinationRouteData(routeData,0);
            }
            return false;
        });

        //EditView失去焦点收起
        post_start.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                InputMethodManager manager = ((InputMethodManager)getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE));
                if (manager != null)
                    manager.hideSoftInputFromWindow(mView.getWindowToken()
                            ,InputMethodManager.HIDE_NOT_ALWAYS);
            }else {
                post_start.setCursorVisible(true);
                if(postSearchRecyclerView.getVisibility()==View.VISIBLE){
                    postSearchRecyclerView.setVisibility(View.GONE);
                    if(postRecyclerView.getVisibility()== View.GONE){
                        postRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
            });

        //EditView失去焦点收起
        post_end.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                InputMethodManager manager = ((InputMethodManager)getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE));
                if (manager != null)
                    manager.hideSoftInputFromWindow(mView.getWindowToken()
                            ,InputMethodManager.HIDE_NOT_ALWAYS);
            }else {
                post_end.setCursorVisible(true);
                if(postSearchRecyclerView.getVisibility()==View.VISIBLE){
                    postSearchRecyclerView.setVisibility(View.GONE);
                    if(postRecyclerView.getVisibility()== View.GONE){
                        postRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        //监听EditView文本变化，并根据变化进行搜索
        post_end.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (only){
                    return;
                }
                if(TextUtils.isEmpty(post_start.getText())||TextUtils.isEmpty(post_end.getText())){
                    mPresenter.items.clear();
                    onDataChange();
                    refreshLayout.setEnabled(false);
                    post.setEnabled(false);
                }
                if(!TextUtils.isEmpty(post_end.getText())){
                    endSearch = true;
                    startSearch = false;
                    mPresenter.startPoiSearch(post_end.getText().toString());
                    LogUtils.d("item长度"+mPresenter.searchItems.size());
                    LogUtils.d(post_end.getText().toString());

                    postRecyclerView.setVisibility(View.GONE);
                }
                if(!TextUtils.isEmpty(post_start.getText())&&!TextUtils.isEmpty(post_end.getText())) {
                    refreshLayout.setEnabled(true);
                    post.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                LogUtils.d(content);

            }
        });

        //监听EditView文本变化，并根据变化进行搜索
        post_start.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (only){
                    return;
                }
                if(TextUtils.isEmpty(post_start.getText())||TextUtils.isEmpty(post_end.getText())){
                    mPresenter.items.clear();
                    onDataChange();
                    refreshLayout.setEnabled(false);
                    post.setEnabled(false);
                }

                if(!TextUtils.isEmpty(post_start.getText())){
                    startSearch =true;
                    endSearch = false;
                    mPresenter.searchItems.clear();
                    mPresenter.startPoiSearch(post_start.getText().toString());
                    LogUtils.d(post_start.getText().toString());
                    postRecyclerView.setVisibility(View.GONE);

                }
                if(!TextUtils.isEmpty(post_start.getText())&&!TextUtils.isEmpty(post_end.getText())) {
                    refreshLayout.setEnabled(true);
                    post.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initPostSearchRecyclerView(){
        postSearchRecyclerView = mView.findViewById(R.id.post_search_recyclerview);
        postSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchAdapter = new PostSearchRecyclerViewAdapter(mPresenter.searchItems);
        searchAdapter.setItemClickListener((position) -> {
            if (startSearch){
                only = true;
                post_start.setText(mPresenter.searchItems.get(position).getLocation());
                routeData.setOriginName(post_start.getText().toString());
                mPresenter.initOriginRouteData(routeData,position);
                only = false;
                startSearch =false;
            }
            if(endSearch) {
                only = true;
                post_end.setText(mPresenter.searchItems.get(position).getLocation());
                routeData.setDestinationName(post_end.getText().toString());
                mPresenter.initDestinationRouteData(routeData,position);
                only = false;
                endSearch = false;
            }
            if(!TextUtils.isEmpty(post_start.getText())&&!TextUtils.isEmpty(post_end.getText())) {
                refreshLayout.setEnabled(true);
                post.setEnabled(true);
            }
            post_start.setCursorVisible(false);
            post_end.setCursorVisible(false);
            InputMethodManager manager = ((InputMethodManager)getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE));
            if (manager != null)
                manager.hideSoftInputFromWindow(mView.getWindowToken()
                        ,InputMethodManager.HIDE_NOT_ALWAYS);
            postSearchRecyclerView.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(post_start.getText())&& !TextUtils.isEmpty(post_end.getText())){
                mPresenter.getSimilarRoute(routeData);
            }
            postRecyclerView.setVisibility(View.VISIBLE);
        });
        postSearchRecyclerView.setAdapter(searchAdapter);
        postSearchRecyclerView.setVisibility(View.GONE);
        postSearchRecyclerView.setOnTouchListener((v, event) -> {
            InputMethodManager manager = ((InputMethodManager)getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE));
            if (manager != null)
                manager.hideSoftInputFromWindow(mView.getWindowToken()
                        ,InputMethodManager.HIDE_NOT_ALWAYS);
            return false;
        });
    }

    public void searchItemChange(){
        searchAdapter.notifyDataSetChanged();
        postSearchRecyclerView.setVisibility(View.VISIBLE);
        postSearchRecyclerView.scrollToPosition(0);

    }

    public void post_carpool() {
        post.setOnClickListener((view)->{
            postDialog.show();
            TextView post_dialog_start;
            TextView post_dialog_end;
            post_dialog_start = postDialog.findViewById(R.id.post_start_text);
            post_dialog_end = postDialog.findViewById(R.id.post_end_text);
            if(!TextUtils.isEmpty(post_start.getText())&& !TextUtils.isEmpty(post_end.getText())){
                post_dialog_start.setText(routeData.getOriginName()+ routeData.getOriginAddressDetail());
                post_dialog_end.setText(routeData.getDestinationName() + routeData.getDestinationAdrssDetail());
            }
            WindowManager.LayoutParams params = postDialog.getWindow().getAttributes();
            params.height = (int) getResources().getDimension(R.dimen.c_390dp);
            postDialog.getWindow().setAttributes(params);
            postInfoPicker = postDialog.findViewById(R.id.post_info_picker);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置你想要的格式
            df.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            Date date = new Date();
            Long time = null;
                try {
                    date = df.parse(postInfoPicker.getTime());
                    time = date.getTime();
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            routeData.setTargetTime(time);
            //post_dialog_time = postDialog.findViewById(R.id.post_dialog_time);
            //post_dialog_peopleNumber = postDialog.findViewById(R.id.post_dialog_member);
            //post_dialog_time.setText(postInfoPicker.getTime());
            postInfoPicker.setonInfoChangeListener((calendar,peopleNumber)->{

                routeData.setTargetTime(calendar.getTime().getTime());
                LogUtils.d("日期"+calendar.get(Calendar.YEAR)+
                        calendar.get(Calendar.MONTH)+calendar.get(Calendar.DAY_OF_MONTH)+
                        calendar.get(Calendar.HOUR_OF_DAY)+calendar.get(Calendar.MINUTE));
                //routeData.setTargetTime("2019-07-30T23:41:16");
            });
            post_cancel = postDialog.findViewById(R.id.post_cancel);
            post_verify_post = postDialog.findViewById(R.id.post_verify_post);
            post_cancel.setOnClickListener(v -> postDialog.dismiss());
            post_verify_post.setOnClickListener((v)->{
                if(routeData.getOriginName()==null||routeData.getDestinationName()==null){
                    showToast("请确认你的起点或终点");
                    return;
                }
                mPresenter.postRoute(routeData);
                postDialog.dismiss();
            });
        });
    }

    public String getCity() {
        return cityName;
    }

    public void clearEditText(){
        if(!TextUtils.isEmpty(post_start.getText())&& !TextUtils.isEmpty(post_end.getText())) {
            post_start.setText("");
            post_end.setText("");
        }
    }

    public void showToast(String info){
        Toast.makeText(mView.getContext(),info,Toast.LENGTH_SHORT).show();
    }
}
