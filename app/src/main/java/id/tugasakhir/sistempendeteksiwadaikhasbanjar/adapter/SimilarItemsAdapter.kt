package id.tugasakhir.sistempendeteksiwadaikhasbanjar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.R

class SimilarItemsAdapter(
    private var items: List<String>,
    private var confidences: FloatArray,
    private val itemClickListener: (String) -> Unit
): RecyclerView.Adapter<SimilarItemsAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val itemTextView: TextView = itemView.findViewById(R.id.tv_itemName)
        val confidenceTextView: TextView = itemView.findViewById(R.id.tv_itemConvidence)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_similar_detection, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val confidence = confidences[position]

        holder.itemTextView.text = item
        holder.confidenceTextView.text = String.format("%.0f%%", confidence * 100)
        holder.progressBar.progress = (confidence * 100).toInt()

        holder.itemView.setOnClickListener { itemClickListener(item) }
    }

    fun updateData(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun updateConfidences(newConfidences: FloatArray) {
        confidences = newConfidences
        notifyDataSetChanged()
    }
}