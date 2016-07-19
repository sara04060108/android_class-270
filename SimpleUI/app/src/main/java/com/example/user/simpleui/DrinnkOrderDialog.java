package com.example.user.simpleui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;//要改因為有兩種版本
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrinnkOrderDialog.OnDrinkOrderListener} interface
 * to handle interaction events.
 * Use the {@link DrinnkOrderDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrinnkOrderDialog extends DialogFragment {
    //Activity要實作介面才能與Fragment溝通
    //會有特殊效果變暗
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private DrinkOrder drinkOrder;
    NumberPicker mediumNumberPicker;
    NumberPicker largeNumberPicker;

    RadioGroup iceRadioGroup;
    RadioGroup sugarRadioGroup;

    EditText noteEditText;


    private OnDrinkOrderListener mListener;

    public DrinnkOrderDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment DrinnkOrderDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static DrinnkOrderDialog newInstance(DrinkOrder drinkOrder) {
        DrinnkOrderDialog fragment = new DrinnkOrderDialog();
        Bundle args = new Bundle();//可將資料帶入的方法，與intent(Activity之間)一樣
        args.putString(ARG_PARAM1, drinkOrder.toData());//定義key值(ATG_PARAM1)，用變數替代，避免打錯
        //drinkorder中要實作toData()
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_drinnk_order_dialog, container, false);//new出layout檔
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getArguments() != null){
            Bundle bundle = getArguments();
            String data = bundle.getString(ARG_PARAM1);
            drinkOrder = DrinkOrder.newInstanceWithData(data);
            if(drinkOrder == null){
                throw new RuntimeException("Instance Drink Order Fail");
            }
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_drinnk_order_dialog,null);

        alertDialogBuilder.setView(contextView)
                    .setTitle(drinkOrder.drink.getName())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            drinkOrder.mNumber = mediumNumberPicker.getValue();
                            drinkOrder.INumber = mediumNumberPicker.getValue();
                            drinkOrder.note = noteEditText.getText().toString();
                            drinkOrder.ice = getSelectedTextFromRadioGroup(iceRadioGroup);
                            drinkOrder.sugar = getSelectedTextFromRadioGroup(sugarRadioGroup);

                            if(mListener != null){
                                mListener.onDrinkOrderFinish(drinkOrder);
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        mediumNumberPicker = (NumberPicker)contextView.findViewById(R.id.mediumNumberPicker);
        mediumNumberPicker.setMaxValue(100);
        mediumNumberPicker.setMinValue(0);
        mediumNumberPicker.setValue(drinkOrder.mNumber);

        largeNumberPicker = (NumberPicker)contextView.findViewById(R.id.largeNumberPicker);
        largeNumberPicker.setMaxValue(100);
        largeNumberPicker.setMinValue(0);
        largeNumberPicker.setValue(drinkOrder.INumber);

        iceRadioGroup = (RadioGroup)contextView.findViewById(R.id.iceRadioGroup);
        sugarRadioGroup = (RadioGroup)contextView.findViewById(R.id.sugarRadioGroup);

        noteEditText = (EditText)contextView.findViewById(R.id.noteEditText);

        return alertDialogBuilder.create();
    }

    private  String getSelectedTextFromRadioGroup(RadioGroup radioGroup){
        //可得到radiogroup回傳的值
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedRadioButtonId);
        return  checkedRadioButton.getText().toString();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDrinkOrderListener) {//溝通完成才能實作，不然跳出
            mListener = (OnDrinkOrderListener) context;
        } else {
            throw new RuntimeException(context.toString()
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
    public interface OnDrinkOrderListener {//實作介面
        // TODO: Update argument type and name
        void onDrinkOrderFinish(DrinkOrder drinkOrder);
    }
}
//Fragment間的溝通由Activity做調配\