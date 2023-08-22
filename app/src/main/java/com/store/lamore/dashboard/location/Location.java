package com.store.lamore.dashboard.location;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.store.lamore.R;
import com.store.lamore.data.OutletLocation;
import com.store.lamore.loginregister.GetStarted;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Location#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Location extends Fragment implements MapEventsReceiver {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private MapView map = null;
    private TextView outlet_name, outlet_location_lat, outlet_location_longi, outlet_street, direction;
    ArrayList<OutletLocation> ol = new ArrayList<>();

    private OutletLocation currentActive;
    private double lats = 0, longis = 0;

    public Location() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Location.
     */
    // TODO: Rename and change types and number of parameters
    public static Location newInstance(String param1, String param2) {
        Location fragment = new Location();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_location, container, false);
        map = (MapView) v.findViewById(R.id.map_location);
        outlet_name = v.findViewById(R.id.outlet_name);
        outlet_location_lat = v.findViewById(R.id.outlet_lat);
        outlet_location_longi = v.findViewById(R.id.outlet_longi);
        outlet_street = v.findViewById(R.id.outlet_street);
        direction = v.findViewById(R.id.direction);

        getOutletLocation();


        return v;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    private void load_map(){
        Context ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(getContext(), this);
        map.getOverlays().add(0, mapEventsOverlay);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        outlet_location_lat.setText(Double.toString(currentActive.getLat()));
        outlet_location_longi.setText(Double.toString(currentActive.getLongi()));


        for(int i = 0; i < ol.size(); i++) {
            Marker marker = new Marker(map);
            marker.setPosition(ol.get(i).getGeoPoint());
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setIcon(getResources().getDrawable(R.drawable.store_location_gps));
            marker.setTitle(ol.get(i).getName());

            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    map.getController().animateTo(marker.getPosition(), 18.0, (long) 1000);
                    OutletLocation ols1 = new OutletLocation();
                    ols1.setLocation(marker.getPosition());
                    ols1.setName(marker.getTitle());

                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> address = null;
                    try {
                        address = geocoder.getFromLocation(ols1.getLat(), ols1.getLongi(), 1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String[] street = address.get(0).getAddressLine(0).split(" ", 2);

                    outlet_street.setText(street[1]);
                    outlet_name.setText(ols1.getName());
                    outlet_location_lat.setText(Double.toString(ols1.getLat()));
                    outlet_location_longi.setText(Double.toString(ols1.getLongi()));

                    direction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", marker.getPosition().getLatitude(), marker.getPosition().getLongitude(), marker.getTitle());
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            intent.setPackage("com.google.android.apps.maps");
                            try{
                                startActivity(intent);
                            }
                            catch(ActivityNotFoundException ex){
                                Toasty.error(getContext(), "Please install the Google Maps Application", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    return false;
                }
            });
            map.getOverlays().add(marker);
        }

        map.getController().setZoom(5);
        map.getController().setCenter(currentActive.getGeoPoint());
        map.getController().animateTo(currentActive.getGeoPoint(), 16.5, (long) 3000);
        map.invalidate();
    }

    private void getOutletLocation(){
        currentActive = new OutletLocation();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("outlet_location").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot qds: queryDocumentSnapshots){
                    OutletLocation ols = new OutletLocation();
                    ols.setName(qds.getString("Name"));
                    ols.setLat(qds.getString("Lat"));
                    ols.setLongi(qds.getString("Longi"));

                    lats += ols.getLat();
                    longis += ols.getLongi();

                    ol.add(ols);
                }
                double lat = lats / ol.size();
                double longi = longis / ol.size();

                currentActive.setLat(lat);
                currentActive.setLongi(longi);

                load_map();
            }
        });
    }
}