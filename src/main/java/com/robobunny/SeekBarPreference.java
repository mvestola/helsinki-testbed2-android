package com.robobunny;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import fi.testbed2.R;
import fi.testbed2.util.MathUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Code copied from: http://robobunny.com/wp/2011/08/13/android-seekbar-preference/
 *
 * @author Kirk Baucom
 */
public class SeekBarPreference extends Preference implements OnSeekBarChangeListener {

    private final String TAG = getClass().getName();

    private static final String ANDROID_NS ="http://schemas.android.com/apk/res/android";
    private static final String ROBOBUNNY_NS ="http://robobunny.com";
    private static final int DEFAULT_VALUE = 50;

    private int mMaxValue      = 100;
    private int mMinValue      = 0;
    private int mInterval      = 1;
    private int mCurrentValue;
    private String mUnitsLeft  = "";
    private String mUnitsRight = "";
    private SeekBar mSeekBar;
    private List<Integer> allowedEntryValues;
    private Map<Integer, String> allowedEntries;

    private TextView mStatusText;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPreference(context, attrs);
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPreference(context, attrs);
    }

    private void initPreference(Context context, AttributeSet attrs) {
        setValuesFromXml(attrs);
        mSeekBar = new SeekBar(context, attrs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mSeekBar.setScaleY(3f);
        }
        mSeekBar.setMax(mMaxValue - mMinValue);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    private boolean onlyAllowedValues() {
        return allowedEntryValues!=null;
    }

    /**
     * Returns the value shown to the user.
     *
     * @param value
     * @return
     */
    private String getValueText(Integer value) {

        if (!onlyAllowedValues() || allowedEntries==null) {
            return String.valueOf(value);
        }

        String text = allowedEntries.get(value);
        if (text==null) {
            text = String.valueOf(value);
        }
        return text;

    }

    /**
     * Returns the allowed entry values (not visible to the user but used by the system)
     *
     * @param allowedValuesArray
     * @return
     */
    private List<Integer> getAllowedEntryValues(String[] allowedValuesArray) {

        if (allowedValuesArray==null) {
            return null;
        }

        List<Integer> allowedValues = new ArrayList<Integer>();
        for (String valueAsString : allowedValuesArray) {
            try {
                allowedValues.add(Integer.parseInt(valueAsString.trim()));
            } catch (Exception e) {
                // Not an integer
                e.printStackTrace();
            }
        }

        return allowedValues;

    }

    /**
     * Returns the allowed entries (these are visible to the user)
     *
     * @param allowedEntriesArray
     * @param allowedEntryValues
     * @return
     */
    private Map<Integer, String> getAllowedEntries(String[] allowedEntriesArray, List<Integer> allowedEntryValues) {

        if (allowedEntriesArray==null) {
            return null;
        }

        Map<Integer, String> allowedEntriesMap = new HashMap<Integer, String>();
        int i = 0;
        for (String entryText : allowedEntriesArray) {
            try {
                allowedEntriesMap.put(allowedEntryValues.get(i), entryText.trim());
            } catch (Exception e) {
                // Value not found, ignore
                e.printStackTrace();
            }
            i++;
        }

        return allowedEntriesMap;

    }

    private void setValuesFromXml(AttributeSet attrs) {
        mMaxValue = attrs.getAttributeIntValue(ANDROID_NS, "max", 100);
        mMinValue = attrs.getAttributeIntValue(ROBOBUNNY_NS, "min", 0);

        int allowedEntryValuesResId = attrs.getAttributeResourceValue(ROBOBUNNY_NS, "allowedEntryValues", -1);
        int allowedEntriesResId = attrs.getAttributeResourceValue(ROBOBUNNY_NS, "allowedEntries", -1);

        if (allowedEntryValuesResId!=-1) {
            allowedEntryValues = getAllowedEntryValues(
                    getContext().getResources().getStringArray(allowedEntryValuesResId));
        }

        if (allowedEntriesResId!=-1) {
            allowedEntries = getAllowedEntries(
                    getContext().getResources().getStringArray(allowedEntriesResId), allowedEntryValues);
        }

        mUnitsLeft = getAttributeStringValue(attrs, ROBOBUNNY_NS, "unitsLeft", "");
        String units = getAttributeStringValue(attrs, ROBOBUNNY_NS, "units", "");
        mUnitsRight = getAttributeStringValue(attrs, ROBOBUNNY_NS, "unitsRight", units);

        try {
            String newInterval = attrs.getAttributeValue(ROBOBUNNY_NS, "interval");
            if(newInterval != null)
                mInterval = Integer.parseInt(newInterval);
        }
        catch(Exception e) {
            Log.e(TAG, "Invalid interval value", e);
        }

    }

    private String getAttributeStringValue(AttributeSet attrs, String namespace, String name, String defaultValue) {
        String value = attrs.getAttributeValue(namespace, name);
        if(value == null)
            value = defaultValue;

        return value;
    }

    @Override
    protected View onCreateView(ViewGroup parent){
        super.onCreateView(parent);

        RelativeLayout layout =  null;

        try {
            LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            layout = (RelativeLayout)mInflater.inflate(R.layout.seek_bar_preference, parent, false);
        }
        catch(Exception e)
        {
            Log.e(TAG, "Error creating seek bar preference", e);
        }

        return layout;

    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);

        try
        {
            // move our seekbar to the new view we've been given
            ViewParent oldContainer = mSeekBar.getParent();
            ViewGroup newContainer = (ViewGroup) view.findViewById(R.id.seekBarPrefBarContainer);

            if (oldContainer != newContainer) {
                // remove the seekbar from the old view
                if (oldContainer != null) {
                    ((ViewGroup) oldContainer).removeView(mSeekBar);
                }
                // remove the existing seekbar (there may not be one) and add ours
                newContainer.removeAllViews();
                newContainer.addView(mSeekBar, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
        catch(Exception ex) {
            Log.e(TAG, "Error binding view: " + ex.toString());
        }

        updateView(view);
    }

    /**
     * Update a SeekBarPreference view with our current state
     * @param view
     */
    protected void updateView(View view) {

        try {
            RelativeLayout layout = (RelativeLayout)view;

            mStatusText = (TextView)layout.findViewById(R.id.seekBarPrefValue);
            mStatusText.setText(getValueText(mCurrentValue));
            mStatusText.setMinimumWidth(30);

            mSeekBar.setProgress(mCurrentValue - mMinValue);

            TextView unitsRight = (TextView)layout.findViewById(R.id.seekBarPrefUnitsRight);
            unitsRight.setText(mUnitsRight);

            TextView unitsLeft = (TextView)layout.findViewById(R.id.seekBarPrefUnitsLeft);
            unitsLeft.setText(mUnitsLeft);

        }
        catch(Exception e) {
            Log.e(TAG, "Error updating seek bar preference", e);
        }

    }

    @Override
    public void onDependencyChanged(Preference dependency, boolean disableDependent) {
        super.onDependencyChanged(dependency, disableDependent);

        if (mStatusText!=null && mSeekBar!=null) {
            mSeekBar.setEnabled(!disableDependent);
            mStatusText.setEnabled(!disableDependent);
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        int newValue = progress + mMinValue;

        if(newValue > mMaxValue) {
            newValue = mMaxValue;
        } else if(newValue < mMinValue) {
            newValue = mMinValue;
        } else if (onlyAllowedValues()) {
            newValue = MathUtil.getClosestValue(newValue, allowedEntryValues);
        } else if(mInterval != 1 && newValue % mInterval != 0) {
            newValue = Math.round(((float)newValue)/mInterval)*mInterval;
        }

        // change rejected, revert to the previous value
        if(!callChangeListener(newValue)){
            seekBar.setProgress(mCurrentValue - mMinValue);
            return;
        }

        // change accepted, store it
        mCurrentValue = newValue;
        mStatusText.setText(getValueText(newValue));
        persistString(""+newValue);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        notifyChanged();
    }


    @Override
    protected Object onGetDefaultValue(TypedArray ta, int index){

        int defaultValue = ta.getInt(index, DEFAULT_VALUE);
        return defaultValue;

    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

        if(restoreValue) {
            mCurrentValue = Integer.valueOf(getPersistedString(""+mCurrentValue));
        }
        else {
            int temp = 0;
            try {
                temp = (Integer)defaultValue;
            }
            catch(Exception ex) {
                Log.e(TAG, "Invalid default value: " + defaultValue.toString());
            }

            persistString(""+temp);
            mCurrentValue = temp;
        }

    }

}

