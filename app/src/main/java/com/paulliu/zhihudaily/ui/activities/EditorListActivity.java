package com.paulliu.zhihudaily.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.entities.EditorEntity;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;
import com.paulliu.zhihudaily.ui.BaseWebViewActivity;
import com.paulliu.zhihudaily.ui.adapter.EditorListAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created on 2017/2/6
 *
 * @author LLW
 */

public class EditorListActivity extends BaseAppCompatActivity{

    public static final String EXTRA_EDITORS = "extra_editors";
    private List<EditorEntity> mEditorEntityList;
    private EditorListAdapter mAdapter;

    @BindView(R.id.rv_editors)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_editor_list;
    }

    @Override
    protected void initView() {
        mAdapter = new EditorListAdapter(this){
            @Override
            public void navigateToEditorPage(String url) {
                Bundle bundle = new Bundle();
                bundle.putString(BaseWebViewActivity.KEY_WEB_URL, url);
                navigateTo(BaseWebViewActivity.class, bundle);
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(mEditorEntityList);
        setDisplayHomeAsUp(null);
    }

    @Override
    protected void getBundleExtra(Bundle extra) {
        mEditorEntityList = extra.getParcelableArrayList(EXTRA_EDITORS);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

}
