package com.example.newsappmvvm.ui.fragments


import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappmvvm.R
import com.example.newsappmvvm.adapters.NewsAdapter
import com.example.newsappmvvm.databinding.FragmentSearchNewsBinding
import com.example.newsappmvvm.models.Article
import com.example.newsappmvvm.ui.NewsViewModel
import com.example.newsappmvvm.util.Constants
import com.example.newsappmvvm.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.newsappmvvm.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment: BaseFragment<NewsViewModel, FragmentSearchNewsBinding>() {


    lateinit var newsAdapter: NewsAdapter
    val TAG = "SearchNewsFragment"

    var isLoading = false
    var isLastPage = false
    var isScrolling = false


    override fun setupUI() {

        setupRecyclerView()

        var job: Job ? = null

        binding.etSearch.addTextChangedListener {
            job?.cancel()

            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)

                it?.let {
                    if (it.toString().isNotEmpty()){
                        viewModel.searchNews(it.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let { newsResponse->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchNewsPage == totalPages


                        if (isLastPage){
                            binding.rvSearchNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let {
                        Log.d(TAG, "Error: $it")
                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }
        })

    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter {article->
            listItemClicked(article)
        }

        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
    }

    private fun listItemClicked(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article",article)
        }
        try {
            val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragmentSearch(article)
            findNavController().navigate(
                action
            )
        } catch (e: Exception) {
            Log.d("SaveNewsFragment", "listItemClicked: ${e.message}")
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition +visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                viewModel.searchNews(binding.etSearch.text.toString())
                isScrolling = false
            }
        }
    }
    private fun hideProgressBar() {
        binding.paginationProgressBar.isVisible = false
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.isVisible = true
        isLoading = true
    }

    override fun getContentView(): Int {
        return R.layout.fragment_search_news
    }

    override fun getViewModelClass(): Class<NewsViewModel> {
        return NewsViewModel::class.java
    }

    /*lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
    }*/
}