package com.example.loginapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.adapters.UserAdapter
import com.example.loginapp.databinding.FragmentListBinding
import com.example.loginapp.objects.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ListFragment : Fragment() {

    private lateinit var binding : FragmentListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var userList: ArrayList<User>
    private lateinit var mDbRef : DatabaseReference
    private lateinit var mAuth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_list,container,false)
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        setHasOptionsMenu(true)
        userList=ArrayList()
        adapter = UserAdapter(requireContext(),userList)
        recyclerView = binding.usersRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mDbRef.child("user").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for(postsnapshot in snapshot.children){
                    val cU = postsnapshot.getValue(User::class.java)
                    if(cU?.uid != mAuth.currentUser?.uid){
                        userList.add(cU!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.my_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logoutMenu){
            alertBox()
            return true
        }
        return true
    }

    private fun alertBox() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_->
            mAuth.signOut()
            findNavController().navigate(ListFragmentDirections.actionListFragmentToLogInFragment())
        }
        builder.setNegativeButton("No"){_,_->

        }

        builder.setTitle("Log out")
        builder.setMessage("Are you sure you want to log out?")
        builder.create().show()
    }
}