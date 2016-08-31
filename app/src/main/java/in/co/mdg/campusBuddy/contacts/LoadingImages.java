package in.co.mdg.campusBuddy.contacts;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import in.co.mdg.campusBuddy.R;

/**
 * Created by Chirag on 27-08-2016.
 */

class LoadingImages {
    static void loadDeptImages(String deptPhoto, final ImageView deptBackDrop, final Callback callback) {
        if (deptPhoto != null) {
            if (deptPhoto.length() < 4)
                deptPhoto = "http://www.iitr.ac.in/departments/" + deptPhoto + "/assets/images/top1.jpg";
            RequestCreator requestCreator = Picasso.with(deptBackDrop.getContext())
                    .load(deptPhoto)
                    .noFade()
                    .fit();
            if (callback != null)
                requestCreator.into(deptBackDrop, callback);
            else
                requestCreator.placeholder(R.drawable.dept_icon).into(deptBackDrop);
        } else {
            if (callback == null)
                Picasso.with(deptBackDrop.getContext()).load(R.drawable.dept_icon).noFade().fit().into(deptBackDrop);
        }
    }

    static void loadContactImages(String profilePicAddr, ImageView profilePic) {
        if (profilePicAddr != null) {
            if (profilePicAddr.equals("") || profilePicAddr.equals("default.jpg")) {
                profilePic.setImageDrawable(
                        ContextCompat.getDrawable(
                                profilePic.getContext()
                                , R.drawable.contact_icon));
            } else {
                if (!profilePicAddr.contains("http"))
                    profilePicAddr = "http://people.iitr.ernet.in/facultyphoto/" + profilePicAddr;
                Picasso.with(profilePic.getContext())
                        .load(profilePicAddr)
                        .placeholder(R.drawable.contact_icon)
                        .noFade()
                        .error(R.drawable.contact_icon)
                        .into(profilePic);
            }
        } else {
            profilePic.setImageDrawable(
                    ContextCompat.getDrawable(
                            profilePic.getContext()
                            , R.drawable.ic_account_circle_black_24dp));
        }
//        String picAddress = contact.getProfilePic();
//        if(picAddress == null)
//        {
//            picAddress ="default.jpg";
//        }
//        if (picAddress.equals("") || picAddress.equals("default.jpg")) {
//            profilePic.setImageDrawable(
//                    ContextCompat.getDrawable(
//                            profilePic.getContext()
//                            , R.drawable.contact_icon));
//        } else {
//            Picasso.with(profilePic.getContext())
//                    .load("http://people.iitr.ernet.in/facultyphoto/" + picAddress)
//                    .placeholder(R.drawable.contact_icon)
//                    .noFade()
//                    .error(R.drawable.contact_icon)
//                    .into(profilePic);
//        }

    }

    static void loadContactImageForContactView(String profilePicAddr, final ImageView profilePic, final ImageView smallProfilePic) {
        if (profilePicAddr != null) {
            if (!(profilePicAddr.equals("") || profilePicAddr.equals("default.jpg"))) {
                if (!profilePicAddr.contains("http"))
                    profilePicAddr = "http://people.iitr.ernet.in/facultyphoto/" + profilePicAddr;
                Picasso.with(profilePic.getContext())
                        .load(profilePicAddr)
                        .noFade()
//                        .error(ContextCompat.getDrawable(smallProfilePic.getContext(), R.drawable.contact_icon))
//                        .placeholder(ContextCompat.getDrawable(smallProfilePic.getContext(), R.drawable.contact_icon))
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                profilePic.setImageBitmap(bitmap);
                                smallProfilePic.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
//                                profilePic.setImageDrawable(errorDrawable);
//                                smallProfilePic.setImageDrawable(errorDrawable);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
//                                profilePic.setImageDrawable(placeHolderDrawable);
//                                smallProfilePic.setImageDrawable(placeHolderDrawable);
                            }
                        });
            }
        }
    }
}
