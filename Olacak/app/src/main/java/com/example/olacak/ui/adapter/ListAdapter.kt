package com.example.olacak.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.olacak.R
import com.example.olacak.data.entity.Gorevler
import com.example.olacak.databinding.CardTasarimBinding
import com.example.olacak.ui.fragment.AnasayfaFragmentDirections
import com.example.olacak.ui.viewmodel.AnasayfaViewModel
import com.example.olacak.util.gecisYap
import com.google.android.material.snackbar.Snackbar

class ListAdapter(var mContext: Context, var gorevlerListesi:List<Gorevler>, var viewModel: AnasayfaViewModel)
    : RecyclerView.Adapter<ListAdapter.CardTasarimTutucu>() {
    inner class CardTasarimTutucu(var tasarim: CardTasarimBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding:CardTasarimBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.card_tasarim,parent,false)
        return CardTasarimTutucu(binding)

    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val gorev = gorevlerListesi[position]
        val t = holder.tasarim

        t.gorevNesnesi = gorev


        t.cardViewSatir.setOnClickListener {
            val gecis = AnasayfaFragmentDirections.gorevDetayGecis(gorev=gorev)
            Navigation.gecisYap(it,gecis)

        }

        t.imageViewSil.setOnClickListener {
            Snackbar.make(it,"${gorev.gorev_adi} Deleted?", Snackbar.LENGTH_SHORT)
                .setAction("Yes"){
                    viewModel.sil(gorev.gorev_id)
                }
                .show()
        }


    }

    override fun getItemCount(): Int {
        return gorevlerListesi.size
    }

    fun updateGorevlerList(newList: List<Gorevler>) {
        gorevlerListesi = newList
        notifyDataSetChanged()
    }


}