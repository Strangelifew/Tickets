package com.example.tickets.ui.short_path

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tickets.databinding.FragmentShortPathBinding

class ShortPathFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentShortPathBinding.inflate(inflater, container, false).root
}