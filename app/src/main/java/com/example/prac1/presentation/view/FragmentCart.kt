package com.example.prac1.presentation.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prac1.app.MyApplication
import com.example.prac1.databinding.FragmentCartBinding
import com.example.prac1.presentation.adapter.CartAdapter
import com.example.prac1.presentation.viewmodel.CartViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentCart : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        (requireActivity().application as MyApplication).appComponent.inject(this)

        cartViewModel = ViewModelProvider(this, viewModelFactory)[CartViewModel::class.java]
        cartAdapter = CartAdapter { flowerId ->
            cartViewModel.getItemById(flowerId)
        }

        lifecycleScope.launch {
            cartViewModel.cartItems.collect { items ->
                cartAdapter.flowerList = items
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.catalogItems.onEach {
                cartAdapter.notifyDataSetChanged()
            }.launchIn(this)
        }

        binding.recyclerViewCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}