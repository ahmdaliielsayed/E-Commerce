package com.ahmdalii.ecommerce.ui.categories.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ahmdalii.ecommerce.databinding.FragmentCategoryBinding
import com.ahmdalii.ecommerce.ui.categories.viewmodel.CategoriesViewModel
import com.ahmdalii.ecommerce.utils.ConnectionLiveData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null

    private val binding get() = _binding!!

    private val categoriesViewModel: CategoriesViewModel by viewModels()

    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)

        initiateObservers()
        initiateAdapter()

        binding.lifecycleOwner = this

        return binding.root
    }

    private fun initiateObservers() {
        ConnectionLiveData(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                categoriesViewModel.getCategoriesFromRemote()
            } else {
                Toast.makeText(requireContext(), "Connection Lost!", Toast.LENGTH_LONG).show()
            }
        }

        categoriesViewModel.navigateToSelectedCategory.observe(viewLifecycleOwner) { categoryName ->
            categoryName?.let {
                findNavController().navigate(
                    CategoryFragmentDirections.actionCategoryFragmentToProductFragment(categoryName)
                )
                categoriesViewModel.onCategoryItemNavigated()
            }
        }

        categoriesViewModel.errorMessageResponse.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
        }

        categoriesViewModel.categoriesResponse.observe(viewLifecycleOwner) { categoryList ->
            if (categoryList.isEmpty()) {
                binding.txtEmptyPage.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                adapter.submitList(categoryList)
                binding.txtEmptyPage.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }

        categoriesViewModel.progressLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) {
                 View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun initiateAdapter() {
        adapter = CategoryAdapter(
            CategoryListener { categoryName ->
                categoriesViewModel.onCategoryItemClicked(categoryName)
            }
        )
        binding.recyclerView.adapter = adapter
    }
}