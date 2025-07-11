

package com.duckduckgo.common.ui.themepreview.ui.component

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duckduckgo.mobile.android.R

@SuppressLint("NoFragment") // we don't use DI here
abstract class ComponentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_component_list, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceBundle: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceBundle)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val adapter = ComponentAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        adapter.submitList(getComponents())
    }

    abstract fun getComponents(): List<Component>
}
