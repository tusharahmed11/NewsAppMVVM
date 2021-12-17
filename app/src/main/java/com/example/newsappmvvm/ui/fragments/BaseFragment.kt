package com.example.newsappmvvm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsappmvvm.BR
import com.example.newsappmvvm.db.ArticleDatabase
import com.example.newsappmvvm.repository.NewsRepository
import com.example.newsappmvvm.ui.NewsViewModel
import com.example.newsappmvvm.ui.NewsViewModelProviderFactory

abstract class BaseFragment<V : ViewModel, T : ViewDataBinding>: Fragment() {

    lateinit var binding: T
    lateinit var viewModel: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getContentView(), container, false)
        val view = binding.root
      //  viewModel = ViewModelProviders.of(this).get(getViewModelClass())

        val newsRepository = context?.let { ArticleDatabase(it) }?.let { NewsRepository(it) }

        val viewModelProviderFactory = newsRepository?.let { NewsViewModelProviderFactory(it) }
        viewModel = viewModelProviderFactory?.let { ViewModelProvider(this, it) }!![getViewModelClass()]


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    fun hideView(view: View){
        view.isVisible = false
    }

    fun showView(view: View){
        view.isVisible = true
    }

    abstract fun setupUI()

    abstract fun getContentView() : Int

    abstract fun getViewModelClass() : Class<V>
}