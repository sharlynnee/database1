package com.example.wazitoecommerce.data


import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.view.ViewParent
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import com.example.wazitoecommerce.models.Parent
import com.example.wazitoecommerce.navigation.LOGIN_URL
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ParentViewModel(var navController:NavHostController, var context: Context) {
    var authViewModel:AuthViewModel
    var progress:ProgressDialog
    init {
        authViewModel = AuthViewModel(navController, context)
        if (!authViewModel.isLoggedIn()){
            navController.navigate(LOGIN_URL)
        }
        progress = ProgressDialog(context)
        progress.setTitle("Loading")
        progress.setMessage("Please wait...")
    }

    fun uploadParent(name:String, age:String, description:String, filePath:Uri){
        val parentId = System.currentTimeMillis().toString()
        val storageRef = FirebaseStorage.getInstance().getReference()
                                    .child("Parents/$parentId")
        progress.show()
        storageRef.putFile(filePath).addOnCompleteListener{
            progress.dismiss()
            if (it.isSuccessful){
                // Save data to db
                storageRef.downloadUrl.addOnSuccessListener {
                    var imageUrl = it.toString()
                    var parent = Parent(name,age,description,imageUrl,parentId)
                    var databaseRef = FirebaseDatabase.getInstance().getReference()
                        .child("Parents/$parentId")
                    databaseRef.setValue(parent).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(this.context, "Upload error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun allParents(
        parent:MutableState<Parent>,
        parents:SnapshotStateList<Parent>):SnapshotStateList<Parent>{
        progress.show()
        var ref = FirebaseDatabase.getInstance().getReference()
            .child("Parents")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                parents.clear()
                for (snap in snapshot.children){
                    var retrievedParent = snap.getValue(Parent::class.java)
                    parent.value = retrievedParent!!
                    parents.add(retrievedParent)
                }
                progress.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "DB locked", Toast.LENGTH_SHORT).show()
            }
        })
        return parents
    }

    fun deleteParent(parentId:String){
        var ref = FirebaseDatabase.getInstance().getReference()
            .child("Parents/$parentId")
        ref.removeValue()
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
    }
}