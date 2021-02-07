package com.unito.ui.courses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.unito.ui.view.GridSlotView;
import com.unito.Provider;
import com.unito.models.entity.Course;
import com.unito.models.entity.Reservation;
import com.unito.models.entity.Tutor;
import com.unito.projium.R;
import com.unito.projium.databinding.FragmentTutorBinding;
import com.unito.viewmodel.ReservationViewModel;
import com.unito.viewmodel.TutorViewModel;
import com.unito.viewmodel.UserViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


public class TutorFragment extends Fragment {

    private List<Reservation> reservationList;

    private UserViewModel userViewModel;
    private ReservationViewModel reservationViewModel;
    private TutorViewModel tutorViewModel;

    private String username;

    private String[] spinnerAdapter;
    private FragmentTutorBinding binding;


    public TutorFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureViewModel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.findItem(R.id.disconnect).setVisible(false);

        inflater.inflate(R.menu.tutor_help_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            getActivity().onBackPressed();
        else if (item.getItemId() == R.id.help)
            initSequence().start();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        binding = FragmentTutorBinding.inflate(inflater, container, false);
        initHandler();


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (TutorialPreferences.getTutorialStatus(getContext())) {
            initSequence().start();
            TutorialPreferences.storeTutorialStatus(getContext(), false);
        }
    }

