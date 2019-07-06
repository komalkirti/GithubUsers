package com.example.komal.networking

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row.view.*

class GithubUserAdapter(val githubusers: ArrayList<Githubusers>):RecyclerView.Adapter<GithubUserAdapter.GithubViewholder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GithubViewholder {
        var li=p0.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater;
        var itemview=li.inflate(R.layout.item_row ,p0 , false);
        return GithubViewholder(itemview);
    }

    override fun getItemCount(): Int {
        return githubusers.size;
    }

    override fun onBindViewHolder(p0: GithubViewholder, p1: Int) {
        p0.bind(githubusers[p1])
    }


    class GithubViewholder(itemView:View): RecyclerView.ViewHolder(itemView) {
        fun bind(githubuser:Githubusers){
            itemView.Viewlogin.text=githubuser.login;
            itemView.Viewhtml.text=githubuser.html_url;
            itemView.Viewscore.text= githubuser.score.toString();
            Picasso.get().load(githubuser.avatar_url).into(itemView.image);
        }

    }
}