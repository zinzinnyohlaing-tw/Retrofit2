package eu.hanna.recyclerviewwithretrofit

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import eu.hanna.recyclerviewwithretrofit.databinding.ActivityMainBinding
import eu.hanna.recyclerviewwithretrofit.databinding.PostLayoutDialogBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRV()
        getAllPosts()

        binding.fabAddPost.setOnClickListener {
            addPostDialog()
        }
    }

    // Create to get all the data at the server
    private fun getAllPosts() {
        lifecycleScope.launchWhenCreated {
            val response = RetrofitInstance.retrofit.getAllPosts()

            if (response.isSuccessful && response.body() != null) {

                // Display the response at the recyclerview
                    postAdapter.differ.submitList(response.body())

                Log.d("response","getAllPosts:${response.body()}")
            }  else {
                Toast.makeText(this@MainActivity,
                "Error Code : ${response.code()}" ,
                Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Bind the adapter to the recyclerview
    private fun setupRV() {
        postAdapter = PostAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = postAdapter
            setHasFixedSize(true)
        }
    }

    // Post the data into the server using retrofit2
    private fun addPostDialog() {
        val mDialog = Dialog(this)
        val mBinding = PostLayoutDialogBinding.inflate(layoutInflater)
        mDialog.setContentView(mBinding.root)

        mBinding.btnCancel.setOnClickListener {
            mDialog.dismiss()
        }

        mBinding.btnPost.setOnClickListener {

            if (mBinding.etPostTitle.text.toString().isNotEmpty() &&
                    mBinding.etBodyPost.text.toString().isNotEmpty()) {

                val title = mBinding.etPostTitle.text.toString()
                val body = mBinding.etBodyPost.text.toString()
                val userID = mBinding.etUserID.text.toString().toInt()

                // makePost
                makePost(userID,title,body)

                Toast.makeText(this,"Post done successfully",Toast.LENGTH_SHORT).show()
                mDialog.dismiss()
            } else {
                Toast.makeText(this,
                    "Title and Body can't be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        mDialog.show()
    }

    // Add the new items to the server
    private fun makePost(userID: Int,title: String,body: String) {

        lifecycleScope.launchWhenCreated {
            val post = PostItem(body,0,title,userID)

            val response = RetrofitInstance.retrofit.addPost(post)

            if(response.isSuccessful && response.body() != null) {
                Log.d("post response", "OurPost: ${response.body()}")
            } else {
                Log.d("post Error", "Error: ${response.code()}")
            }
        }
    }

    // Delete the new item
    private fun deletePost () {
        lifecycleScope.launchWhenCreated {
            val response = RetrofitInstance.retrofit.deletePost()

            if (response.isSuccessful) {
                Toast.makeText(
                    this@MainActivity,
                    "Fake Post Deleted",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Log.d("error", "Error when delete post: ${
                    response.code()
                }")
            }
        }
    }
}