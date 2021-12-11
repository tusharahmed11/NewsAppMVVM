package com.example.newsappmvvm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsappmvvm.R
import com.example.newsappmvvm.databinding.ItemArticlePreviewBinding
import com.example.newsappmvvm.models.Article

class NewsAdapter(private val clickListener: (Article)-> Unit) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        /* val layoutInflater = LayoutInflater.from(parent.context)
        val  listItemView = layoutInflater.inflate(R.layout.item_article_preview,parent,false)
        return NewsViewHolder(listItemView)*/

        return NewsViewHolder(
            ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.binding.apply {
            Glide.with(holder.binding.root).load(article.urlToImage).into(ivArticleImage)
            tvSource.text = article.source?.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublishedAt.text = article.publishedAt
        }

        holder.binding.root.setOnClickListener {
            clickListener(article)
        }
        //    holder.bind(article,clickListener)
        /*holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(findViewById(R.id.ivArticleImage))
        }*/
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class NewsViewHolder(val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root)

/*{

        *//*fun bind(article: Article,clickListener: (Article)-> Unit){
            Glide.with(view).load(article.urlToImage).into(view.findViewById<ImageView>(R.id.ivArticleImage))
            view.findViewById<TextView>(R.id.tvSource).text = article.source.name
            view.findViewById<TextView>(R.id.tvTitle).text = article.title
            view.findViewById<TextView>(R.id.tvDescription).text = article.description
            view.findViewById<TextView>(R.id.tvPublishedAt).text = article.publishedAt

            view.setOnClickListener{
                clickListener(article)
            }
        }*//*
    }
}*/

}