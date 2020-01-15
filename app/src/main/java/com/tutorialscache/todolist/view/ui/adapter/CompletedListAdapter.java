package com.tutorialscache.todolist.view.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.tutorialscache.todolist.R;
import com.tutorialscache.todolist.model.entity.TodoModel;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

//we will refactor our RecyclerView.Adapter to a ListAdapter ,so now list adapter will handle all operation
public class CompletedListAdapter extends ListAdapter<TodoModel,CompletedListAdapter.TodoListHolder> {
    private ItemCompletedListener mListener;

    public CompletedListAdapter() {
        super(DIFF_CALLBACK);
    }
    //to check weather to items have same id or not
    private static final DiffUtil.ItemCallback<TodoModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<TodoModel>() {
        @Override
        public boolean areItemsTheSame(TodoModel oldItem, TodoModel newItem) {
            return oldItem.getId() == newItem.getId();
        }
        //to check weather to items have same contects or not
        @Override
        public boolean areContentsTheSame(TodoModel oldItem, TodoModel newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getCreated_at().equals(newItem.getCreated_at());
        }
    };

    @NonNull
    @Override
    public TodoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_list, parent, false);
        return new TodoListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListHolder holder, int position) {
        TodoModel currentTodo = getItem(position);
        holder.title.setText(currentTodo.getTitle());
        holder.discription.setText(currentTodo.getCreated_at());
    }
    /**
     * Sets click listener.
     * @param itemCompletedListener the item click listener
     */
    // allows clicks events to be caught
    public void setCompletedListner(ItemCompletedListener itemCompletedListener) {
        this.mListener = itemCompletedListener;
    }
    /**
     * The interface Item click listener.
     */
    // parent activity will implement this method to respond to click events
    public interface ItemCompletedListener {
        void onDeleteItem(TodoModel todoModel);
        void onUndoItem(TodoModel todoModel);
    }

    public class TodoListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView discription;
        ImageButton  DeleteTodo;
        ImageView undoTodo;

        public TodoListHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_item_title);
            discription = itemView.findViewById(R.id.tv_item_content);
            DeleteTodo = itemView.findViewById(R.id.delete_todo);
            DeleteTodo.setOnClickListener(this);
            undoTodo = itemView.findViewById(R.id.undo_todo);
            undoTodo.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (view.getId())
            {
                case R.id.delete_todo:
                    if (mListener!=null)
                        mListener.onDeleteItem(getItem(position));
                    break;
                case R.id.undo_todo:
                    if (mListener != null)
                        mListener.onUndoItem(getItem(position));
                    break;
                default:
                    break;
            }
        }
    }
}
