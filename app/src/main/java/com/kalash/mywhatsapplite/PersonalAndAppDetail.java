package com.kalash.mywhatsapplite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.kalash.mywhatsapplite.databinding.ActivityPersonalAndAppDetailBinding;

public class PersonalAndAppDetail extends AppCompatActivity {
ActivityPersonalAndAppDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPersonalAndAppDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String content=getIntent().getStringExtra("content");
        binding.detail.setText(content);

    }
}