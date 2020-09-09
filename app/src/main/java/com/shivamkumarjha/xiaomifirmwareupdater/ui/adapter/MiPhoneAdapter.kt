package com.shivamkumarjha.xiaomifirmwareupdater.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.shivamkumarjha.xiaomifirmwareupdater.R
import com.shivamkumarjha.xiaomifirmwareupdater.model.MiPhone
import java.util.*
import kotlin.collections.ArrayList

class MiPhoneAdapter(private val clickListenerMi: MiPhoneClickListener) :
    RecyclerView.Adapter<MiPhoneViewHolder>(), Filterable {
    private var miPhones: ArrayList<MiPhone> = arrayListOf()
    private var backupList: ArrayList<MiPhone> = miPhones

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiPhoneViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_content_mi, parent, false)
        return MiPhoneViewHolder(itemView, clickListenerMi)
    }

    override fun getItemCount(): Int = miPhones.size

    override fun onBindViewHolder(holder: MiPhoneViewHolder, position: Int) {
        holder.initialize(miPhones[position])
    }

    fun getMiPhones() = miPhones

    fun setMiPhones(miPhones: ArrayList<MiPhone>) {
        this.miPhones = miPhones
        backupList = miPhones
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                var filteredList: ArrayList<MiPhone>? = ArrayList()
                if (charSequence.toString().isEmpty()) {
                    filteredList = backupList
                } else {
                    for (miPhone in miPhones) {
                        if (miPhone.name.toLowerCase(Locale.ROOT)
                                .contains(charSequence.toString().toLowerCase(Locale.ROOT)) ||
                            miPhone.codename.toLowerCase(Locale.ROOT)
                                .contains(charSequence.toString().toLowerCase(Locale.ROOT)) ||
                            miPhone.version.toLowerCase(Locale.ROOT)
                                .contains(charSequence.toString().toLowerCase(Locale.ROOT)) ||
                            miPhone.android.toString().toLowerCase(Locale.ROOT)
                                .contains(charSequence.toString().toLowerCase(Locale.ROOT))
                        ) {
                            filteredList?.add(miPhone)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults?
            ) {
                miPhones = filterResults?.values as ArrayList<MiPhone>
                notifyDataSetChanged()
            }
        }
    }
}
