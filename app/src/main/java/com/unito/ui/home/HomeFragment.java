package com.unito.ui.home;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unito.Provider;
import com.unito.adapters.ReservationsAdapter;
import com.unito.models.entity.Reservation;
import com.unito.projium.R;
import com.unito.projium.databinding.FragmentHomeBinding;
import com.unito.viewmodel.ReservationViewModel;
import com.unito.viewmodel.UserViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public static final String VIEW_ARG = "viewType";

    private ReservationsAdapter upcomingReservationAdap;
    private ReservationsAdapter completedReservationAdap;
    private ReservationsAdapter deletedReservationAdap;

    private CourseItemListener upcomingRecyclerListener;
    private CourseItemListener completedRecyclerListener;
    private CourseItemListener deletedRecyclerListener;

    private ReservationViewModel reservationViewModel;
    private UserViewModel userViewModel;

    private FragmentHomeBinding binding;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureViewModel();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BottomNavigationView) getActivity().findViewById(R.id.nav_view)).setVisibility(View.VISIBLE);


        /* Initialize click listeners */
        initListener();

        /* initialize recycler view components */
        initRecyclersView();


    }


    /**
     * Instantiate ViewModels and set up observers
     */
    private void configureViewModel() {

        /* Instantiate ReservationViewModel and UserViewModel */
        Provider mia = Provider.getProvider(getActivity().getApplication());
        reservationViewModel = new ViewModelProvider(requireActivity()).get(ReservationViewModel.class);
        reservationViewModel.setRepo(mia.rr);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setRepo(mia.ur);

        String username = userViewModel.getLoggedUser();
        binding.setUser(username);

        /* Get upcoming lessons for selected user and set the UI properly */
        reservationViewModel.getReservationByUser(username,
                Reservation.RESERVATION_STATUS_RESERVED).observe(getViewLifecycleOwner(), reservations -> {

            /* Limit list display to 2 elements */
            upcomingReservationAdap.setData(reservations.size() > 2 ? reservations.subList(0, 2) : reservations);

            binding.setUpcomingListSize(reservations.size());

        });

        /* Get completed lessons for selected user and set the UI properly */
        reservationViewModel.getReservationByUser(username, Reservation.RESERVATION_STATUS_COMPLETED)
                .observe(getViewLifecycleOwner(), reservations -> {
                    /* Limit list display to 2 elements */
                    completedReservationAdap.setData(reservations.size() > 2 ? reservations.subList(0, 2) : reservations);

                    binding.setCompletedListSize(reservations.size());
                });

        /* Get deleted lessons for selected user and set the UI properly */
        reservationViewModel.getReservationByUser(username, Reservation.RESERVATION_STATUS_DELETE)
                .observe(getViewLifecycleOwner(), reservations -> {

                    /* Limit list display to 2 elements */
                    deletedReservationAdap.setData(reservations.size() > 2 ? reservations.subList(0, 2) : reservations);

                    binding.setDeletedListSize(reservations.size());
                });

    }

    /**
     * Initialize label onclick listeners
     */
    private void initListener() {
        /* On click "show all" on upcoming card */
        binding.showReservedLabel.setOnClickListener((View.OnClickListener) v ->
                navigateToShowAll(Reservation.RESERVATION_STATUS_RESERVED));

        /* On click "show all" on completed card */
        binding.showCompletedLabel.setOnClickListener((View.OnClickListener) v ->
                navigateToShowAll(Reservation.RESERVATION_STATUS_COMPLETED));

        /* On click "show all" on deleted card */
        binding.showDeletedLabel.setOnClickListener((View.OnClickListener) v ->
                navigateToShowAll(Reservation.RESERVATION_STATUS_DELETE));

    }

    /**
     * Initialize all recyclerViews components.<br>
     * Initialize adapters.<br>
     * Initialize recyclerViews listener.<br>
     */
    private void initRecyclersView() {

        int orientation = getResources().getConfiguration().orientation;

        /* Create adapter for 'Upcoming lessons' */
        upcomingReservationAdap = new ReservationsAdapter(getContext(), new ArrayList<>());
        upcomingRecyclerListener = new CourseItemListener();
        upcomingReservationAdap.setClickListener(upcomingRecyclerListener);


        /* Initialize recyclerview for 'Upcoming lessons'  */
        binding.recyclerReserved.addItemDecoration(new DividerItemDecoration(getContext(),
                orientation));
        binding.recyclerReserved.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerReserved.setAdapter(upcomingReservationAdap);

        /* Create adapter for 'Upcoming lessons' */
        completedReservationAdap = new ReservationsAdapter(getContext(), new ArrayList<>());
        completedRecyclerListener = new CourseItemListener();
        completedReservationAdap.setClickListener(completedRecyclerListener);

        /* Initialize recyclerview for 'Upcoming lessons'  */
        binding.recyclerCompleted.addItemDecoration(new DividerItemDecoration(getContext(),
                orientation));
        binding.recyclerCompleted.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerCompleted.setAdapter(completedReservationAdap);

        /* Create adapter for 'Deleted lessons' */
        deletedReservationAdap = new ReservationsAdapter(getContext(), new ArrayList<>());
        deletedRecyclerListener = new CourseItemListener();
        deletedReservationAdap.setClickListener(deletedRecyclerListener);

        /* Initialize recyclerview for 'Deleted lessons'  */
        binding.deletedRecycler.addItemDecoration(new DividerItemDecoration(getContext(),
                orientation));
        binding.deletedRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.deletedRecycler.setAdapter(deletedReservationAdap);
    }


    /**
     * Navigate to showAllFragment
     * @param viewType      indicates the type of courses the view should visualize. Could be<br>
     *                      RESERVATION_STATUS_RESERVED: All reserved courses<br>
     *                      RESERVATION_STATUS_COMPLETED: All completed courses<br>
     *                      RESERVATION_STATUS_DELETE: All deleted courses<br>
     */
    private void navigateToShowAll(String viewType) {

        Bundle bundle = new Bundle();
        bundle.putString(VIEW_ARG, viewType);
        ((BottomNavigationView) getActivity().findViewById(R.id.nav_view)).setVisibility(View.INVISIBLE);
        NavHostFragment.findNavController(HomeFragment.this).
                navigate(R.id.action_navigation_dashboard_to_navitagion_showAll, bundle);
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