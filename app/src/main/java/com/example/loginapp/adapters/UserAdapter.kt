package com.example.loginapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.ListFragmentDirections
import com.example.loginapp.R
import com.example.loginapp.objects.User

class UserAdapter(val context: Context,val userList : ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserViewModel>() {
    class UserViewModel(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.userName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewModel {
        return UserViewModel(LayoutInflater.from(context).inflate(R.layout.user_list_view,parent,false))
    }

    override fun onBindViewHolder(holder: UserViewModel, position: Int) {
        val currentUser = userList[position]
        holder.name.text = currentUser.name

        val n=currentUser.name
        val u=currentUser.uid
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(ListFragmentDirections.actionListFragmentToChatFragment(n,u))
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}