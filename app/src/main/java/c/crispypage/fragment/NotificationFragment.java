package c.crispypage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import c.crispypage.R;
import c.crispypage.other.CircleTransform;


/**
 * Created by kjaganmohan on 08/12/17.
 */

public class NotificationFragment extends Fragment {
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        mAuth = FirebaseAuth.getInstance();
       FirebaseUser user = mAuth.getCurrentUser();

        //GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(getActivity());

        TextView name,email;
        ImageView profileImg;


        name =(TextView)view.findViewById(R.id.name);
        email = (TextView)view.findViewById(R.id.email);


        profileImg=(ImageView)view.findViewById(R.id.img_profile);

        if (user != null) {
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());

            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getActivity()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profileImg);
        }

        return view;
    }
}
