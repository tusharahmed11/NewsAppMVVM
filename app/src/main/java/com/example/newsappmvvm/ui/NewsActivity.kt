package com.example.newsappmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsappmvvm.R
import com.example.newsappmvvm.databinding.ActivityNewsBinding
import com.example.newsappmvvm.db.ArticleDatabase
import com.example.newsappmvvm.repository.NewsRepository


class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)

        val newsRepository = NewsRepository(ArticleDatabase(this))

        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory)[NewsViewModel::class.java]


        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.navHostFragment))

    }
}