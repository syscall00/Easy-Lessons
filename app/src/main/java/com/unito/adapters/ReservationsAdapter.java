package com.unito.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.unito.models.entity.Reservation;
import com.unito.projium.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ViewHolder> {

    private List<Reservation> mData;
    private List<Reservation> mDataCopy;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    public ReservationsAdapter(Context context, List<Reservation> data) {
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
        if(text.isEmpty()) {
            mData.addAll(mDataCopy);
        }
        else {
            text = text.toLowerCase();
            for(Reservation r : mDataCopy) {
                if(r.getCourse_name().toLowerCase().contains(text) || r.getTutorFullName().toLowerCase().contains(text) )
                    mData.add(r);

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
    @NotNull @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_course, parent, false);

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
        Reservation user = mData.get(position);
        holder.headText.setText(user.getCourse_name());
        holder.tutorText.setText(user.getTutorFullName());
        holder.slotText.setText(user.getDisplayableSlot());


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
        TextView tutorText;
        TextView slotText;

        ViewHolder(View itemView) {
            super(itemView);
            headText = itemView.findViewById(R.id.course_name_label);
            tutorText = itemView.findViewById(R.id.course_tutor_label);
            slotText = itemView.findViewById(R.id.course_date_label);


            itemView.findViewById(R.id.imageButton).setOnClickListener(v -> {
                if (mClickListener != null)
                    mClickListener.onItemClick(v, mData.get(getAdapterPosition()), getAdapterPosition());

            });

        }
    }

    /**
     * Update the data adapter
     *
     * @param newData new data to be set
     */
    public void setData(List<Reservation> newData) {

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
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    /**
     * interface of listener. It provides one method:<br>
     * -onItemClick: it get triggered when the item is clicked and provides the view clicked,
     * the data linked to the item clicked and the position on the item clicked in the list.
     */
    public interface ItemClickListener {
        void onItemClick(View view, Reservation elem, int position);
    }

    class PostDiffCallback extends DiffUtil.Callback {

        private final List<Reservation> oldPosts;
        private final List<Reservation> newPosts;

        public PostDiffCallback(List<Reservation> oldPosts, List<Reservation> newPosts) {
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