    /**
     * Initialize all view's handlers
     */
    private void initHandler() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        /* Handle click on reserveButton */
        binding.reserveButton.setOnClickListener(v -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    operationsHandler(0);
                }
            };
            builder.setMessage(R.string.you_sure).setPositiveButton(R.string.yes, dialogClickListener)
                    .setTitle(R.string.dialog_reserved).setNegativeButton(R.string.no, dialogClickListener).show();
        });

        /* Handle click on completeButton */
        binding.completeButton.setOnClickListener(v -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    operationsHandler(1);
                }
            };
            builder.setMessage(R.string.you_sure).setPositiveButton(R.string.yes, dialogClickListener)
                    .setTitle(R.string.dialog_completed).setNegativeButton(R.string.no, dialogClickListener).show();
        });

        /* Handle click on deleteButton */
        binding.deleteButton.setOnClickListener(v -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    operationsHandler(2);
                }
            };
            builder.setMessage(R.string.you_sure).setPositiveButton(R.string.yes, dialogClickListener)
                    .setTitle(R.string.dialog_deleted).setNegativeButton(R.string.no, dialogClickListener).show();
        });

        /* Handle click on backToReservedButton */
        binding.backToReserved.setOnClickListener(v -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    operationsHandler(3);
                }
            };
            builder.setMessage(R.string.you_sure).setPositiveButton(R.string.yes, dialogClickListener)
                    .setNegativeButton(R.string.no, dialogClickListener).show();
        });


        /* Handle click on GridSlotView components*/
        binding.slotHour.setListener(selection -> {
            Reservation r = null;
            if (selection >= 0) {

                r = reservationList.get(selection);

                binding.spinner.setEnabled(r.isFree());

                if (r.isReserved() || r.isCompleted())
                    if (r.getUsername().equals(username)) {
                        binding.spinner.setSelection(Arrays.asList(spinnerAdapter).
                                indexOf(r.getCourse_name()));
                    } else {
                        r = null;
                        Toast.makeText(getContext(), R.string.not_available_slot, Toast.LENGTH_SHORT).show();
                    }
            }

            /* Edit view variable for update the binded UI */
            binding.setRes(r);

        });

    }


    /**
     * Handle all operations buttons
     *
     * @param op Operation should be executed. Could be<br>
     *           0: Click on reserveButton - Reserve the selected lesson<br>
     *           1: Click on completeButton - Set as completed the selected lessons<br>
     *           2: Click on deleteButton - Delete the selected reservation<br>
     *           3: Click on backAtReservedButton - Reset the reservation
     */
    private void operationsHandler(int op) {
        int checkedPosition = binding.slotHour.getSelection();
        if (checkedPosition >= 0) {
            Reservation clickedReservation = reservationList.get(checkedPosition);
            switch (op) {

                case 0:
                    if (clickedReservation.isFree()) {
                        clickedReservation.setReservationStatus(Reservation.RESERVATION_STATUS_RESERVED);
                        clickedReservation.setUsername(username);
                        clickedReservation.setCourse_name(binding.spinner.getSelectedItem().toString());
                        reservationViewModel.updateReservation(clickedReservation);
                    }
                    break;
                case 1:
                    if (clickedReservation.isReserved()) {
                        clickedReservation.setReservationStatus(Reservation.RESERVATION_STATUS_COMPLETED);
                        reservationViewModel.updateReservation(clickedReservation);
                    }
                    break;
                case 2:
                    if (clickedReservation.isReserved()) {
                        reservationViewModel.deleteReservation(clickedReservation);
                    }
                    break;
                case 3:
                    if (clickedReservation.isCompleted()) {
                        clickedReservation.setReservationStatus(Reservation.RESERVATION_STATUS_RESERVED);
                        reservationViewModel.updateReservation(clickedReservation);
                    }
                    break;
            }
            binding.slotHour.setSelection(-1);
        }
    }


    /**
     * Instantiate ViewModels and set up observers
     */
    private void configureViewModel() {
        Provider mia = Provider.getProvider(getActivity().getApplication());

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setRepo(mia.ur);

        reservationViewModel = new ViewModelProvider(requireActivity()).get(ReservationViewModel.class);
        reservationViewModel.setRepo(mia.rr);

        tutorViewModel = new ViewModelProvider(requireActivity()).get(TutorViewModel.class);
        tutorViewModel.setRepo(mia.tr);

        username = userViewModel.getLoggedUser();
        Tutor tutor = tutorViewModel.getTutor();

        binding.tutorNameLabel.setText(tutor.getTutorCompleteName());


        /* get all tutor courses and populate the spinner */
        tutorViewModel.getAssociationByTutor(tutor.getID()).observe(getViewLifecycleOwner(),
                item -> {
                    if (item == null) return;
                    List<Course> courses = item.getCourses();
                    spinnerAdapter = new String[courses.size()];
                    for (int i = 0; i < courses.size(); i++) {
                        spinnerAdapter[i] = courses.get(i).getCourseName();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, spinnerAdapter);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinner.setAdapter(adapter);
                });

        /* get all tutor reservations and populate the GridSlotView */
        reservationViewModel.getReservationsByTutor(tutor.getID()).observe(getViewLifecycleOwner(), item -> {
            if (item != null) {
                reservationList = item;
                for (int i = 0; i < 20; i++)
                    binding.slotHour.setColor(i, getStatus(item.get(i)));
            }
        });

    }

    /**
     * Returns an int resource id corresponding the reservation state of given reservation r.
     *
     * @param r a reservation
     * @return an int resource id corresponding a drawable representing the state of given reservation
     */
    private int getStatus(Reservation r) {
        if (r.isFree())
            return GridSlotView.AVAILABLE;
        else if (r.isReserved() && username.equals(r.getUsername()))
            return GridSlotView.RESERVED;
        else if (r.isCompleted() && username.equals(r.getUsername()))
            return GridSlotView.COMPLETED;
        else
            return GridSlotView.NOT_AVAILABLE;
    }

    /**
     * Initialize a TapTargetSequence which contains a tutorial for the view usage
     * You can start this sequence whit .start()
     *
     * @return a TapTargetSequence sequence already initialized
     */
    private TapTargetSequence initSequence() {
        View view = getView();
        View slot0 = view.findViewById(R.id.slot0);
        TapTargetSequence sequence = new TapTargetSequence(getActivity())
                .targets(
                        // Set attributes for each TapTarget
                        TapTarget.forView(binding.spinner, getString(R.string.tap1_title), getString(R.string.tap1_description))
                                .transparentTarget(true).id(0).drawShadow(true).outerCircleColor(R.color.secondaryColor)
                                .textTypeface(Typeface.SANS_SERIF).titleTextSize(18).descriptionTextSize(14).dimColor(R.color.black),

                        TapTarget.forView(slot0, getString(R.string.tap2_title), getString(R.string.tap2_description))
                                .transparentTarget(true).id(1).drawShadow(true).outerCircleColor(R.color.secondaryColor)
                                .textTypeface(Typeface.SANS_SERIF).titleTextSize(18).descriptionTextSize(14).dimColor(R.color.black),

                        TapTarget.forView(binding.reserveButton, getString(R.string.tap3_title), getString(R.string.tap3_description))
                                .transparentTarget(true).id(2).drawShadow(true).outerCircleColor(R.color.secondaryColor)
                                .textTypeface(Typeface.SANS_SERIF).titleTextSize(18).descriptionTextSize(14).dimColor(R.color.black),

                        TapTarget.forView(slot0, getString(R.string.tap4_title), getString(R.string.tap4_description))
                                .transparentTarget(true).drawShadow(true).outerCircleColor(R.color.secondaryColor)
                                .textTypeface(Typeface.SANS_SERIF).titleTextSize(18).descriptionTextSize(14).dimColor(R.color.black),

                        TapTarget.forView(binding.completeButton, getString(R.string.tap5_title), getString(R.string.tap5_description))
                                .transparentTarget(true).id(4).drawShadow(true).outerCircleColor(R.color.secondaryColor)
                                .textTypeface(Typeface.SANS_SERIF).titleTextSize(18).descriptionTextSize(14).dimColor(R.color.black),

                        TapTarget.forView(slot0, getString(R.string.tap6_title), getString(R.string.tap6_description))
                                .transparentTarget(true).drawShadow(true).outerCircleColor(R.color.secondaryColor)
                                .textTypeface(Typeface.SANS_SERIF).titleTextSize(18).descriptionTextSize(14).dimColor(R.color.black),

                        TapTarget.forView(binding.backToReserved, getString(R.string.tap7_title), getString(R.string.tap7_description))
                                .transparentTarget(true).id(6).drawShadow(true).outerCircleColor(R.color.secondaryColor)
                                .textTypeface(Typeface.SANS_SERIF).titleTextSize(18).descriptionTextSize(14).dimColor(R.color.black),

                        TapTarget.forView(slot0, getString(R.string.tap8_title), getString(R.string.tap8_description))
                                .transparentTarget(true).drawShadow(true).outerCircleColor(R.color.secondaryColor)
                                .textTypeface(Typeface.SANS_SERIF).titleTextSize(18).descriptionTextSize(14).dimColor(R.color.black),

                        TapTarget.forView(binding.deleteButton, getString(R.string.tap9_title), getString(R.string.tap9_description))
                                .transparentTarget(true).id(8).drawShadow(true).outerCircleColor(R.color.secondaryColor)
                                .textTypeface(Typeface.SANS_SERIF).titleTextSize(18).descriptionTextSize(14).dimColor(R.color.black),

                        TapTarget.forView(slot0, getString(R.string.tap10_title), getString(R.string.tap10_description))
                                .transparentTarget(true).drawShadow(true).outerCircleColor(R.color.secondaryColor)
                                .textTypeface(Typeface.SANS_SERIF).titleTextSize(18).descriptionTextSize(14).dimColor(R.color.black)
                )
                // Set TapTargetSequence listener
                .listener(new TapTargetSequence.Listener() {

                    /* At the end of sequence, reset all changes made during tutorial */
                    @Override
                    public void onSequenceFinish() {
                        binding.slotHour.setColor(0, getStatus(reservationList.get(0)));
                        binding.slotHour.setSelection(-1);


                    }



                    /* For each step, set up the UI for the tutorial. */
                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        switch (lastTarget.id()) {
                            case 0:
                                binding.reserveButton.setVisibility(View.VISIBLE);
                                binding.completeButton.setVisibility(View.GONE);
                                binding.deleteButton.setVisibility(View.GONE);
                                binding.backToReserved.setVisibility(View.GONE);
                                binding.slotHour.setColor(0, GridSlotView.AVAILABLE);
                                break;
                            case 1:
                                binding.slotHour.setSelection(0);
                                break;
                            case 2:
                                binding.slotHour.setColor(0, GridSlotView.RESERVED);
                                binding.reserveButton.setVisibility(View.GONE);
                                binding.completeButton.setVisibility(View.VISIBLE);
                                binding.deleteButton.setVisibility(View.VISIBLE);
                                binding.backToReserved.setVisibility(View.GONE);

                                break;
                            case 4:
                                binding.slotHour.setColor(0, GridSlotView.COMPLETED);
                                binding.backToReserved.setVisibility(View.VISIBLE);
                                binding.completeButton.setVisibility(View.GONE);
                                binding.deleteButton.setVisibility(View.GONE);
                                binding.reserveButton.setVisibility(View.GONE);

                                break;
                            case 6:
                                binding.slotHour.setColor(0, GridSlotView.RESERVED);
                                binding.backToReserved.setVisibility(View.GONE);
                                binding.completeButton.setVisibility(View.VISIBLE);
                                binding.deleteButton.setVisibility(View.VISIBLE);
                                binding.reserveButton.setVisibility(View.GONE);

                                break;
                            case 8:
                                binding.slotHour.setColor(0, GridSlotView.AVAILABLE);
                                binding.slotHour.setSelection(-1);
                                break;
                            default:
                                break;
                        }

                    }

                    /* In case the user doesn't complete the sequence, reset all changes  */
                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        binding.slotHour.setColor(0, getStatus(reservationList.get(0)));
                        binding.slotHour.setSelection(-1);

                    }
                });

        return sequence;

    }

    /**
     * A preference handler class. It is defined here because I will not use anywhere by
     * now, but if I would tutorial in others view, I could consider to do a public class
     */
    private static class TutorialPreferences {
        private static void storeTutorialStatus(Context context, boolean show) {
            SharedPreferences preferences = context.getSharedPreferences("showTutorial", Context.MODE_PRIVATE);
            preferences.edit().putBoolean("show", show).apply();
        }

        private static boolean getTutorialStatus(Context context) {
            SharedPreferences preferences = context.getSharedPreferences("showTutorial", Context.MODE_PRIVATE);
            return preferences.getBoolean("show", true);
        }
    }

}
