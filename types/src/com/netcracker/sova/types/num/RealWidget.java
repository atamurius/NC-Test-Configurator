package com.netcracker.sova.types.num;

import javax.swing.SpinnerNumberModel;

import com.netcracker.sova.model.Type;

public class RealWidget extends IntWidget
{
    public RealWidget()
    {
        spinner.setModel(new SpinnerNumberModel(
                0.0,
                Double.NEGATIVE_INFINITY, 
                Double.POSITIVE_INFINITY, 1));
    }
    
    @Override
    public Class<? extends Type> getType()
    {
        return RealType.class;
    }
}
