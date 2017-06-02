package com.firstaid.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firstaid.oldman.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/16.
 */
public class FragmentHomeOld extends BaseFragment implements View.OnClickListener {

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private int currentIndex;
    
    private ListView newsListView;
    private List<Map<String, Object>> data;

    /**
     * 亲属三个按钮
     */
    private RelativeLayout mDaughter;
    private RelativeLayout mSon;
    private RelativeLayout mErxi;

    private View myinflate;

    private TextView tvDaughter;
    private TextView tvSon;
    private TextView tvErxi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myinflate = inflater.inflate(R.layout.fragment_home_old, container, false);

        mViewPager = (ViewPager) myinflate.findViewById(R.id.mykinsfolk_vp);

        initView();//初始化控件

        /**
         * 设置主页新闻ListView
         */
        //获取将要绑定的数据设置到data中
        data=getData();
        MyAdapter myAdapter=new MyAdapter(this);
        newsListView.setAdapter(myAdapter);


        mAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }

        };

        mViewPager.setAdapter(mAdapter);

        //设置viewpager启动首页从哪里开始
        mViewPager.setCurrentItem(1);

        //viewpager滑动监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                resetTabBtn();

                //设置选中时的字体颜色
                switch (position) {
                    case 0:
                        tvDaughter.setTextColor(Color.parseColor("#3BCF67"));
                        break;
                    case 1:
                        tvSon.setTextColor(Color.parseColor("#3BCF67"));
                        break;
                    case 2:
                        tvErxi.setTextColor(Color.parseColor("#3BCF67"));
                        break;

                }

                currentIndex = position;//设置按钮和滑动页面联动
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }


        });

        return myinflate;
    }

    /**
     * 对字体颜色进行重置
     */
    protected void resetTabBtn() {

        tvDaughter.setTextColor(Color.parseColor("#000000"));
        tvSon.setTextColor(Color.parseColor("#000000"));
        tvErxi.setTextColor(Color.parseColor("#000000"));
    }


    /**
     * 初始化控件
     */
    private void initView() {

       /* mDaughter = (RelativeLayout) myinflate.findViewById(R.id.rl_daughter);
        mSon = (RelativeLayout) myinflate.findViewById(R.id.rl_son);
        mErxi = (RelativeLayout) myinflate.findViewById(R.id.rl_erxi);

        mDaughter.setOnClickListener(this);
        mSon.setOnClickListener(this);
        mErxi.setOnClickListener(this);

        tvDaughter = (TextView) mDaughter.findViewById(R.id.tv_daughter);
        tvSon = (TextView) mSon.findViewById(R.id.tv_son);
        tvErxi = (TextView) mErxi.findViewById(R.id.tv_erxi);

        FragmentKinsfolkDaughter tab01 = new FragmentKinsfolkDaughter();
        FragmentKinsfolkSon tab02 = new FragmentKinsfolkSon();
        FragmentKinsfolkErxi tab03 = new FragmentKinsfolkErxi();

        mFragments.add(tab01);
        mFragments.add(tab02);
        mFragments.add(tab03);

        currentIndex = 0;

        newsListView = (ListView) myinflate.findViewById(R.id.lv_news);*/
    }


    /**
     * 点击事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
       /* switch (v.getId()) {
            case R.id.rl_daughter:
                currentIndex = 0;
                break;
            case R.id.rl_son:
                currentIndex = 1;
                break;
            case R.id.rl_erxi:
                currentIndex = 2;
                break;
        }*/
        mViewPager.setCurrentItem(currentIndex);
    }

    public List<Map<String,Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for(int i=0;i<10;i++)
        {
            map = new HashMap<String, Object>();
            map.put("news", "新闻新闻新闻新闻新闻新闻新闻新闻新闻");
            list.add(map);
        }
        return list;
    }

    //ViewHolder静态类
    static class ViewHolder
    {
        public TextView news;
    }

    public class MyAdapter extends BaseAdapter
    {
        private LayoutInflater mInflater = null;
        private MyAdapter(FragmentHomeOld context)
        {
            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
            this.mInflater = LayoutInflater.from(getContext());
        }

        @Override
        public int getCount() {
            //How many items are in the data set represented by this Adapter.
            //在此适配器中所代表的数据集中的条目数
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // Get the data item associated with the specified position in the data set.
            //获取数据集中与指定索引对应的数据项
            return position;
        }

        @Override
        public long getItemId(int position) {
            //Get the row id associated with the specified position in the list.
            //获取在列表中与指定索引对应的行id
            return position;
        }

        //Get a View that displays the data at the specified position in the data set.
        //获取一个在数据集中指定索引的视图来显示数据
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if(convertView == null)
            {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.item_homenews, null);
                holder.news = (TextView)convertView.findViewById(R.id.home_news);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.news.setText((String)data.get(position).get("news"));

            return convertView;
        }

    }
}