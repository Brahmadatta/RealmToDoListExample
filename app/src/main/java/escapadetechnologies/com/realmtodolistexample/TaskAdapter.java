package escapadetechnologies.com.realmtodolistexample;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class TaskAdapter extends RealmBaseAdapter<Task> implements ListAdapter {

    private MainActivity mainActivity;

    public TaskAdapter(@Nullable OrderedRealmCollection<Task> data, MainActivity mainActivity) {
        super(data);
        this.mainActivity = mainActivity;
    }

    private static class ViewHolder{
        TextView taskName;
        CheckBox isTaskDone;
        ImageView isClicked;
        LinearLayout taskLinearlayout;
        //Button deleteAll;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.taskName = convertView.findViewById(R.id.taskName);
            viewHolder.isTaskDone = convertView.findViewById(R.id.todoDone);
            viewHolder.isClicked = convertView.findViewById(R.id.deleteTask);
            viewHolder.taskLinearlayout = convertView.findViewById(R.id.taskLinearlayout);
            //viewHolder.deleteAll = convertView.findViewById(R.id.deleteAll);

            viewHolder.isTaskDone.setOnClickListener(listener);
            viewHolder.isClicked.setOnClickListener(deleteListener);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (adapterData != null){
            Task task = adapterData.get(position);
            viewHolder.taskName.setText(task.getName());
            viewHolder.isTaskDone.setChecked(task.isDone());
            viewHolder.isTaskDone.setTag(position);
            viewHolder.isClicked.setOnClickListener(deleteListener);
            viewHolder.isClicked.setTag(position);

            //viewHolder.deleteAll.setVisibility(View.VISIBLE);

            if (viewHolder.isTaskDone.isChecked()){
                /*viewHolder.taskName.setPaintFlags(viewHolder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);*/
                viewHolder.taskName.setTextColor(Color.BLACK);
                /*viewHolder.taskLinearlayout.setBackgroundColor(Color.RED);*/
                viewHolder.taskName.setPaintFlags(viewHolder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else {
                viewHolder.taskName.setTextColor(Color.BLACK);
                /*viewHolder.taskLinearlayout.setBackgroundColor(Color.WHITE);*/
                viewHolder.taskName.setPaintFlags(Paint.DITHER_FLAG);
            }

            Intent intent = new Intent("data_notNull");
            LocalBroadcastManager.getInstance(convertView.getContext()).sendBroadcast(intent);
        }

        return convertView;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            if (adapterData != null){
                Task task = adapterData.get(pos);
                mainActivity.changeTaskDone(task.getId());
            }
        }
    };

    private View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            if (adapterData != null){
                Task task = adapterData.get(pos);
                mainActivity.deleteTaskDone(task.getId());
            }
        }
    };

}
