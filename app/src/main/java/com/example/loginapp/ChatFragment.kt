package com.example.loginapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.adapters.MessageAdapter
import com.example.loginapp.databinding.FragmentChatBinding
import com.example.loginapp.objects.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatFragment : Fragment() {

    private lateinit var binding:FragmentChatBinding
    private lateinit var args: ChatFragmentArgs
    private lateinit var messageList: ArrayList<Message>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : MessageAdapter
    private lateinit var mDbRef : DatabaseReference
    private lateinit var mAuth : FirebaseAuth
    var cUN : String?=null
    var cUU : String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chat,container,false)
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        args = ChatFragmentArgs.fromBundle(requireArguments())
        cUN = args.nameSent
        cUU = args.uidSent

        val senderUid = mAuth.currentUser?.uid
        val senderRoom = senderUid + cUU
        val receiverRoom = cUU + senderUid

        messageList = ArrayList()
        adapter = MessageAdapter(requireContext(),messageList)
        recyclerView = binding.chatRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mDbRef.child("chats").child(senderRoom).child("messages").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()

                for(postsnapshot in snapshot.children){
                    val curr = postsnapshot.getValue(Message::class.java)
                    messageList.add(curr!!)
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.sentImageView.setOnClickListener{
            val tempMessage = binding.messageEditText.text.toString()
            val tempMessageObject = Message(tempMessage,mAuth.currentUser?.uid)
            mDbRef.child("chats").child(senderRoom).child("messages").push().setValue(tempMessageObject).addOnSuccessListener {
                mDbRef.child("chats").child(receiverRoom).child("messages").push().setValue(tempMessageObject)
            }
            binding.messageEditText.setText("")
        }

        return binding.root
    }
}