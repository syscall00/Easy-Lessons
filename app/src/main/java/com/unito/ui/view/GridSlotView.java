package com.unito.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.unito.projium.R;

import java.util.ArrayList;
import java.util.List;

// TODO: Scrivere nomi giusti.
public class GridSlotView extends ConstraintLayout {

    public static final int AVAILABLE = R.drawable.gridslot_reserved;
    public static final int RESERVED = R.drawable.gridslot_not_available;
    public static final int COMPLETED = R.drawable.gridslot_completed;
    public static final int NOT_AVAILABLE = R.drawable.gridslot_free;

    private final int SELECTOR = Color.parseColor("#522C2C2C");

    private int selection;

    private GridSlotViewListener listener;

    private List<ImageView> slots;

    public GridSlotView(Context context) {
        super(context);
        slots = new ArrayList<>();
        listener = null;
    }

    public GridSlotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
        slots = new ArrayList<>();
        listener = null;

    }

    public GridSlotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
        slots = new ArrayList<>();
        listener = null;
    }


    /**
     * Inflate the component view
     * @param context
     */
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.grid, this);

        selection = -1;
    }

    /**
     * @return number of the selected slot in the view
     */
    public int getSelection() {
        return selection;
    }

    /**
     * set the selected slot in the view
     * @param selection
     */
    public void setSelection(int selection) {

        if (this.selection >= 0)
            slots.get(this.selection).setColorFilter(null);
        if (selection >= 0) {
            slots.get(selection).setColorFilter(SELECTOR);
        }
        if (listener != null)
            listener.onSelectionChanged(selection);


        this.selection = selection;

    }

    /**
     * Change the color of a specific slot
     * @param position      the position of the slot to be colored
     * @param state         the state corresponding at the color
     */
    public void setColor(int position, int state) {
        ImageView i = slots.get(position);
        Drawable d = ContextCompat.getDrawable(getContext(), state);
        i.setImageDrawable(d);
    }


    /**
     * at the end of inflating view, set the click listener on every slot
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < 20; i++) {
            int finalI = i;

            String name = "slot" + i;

            int id = this.getContext().getResources().getIdentifier(name, "id", this.getContext().getPackageName());
            ImageView image = (ImageView) this.findViewById(id);
            image.setOnClickListener(v -> setSelection(finalI));
            slots.add(image);
        }
    }

    /**
     * set an onSelectionChanged listener
     * @param listener
     */
    public void setListener(GridSlotViewListener listener) {
        this.listener = listener;
    }


    /**
     * interface of listener. It provides one method:<br>
     * -onSelectionChanged: it get triggered when the selection item in the grid is changed and
     * provides the position of the slot.
     */
    public interface GridSlotViewListener {
        void onSelectionChanged(int selection);
    }

}
