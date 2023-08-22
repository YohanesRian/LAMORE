package com.store.lamore.loginregister;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.PluralsRes;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.store.lamore.R;
import com.store.lamore.dashboard.Dashboard;
import com.store.lamore.data.UserData;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private EditText name, email, phone, password, password2, referal;
    private TextView login;
    private Button register;
    private boolean referal_found = false;
    private String referal_person;
    private long referal_person_point;

    public Register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Register.
     */
    // TODO: Rename and change types and number of parameters
    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
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
        v = inflater.inflate(R.layout.fragment_register, container, false);
        login = v.findViewById(R.id.login_btn);
        register = v.findViewById(R.id.register_btn);
        name = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email);
        phone = v.findViewById(R.id.phone);
        password = v.findViewById(R.id.password);
        password2 = v.findViewById(R.id.password2);
        referal = v.findViewById(R.id.referal);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login l = new Login();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right, R.anim.enter_right_to_left, R.anim.exit_right_to_left).replace(R.id.container_form, l).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.setEnabled(false);
                if(!check_empty()){
                    UserData ud = new UserData();
                    ud.setEmail(email.getText().toString().trim());
                    ud.setPassword(password.getText().toString().trim());
                    ud.setName(name.getText().toString().trim());
                    ud.setPhone_number(phone.getText().toString());
                    ud.setPoint(0);
                    if(referal.getText().toString().trim().isEmpty()){
                        register(ud);
                    }
                    else{
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                boolean result = false;
                                for(QueryDocumentSnapshot qds: queryDocumentSnapshots){
                                    String temp = qds.getString("MemberID");
                                    if(temp.equals(referal.getText().toString().trim())){
                                        referal_person = qds.getId();
                                        referal_person_point = qds.getLong("Point");
                                        ud.setPoint(20);
                                        referal_person_point += 50;
                                        result = true;
                                        referal_found = true;
                                        break;
                                    }
                                }
                                if(!result){
                                    Toasty.warning(getContext(), "The referal code is invalid.").show();
                                }
                                else{
                                    register(ud);
                                }
                            }
                        });
                    }
                }
                register.setEnabled(true);
            }
        });

        return v;
    }

    private void register(UserData ud){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.fetchSignInMethodsForEmail(ud.getEmail()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if(task.getResult().getSignInMethods().size() == 0){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(ud.getEmail(), ud.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference dr = db.collection("users").document(auth.getCurrentUser().getUid());
                                Map<String, Object> user = new HashMap<>();
                                ud.generateMemberID();
                                user.put("MemberID", ud.getMemberID());
                                user.put("Name", ud.getName());
                                user.put("Email", ud.getEmail());
                                user.put("Phone_number", ud.getPhone_number());
                                user.put("Point", ud.getPoint());
                                dr.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        if(referal_found){
                                            Map<String, Object> user2 = new HashMap<>();
                                            DocumentReference dr2 = db.collection("users").document(referal_person);
                                            user2.put("Point", referal_person_point);

                                            dr2.set(user2, SetOptions.merge());
                                        }
                                        Toasty.success(getContext(), "Welcome, "+ ud.getName()+".", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(), Dashboard.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });
                            }
                            else{
                                Toasty.error(getContext(), "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toasty.warning(getContext(), "The email is already registered.", Toast.LENGTH_SHORT).show();
                    Login l = new Login();
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right, R.anim.enter_right_to_left, R.anim.exit_right_to_left).replace(R.id.container_form, l).commit();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    private boolean check_empty(){
        boolean result = false;
        if(name.getText().toString().trim().isEmpty()){
            name.setError("Name cannot be empty.");
            result = !result;
        }
        if(email.getText().toString().trim().isEmpty()){
            email.setError("Email cannot be empty.");
            result = !result;
        }
        if(phone.getText().toString().trim().isEmpty()){
            phone.setError("Phone number cannot be empty.");
            result = !result;
        }
        if(password.getText().toString().trim().isEmpty()){
            password.setError("Password cannot be empty.");
            result = !result;
        }
        if(password2.getText().toString().trim().isEmpty()){
            password2.setError("Password cannot be empty.");
            result = !result;
        }
        if(password.getText().toString().trim().length() < 6){
            password.setError("Password must be equals or more than 6 character.");
            result = !result;
        }
        if(password2.getText().toString().trim().length() < 6){
            password2.setError("Password must be equals or more than 6 character.");
            result = !result;
        }
        if(!password.getText().toString().trim().equals(password2.getText().toString().trim())){
            password.setError("Password is not match.");
            password2.setError("Password is not match.");
            result = !result;
        }
        return result;
    }
}