package `in`.silive.spc

import `in`.silive.spc.databinding.ItemContentBinding
import `in`.silive.spc.model.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load


class FoodAdapter : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {
    var data = mutableListOf<Item>()
    fun addData(category: List<Item>) {
            data=category.toMutableList()
    }
    inner class ViewHolder(var binding: ItemContentBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemContentBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_content,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.itemName.text=data[position].product.name.toString()
        holder.binding.Quantity.text="Quantity : "+data[position].quantity.toString()
        holder.binding.itemIcon.load("http://44.202.84.23:3000"+data[position].product.imageUrl[0].toString())
        holder.binding.itemPrice.text="₹" + data[position].product.price.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }

}



