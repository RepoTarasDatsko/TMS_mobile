package com.papyrus.mobile.mobile_tms;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import java.lang.reflect.Field;
import android.content.DialogInterface;
import android.view.MenuItem;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.TextView;
import github.nisrulz.recyclerviewhelper.RVHAdapter;
import github.nisrulz.recyclerviewhelper.RVHViewHolder;
import android.widget.PopupMenu;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import android.content.Intent;
import android.content.Context;
import android.view.MotionEvent;
import android.graphics.Color;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import android.widget.NumberPicker;
import android.widget.EditText;
import android.graphics.Paint;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder>
        implements RVHAdapter {
    private Context mContext;
    private  SharedPreferences mPrefs;
    public class ItemViewHolder extends RecyclerView.ViewHolder implements RVHViewHolder {

        final TextView txt;
        final TextView timeDelivery;
        final TextView desc;
        private ImageView image;
        private ImageView pop_up;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
            timeDelivery = itemView.findViewById(R.id.time_delivery);
            desc = itemView.findViewById(R.id.textViewDesc);
            txt.setTextColor(Color.parseColor("#000080"));
            timeDelivery.setTextColor(Color.parseColor("#000080"));
            image = (ImageView) itemView.findViewById(R.id.arrow);
            pop_up = (ImageView) itemView.findViewById(R.id.pop_up);
            mContext =  itemView.getContext();
            mPrefs =  mContext.getSharedPreferences("Global", 0);
        }

        @Override
        public void onItemClear() {
            System.out.println("Item is unselected");
        }

        @Override
        public void onItemSelected(int actionstate) {
            System.out.println("Item is selected");
        }
    }

    private final List<DataItems> dataList;

    public MyAdapter(List<DataItems> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void Clear() {
        int size = dataList.size();
        dataList.clear();
        notifyItemRangeRemoved(0, size);
    }

    @SuppressLint("ClickableViewAccessibility")

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        Gson gson = new Gson();
        String json = mPrefs.getString("orders", "");
        ArrayList orders = gson.fromJson(json,  ArrayList.class);
         if(orders != null) {
            LinkedTreeMap order = ( LinkedTreeMap )orders.get(position);
            String notation = (String)order.get("mCondition");
             ArrayList  orderNumber = (ArrayList)order.get("mOrdersNumber");
             if(orderNumber != null) {
                 LinkedTreeMap kindOrder = ( LinkedTreeMap )orderNumber.get(0);
                 String k = (String)kindOrder.get("mКindOf");
                if (k.equals("Получение")) {
                     holder.itemView.setBackgroundColor(Color.parseColor("#f5e6cd"));
                 }
                 else
                {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                }
             }
            switch (notation) {
                case "00011":
                    holder.txt.setTextColor(Color.parseColor("#228B22"));
                    holder.timeDelivery.setTextColor(Color.parseColor("#228B22"));
                    holder.pop_up.setImageResource(R.drawable.ic_reorder_grey_checked600_24dp);
                    break;
                case "00012":
                    holder.txt.setTextColor(Color.GRAY);
                    holder.timeDelivery.setTextColor(Color.GRAY);
                    holder.pop_up.setImageResource(R.drawable.ic_reorder_grey_600_24dp);
                    break;
                case "00010":
                    holder.txt.setTextColor(Color.parseColor("#000080"));
                    holder.timeDelivery.setTextColor(Color.parseColor("#000080"));
                    holder.pop_up.setImageResource(R.drawable.ic_reorder_grey_600_24dp);
                    break;
                default:
                    holder.txt.setTextColor(Color.parseColor("#000080"));
                    holder.timeDelivery.setTextColor(Color.parseColor("#000080"));
                    holder.pop_up.setImageResource(R.drawable.ic_reorder_grey_600_24dp);
            }
        }
        DataItems currentItem =  dataList.get(position);
        holder.txt.setText(currentItem.getOrderData());
        if(!currentItem.getOrderDesc().equals("anyType{}")) {
            holder.desc.setText(currentItem.getOrderDesc().length() > 30 ? currentItem.getOrderDesc().substring(0, 30) : currentItem.getOrderDesc());
        }else{
            holder.desc.setText("");
        }

        if (!currentItem.getTimeDelivery().equals("anyType{}")) {
            holder.timeDelivery.setText(currentItem.getTimeDelivery());
        } else {
            holder.timeDelivery.setText("");
        }

        holder.pop_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                PopupMenu popup = new PopupMenu(mContext, view);
                // Inflate the menu from xml
                popup.inflate(R.menu.popup_status);
                // Setup menu item selection
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Gson gson = new Gson();
                        String json = mPrefs.getString("orders", "");
                        ArrayList orders = gson.fromJson(json,  ArrayList.class);
                        json = mPrefs.getString("flight", "");
                        ArrayList flights = gson.fromJson(json,  ArrayList.class);
                        String currentFlight = mPrefs.getString("CurrentOrdersNumber", "");
                        LinkedTreeMap order;
                        SharedPreferences.Editor prefsEditor;
                        prefsEditor = mPrefs.edit();
                        SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                        String curData = dateformat.format(new Date());
                        switch (item.getItemId()) {
                            case R.id.menu_done:
                                holder.txt.setTextColor(Color.parseColor("#228B22"));
                                holder.timeDelivery.setTextColor(Color.parseColor("#228B22"));
                                order = (LinkedTreeMap)orders.get(position);
                                LinkedTreeMap loading = (LinkedTreeMap)orders.get(0);
                                String notation = (String)loading.get("mCondition");
                                boolean routingCalculus = false;
                                if (position != 0){
                                    if(!notation.equals("00011")) {
                                        loading.put("mCondition", "00011");
                                        loading.put("mCheckOutTime", curData);
                                        orders.set(0, loading);
                                        routingCalculus = true;
                                    }

                                }else {
                                        routingCalculus = true;
                                }
                                if(!order.get("mCondition").equals("00011")) {
                                    order.put("mCheckOutTime", curData);
                                }
                                order.put("mCondition", "00011");
                                orders.set(position, order);
                                json = gson.toJson(orders);
                                prefsEditor.putString("orders", json);
                                for (int i = 0; i < flights.size(); i++) {
                                    LinkedTreeMap fl = (LinkedTreeMap )flights.get(i);
                                    if(fl.get("mFlightsNumber").equals(currentFlight)){
                                        fl.put("mOrders", orders);
                                        if(routingCalculus) {
                                            fl.put("mSwap", true);
                                        }
                                        flights.set(i, fl);
                                    }
                                }

                                json = gson.toJson(flights);
                                prefsEditor.putString("flight", json);
                                prefsEditor.apply();
                                holder.pop_up.setImageResource(R.drawable.ic_reorder_grey_checked600_24dp);
                                return true;
                            case R.id.menu_delayed:
                                holder.txt.setTextColor(Color.GRAY);
                                holder.timeDelivery.setTextColor(Color.GRAY);
                                order = (LinkedTreeMap)orders.get(position);
                                order.put("mCondition", "00012");
                                order.put("mCheckOutTime", curData);
                                orders.set(position, order);
                                json = gson.toJson(orders);
                                prefsEditor.putString("orders", json);
                                for (int i = 0; i < flights.size(); i++) {
                                    LinkedTreeMap fl = (LinkedTreeMap )flights.get(i);
                                    if(fl.get("mFlightsNumber").equals(currentFlight)){
                                        fl.put("mOrders", orders);
                                        flights.set(i, fl);
                                    }
                                }

                                json = gson.toJson(flights);
                                prefsEditor.putString("flight", json);
                                prefsEditor.commit();
                                holder.pop_up.setImageResource(R.drawable.ic_reorder_grey_600_24dp);
                                return true;
                            case R.id.on_the_way:
                                holder.txt.setTextColor(Color.parseColor("#000080"));
                                holder.timeDelivery.setTextColor(Color.parseColor("#000080"));
                                order = (LinkedTreeMap)orders.get(position);
                                order.put("mCondition", "00010");
                                order.put("mCheckOutTime", curData);
                                orders.set(position, order);
                                json = gson.toJson(orders);
                                prefsEditor.putString("orders", json);
                                for (int i = 0; i < flights.size(); i++) {
                                    LinkedTreeMap fl = (LinkedTreeMap )flights.get(i);
                                    if(fl.get("mFlightsNumber").equals(currentFlight)){
                                        fl.put("mOrders", orders);
                                        flights.set(i, fl);
                                    }
                                }
                                json = gson.toJson(flights);
                                prefsEditor.putString("flight", json);
                                prefsEditor.commit();
                                holder.pop_up.setImageResource(R.drawable.ic_reorder_grey_600_24dp);
                                return true;
                            case R.id.position_change:
                                final NumberPicker numberPicker = new NumberPicker(mContext);
                                numberPicker.setMaxValue(orders.size() - 1);
                                numberPicker.setMinValue(1);
                                setNumberPickerTextColor(numberPicker);
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int toPosition = numberPicker.getValue();

                                        int itemPosition;
                                        if(holder.getAdapterPosition() < 0){
                                            itemPosition = position;
                                        }else{
                                            itemPosition = holder.getAdapterPosition();
                                        }

                                        if(itemPosition < toPosition) {
                                            for (int i = itemPosition; i < toPosition; i++) {
                                                onItemMove(i, i + 1);
                                            }
                                            notifyItemRangeChanged(itemPosition, toPosition - itemPosition + 1 );
                                        }else{
                                            for (int i = itemPosition; i > toPosition; i--) {
                                                onItemMove(i, i - 1);
                                            }
                                            notifyItemRangeChanged(toPosition, itemPosition - toPosition + 1);
                                        }
                                    }
                                });

                                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                     return;
                                    };
                                });
                                builder.setView(numberPicker);
                                builder.show();

                                return true;
                            default:
                                return false;
                        }
                    }
                });
                // Handle dismissal with: popup.setOnDismissListener(...);
                // Show the menu
                popup.show();

                return false;

            }

        });
        holder.image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent;
                intent = new Intent(mContext, OrderActivity.class);
                Gson gson = new Gson();
                String json = mPrefs.getString("orders", "");
                ArrayList orders = gson.fromJson(json,  ArrayList.class);
                LinkedTreeMap order = (LinkedTreeMap)orders.get(holder.getAdapterPosition());
                intent.putExtra("Customer",  order.get("mCustomer").toString());
                intent.putExtra("Adress",  order.get("mAdress").toString());
                intent.putExtra("latitude",  order.get("mlatitude").toString());
                intent.putExtra("longitude",  order.get("mlongitude").toString());
                //String  notation = order.get("mNotation").toString();
                ArrayList ordersNumber = (ArrayList) order.get("mOrdersNumber");
                String notation = "";
                String contactPerson = "";
                String manager = "";
                String typeOfPayment = "";
                String warehouse = "";
                String kindOf = "";
                for (int y = 0; y < ordersNumber.size(); y++) {
                    LinkedTreeMap num = (LinkedTreeMap)ordersNumber.get(y);
                    String n = "";
                    if(!notation.equals(num.get("mNotation").toString())) {
                        n = num.get("mNotation").toString();
                        if (!n.equals("anyType{}")) {
                            notation += n;
                        }
                    }
                    if(!contactPerson.equals(num.get("mContactPerson").toString())) {
                        n = num.get("mContactPerson").toString();
                                if (!n.equals("anyType{}")) {
                                    contactPerson += n;
                                }
                            }
                    if(!manager.equals(num.get("mManager").toString())) {
                        n = num.get("mManager").toString();
                        if (!n.equals("anyType{}")) {
                            manager += n;
                        }
                    }
                    if(!typeOfPayment.equals(num.get("mTypeOfPayment").toString())) {
                        n = num.get("mTypeOfPayment").toString();
                        if (!n.equals("anyType{}")) {
                            typeOfPayment += n;
                        }
                    }
                    if(!warehouse.equals(num.get("mWarehouse").toString())) {
                        n = num.get("mWarehouse").toString();
                        if (!n.equals("anyType{}")) {
                            warehouse += n;
                        }
                    }
                    if(!kindOf.equals(num.get("mКindOf").toString())) {
                        n = num.get("mКindOf").toString();
                        if (!n.equals("anyType{}")) {
                            kindOf += n;
                        }
                    }
                    }

               // if(!notation.equals("anyType{}")) {
                intent.putExtra("notation",  notation + " " +  typeOfPayment);
                intent.putExtra("ContactPerson",  contactPerson);
                intent.putExtra("Manager",  manager);
                intent.putExtra("Warehouse",  warehouse);
                intent.putExtra("КindOf",  kindOf);
               // }
                String  condition =  order.get("mCondition").toString();
                intent.putExtra("condition", condition);
                mContext.startActivity(intent);
                if(holder.getAdapterPosition() != position){
                    notifyDataSetChanged();
                }
                return false;
            }

        });
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker)
    {
        final int count = numberPicker.getChildCount();
        int color = Color.parseColor("#000000");
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){

                }
                catch(IllegalAccessException e){

                }
                catch(IllegalArgumentException e){

                }
            }
        }
        return false;
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onItemDismiss(int position, int direction) {
        remove(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if(toPosition == 0 || fromPosition == 0){
            return false;
        }
        Gson gson = new Gson();
        String json = mPrefs.getString("flight", "");
        ArrayList flights = gson.fromJson(json,  ArrayList.class);
        json = mPrefs.getString("orders", "");
        ArrayList or = gson.fromJson(json,  ArrayList.class);
        LinkedTreeMap fPosition = (LinkedTreeMap)or.get(fromPosition);
        LinkedTreeMap tPosition = (LinkedTreeMap)or.get(toPosition);
        DataItems from =  dataList.get(fromPosition);
        from.setOrderData(String.valueOf(toPosition) +  ") " + fPosition.get("mCustomer").toString() + " " + fPosition.get("mAdress").toString());
        DataItems to = dataList.get(toPosition);
        to.setOrderData( String.valueOf(fromPosition) +  ") " + tPosition.get("mCustomer").toString() + " " + tPosition.get("mAdress").toString());
        Double t = (Double)tPosition.get("mNumberOfPosition");
        int temp = t.intValue();
        String tempTime = tPosition.get("mTimeCalculated").toString();
        t = (Double)fPosition.get("mNumberOfPosition");
        int f = t.intValue();
        tPosition.put("mTimeCalculated", fPosition.get("mTimeCalculated").toString());
        tPosition.put("mNumberOfPosition", f);
        fPosition.put("mTimeCalculated", tempTime);
        fPosition.put("mNumberOfPosition", temp);
        or.set(fromPosition, tPosition);
        or.set(toPosition, fPosition);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        json = gson.toJson(or);
        String currentFlight = mPrefs.getString("CurrentOrdersNumber", "");
        prefsEditor.putString("orders", json);
        for (int i = 0; i < flights.size(); i++) {
            LinkedTreeMap fl = (LinkedTreeMap )flights.get(i);
            if(fl.get("mFlightsNumber").equals(currentFlight)){
                fl.put("mOrders", or);
                fl.put("mSwap", true);
                flights.set(i, fl);
            }
        }
        String jsonflight = gson.toJson(flights);
        prefsEditor.putString("flight", jsonflight);
        prefsEditor.apply();
        swap(fromPosition, toPosition);
        //notifyItemChanged(fromPosition);
        //notifyItemChanged(toPosition);
        return false;
    }

    private void remove(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    private void insert(int firstPosition, DataItems to) {

       dataList.add(firstPosition, to);

    }

    private void swap(int firstPosition, int secondPosition) {
        Collections.swap(dataList, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }
}
