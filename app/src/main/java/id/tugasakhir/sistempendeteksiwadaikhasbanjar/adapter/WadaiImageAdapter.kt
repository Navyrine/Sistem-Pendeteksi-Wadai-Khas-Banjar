package id.tugasakhir.sistempendeteksiwadaikhasbanjar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.R
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.data.WadaiImage

class WadaiImageAdapter(private val wadaiImageList: List<WadaiImage>):
    RecyclerView.Adapter<WadaiImageAdapter.WadaiImageViewHolder>() {
    class WadaiImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val wadaiImageView: ImageView = itemView.findViewById(R.id.iv_item_wadai_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WadaiImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wadai_image_item, parent, false)
        return WadaiImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return wadaiImageList.size
    }

    override fun onBindViewHolder(holder: WadaiImageViewHolder, position: Int) {
        val wadai = wadaiImageList[position]
        holder.wadaiImageView.setImageResource(wadai.wadaiImage)
    }
}