package com.example.tickets.ui.subscribes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tickets.databinding.FragmentSubscribesBinding

class SubscribesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSubscribesBinding.inflate(inflater, container, false).root
}