package com.unito.ui.home;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unito.adapters.ReservationsAdapter;
import com.unito.models.entity.Reservation;
import com.unito.Provider;
import com.unito.projium.R;
import com.unito.viewmodel.ReservationViewModel;
import com.unito.viewmodel.UserViewModel;

import java.util.ArrayList;

public class ShowCoursesFragment extends Fragment {


    private RecyclerView recyclerView;
    private ReservationViewModel reservationViewModel;
    private ReservationsAdapter.ItemClickListener reservationListener;
    private UserViewModel userViewModel;
    private ReservationsAdapter adap;

    private String viewType;

    public ShowCoursesFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureViewModel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            viewType = getArguments().getString(HomeFragment.VIEW_ARG);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        menu.findItem(R.id.disconnect).setVisible(false);
        inflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        /* Handle filtering on recyclerview */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adap.filter(newText);
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_show_courses, container, false);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.showAll_recycler);

        initRecycler();
    }


    /**
     * Instantiate ViewModels and set up observers
     */
    private void configureViewModel() {
        Provider mia = Provider.getProvider(Provider.a);
        reservationViewModel = new ViewModelProvider(requireActivity()).get(ReservationViewModel.class);
        reservationViewModel.setRepo(mia.rr);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setRepo(mia.ur);

        reservationViewModel.getReservationByUser(userViewModel.getLoggedUser(), viewType)
                .observe(getViewLifecycleOwner(), reservations -> adap.setData(reservations));

    }

    /**
     * Initialize the recyclerView.
     */
    private void initRecycler() {
        recyclerView.addItemDecoration(new DividerItemDecoration(
                recyclerView.getContext(), getResources().getConfiguration().orientation));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adap = new ReservationsAdapter(getContext(), new ArrayList<>());
        reservationListener = new CourseItemListener();
        adap.setClickListener(reservationListener);
        recyclerView.setAdapter(adap);
    }

    /**
     *  Click handler which open a menu with operation on each lesson
     */
    private class CourseItemListener implements ReservationsAdapter.ItemClickListener {

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onItemClick(View view, Reservation mData, int position) {

            PopupMenu popup = new PopupMenu(getContext(), view);
            popup.setOnMenuItemClickListener(item -> {

                boolean haveMatched = true;

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                DialogInterface.OnClickListener dialogClickListener = null;
                String title = "";
                String message = getString(R.string.you_sure);
                switch (item.getItemId()) {
                    case R.id.completed:
                        dialogClickListener = (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                mData.setReservationStatus(Reservation.RESERVATION_STATUS_COMPLETED);
                                reservationViewModel.updateReservation(mData);
                            }
                        };
                        title = getString(R.string.dialog_completed);
                        break;
                    case R.id.delete:
                        dialogClickListener = (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE)
                                reservationViewModel.deleteReservation(mData);
                        };
                        title = getString(R.string.dialog_deleted);
                        break;
                    case R.id.reserved:
                        dialogClickListener = (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                mData.setReservationStatus(Reservation.RESERVATION_STATUS_RESERVED);
                                reservationViewModel.updateReservation(mData);
                            }
                        };
                        title = getString(R.string.dialog_reserved);
                        break;

                    case R.id.hide:
                        dialogClickListener = (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                mData.setReservationStatus(Reservation.RESERVATION_STATUS_HIDE);
                                reservationViewModel.updateReservation(mData);
                            }
                        };
                        title = getString(R.string.dialog_remove);
                        message = getString(R.string.hide_description);
                        break;
                    default:
                        haveMatched = false;

                }
                if (haveMatched)
                    builder.setMessage(message).setPositiveButton(R.string.yes, dialogClickListener)
                            .setTitle(title).setNegativeButton(R.string.no, dialogClickListener).show();

                return haveMatched;
            });

            popup.inflate(getMenu(mData.getReservationStatus()));
            popup.show();

        }

        private int getMenu(String reservationStatus) {
            int menu;
            switch (reservationStatus) {
                case Reservation.RESERVATION_STATUS_RESERVED:
                    menu = R.menu.courses_reserved_menu;
                    break;
                case Reservation.RESERVATION_STATUS_COMPLETED:
                    menu = R.menu.course_completed_menu;
                    break;
                case Reservation.RESERVATION_STATUS_DELETE:
                    menu = R.menu.course_deleted_menu;
                    break;
                default:
                    menu = -1;
                    break;
            }
            return menu;

        }

    }
}