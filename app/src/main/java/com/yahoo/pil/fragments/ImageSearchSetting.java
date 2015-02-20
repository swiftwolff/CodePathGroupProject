package com.yahoo.pil.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.yahoo.pil.R;
import com.yahoo.pil.models.SearchSetting;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageSearchSetting.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageSearchSetting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageSearchSetting extends Fragment {

    private static final String SEARCH_SETTING = "search_setting";

    private SearchSetting searchSettingParcelable;

    private OnFragmentInteractionListener mListener;

    private Spinner spImageSize;
    private Spinner spColorFilter;
    private Spinner spImageType;

    private EditText etSiteFilter;


    /**
     * @param searchSetting
     * @return
     */
    public static ImageSearchSetting newInstance(SearchSetting searchSetting) {
        ImageSearchSetting fragment = new ImageSearchSetting();
        Bundle args = new Bundle();
        args.putParcelable(SEARCH_SETTING, searchSetting);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageSearchSetting() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchSettingParcelable = getArguments().getParcelable(SEARCH_SETTING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout fragmentLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_image_search_setting, container, false);
        this.spImageSize = (Spinner) fragmentLayout.findViewById(R.id.spImageSize);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.image_size_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spImageSize.setAdapter(adapter1);
        this.spImageSize.setSelection(searchSettingParcelable.getSelectedImageSizeArrayIndex());


        this.spColorFilter = (Spinner) fragmentLayout.findViewById(R.id.spColorFilter);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.image_color_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spColorFilter.setAdapter(adapter2);
        this.spColorFilter.setSelection(searchSettingParcelable.getSelectedImageColorArrayIndex());

        this.spImageType = (Spinner) fragmentLayout.findViewById(R.id.spImageType);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getActivity(),
                R.array.image_type_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spImageType.setAdapter(adapter3);
        this.spImageType.setSelection(searchSettingParcelable.getSelectedImageTypeArrayIndex());

        this.etSiteFilter = (EditText) fragmentLayout.findViewById(R.id.etSiteFilter);
        this.etSiteFilter.setText(searchSettingParcelable.getSite());

        Button saveButton = (Button) fragmentLayout.findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSearchSetting.this.hideFragment();
                ImageSearchSetting.this.saveSettings();
            }
        });

        Button closeButton = (Button) fragmentLayout.findViewById(R.id.btnClose);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSearchSetting.this.hideFragment();
            }
        });

        return fragmentLayout;
    }

    private void hideFragment() {
        FragmentTransaction ft = ImageSearchSetting.this.getFragmentManager().beginTransaction();
        ft.hide(ImageSearchSetting.this);
        ft.commit();
    }


    public void saveSettings() {
        if (mListener != null) {
            String imageSize = (String) this.spImageSize.getSelectedItem();
            String colorFilter = (String) this.spColorFilter.getSelectedItem();
            String imageType = (String) this.spImageType.getSelectedItem();
            String siteFilter = this.etSiteFilter.getText().toString();

            int selectedImageSizeArrayIndex = this.spImageSize.getSelectedItemPosition();
            int selectedImageColorArrayIndex = this.spColorFilter.getSelectedItemPosition();
            int selectedImageTypeArrayIndex = this.spImageType.getSelectedItemPosition();

            SearchSetting newSearchSetting = new SearchSetting(imageSize, colorFilter, imageType, siteFilter);
            newSearchSetting.setSelectedImageSizeArrayIndex(selectedImageSizeArrayIndex);
            newSearchSetting.setSelectedImageColorArrayIndex(selectedImageColorArrayIndex);
            newSearchSetting.setSelectedImageTypeArrayIndex(selectedImageTypeArrayIndex);

            mListener.onFragmentInteraction(newSearchSetting);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(SearchSetting searchSetting);
    }

}
