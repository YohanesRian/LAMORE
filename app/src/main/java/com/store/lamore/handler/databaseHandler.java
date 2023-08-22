package com.store.lamore.handler;

import com.store.lamore.R;
import com.store.lamore.data.History;
import com.store.lamore.data.Product;

import java.util.ArrayList;

public class databaseHandler {
    private String[] product_name = {"T-Shirt No.7", "Cotton Twill Shorts", "Trainers", "Relax Hoodie", "Relaxed Patterned Resort Shirt",
                                    "Chino Shorts", "Smile Pool Sliders", "Swim Shorts", "Cap With An Applique", "Waist Bag"};
    private String[] price = {"299.900", "199.900", "499.900", "459.900", "275.000", "295.000", "429.900", "125.000", "279.900", "599.900"};
    private String[] size = {"XL", "36", "44", "XXL", "XL", "L", "43/44", "XL", "All Size", "All Size"};
    private String[] outlet_name = {"Outlet A", "Outlet A", "Outlet A", "Outlet B", "Outlet B", "Outlet A", "Outlet C", "Outlet C", "Outlet C", "Outlet A"};
    private int[] img_ref = {R.drawable.t_shirt_no7, R.drawable.cotton_twill_shorts, R.drawable.trainers, R.drawable.relaxed_hoodie, R.drawable.relaxed_patterned_resort_shirt,
                            R.drawable.chino_shorts, R.drawable.smile_pool_sliders, R.drawable.swim_shorts, R.drawable.cap_with_an_applique, R.drawable.waist_bag};
    private int[] date = {20230702, 20230702, 20230702, 20230618, 20230618, 20230611, 20230528, 20230528, 20230528, 20230506};


    public databaseHandler(){}

    public ArrayList<Product> getLastPurchase(){
        ArrayList<Product> alp= new ArrayList<>();
        int current_month_year = 0;
        int i = 0;
        while(true){
            if(i == 0){
                current_month_year = date[0] / 100;
            }
            if(current_month_year != (date[i] / 100)){
                break;
            }
            Product p = new Product();
            p.setProductName(product_name[i]);
            p.setPrice(price[i]);
            p.setImg_ref(img_ref[i]);
            p.setSize(size[i]);
            alp.add(p);
            i++;
        }
        return alp;
    }

    public ArrayList<ArrayList<History>> getHistory(){
        ArrayList<ArrayList<History>> alalh =  new ArrayList<>();
        int current_month_year = 0;
        int i = 0;
        while(i < date.length){
            ArrayList<History> alh = new ArrayList<>();
            current_month_year = date[i] / 100;
            while(i < date.length){
                if(current_month_year != (date[i] / 100)){
                    break;
                }
                History h = new History();
                h.setProductName(product_name[i]);
                h.setPrice(price[i]);
                h.setDate(date[i]);
                h.setImg_ref(img_ref[i]);
                h.setSize(size[i]);
                h.setOutletName(outlet_name[i]);
                alh.add(h);
                i++;
            }
            alalh.add(alh);
        }
        return alalh;
    }
}
