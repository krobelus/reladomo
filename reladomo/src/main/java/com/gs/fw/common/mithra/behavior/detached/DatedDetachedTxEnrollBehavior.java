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

package com.gs.fw.common.mithra.behavior.detached;

import com.gs.fw.common.mithra.MithraDatedTransactionalObject;
import com.gs.fw.common.mithra.MithraTransaction;import com.gs.fw.common.mithra.DatedTransactionalState;
import com.gs.fw.common.mithra.MithraDataObject;
import com.gs.fw.common.mithra.behavior.AbstractDatedTransactionalBehavior;
import com.gs.fw.common.mithra.behavior.DatedTransactionalBehavior;
import com.gs.fw.common.mithra.behavior.inmemory.DatedInMemoryTxEnrollBehavior;
import com.gs.fw.common.mithra.behavior.state.DatedPersistenceState;


public class DatedDetachedTxEnrollBehavior extends DatedInMemoryTxEnrollBehavior
{
    public DatedDetachedTxEnrollBehavior()
    {
        super((byte) DatedPersistenceState.DETACHED, true, true);
    }

    public DatedTransactionalBehavior enrollInTransaction(MithraDatedTransactionalObject mithraObject, MithraTransaction tx, DatedTransactionalState prevState, int persistenceState)
    {
        if (mithraObject.zEnrollInTransactionForRead(prevState, tx, persistenceState))
        {
            return AbstractDatedTransactionalBehavior.getDetachedSameTxBehavior();
        }
        return null;
    }

    public DatedTransactionalBehavior enrollInTransactionForWrite(MithraDatedTransactionalObject mto, MithraTransaction tx, DatedTransactionalState prevState, int persistenceState)
    {
        MithraDataObject data = mto.zGetCurrentData();
        if (data == null) data = mto.zAllocateData();
        else data = data.copy();
        if (mto.zEnrollInTransactionForWrite(prevState, null, data, tx))
        {
            return AbstractDatedTransactionalBehavior.getDetachedSameTxBehavior();
        }
        return null;
    }

    public DatedTransactionalBehavior enrollInTransactionForDelete(MithraDatedTransactionalObject mto, MithraTransaction tx, DatedTransactionalState prevState, int persistenceState)
    {
        MithraDataObject data = mto.zGetCurrentData();
        if (data == null) data = mto.zAllocateData();
        else data = data.copy();
        if (mto.zEnrollInTransactionForWrite(prevState, null, data, tx))
        {
            return AbstractDatedTransactionalBehavior.getDetachedSameTxBehavior();
        }
        return null;
    }

    public MithraDatedTransactionalObject updateOriginalOrInsert(MithraDatedTransactionalObject obj)
    {
        throw new RuntimeException("should never get here");
    }

    public void cascadeUpdateInPlaceBeforeTerminate(MithraDatedTransactionalObject obj)
    {
        throw new RuntimeException("should never get here");
    }
}
