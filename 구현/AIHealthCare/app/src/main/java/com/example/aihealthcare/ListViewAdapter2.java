package com.example.aihealthcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter2 extends BaseAdapter {

    private ArrayList<ListViewItem2> listViewItemList2 = new ArrayList<ListViewItem2>();
    public ListViewAdapter2() {

    }
    @Override
    public int getCount() {
        return listViewItemList2.size();
    }

    //position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        //listViewItemList layout을 convertview 참조 획득
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem2, parent, false);
        }
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView WorkNameTextView = (TextView) convertView.findViewById(R.id.tvWorkName);
        TextView ExplainTextView = (TextView) convertView.findViewById(R.id.tvExplain) ;
        TextView PartTextView = (TextView) convertView.findViewById(R.id.tvPart) ;
        TextView CaloriesTextView = (TextView) convertView.findViewById(R.id.tvCalories) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem2 listViewItem = listViewItemList2.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        WorkNameTextView.setText(listViewItem.getName());
        ExplainTextView.setText(listViewItem.getExplain());
        PartTextView.setText("부위 : "+listViewItem.getPart());
        CaloriesTextView.setText(listViewItem.getCalories() + "칼로리");

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(String name, String explain, String Part, String calorise){
        ListViewItem2 item = new ListViewItem2();

        item.setName(name);
        item.setExplain(explain);
        item.setPart(Part);
        item.setCalories(calorise);

        listViewItemList2.add(item);
    }
}