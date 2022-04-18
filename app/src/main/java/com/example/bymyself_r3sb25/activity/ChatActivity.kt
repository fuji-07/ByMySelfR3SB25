package com.example.bymyself_r3sb25.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bymyself_r3sb25.Post
import com.example.bymyself_r3sb25.R
import com.example.bymyself_r3sb25.databinding.ActivityChatBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    // Firebase関連
    private lateinit var database: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference

    private lateinit var adapter: RecyclerAdapter
    private var postList: MutableList<Post> = mutableListOf()

    private var name: String? = null

    private val sdf: SimpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        name = pref.getString("name", "名無しさん")

        // データベースの初期化
        database = FirebaseDatabase.getInstance()

        var path = "chat_room"
        when (intent.getIntExtra("class", 0)) {
            R.id.r3saButton -> path += "/r3sa"
            R.id.r3sbButton -> path += "/r3sb"
        }

        dbRef = database.getReference(path)

        // アダプターの初期化
        adapter = RecyclerAdapter(postList)
        binding.postRecyclerView.adapter = adapter

        // 区切り線の初期化
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.postRecyclerView.addItemDecoration(itemDecoration)

        binding.postRecyclerView.layoutManager = LinearLayoutManager(this)

        // チャット送信時の処理
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val post = snapshot.getValue<Post>()
                if (post != null) {
                    postList.add(post)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        dbRef.addChildEventListener(childEventListener)

        // メッセージ送信
        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString()

            if (message == "") {
                val text = "メッセージを入力してください"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            } else {
                val date = sdf.format(Date())
                val post = Post(name, message, date)
                dbRef.push().setValue(post)

                binding.messageInput.text = null
            }
        }


    }

    class PostViewHolder(postView: View) : RecyclerView.ViewHolder(postView) {
        val name: TextView = postView.findViewById(R.id.nameView)
        val message: TextView = postView.findViewById(R.id.messageView)
        val date: TextView = postView.findViewById(R.id.dateView)
    }

    class RecyclerAdapter(val list: List<Post>) : RecyclerView.Adapter<PostViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            val postView = LayoutInflater.from(parent.context)
                .inflate(R.layout.post_recycler_list, parent, false)
            return PostViewHolder(postView)
        }

        override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
            holder.name.text = list[position].name
            holder.message.text = list[position].message
            holder.date.text = list[position].date
        }

        override fun getItemCount(): Int = list.size
    }
}

