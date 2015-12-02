//package com.smartsense.gifkar.adapter;
//
//import android.content.Context;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.TextView;
//
//import com.android.volley.toolbox.ImageLoader;
//import com.smartsense.gifkar.GifkarApp;
//import com.smartsense.gifkar.R;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//public class CityAdapter extends BaseAdapter implements Filterable {
//    JSONArray dataArray;
//    Boolean isCity;
//    private LayoutInflater inflater;
//    ImageLoader imageLoader = GifkarApp.getInstance().getDiskImageLoader();
//
//    private ItemFilter mFilter = new ItemFilter();
//    JSONArray originalDataArray;
//    String highlight = null;
//
//
//    public CityAdapter(Context context, JSONArray dataArray, Boolean check) {
//
//        this.dataArray = dataArray;
//        this.originalDataArray = dataArray;
//        isCity = check;
//        inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        // TODO Auto-generated method stub
//        return dataArray.length();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        // TODO Auto-generated method stub
//        return dataArray.optJSONObject(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        // TODO Auto-generated method stub
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup parent) {
//        final ViewHolder holder;
//
//        if (view == null) {
//            holder = new ViewHolder();
//            view = inflater.inflate(R.layout.city_element, parent, false);
//            // TODO Auto-generated method stub
//            holder.tv_cityName_City = (TextView) view.findViewById(R.id.tv_cityName_City);
//
//            view.setTag(holder);
//
//        } else {
//            holder = (ViewHolder) view.getTag();
//        }
//
//        JSONObject testJson = dataArray.optJSONObject(position);
//        String tmpstrname;
//
//        if (isCity) {
////            if (highlight != null) {
////                //for highlight color
////                tmpstrname = testJson.optString("city_name").replace(highlight, "<font color='red'>" + highlight + "</font>");
////            } else {
//            tmpstrname = testJson.optString("city_name");
////            }
//            holder.tv_cityName_City.setText(Html.fromHtml(tmpstrname), TextView.BufferType.SPANNABLE);
//            holder.tv_cityName_City.setTag(testJson.optString("city_id"));
//
//        } else {
////            if (highlight != null) {
////                //for highlight color
////                tmpstrname = testJson.optString("area_name").replace(highlight, "<font color='red'>" + highlight + "</font>");
////            } else {
//            tmpstrname = testJson.optString("area_name");
////            }
////            holder.tv_cityName_City.setText(testJson.optString("area_name"));
//            holder.tv_cityName_City.setText(Html.fromHtml(tmpstrname), TextView.BufferType.SPANNABLE);
//            holder.tv_cityName_City.setTag(testJson.optString("area_id"));
//
//        }
//
//        return view;
//    }
//
//    @Override
//    public Filter getFilter() {
//        return mFilter;
//    }
//
//    class ViewHolder {
//
//        TextView tv_cityName_City;
//    }
//
//    private class ItemFilter extends Filter {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//
//            String filterString = constraint.toString().toLowerCase();
//            highlight = filterString;
//
//            FilterResults results = new FilterResults();
//
//            //take original json array from constructor
//            JSONArray tempjson = originalDataArray;
//
//            //json array size
//            int count = tempjson.length();
//
//            //store new objects
//            final JSONArray newj = new JSONArray();
//
//            if (isCity) {
//                for (int i = 0; i < count; i++) {
//                    JSONObject tempobj = tempjson.optJSONObject(i);
//
//                    if (tempobj.optString("city_name").toLowerCase().contains(filterString)) {
//                        newj.put(tempobj);
//                    }
//                }
//            } else if (!isCity) {
//                for (int i = 0; i < count; i++) {
//                    JSONObject tempobj = tempjson.optJSONObject(i);
//
//                    if (tempobj.optString("area_name").toLowerCase().contains(filterString)) {
//                        newj.put(tempobj);
//                    }
//                }
//            }
//
//
////            for (int i = 0; i < count; i++) {
////                filterableString = list.get(i);
////                if (filterableString.toLowerCase().contains(filterString)) {
////                    nlist.add(filterableString);
////                }
////            }
//
//            results.values = newj;
//            results.count = newj.length();
//
//            return results;
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            dataArray = (JSONArray) results.values;
//            notifyDataSetChanged();
//        }
//
//    }
//}