package com.unito.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.unito.models.entity.Association;
import com.unito.models.entity.Course;
import com.unito.models.entity.Tutor;
import com.unito.projium.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TutorsAdapter extends RecyclerView.Adapter<TutorsAdapter.ViewHolder> {

    private List<Association> mData;
    private List<Association> mDataCopy;

    private LayoutInflater mInflater;
    private OnItemClickListener mClickListener;


    public TutorsAdapter(Context context, List<Association> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }


    /**
     * filter the mData list with a input pattern
     *
     * @param text      input filter pattern
     */
    public void filter(String text) {
        mData.clear();
        if (text.isEmpty()) {
            mData.addAll(mDataCopy);
        } else {
            text = text.toLowerCase();
            for (Association a : mDataCopy) {
                for(Course c : a.getCourses()) {
                    if(!mData.contains(a) && c.getCourseName().toLowerCase().contains(text) )
                        mData.add(a);
                }
                if (!mData.contains(a) && a.getTutor().getTutorCompleteName().toLowerCase().contains(text))
                    mData.add(a);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * inflates the row layout from xml when needed
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_tutor, parent, false);
        return new ViewHolder(view);
    }


    /**
     * binds the data to the TextView in each row
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Association association = mData.get(position);
        Tutor t = association.getTutor();

        holder.headText.setText(t.getTutorCompleteName());
        holder.slotText.setText(association.getFormattedCourses());


        holder.itemView.setOnClickListener(v -> {
            if (mClickListener != null)
                mClickListener.onItemClick(v, mData.get(position), position);

        });
    }


    /**
     * total number of rows
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * stores and recycles views as they are scrolled off screen
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView headText;
        TextView slotText;

        ViewHolder(View itemView) {
            super(itemView);
            headText = itemView.findViewById(R.id.course_name_label);
            slotText = itemView.findViewById(R.id.course_date_label);
        }
    }

    /**
     * Update the data adapter
     *
     * @param newData new data to be set
     */
    public void setData(List<Association> newData) {
        if (mData != null) {
            PostDiffCallback postDiffCallback = new PostDiffCallback(mData, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);
            mData.clear();
            mData.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        } else {
            mData = newData;
        }
        mDataCopy = new ArrayList<>(mData);

    }


    /**
     * set an onitemclick listener
     *
     * @param itemClickListener listener to be set
     */
    public void setClickListener(OnItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    /**
     * interface of listener. It provides one method:<br>
     * -onItemClick: it get triggered when the item is clicked and provides the view clicked,
     * the data linked to the item clicked and the position on the item clicked in the list.
     */
    public interface OnItemClickListener {
        void onItemClick(View view, Association mData, int position);
    }

    class PostDiffCallback extends DiffUtil.Callback {

        private final List<Association> oldPosts;
        private final List<Association> newPosts;

        public PostDiffCallback(List<Association> oldPosts, List<Association> newPosts) {
            this.oldPosts = oldPosts;
            this.newPosts = newPosts;
        }

        @Override
        public int getOldListSize() {
            return oldPosts.size();
        }

        @Override
        public int getNewListSize() {
            return newPosts.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).equals(newPosts.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).equals(newPosts.get(newItemPosition));
        }
    }
}