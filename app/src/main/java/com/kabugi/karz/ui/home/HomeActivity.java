package com.kabugi.karz.ui.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kabugi.karz.R;

public class HomeActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

    }
}
//private ItemViewModel viewModel;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
//        viewModel.getSelectedItem().observe(this, item -> {
//            // Perform an action with the latest item data
//        });
//    }
//}
//
//public class ListFragment extends Fragment {
//    private ItemViewModel viewModel;
//
//    @Override
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
//
//        ...
//
//        items.setOnClickListener(item -> {
//            // Set a new item
//            viewModel.select(item);
//        });
//    }
//}