package in.co.mdg.campusBuddy.contacts;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.fragments.ContactsFragment;

/**
 * Created by Chirag on 27-08-2016.
 */

class LoadingImages {
    static void loadDeptImages(String deptPhoto, final ImageView deptBackDrop) {
        if (ContactsFragment.loadImages) {
            if (deptPhoto != null) {
                if (deptPhoto.length() < 4)
                    deptPhoto = "http://www.iitr.ac.in/departments/" + deptPhoto + "/assets/images/top1.jpg";

                Glide.with(deptBackDrop.getContext())
                        .load(deptPhoto)
                        .centerCrop()
                        .dontAnimate()
                        .placeholder(R.drawable.dept_icon)
                        .into(deptBackDrop);
            } else {
                deptBackDrop.setImageDrawable(
                        ContextCompat.getDrawable(
                                deptBackDrop.getContext()
                                , R.drawable.dept_icon));
            }
        }

    }

    static void loadDeptImages(String deptPhoto, final BitmapImageViewTarget target) {
        if (ContactsFragment.loadImages) {
            if (deptPhoto != null) {
                if (deptPhoto.length() < 4)
                    deptPhoto = "http://www.iitr.ac.in/departments/" + deptPhoto + "/assets/images/top1.jpg";

                Glide.with(target.getView().getContext())
                        .load(deptPhoto)
                        .asBitmap()
                        .centerCrop()
                        .dontAnimate()
                        .placeholder(R.drawable.dept_default)
                        .into(target);
            }
        }
    }

    static void loadContactImages(String profilePicAddr, ImageView profilePic) {
        if (ContactsFragment.loadImages) {
            if (profilePicAddr != null) {
                if (profilePicAddr.equals("") || profilePicAddr.equals("default.jpg")) {
                    profilePic.setImageDrawable(
                            ContextCompat.getDrawable(
                                    profilePic.getContext()
                                    , R.drawable.contact_icon));
                } else {
                    if (!profilePicAddr.contains("http"))
                        profilePicAddr = "http://people.iitr.ernet.in/facultyphoto/" + profilePicAddr;
                    Glide.with(profilePic.getContext())
                            .load(profilePicAddr)
                            .placeholder(R.drawable.contact_icon)
                            .centerCrop()
                            .dontAnimate()
                            .error(R.drawable.contact_icon)
                            .into(profilePic);
                }
            } else {
                profilePic.setImageDrawable(
                        ContextCompat.getDrawable(
                                profilePic.getContext()
                                , R.drawable.ic_account_circle_black_24dp));
            }
        }
    }

    static void loadContactImageForContactView(String profilePicAddr, final ImageView profilePic, final ImageView smallProfilePic) {
        if (ContactsFragment.loadImages) {
            if (profilePicAddr != null) {
                if (!(profilePicAddr.equals("") || profilePicAddr.equals("default.jpg"))) {
                    if (!profilePicAddr.contains("http"))
                        profilePicAddr = "http://people.iitr.ernet.in/facultyphoto/" + profilePicAddr;
                    Glide.with(profilePic.getContext())
                            .load(profilePicAddr)
                            .dontAnimate()
                            .centerCrop()
//                        .error(ContextCompat.getDrawable(smallProfilePic.getContext(), R.drawable.contact_icon))
//                        .placeholder(ContextCompat.getDrawable(smallProfilePic.getContext(), R.drawable.contact_icon))
                            .into(new SimpleTarget<GlideDrawable>() {
                                @Override
                                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                    profilePic.setImageDrawable(resource);
                                    smallProfilePic.setImageDrawable(resource);
                                }

                                @Override
                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                    e.printStackTrace();
                                }
                            });
                }
            }
        }
    }
}
