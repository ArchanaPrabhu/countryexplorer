package com.demo.countryexplorer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.demo.countryexplorer.databinding.LayoutCountryItemBinding
import com.demo.countryexplorer.models.Country

class CountryListAdapter(private val countryList: MutableList<Country> = mutableListOf()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CountryViewHolder(
            LayoutCountryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CountryViewHolder) {
            holder.bind(countryList.get(position))
        }
    }

    class CountryViewHolder(val binding: LayoutCountryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Country) {
            binding.countryCapitol.text = item.capitol
            binding.countryName.text = item.name
            binding.countryRegion.text = item.region
            binding.countryCode.text = item.code
        }
    }

    fun submitList(newList: List<Country>) {
        countryList.clear()
        countryList.addAll(newList)
    }
}