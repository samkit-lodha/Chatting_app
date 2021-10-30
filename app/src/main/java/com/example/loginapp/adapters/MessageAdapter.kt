package com.example.loginapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R
import com.example.loginapp.objects.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context : Context, val messageList: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val Sent_MESSAGE = 1
    val RECEIVE_MESSAGE = 2
    val mAuth = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1){
            return SendViewModel(LayoutInflater.from(context).inflate(R.layout.send_list_layout,parent,false))
        }else{
            return ReceiveViewHolder(LayoutInflater.from(context).inflate(R.layout.receive_list_layout,parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.javaClass == SendViewModel::class.java){
            val viewHolder = holder as SendViewModel
            viewHolder.sm.text = messageList[position].message
        }else{
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.rm.text = messageList[position].message
        }
    }
    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        if(mAuth?.uid.equals(messageList[position].senderId)){
            return Sent_MESSAGE
        }else{
            return RECEIVE_MESSAGE
        }
    }

    class SendViewModel(itemView : View) : RecyclerView.ViewHolder(itemView){
        val sm = itemView.findViewById<TextView>(R.id.sMessage)
    }

    class ReceiveViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val rm = itemView.findViewById<TextView>(R.id.rMessage)
    }

}