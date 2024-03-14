package com.ahmdalii.ecommerce.ui.products.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ahmdalii.ecommerce.databinding.FragmentProductBinding
import com.ahmdalii.ecommerce.ui.categories.view.CategoryAdapter
import com.ahmdalii.ecommerce.ui.categories.view.CategoryFragmentDirections
import com.ahmdalii.ecommerce.ui.categories.view.CategoryListener
import com.ahmdalii.ecommerce.ui.products.viewmodel.ProductsViewModel
import com.ahmdalii.ecommerce.utils.ConnectionLiveData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private val productsViewModel: ProductsViewModel by viewModels()

    private lateinit var adapter: ProductAdapter

    private val args by navArgs<ProductFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)

        initiateObservers()
        initiateAdapter()

        binding.lifecycleOwner = this

        return binding.root
    }

    private fun initiateObservers() {
        ConnectionLiveData(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                productsViewModel.getProductsFromRemote(args.categoryName)
            } else {
                Toast.makeText(requireContext(), "Connection Lost!", Toast.LENGTH_LONG).show()
            }
        }

        productsViewModel.navigateToSelectedProductDetails.observe(viewLifecycleOwner) { product ->
            product?.let {
//                findNavController().navigate(
//                    CategoryFragmentDirections.actionCategoryFragmentToProductFragment(categoryName)
//                )
                Toast.makeText(requireContext(), product.title, Toast.LENGTH_LONG).show()
                productsViewModel.onProductItemNavigated()
            }
        }

        productsViewModel.errorMessageResponse.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
        }

        productsViewModel.productsResponse.observe(viewLifecycleOwner) { productList ->
            if (productList.isEmpty()) {
                binding.txtEmptyPage.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                adapter.submitList(productList)
                binding.txtEmptyPage.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }

        productsViewModel.progressLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun initiateAdapter() {
        adapter = ProductAdapter(
            ProductListener { product ->
                productsViewModel.onProductItemClicked(product)
            }
        )
        binding.recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsViewModel.getLocalProducts(args.categoryName)
    }
}