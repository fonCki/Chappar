package com.example.chappar10.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.chappar10.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainFragment extends Fragment {
    EditText message;
    TextView viewMessage, viewList;
    MainFragmentViewModel viewModel;
    private FirebaseAuth mAuth;
    Button button;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);

        }

        public void onViewCreated(View view, Bundle savedInstanceState) {
            mAuth = FirebaseAuth.getInstance();
            button = view.findViewById(R.id.button);
            button.setOnClickListener(v -> {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            });
            viewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);
            viewModel.init();
            viewMessage = view.findViewById(R.id.textView3);
            viewList = view.findViewById(R.id.textView4);

            viewModel.getMessage().observe(this, message -> {
                if (message != null)
                    viewMessage.setText(message.getBody());
            });



            Button sendButton = view.findViewById(R.id.send);
            sendButton.setOnClickListener(v -> {
                message = view.findViewById(R.id.message);
                String messagev= message.getText().toString();
                Toast.makeText(getActivity(), messagev, Toast.LENGTH_SHORT).show();
                viewModel.saveMessage(messagev);
            });

//            NavigationView navigationView = getActivity().findViewById(R.id.item_logout);
//            navigationView.getMenu().findItem(R.id.item_logout).setOnMenuItemClickListener(menuItem -> {
//                Log.d("TAG", "onViewCreated: ");
//                return true;
//            });

        }

}