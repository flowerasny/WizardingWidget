package com.flowerasny.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.flowerasny.widget.databinding.FragmentFirstBinding
import com.flowerasny.widget.dogs.ElixirsRepository
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonInvalidateData.setOnClickListener {
            val widgetManager = AppWidgetManager.getInstance(requireContext())
            widgetManager.getAppWidgetIds(ComponentName(requireContext(), MyAppWidgetProvider::class.java))
                .forEach {
                    widgetManager.notifyAppWidgetViewDataChanged(it, R.id.lvItems)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
