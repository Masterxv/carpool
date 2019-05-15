package com.campussay.carpool.ui.self;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.R;
import com.campussay.carpool.net.BaseBean;
import com.campussay.carpool.net.RxSchedulers;
import com.campussay.carpool.net.app_net.AppNetClient;
import com.campussay.carpool.ui.adapter.MessageAdapter;
import com.campussay.carpool.ui.base.BasePresenter;
import com.campussay.carpool.ui.chat.ChatActivity;
import com.campussay.carpool.ui.self.testbean.MessagesBean;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Create by Zhangfan on 2019/4/19
 **/
public class MessagePresenter extends BasePresenter<MessageIView> {

    private static final String TOKEN = CarpoolApplication.getAccount().getToken();
    private static final long SELF_ID = CarpoolApplication.getAccount().getId();
    private static final String HEAD_URL = CarpoolApplication.getAccount().getHeadUrl();
    private BaseBean<List<MessagesBean.Data>> list = new BaseBean<>();
    private boolean isNeting = false;

    public void getMessageList(){
        if (isNeting) return;
        if (TOKEN != null){
            isNeting = true;
            AppNetClient.getInstance().getSelfMessagesList(TOKEN)
                    .compose(RxSchedulers.applySchedulers())
                    .subscribe(new Observer<BaseBean<List<MessagesBean.Data>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseBean<List<MessagesBean.Data>> listBaseBean) {

                            if (listBaseBean.isSuccess()) {
                                if (list.getData() == null) {
                                    list = listBaseBean;
                                }else if (list.getData() != null && listBaseBean.getData() != null){
                                    list.getData().clear();
                                    list = listBaseBean;
                                    /*for (int j=0;j< listBaseBean.getData().size();j++){

                                        for (int i = 0;i < list.getData().size();i++){

                                            if (list.getData().get(i).getType() == listBaseBean.getData().get(j).getType()

                                                    && list.getData().get(i).getId() == listBaseBean.getData().get(j).getId()){

                                                list.getData().remove(i);
                                            }
                                        }
                                    }

                                    for (int k=0;k<listBaseBean.getData().size();k++){
                                        list.getData().add(0,listBaseBean.getData().get(k));
                                    }*/
                                }
                            }
                            mView.getMessageList(list.getData());
                            mView.isRefreshSuccess("刷新成功");
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.isRefreshSuccess("刷新失败");
                            isNeting = false;
                        }

                        @Override
                        public void onComplete() {
                            isNeting = false;
                        }
                    });
        }

    }

    public View.OnClickListener getOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMessageList();
            }
        };
    }

    public SwipeRefreshLayout.OnRefreshListener getRefreshMessageList(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessageList();
            }
        };
    }

    public MyItemClickListener getMessageItemListener(Activity context, MessageAdapter adapter){
        return (v,position) -> {
            v.setVisibility(View.GONE);
            if (TOKEN != null && SELF_ID != 0 && HEAD_URL != null) {
                if (list.getData().get(position).getType() == 0) {//好友私聊
                    ChatActivity.startSelfChatting(list.getData().get(position).getTheme(), context,
                            TOKEN, SELF_ID, Long.parseLong(list.getData().get(position).getId()),
                            HEAD_URL, list.getData().get(position).getHeadUrl());
                } else {
                    ChatActivity.startGroupChatting(list.getData().get(position).getTheme() + "—>" + list.getData().get(position).getSencondTheam(),
                            context, TOKEN, SELF_ID, Long.parseLong(list.getData().get(position).getId()), HEAD_URL);
                }
            }else{
                Toast.makeText(context, "token等参数未知", Toast.LENGTH_SHORT).show();
            }
            remove(adapter,list.getData(),position);
        } ;
    }

    public ItemLongClickListener getLongListener(Context context,MessageAdapter adapter){
        return (v, position) -> showPopWindow(context,v,position,adapter);
    }

    public void showPopMenu(Context context, View view, final int pos, MessageAdapter adapter){
        PopupMenu popupMenu = new PopupMenu(context,view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_item_delete,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item ->  {
            adapter.removeItem(pos);
            return false;
        });
        popupMenu.setOnDismissListener(menu -> Toast.makeText(context, "关闭PopupMenu", Toast.LENGTH_SHORT).show());
        popupMenu.show();
    }

    public void showPopWindow(Context context,View v,final int pos, MessageAdapter adapter){
        View view = LayoutInflater.from(context).inflate(R.layout.item_popip, null, false);
        TextView tv_delete = view.findViewById(R.id.tv_delete);
        TextView tv_first = view.findViewById(R.id.tv_first);
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效

        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 200, 10);

        //设置popupWindow里的按钮的事件
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               remove(adapter,list.getData(),pos);
            }
        });

        tv_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "暂不支持该功能", Toast.LENGTH_SHORT).show();
                popWindow.dismiss();
            }
        });
    }

    private void remove(MessageAdapter adapter,List<MessagesBean.Data> data,int movePosition){
        data.remove(movePosition);
        adapter.setList(data);
        adapter.notifyDataSetChanged();
    }
}
