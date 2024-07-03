package com.example.olacak.util

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun makeToast(message: String, context: Context){
    Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
}


/*
fun Fragment.addMenuProvider(@MenuRes menuRes: Int, callback: (id: Int) -> Boolean) {
    val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(menuRes, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem) = callback(menuItem.itemId)

    }
    (requireActivity() as MenuHost).addMenuProvider(
        menuProvider,
        viewLifecycleOwner,
        Lifecycle.State.RESUMED
    )
}

 */

fun makeAlertDialog(context: Context) {
    val alertDialogBuilder = AlertDialog.Builder(context)
    //alertDialogBuilder.setTitle("How To Use")
    //alertDialogBuilder.setMessage(R.string.how_to_use)

    // ders secimi burayaaaa!!!!!!!!!!!!!!!!!!
    alertDialogBuilder.create()
    alertDialogBuilder.show()
}