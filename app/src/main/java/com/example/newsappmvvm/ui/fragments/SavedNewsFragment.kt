package com.example.newsappmvvm.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappmvvm.R
import com.example.newsappmvvm.adapters.NewsAdapter
import com.example.newsappmvvm.databinding.FragmentSavedNewsBinding
import com.example.newsappmvvm.models.Article
import com.example.newsappmvvm.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment: BaseFragment<NewsViewModel, FragmentSavedNewsBinding>() {

    lateinit var newsAdapter: NewsAdapter
/*    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
    }*/

    override fun setupUI() {
        setupRecyclerView()

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]

                viewModel.deleteArticle(article)

                Snackbar.make(binding.root,"Article deleted successfully", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles->
            newsAdapter.differ.submitList(articles)
        })
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter {article->
            listItemClicked(article)
        }

        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun listItemClicked(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article",article)
        }
        try {
            val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragmentFavorite(article)
            findNavController().navigate(
                action
            )
        } catch (e: Exception) {
            Log.d("SaveNewsFragment", "listItemClicked: ${e.message}")
        }
    }

    override fun getContentView(): Int {
        return R.layout.fragment_saved_news
    }

    override fun getViewModelClass(): Class<NewsViewModel> {
        return NewsViewModel::class.java
    }
}