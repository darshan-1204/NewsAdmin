package com.example.newsadmin

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsadmin.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var uri: Uri

    lateinit var storageRef: StorageReference
    lateinit var refrence: DatabaseReference
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storageRef = FirebaseStorage.getInstance().reference
        refrence = FirebaseDatabase.getInstance().reference
        binding.btnSelectImg.setOnClickListener {
            var intent = Intent(ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 25)
        }

        binding.btnSubmit.setOnClickListener {

            val ref = storageRef.child("images/${uri.lastPathSegment}.jpg")
            var uploadTask = ref.putFile(uri)

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    var key = refrence.root.push().key
                    var title = binding.edtTitle.text.toString()
                    var description = binding.edtDescription.text.toString()

                    var data =
                        NewsModel(title, description, key!!.toString(), downloadUri.toString())
                    refrence.root.child("User").child(key.toString()).setValue(data)

                    Toast.makeText(this, "Data Added Successfully", Toast.LENGTH_SHORT).show()
                    binding.edtTitle.setText("")
                    binding.edtDescription.setText("")
                    binding.btnSelectImg.setImageBitmap(null)




                } else {
                    // Handle failures
                    // ...
                }
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            if (requestCode == 25) {
                uri = data?.data!!
                Log.e(TAG, "onActivityResult: ========" + uri.lastPathSegment)
            }
        }
    }
}