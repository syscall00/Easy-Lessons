package com.unito.ui.courses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unito.Provider;
import com.unito.adapters.TutorsAdapter;
import com.unito.projium.R;
import com.unito.viewmodel.TutorViewModel;

import java.util.ArrayList;

public class CoursesFragment extends Fragment {

    private TutorsAdapter tutorsAdapter;
    private RecyclerView recyclerTutor;
    private TutorViewModel tutorViewModel;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BottomNavigationView) getActivity().findViewById(R.id.nav_view)).setVisibility(View.VISIBLE);

        recyclerTutor = getActivity().findViewById(R.id.recycler_tutor);

        initRecyclerView();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        /* Handle filtering on recyclerview */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}

            @Override
            public boolean onQueryTextChange(String newText) {
                tutorsAdapter.filter(newText);
                return false;
            }
        });
    }

    /**
     * Instantiate ViewModel and set up observer
     */
    private void configureViewModel() {
        Provider mia = Provider.getProvider(getActivity().getApplication());

        tutorViewModel = new ViewModelProvider(requireActivity()).get(TutorViewModel.class);
        tutorViewModel.setRepo(mia.tr);

        tutorViewModel.getAssociations().observe(getViewLifecycleOwner(), item -> tutorsAdapter.setData(item));
    }


    /**
     * initialize main recyclerView and set up listeners
     */
    private void initRecyclerView() {
        tutorsAdapter = new TutorsAdapter(getContext(), new ArrayList<>());

        recyclerTutor.addItemDecoration(new DividerItemDecoration(recyclerTutor.getContext(),
                getResources().getConfiguration().orientation));
        recyclerTutor.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerTutor.setAdapter(tutorsAdapter);

        tutorsAdapter.setClickListener((view, mData, position) -> {
            /* Hide nav bar */
            getActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);

            /* Pass selected tutorID to tutorFragment */
            tutorViewModel.setTutor(mData.getTutor());

            /* Open tutor fragment */
            NavHostFragment.findNavController(CoursesFragment.this).
                    navigate(R.id.action_navigation_courses_to_navigation_tutor);

        });



    }
}