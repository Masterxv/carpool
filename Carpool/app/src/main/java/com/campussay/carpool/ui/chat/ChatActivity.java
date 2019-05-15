package com.campussay.carpool.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.campussay.carpool.R;
import com.campussay.carpool.ui.base.BaseActivity;
import com.campussay.carpool.ui.chat.other.SmoothScrollLayoutManager;
import com.campussay.carpool.ui.chat.widget.ChatContentLayout;
import com.campussay.carpool.ui.chat.widget.ChatFrameLayout;
import com.campussay.carpool.utils.ScreenAttrUtil;

import java.util.EnumSet;
import java.util.Iterator;

public class ChatActivity extends BaseActivity<ChatPresenter> implements IChatView,
        View.OnClickListener {

    private RecyclerView rvChat;

    private TextView tvChatVoice;

    private EditText etChat;

    private ImageView ivBack;

    private TextView tvTitle;

    private TextView tvSend;

    private ImageView ivVoice;

    //当前是什么输入方式
    private boolean isVoice = false;

    //语音输入弹框
    private AlertDialog mVoiceDialog ;

    private SmoothScrollLayoutManager mRecyclerManager;

    private ChatContentLayout mLayout;

    private ChatFrameLayout cflChat;

    @Override
    public ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_chat;
    }

    @Override
    public void initView() {
        findView();

        //解析是哪一种聊天形式
        mPresenter.parseIntentExtra(getIntent());

        //监听软键盘是否弹起
        View rootView = getWindow().getDecorView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                mPresenter.createRootGlobalListener(rootView));

        init();
    }

    private void findView() {
        rvChat = findViewById(R.id.rv_chat);
        tvChatVoice = findViewById(R.id.tv_chat_voice);
        etChat = findViewById(R.id.et_chat);
        ivBack = findViewById(R.id.iv_chat_back);
        tvTitle = findViewById(R.id.tv_chat_title);
        tvSend = findViewById(R.id.tv_chat_send);
        ivVoice = findViewById(R.id.iv_chat_voice);
        mLayout = findViewById(R.id.cl_chat);
        cflChat = findViewById(R.id.cfl_chat_layout);
    }

    private void init() {
        ivBack.setOnClickListener(this);

        cflChat.setOnRefreshListener(mPresenter.createRefreshListener());

        ivVoice.setClickable(true);
        ivVoice.setOnClickListener(this);

        mRecyclerManager = new SmoothScrollLayoutManager(this);
        rvChat.setLayoutManager(mRecyclerManager);
        rvChat.setAdapter(mPresenter.createRecyclerAdapter());
        //主要是软键盘弹出时，滑动到底部
        rvChat.getViewTreeObserver().addOnGlobalLayoutListener(mPresenter.createRecyclerGlobalListener());

        tvSend.setOnClickListener(this);

        //主要设置上滑取消语音
        tvChatVoice.setOnClickListener(this);
        tvChatVoice.setClickable(true);
        tvChatVoice.setOnTouchListener(mPresenter.createVoiceBtnListener());

        //设置最大触发关闭界面的滑动距离
        mLayout.setCloseDistance(500);
        mLayout.setRefreshListener(() -> finish());

        //弹出软键盘
        etChat.setOnTouchListener((v, event) -> {
            if (mPresenter.mkeyboardShow) return false;
            etChat.requestFocus();

            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etChat,0);
            return true;
        });

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etChat,0);
    }

    @Override
    public void showVoiceDialog(boolean isFirst, boolean cancel) {
        TextView tvShow = null;
        if (isFirst) {
            mVoiceDialog = new AlertDialog.Builder(this)
                    .setView(R.layout.dialog_chat_voice)
                    .show();
            mVoiceDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams p = mVoiceDialog.getWindow().getAttributes();
            p.width = (int) (160f * ScreenAttrUtil.getDensity());
            p.height = (int) (160f * ScreenAttrUtil.getDensity());
            p.gravity = Gravity.CENTER;
            mVoiceDialog.getWindow().setAttributes(p);
        }
        tvShow = mVoiceDialog.findViewById(R.id.tv_dialog_voice);
        if (cancel) {
            tvShow.setText("松开手指\n取消发送");
            tvShow.setTextColor(Color.RED);
        } else {
            tvShow.setText("手指上滑\n取消发送");
            tvShow.setTextColor(Color.WHITE);
        }
    }

    @Override
    public void hideVoiceDialog() {
        mVoiceDialog.dismiss();
        mVoiceDialog = null;
    }

    @Override
    public void openLayoutSlideBack(boolean open) {
        mLayout.setOpen(open);
    }

    @Override
    public void scrollToPosition(int position) {
        rvChat.scrollToPosition(mPresenter.getRecyclerAdapter().getListSize());
    }

    @Override
    public void smoothToPosition(int position) {
        float i = position - mRecyclerManager.findLastVisibleItemPosition();
        float m = 1f / (i == 0 ? 0.5f : i);
        m = m <= 0.2f ? 0.2f : m;
        mRecyclerManager.setSpeedMultiple(m);
        rvChat.smoothScrollToPosition(position);
    }

    @Override
    public void saveOldPosition(int position) {
        int of = rvChat.getChildAt(0).getMeasuredHeight();
        ((LinearLayoutManager) rvChat.getLayoutManager())
                .scrollToPositionWithOffset(position, of);
    }

    @Override
    public void hideTopRefreshView() {
        cflChat.hide();
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public void setTitle(String title) {
        if (title.length() >= 22)
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    tvTitle.getTextSize() - 6f * ScreenAttrUtil.getDensity());
        else if (title.length() >= 17)
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    tvTitle.getTextSize() - 4f * ScreenAttrUtil.getDensity());
        else if (title.length() >= 12)
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    tvTitle.getTextSize() - 2f * ScreenAttrUtil.getDensity());
        tvTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_chat_voice:
                if (isVoice) {
                    ivVoice.setImageDrawable(getDrawable(R.drawable.ic_voice));
                    etChat.setVisibility(View.VISIBLE);
                    tvChatVoice.setVisibility(View.GONE);
                } else {
                    ivVoice.setImageDrawable(getDrawable(R.drawable.ic_jianpan));
                    etChat.setVisibility(View.GONE);
                    tvChatVoice.setVisibility(View.VISIBLE);
                }
                isVoice = !isVoice;
                break;
            case R.id.tv_chat_voice:
                break;
            case R.id.tv_chat_send:
                String str = etChat.getText().toString();
                if (!str.isEmpty()) {
                    mPresenter.sendMessage(str);
                    etChat.setText("");
                }
                break;
            case R.id.iv_chat_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        mPresenter.hideSoftKeyboard(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onActivityResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onActivityStop();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_in_other, R.anim.anim_finish_chat);
    }

    public static void startSelfChatting(String title, Activity context, String token, long selfId,
                                     long otherId, String selfHeadUrl, String otherHeadUrl) {
        startChatting(title, ChatType.SELF_CHAT, context, token, selfId, otherId,
                selfHeadUrl, otherHeadUrl);
    }

    public static void startGroupChatting(String title, Activity context, String token, long selfId,
                                          long groupId, String selfUrl) {
        startChatting(title, ChatType.GROUP_CHAT, context, token, selfId, groupId,
                selfUrl, "");
    }

    public static void startPlaceChatting(String title, Activity context, String token, long selfId,
                                          String selfHeadUrl) {
        startChatting(title, ChatType.PLACE_CHAT, context, token, selfId, 0,
                selfHeadUrl, "");
    }

    private static void startChatting(String title, ChatType type, Activity context, String token,
                                     long selfId, long otherId, String selfHeadUrl,
                                      String otherHeadUrl) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("type", type.intValue);
        intent.putExtra("selfId", selfId);
        intent.putExtra("otherId", otherId);
        intent.putExtra("token", token);
        intent.putExtra("selfHeadUrl", selfHeadUrl);
        intent.putExtra("otherHeadUrl", otherHeadUrl);
        context.startActivity(intent);
        context.overridePendingTransition( R.anim.anim_in_chat, R.anim.anim_close_other);
    }

    public enum ChatType {
        SELF_CHAT(1001), GROUP_CHAT(1002), PLACE_CHAT(1003), NULL(-1);

        public final int intValue;

        ChatType(int intValue) {
            this.intValue = intValue;
        }

        public static ChatType get(int value) {
            Iterator i = EnumSet.allOf(ChatType.class).iterator();

            while (i.hasNext()) {
                ChatType c = (ChatType) i.next();
                if (c.intValue == value) return c;
            }
            return NULL;
        }
    }
}
