/*
 Copyright 2016 Goldman Sachs.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

package com.gs.fw.common.mithra.aggregate.attribute;

import com.gs.fw.common.mithra.attribute.calculator.aggregateFunction.AggregateAttributeCalculator;
import com.gs.fw.common.mithra.aggregate.operation.*;
import com.gs.fw.common.mithra.AggregateAttribute;

import java.sql.PreparedStatement;
import java.sql.SQLException;



public class DoubleAggregateAttribute extends AggregateAttribute
{
    public DoubleAggregateAttribute(){}

    public DoubleAggregateAttribute(AggregateAttributeCalculator calculator)
    {
        super(calculator);
    }

    public com.gs.fw.common.mithra.HavingOperation eq(double value)
    {
        return new HavingAtomicOperation(this, value, HavingEqFilter.getInstance());
    }

    public com.gs.fw.common.mithra.HavingOperation notEq(double value)
    {
        return new HavingAtomicOperation(this, value, HavingNotEqFilter.getInstance());
    }

    public com.gs.fw.common.mithra.HavingOperation greaterThan(double value)
    {
        return new HavingAtomicOperation(this, value, HavingGreaterThanFilter.getInstance());
    }

    public com.gs.fw.common.mithra.HavingOperation lessThan(double value)
    {
        return new HavingAtomicOperation(this, value, HavingLessThanFilter.getInstance());
    }

    public com.gs.fw.common.mithra.HavingOperation greaterThanEquals(double value)
    {
        return new HavingAtomicOperation(this, value, HavingGreaterThanEqualsFilter.getInstance());
    }

    public com.gs.fw.common.mithra.HavingOperation lessThanEquals(double value)
    {
        return new HavingAtomicOperation(this, value, HavingLessThanEqualsFilter.getInstance());
    }

    public void setSqlParameter(PreparedStatement pstmt, int startIndex, Object value)
            throws SQLException
    {
         pstmt.setDouble(startIndex,(Double) value);
    }

}
