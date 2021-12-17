package com.example.newsappmvvm.ui.fragments

import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsappmvvm.R
import com.example.newsappmvvm.databinding.FragmentArticleBinding

import com.example.newsappmvvm.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment: BaseFragment<NewsViewModel, FragmentArticleBinding>() {

    private val args by navArgs<ArticleFragmentArgs>()

    override fun setupUI() {
        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(binding.root,"Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }

    }

    override fun getContentView(): Int {
        return R.layout.fragment_article
    }

    override fun getViewModelClass(): Class<NewsViewModel> {
        return NewsViewModel::class.java
    }

    /*   lateinit var viewModel: NewsViewModel

       override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
           super.onViewCreated(view, savedInstanceState)
           viewModel = (activity as NewsActivity).viewModel
       }*/
}