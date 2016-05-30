package com.android.test.freecourse.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import com.android.test.freecourse.Detail;
import com.android.test.freecourse.R;
import com.android.test.freecourse.model.Course;
import com.android.test.freecourse.listener.OnLoadMoreListener;

import java.util.List;

/**
 * MyRecyclerAdapter
 * <p></p>
 * @author ToanNDD
 * @version 1.0.0
 * created 2016/29/05
 * company bonsey
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder> {
    private List<Course> courseList;
    private Context mContext;
    //The minimum amount of items to have below your current scroll position
    //before loading more.
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public MyRecyclerAdapter(final Context context, List<Course> courseList, RecyclerView recyclerView) {
        this.courseList = courseList;
        this.mContext = context;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            loading = false;

            //add scroll feature to the recyclerView
            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            //Log.e("adapter", "onScrolled adapter called!");
                            super.onScrolled(recyclerView, dx, dy);
                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading &&
                                    totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                //End has been reached
                                //Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        Course feedItem = courseList.get(i);

        //Download image using picasso library
        //Picasso.with(mContext).load(feedItem.getThumbnail())
                //.error(R.drawable.placeholder)
                //.placeholder(R.drawable.placeholder)
                //.into(customViewHolder.imageView);

        //Setting text view title
        customViewHolder.textView.setText(Html.fromHtml(feedItem.getTitle()));


        //Handle click event on both title and image click
        customViewHolder.textView.setOnClickListener(clickListener);
        customViewHolder.imageView.setOnClickListener(clickListener);

        customViewHolder.textView.setTag(customViewHolder);
        customViewHolder.imageView.setTag(customViewHolder);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CustomViewHolder holder = (CustomViewHolder) view.getTag();
            int position = holder.getPosition();

            Course feedItem = courseList.get(position);
            //Toast.makeText(mContext, feedItem.getTitle(), Toast.LENGTH_SHORT).show();

            //Starting detail activity
            Intent in = new Intent(holder.context,
                    Detail.class);
            in.putExtra("title", feedItem.getTitle());
            in.putExtra("description", feedItem.getDescription());
            //in.putExtra("thumbnail", feedItem.getThumbnail());
            holder.context.startActivity(in);
        }
    };

    @Override
    public int getItemCount() {
        return (null != courseList ? courseList.size() : 0);
    }

    //Clean all elements of the recycler
    public void clear() {
        courseList.clear();
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;
        protected Context context;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.textView = (TextView) view.findViewById(R.id.title);
            this.context = view.getContext();
        }
    }
}