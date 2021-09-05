package com.baiganov.devlife.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.baiganov.devlife.R
import com.baiganov.devlife.adapters.PostAdapter
import com.baiganov.devlife.databinding.FragmentBaseBinding
import com.baiganov.devlife.models.ResultItem
import com.baiganov.devlife.util.Constants
import com.baiganov.devlife.util.Constants.Companion.SECTION_HOT
import com.baiganov.devlife.util.NetworkResult
import com.baiganov.devlife.viemodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HotFragment : Fragment() {

    private lateinit var mAdapter: PostAdapter
    private lateinit var mainViewModel: MainViewModel

    private var _binding: FragmentBaseBinding? = null
    private val binding get() = _binding!!
    private var page = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBaseBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        setupRecyclerView(layoutManager)
        setupClickListeners(layoutManager)

        mainViewModel.getData(SECTION_HOT, page++)
        mainViewModel.hotPostResponse.observe(viewLifecycleOwner, { response ->
            handleNetworkResult(response, layoutManager)
        })

        return binding.root
    }

    private fun handleNetworkResult(response: NetworkResult<List<ResultItem>>, layoutManager: LinearLayoutManager) {
        when(response) {
            is NetworkResult.Success -> {
                hideProgressBar()
                response.data?.let {
                    mAdapter.setData(it)
                    binding.recyclerView.smoothScrollToPosition(layoutManager.findLastCompletelyVisibleItemPosition() + 1)
                }
            }
            is NetworkResult.Error -> {
                hideProgressBar()
                if (response.message == "No Network Connection") {
                    showNoInternetConnection()
                } else {
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            is NetworkResult.Loading -> {
                showProgressBar()
            }
        }
    }

    private fun setupRecyclerView(layoutManager: LinearLayoutManager) {
        mAdapter = PostAdapter()
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = layoutManager
        val shapHelper = PagerSnapHelper()
        shapHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun setupClickListeners(layoutManager: LinearLayoutManager) {
        binding.btnBack.setOnClickListener {
            if (layoutManager.findLastCompletelyVisibleItemPosition() > 0) {
                binding.recyclerView.smoothScrollToPosition(layoutManager.findLastCompletelyVisibleItemPosition() - 1)
            }
            if (layoutManager.findLastCompletelyVisibleItemPosition() == 1) {
                makeNoActiveButton()
            }
        }

        binding.btnNext.setOnClickListener {
            if (layoutManager.findLastCompletelyVisibleItemPosition() == 0) {
                makeActiveButton()
            }
            if (layoutManager.findLastCompletelyVisibleItemPosition() < (mAdapter.itemCount - 1)) {
                binding.recyclerView.smoothScrollToPosition(layoutManager.findLastCompletelyVisibleItemPosition() + 1)
            }
            if (layoutManager.findLastCompletelyVisibleItemPosition() == (mAdapter.itemCount - 1)) {
                mainViewModel.getData(SECTION_HOT, page++)
            }
        }

        binding.btnRetry.setOnClickListener {
            mainViewModel.getData(Constants.SECTION_LATEST, --page)
        }

        binding.recyclerView.addOnScrollListener( object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position: Int = layoutManager.findFirstVisibleItemPosition()
                    if (position >= 1) {
                        makeActiveButton()
                    } else {
                        makeNoActiveButton()
                    }
                }
            }
        })
    }

    private fun hideProgressBar() {
        binding.apply {
            progressBar.visibility = View.GONE
        }
    }

    private fun showProgressBar() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            btnBack.visibility = View.VISIBLE
            btnNext.visibility = View.VISIBLE
            btnRetry.visibility = View.GONE
            imgCloud.visibility = View.GONE
            tvError.visibility = View.GONE
        }
    }

    private fun makeNoActiveButton() {
        binding.btnBack.alpha = 0.2f
        binding.btnBack.isClickable = false
    }

    private fun makeActiveButton() {
        binding.btnBack.alpha = 1f
        binding.btnBack.isClickable = true
    }

    private fun showNoInternetConnection() {
        binding.apply {
            recyclerView.visibility = View.GONE
            btnBack.visibility = View.GONE
            btnNext.visibility = View.GONE
            btnRetry.visibility = View.VISIBLE
            imgCloud.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
        }
    }
}